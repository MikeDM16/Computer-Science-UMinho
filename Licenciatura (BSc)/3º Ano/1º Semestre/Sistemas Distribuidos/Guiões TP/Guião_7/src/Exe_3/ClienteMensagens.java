/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_3;

/**
 *
 * @author migue
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteMensagens implements Runnable {
    private Socket cliente;
    private PrintWriter out;
    private BufferedReader in;
    private Mensagens mensagens;
    private final int c; 
    
    public ClienteMensagens(Socket cliente, Mensagens m, int c) throws IOException{
        this.cliente = cliente;
        this.out = new PrintWriter(cliente.getOutputStream(), true);
        this.in = new BufferedReader( new InputStreamReader( cliente.getInputStream()));
        this.mensagens = m; this.c = c; 
    }
    
    public void run() {
        String atual = null;
        try {
            while((atual = in.readLine())!=null){
                if(atual.equals("exit")){
                    out.println("Terminou Sessão\n");
                    System.out.println("O cliente " + c + " terminou sessão.\n");
                    break;
                }else{                
                    mensagens.add(atual);
                }
            }
            out.close();
            in.close();
            cliente.close();        
        } catch (IOException ex) {}        
    }
}
