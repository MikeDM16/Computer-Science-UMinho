/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_1;

import java.io.IOException;
import java.io.*;
import java.net.*;

/**
 *
 * @author migue
 */
public class Cliente_exe1 {
    public static void main(String[] args) throws IOException{
        Socket cliSocket = new Socket("127.0.0.1", 9999);
        
        PrintWriter outCli = new PrintWriter( cliSocket.getOutputStream(), true);
        BufferedReader inCli = new BufferedReader( new InputStreamReader( cliSocket.getInputStream() ));
        
        String escreve;
        BufferedReader cliInput = new BufferedReader( new InputStreamReader( System.in ));
        /*bufferedReader para ler aquilo que o cliente escreve*/
        
        while((escreve = cliInput.readLine())!=null){
            if(escreve.equals("fim")){break;}
            outCli.println(escreve); 
            /*manda o imput do cliente, do System.in para o servidor*/
            
            System.out.println("Recebe do servidor: " + inCli.readLine());
            /*Imprime no seu terminal aquilo que recebe do servidor*/
        }
        inCli.close(); outCli.close(); cliInput.close();
        cliSocket.close();
    }
}
