/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_5;

/**
 *
 * @author migue
 */
import java.io.*;
import java.net.*;

public class Cliente_exe5 {
 
    
    public static void main(String args[]) throws IOException{
        Socket cliente = new Socket("127.0.0.1", 9999);
        
        PrintWriter out = new PrintWriter( cliente.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        
        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
        String leitura;
        
        while((leitura = terminal.readLine())!=null){
            out.println(leitura);
            System.out.println("O cliente recebeu do servidor\n" + in.readLine());
        }
        
        terminal.close();
        in.close();
        out.close();        
    }
}
