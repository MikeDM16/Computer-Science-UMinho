/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import static java.lang.System.out;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ruifreitas
 */
public class UMinhoBoleias implements UMinhoBoleiasIface {

	private Map<String, Utilizador> utilizadores;
	ReentrantLock lockUsers;
	Condition esperaPass;
	Condition esperaCond;
    
	
	public UMinhoBoleias(){
		this.utilizadores = new HashMap<>();
		this.lockUsers = new ReentrantLock();
		this.esperaPass = this.lockUsers.newCondition();
		this.esperaCond = this.lockUsers.newCondition();
	}
	
	@Override
	public boolean registaUtilizador(String mail, String pass) {
		boolean ret = false;
		try{
			this.lockUsers.lock();
			if(!utilizadores.containsKey(mail)){
				utilizadores.put(mail, new Utilizador(mail, pass));
		        out.println("Registado com sucesso!");
		        ret= true;
			}else{
				out.println("Já existe um utilizador com esse nome");
			}
		}finally{
			this.lockUsers.unlock();
		}
		return ret;
    }

	@Override
	public boolean autenticar(String mail, String pass) {
		out.println("Autenticar");
		boolean ret = false;
		try{
			this.lockUsers.lock();
			if(utilizadores.containsKey(mail)){
				out.println("O ultizador existe");
				Utilizador u = utilizadores.get(mail);
				if(!u.isAutent() && u.autenticar(pass)){
					out.println("O ultizador autenticado");
					ret = true;
				}
			}
		}finally{
			this.lockUsers.unlock();
		}
		return ret;
	}

	public Utilizador getUser(String email){
		Utilizador ret = null;
		try{
			this.lockUsers.lock();
			ret = this.utilizadores.get(email);
			
		}finally{
			this.lockUsers.unlock();
		}
		return ret;
	}
	
	private boolean existPassEspera(){
		boolean ret = false;
		Utilizador u = null;
		Iterator<Utilizador> ui  = utilizadores.values().iterator();
		while(ret==false && ui.hasNext()){
			 u = ui.next();
			if(u.isActiv() && !u.isCondutor() && !u.isOcupado()){
				ret = true;
			}
		}
		return ret;
	}
	
	private boolean existCondDisp(){
		boolean ret = false;
		Utilizador u = null;
		Iterator<Utilizador> ui  = utilizadores.values().iterator();
		out.println("A ver se existem condutoes disponiveis");
		while(ret==false && ui.hasNext()){
			 u = ui.next();
			 out.println(u.toString());
			if(u.isActiv() && u.isCondutor() && !u.isOcupado()){
				ret = true;
				out.println("encotrei condutor disponivel");
			}
		}
		return ret;
	}
	
	
	@Override
	public String solicitarViagem(String mail, Local partida, Local destino) {
		String ret = null;
		double distancia = Double.MAX_VALUE;
		Utilizador condutor=null;
		Veiculo v;
		Utilizador log =null;
		
		try{
			this.lockUsers.lock();
			log=utilizadores.get(mail);
			while(!this.existCondDisp()){
				try {
					out.println("Não ha condutores vou esperar");
					this.esperaCond.await();
					out.println("existe condutor vou ver se ninguem escolheu");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//ja tenho condutor
			out.println("Tenho condutor, vou ecolher o melhor");
			for(Utilizador u : utilizadores.values()){
				if(u.isCondutor() && !u.isOcupado()){
					if(u.getLoc().distancia(partida)<distancia){
						distancia = u.getLoc().distancia(partida);
						condutor = u;
					}
				}
			}
			
			out.println("Vou fazer acossiaçao");
			//acossiar
			log.setPar(condutor);
			condutor.setPar(log);
			//estao acosiados
			out.println("ja escolhi o condutor, meter tudo coupado");
			condutor.setOcupado(true);
			log.setOcupado(true);
			//ja estao coupados
			out.println("ja estamos acossiados, vou avisar condutores para ver quem escolhi");
			this.esperaPass.signalAll();
			out.println("ja avisei");
		}finally{
			this.lockUsers.unlock();
		}
		
		v = condutor.getVeiculo();
		
		if(distancia==0){
			ret =  new String(condutor.getEmail()+":"+v.getMatricula()+":"+v.getModelo());
		}else{
			ret =  new String(condutor.getEmail()+":"+v.getMatricula()+":"+v.getModelo()+":"+((condutor.getLoc().distancia(partida))/50.0));
		}
		return ret;
	}

	@Override
	public String disponivelViagem(String mail, Local actual, String matricula, String modelo, double custoUnitario) {
		String ret = null;
		Utilizador passageiro=null;
		Veiculo v;
		Utilizador log =null;
		
		try{
			this.lockUsers.lock();
			log=utilizadores.get(mail);
			//estou disponivel aviso todos que estou disponivel
			out.println("Vou avisar todos que estou disponivel");
			this.esperaCond.signalAll();
			out.println("ja avisei");
			//espero enquanto ningem me escolhes
			while(log.getPar()==null){
				try {
					out.println("Vou espera que algeum me escolha");
					this.esperaPass.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//ja tenho passageiro
			out.println("fui escolhido ja tenho passageiro");			
		}finally{
			this.lockUsers.unlock();
		}
		passageiro = log.getPar();
		double distancia = log.getLoc().distancia(passageiro.getLoc());
		
		if (distancia!=0){
			ret =  new String(passageiro.getEmail()+":"+passageiro.getLoc().toString()+":"+passageiro.getDest().toString()+":"+(distancia/50.0));
			
		}else{
			ret =  new String(passageiro.getEmail()+":"+passageiro.getLoc().toString()+":"+passageiro.getDest().toString());
		}
		return ret;
	}

	@Override
	public void logout(String username) {
		try{
			this.lockUsers.lock();
			this.utilizadores.get(username).exit();
		}finally{
			this.lockUsers.unlock();
		}
		
	}
    
}
