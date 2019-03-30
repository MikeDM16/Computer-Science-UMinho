/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author ruifreitas
 */
public class ServerConnection implements Runnable{
    
    static final String REGISTAUTILIZADOR ="REGISTAUTILIZADOR";
    static final String AUTENTICAR = "AUTENTICAR";
    static final String SOLICITARVIAGEM ="SOLICITARVIAGEM";
    static final String DISPONIVELVIAGEM ="DISPONIVELVIAGEM";
    static final String LOGOUT ="LOGOUT";
    static int sleepFactor = 10000;
    private Socket sock;
    private UMinhoBoleias umb;
    private BufferedWriter out;
    private BufferedReader in;
    private Utilizador login;
    
    public ServerConnection(Socket sock,UMinhoBoleias umb) throws IOException{
        this.sock=sock;
        this.umb=umb;
        this.out=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        this.in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }
    
    private void regista() throws IOException{
        String info = in.readLine();
        String[] arr = info.split(":");
        
        if(umb.registaUtilizador(arr[0], arr[1])){ 
            //conseguiu
            out.write("OK");
            out.newLine();
            out.flush();
        }else{ 
            //falhou
            out.write("KO");
            out.newLine();
            out.flush();
        }
    }
    
    private void login() throws IOException{
        String info = in.readLine();
        String[] arr = info.split(":");
        if(umb.autenticar(arr[0], arr[1])){ 
            //autenticou
            login = umb.getUser(arr[0]);    
            //atribui sessao
            out.write("OK");
            out.newLine();
            out.flush();
        }else{
            out.write("KO");                
            //falhou
            out.newLine();
            out.flush();
        }
    }
        
    private void solicita() throws IOException{
        String info = in.readLine();
        String[] arr = info.split(":");
        Local partida = new Local(arr[1]);
        Local destino = new Local(arr[2]);
        Double sleep;
        
        //login
        login.login(login.getPw(), false, null, partida, in, out, destino); 
        //string return apos solicitar viagem
        String aux = umb.solicitarViagem(arr[0], partida, destino);         
        String[] arr2 = aux.split(":");
        
        //condutor ja está lá
        if(arr2.length==3){                                                 
            sleep = 0.0;
        }else{
            //condutor está a caminho e demora arr2[3]
            sleep = Double.parseDouble(arr2[3]);                        
        }
        
        out.write(aux);
        out.newLine();
        out.flush();
        
        //sleep * 60 * 1000 (min * milisegundos)
        try {
            Thread.sleep(((int)(sleep*sleepFactor)) );                                      
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        out.write("OK");
        out.newLine();
        out.flush();
        
        //informa o condutor que chegou ao local de partida do cliente
        /*condutor = login.getPar();
        condutor.getOut().write("OK"); 
        condutor.getOut().newLine();
        condutor.getOut().flush();
        */
        
        //sleep da viagem
        Double sleepViagem = partida.distancia(destino)/50.0;
        
        try{
            Thread.sleep((int)(sleepViagem*sleepFactor));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        //envia o custo da viagem ao cliente
        out.write(String.valueOf((login.getPar().getCustoUnitario()*partida.distancia(destino))));
        out.newLine();
        out.flush();
        login.logout();
        //envia que chegou ao destino ao condutor
        /*condutor.getOut().write("OK"); 
        condutor.getOut().newLine();
        condutor.getOut().flush();
        */
    }
    
    private void disponivel() throws IOException{
        String info = in.readLine();
        String[] arr = info.split(":");
        Utilizador cliente = umb.getUser(arr[0]);
        Local l = new Local(arr[1]);
        String matricula = arr[2];
        String modelo = arr[3];
        double custoUnitario = Double.parseDouble(arr[4]);
        double sleep;
        //login
        login.login(login.getPw(), true, new Veiculo(modelo, matricula), l, in, out, null); 
        //muda custo de viagem pro que recebi
        login.setCustoUnitario(custoUnitario);
        String aux = umb.disponivelViagem(cliente.getEmail(), l, matricula, modelo, custoUnitario);
        String[] arraux = aux.split(":");
        //ve se a string tem 4 elementos, ou seja tem tempo de viagem
        if(arraux.length==4){
            sleep = Double.parseDouble(arraux[3]);          
        }else{
            sleep = 0.0;
        }
        out.write(aux);
        out.newLine();
        out.flush();
    
        //sleep da ate ao passageiro
        try{
            Thread.sleep((int)(sleep*sleepFactor));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        //sleep deslocacao ate ao passageiro
        out.write("OK");
        out.newLine();
        out.flush();
        
        //sleep da viagem
        double distanciaSleep = (new Local(arraux[1])).distancia(new Local(arraux[2]))/50.0;
        try{
            Thread.sleep((int)(distanciaSleep*sleepFactor));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        
        //envia o custo da viagem ao cliente
        out.write("OK");
        out.newLine();
        out.flush();
        login.logout();     
    }
    
    private void logout(){
        if(this.login!=null){
            this.umb.logout(login.getEmail());
        }
    }
    public void run() {
        String op="a";
        boolean lout=false;
        try{
            while(sock.isConnected() && op !=null){
                op = in.readLine();
                if(op!=null){
                    switch(op){
                        case REGISTAUTILIZADOR:
                            regista();
                            break;
                        case AUTENTICAR:
                            login();
                            break;
                        case SOLICITARVIAGEM:
                            solicita();
                            break;
                        case DISPONIVELVIAGEM:
                            disponivel();
                            break;
                        case LOGOUT:
                            logout();
                            lout=true;
                            break;
                    }
                }
            }
        }catch(IOException ex){
            
        }finally {
            if(!lout){
                logout();
            }
        }
    }
    
}
