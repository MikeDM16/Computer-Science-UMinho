/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_5;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migue
 */
public class SomaCli implements Runnable{
    
    private int soma;
    private int n;
    private Socket cliente;
    private PrintWriter out; 
    private BufferedReader in;
    
    public SomaCli(Socket cliente) throws IOException{
        this.cliente = cliente;
        this.out = new PrintWriter(cliente.getOutputStream() , true);
        this.in = new BufferedReader( new InputStreamReader(cliente.getInputStream()));
        this.soma = 0; this.n = 0;
    }
    
    public void run(){
        String atual = null;
               
        try {
            while((atual=in.readLine())!=null){
                if(atual.equals("exit")){
                    out.println("A sua média final é de: "+ (soma/n));
                    System.out.println("O cliente saiu com uma média de " + (soma/n));
                    break;        
                }else{                     
                    try{
                        soma += Integer.parseInt(atual);
                    }catch(NumberFormatException e){}
                    n++;
                    out.println("A sua soma atual é: " + (soma));
                }
            }
            in.close(); out.close();
            cliente.close();
        } catch (IOException ex) {}
    }
    
    
}
