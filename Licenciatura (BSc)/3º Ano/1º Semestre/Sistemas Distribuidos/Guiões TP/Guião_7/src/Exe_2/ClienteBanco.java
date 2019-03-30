/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_2;

/**
 *
 * @author migue
 */
import java.net.*;
import java.io.*;

public class ClienteBanco implements Runnable {
    private Banco banco;
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;
    
    public ClienteBanco(Socket cliente, Banco banco) throws IOException {
        this.banco = banco;
        this.cliente = cliente;
        this.out = new PrintWriter( cliente.getOutputStream(), true);
        this.in = new BufferedReader( new InputStreamReader( cliente.getInputStream()));
    }
    
    public void run (){
        
    }
}
