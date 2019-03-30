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
public class Cliente_exe2 {
    
    public static void main(String args[]) throws IOException{
        Socket cliente = new Socket("127.0.0.1", 9999);
        PrintWriter outCli = new PrintWriter( cliente.getOutputStream(), true);
        BufferedReader inCli = new BufferedReader( new InputStreamReader(cliente.getInputStream()));
        String atual;
        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
        while((atual = terminal.readLine())!=null){
            outCli.println(atual);
            System.out.println("recebi do servidor\n" + inCli.readLine() );
        }
        inCli.close(); outCli.close(); cliente.close();
    }    
}
