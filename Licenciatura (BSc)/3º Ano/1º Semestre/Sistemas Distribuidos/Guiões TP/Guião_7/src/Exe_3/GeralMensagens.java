/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_3;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migue
 */
public class GeralMensagens implements Runnable {
    private Socket cliente;
    private PrintWriter out;
    private BufferedReader in;
    private Mensagens mensagens;
    
    public GeralMensagens(Socket cliente, Mensagens m) throws IOException{
        this.cliente = cliente;
        this.out = new PrintWriter(cliente.getOutputStream(), true);
        this.in = new BufferedReader( new InputStreamReader( cliente.getInputStream()));
        this.mensagens = m;
    }
        
    public void run(){
        String s;
        while(true){
            try {
                while((s = mensagens.getMensagem())!=null){
                    out.println("Foi adicionada a mensagem " + s);
                }
            } catch (InterruptedException ex) {}
        }
    }
    
}
