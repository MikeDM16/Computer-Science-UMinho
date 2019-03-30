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

public class Cliente {
    
    public static void main(String args[]) throws IOException{
        Socket cliente = new Socket("127.0.0.1", 9999);
        BufferedReader inCli = new BufferedReader(new InputStreamReader( cliente.getInputStream()));
        PrintWriter outCli = new PrintWriter(cliente.getOutputStream(), true);
        
        BufferedReader terminal = new BufferedReader( new InputStreamReader(System.in));
        String escreve;
        
        while((escreve = terminal.readLine())!=null){
            outCli.println(escreve);
            System.out.println("Recebeu do servidor:"+ inCli.readLine());
        }
        inCli.close(); outCli.close(); cliente.close(); terminal.close();
    }
    
}
