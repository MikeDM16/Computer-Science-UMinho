import java.net.InetAddress;
import java.text.DecimalFormat;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 *
 * @author migue
 */
public class GestaoMonitores {
    
    private Map<Integer, BackEndServer> servidoresBackEnd;
    private Lock lockVar;
    private int listar, isReverseRunning;
    /*
    Construtor da classe sem argumentos
    */
    public GestaoMonitores(){
        this.servidoresBackEnd = new TreeMap<>();
        this.lockVar = new ReentrantLock();
        this.listar = 0;
        this.isReverseRunning = 1;
    }
    
    /*
    Função que atualiza os dados da variavel global, que representa a tabela com  as 
    informações dos servidores de backEnd.
    Se o servidor BackEnd já existir, atualiza os seus campos.
    Se não existir, adiciona-o. 
    */
    public void atualizaDados(int idBackEnd, String designacao, int porta, int nrSeq, double tEnvio, long tRececao, int nrCliAtv, InetAddress endIP){
        this.lockVar.lock();
        BackEndServer server;
        if( this.servidoresBackEnd.containsKey(idBackEnd)){
            server = this.servidoresBackEnd.get(idBackEnd);
            server.atualizar( tEnvio, tRececao, nrCliAtv, nrSeq);
        }
        /*Caso o servidor backend ainda nao seja conhecido*/
        else{
            server = new BackEndServer(idBackEnd, designacao, porta, nrSeq, tRececao, nrCliAtv, endIP);
            this.servidoresBackEnd.put(idBackEnd, server);
        }
        this.lockVar.unlock();
    }

    public void atualizaDadosProbing(int id, int nrSeq, double tEnvio, long tempoRececao ){
        this.lockVar.lock();
        BackEndServer server;
        if( this.servidoresBackEnd.containsKey(id)){
            server = this.servidoresBackEnd.get(id);
            server.atualizaDadosProbing( nrSeq, tEnvio, tempoRececao);
        }
        this.lockVar.unlock();
    }
    
    public void listarServidores(){
        this.lockVar.lock();
        
        for(Map.Entry<Integer, BackEndServer> e : servidoresBackEnd.entrySet()){
            BackEndServer server = e.getValue();
            String designacao = server.getDesignacao();
            int id = server.getIdBackEnd();
            int cli = server.getNrCliAtv();
            double time = server.getRTTChegadaPDU();
            DecimalFormat df = new DecimalFormat("#.###");
            double pckLost;
            try{
                pckLost = server.getNrPerdasProbes() / server.getNrProbesEnviados();
            }catch(Exception exc){
                pckLost = -1;
            }
            String IP = server.getEndIP().getHostAddress().toString();
            
            StringBuilder s = new StringBuilder();
            s.append("Servidor: "+ designacao + "; ");
            s.append("id: " + id + "; ");
            s.append("IP: " + IP + "; ");
            s.append("% Pacotes perdidos:" + pckLost + "; ");
            s.append("cli ativos: " + cli + "; ");
            s.append("RTT PDU: " + df.format(time) + ".");
            System.out.println(s.toString());
        }
        this.lockVar.unlock();
        System.out.println();
    }
    
    public void setListar(int v){ this.lockVar.lock(); this.listar = v; this.lockVar.unlock();}
    public int getListar(){return this.listar; }

    public int nrServers(){ return this.servidoresBackEnd.size(); }

    public void getLock(){ this.lockVar.lock(); }
    public void getUnlock(){ this.lockVar.unlock(); }
    
    public void setReverseRunning(int i ){ this.lockVar.lock(); this.isReverseRunning = i; this.lockVar.unlock();}
    public int getReverseRunning(){ return this.isReverseRunning; }



  /*------------------------------------------------------------
        Metodos usados no processo de receber Cliente   
    -------------------------------------------------------------*/
    public void incNrCliAtv( int id ){
        this.lockVar.lock(); 
        if( this.servidoresBackEnd.containsKey(id))
            this.servidoresBackEnd.get(id).incNrCliAtv(); 
        this.lockVar.unlock(); 
    }
    public void decNrCliAtv( int id ){
        this.lockVar.lock(); 
        if( this.servidoresBackEnd.containsKey(id))
            this.servidoresBackEnd.get(id).decNrCliAtv(); 
        this.lockVar.unlock(); 
    }
    public BackEndServer getBestServer(){
        double min = Double.MAX_VALUE;
        BackEndServer servidor = null;
        this.lockVar.lock();

        for(Map.Entry<Integer, BackEndServer> e : servidoresBackEnd.entrySet()){
            BackEndServer aux = e.getValue();
            if( aux.testaAtividade()==true){
                double r = aux.getIndiceDisponibilidade();
                if(r < min){
                    min = r;
                    servidor = aux;
                }
            }
        }
        this.lockVar.unlock();
        return servidor;
    }

    /*------------------------------------------------------------
        Metodos usados no processo de probing no ReverseProxy   
    -------------------------------------------------------------*/

    //Get dos servidores
    public Map<Integer, BackEndServer> getServidores(){ 
        return this.servidoresBackEnd; 
    }

    public BackEndServer getServer(int id){ 
        BackEndServer s = null;
        if( this.servidoresBackEnd.containsKey(id))
            s = this.servidoresBackEnd.get(id); 
        return s;
    }
    //Remover um determiando servidor devido À falta de resposta do mesmo
    public void remover(int id){
        this.lockVar.lock(); 
        this.servidoresBackEnd.remove(id); 
        this.lockVar.unlock();
    }

    public void incProbesEnviados(int id){
        this.lockVar.lock(); 
        this.servidoresBackEnd.get(id).incProbesEnviados(); 
        this.lockVar.unlock();
    }
    public void setLastProbeTime(int id, long time){
        this.lockVar.lock(); 
        this.servidoresBackEnd.get(id).setLastProbeTime(time); 
        this.lockVar.unlock();
    }
    /*------------------------------------------------------------
    --------------------------------------------------------------*/


}

