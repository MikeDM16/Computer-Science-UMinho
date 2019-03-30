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
import java.io.*;
import java.net.*;
public class Cliente {
    public static void main(String args[]) throws IOException {
        Socket cliente = new Socket("127.0.0.1", 9999);
        PrintWriter out = new PrintWriter( cliente.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( cliente.getInputStream()));
        
        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
        String escreve = null;
        while((escreve = terminal.readLine())!=null){
            out.println(escreve); /*manda para o servidor aquilo que o cliente escreve*/
            System.out.println("O servidor respondeu\n" + in.readLine()); 
            /*escreve no seu terminal aquilo que o servidor responde*/
        }
        in.close(); out.close(); terminal.close(); cliente.close();
    }
}
