/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_1;

import java.io.*;
import java.net.*;
import java.net.Socket;

/**
 *
 * @author migue
 */
public class Servidor_exe1 {
    
    public static void main(String args[]) throws IOException{
        ServerSocket ss = new ServerSocket(9999);
        Socket cli = null;
        String inString;
        PrintWriter outCli = null;
        BufferedReader inCli = null;
        int i = 1;
        while((cli=ss.accept()) != null){
            /*Como atende umn cliente de cada vez nao faço threads ? Meto aqui o ciclo enquanto há cliente*/
            outCli = new PrintWriter( cli.getOutputStream(), true);
            inCli = new BufferedReader( new InputStreamReader( cli.getInputStream() ));
            System.out.println("Estou a trabalhar com o cliente "+ i);
            while((inString = inCli.readLine())!=null){
                outCli.println(inString);
                System.out.println("Servidor got echo do cliente " + i + ": "+ inString);
            }
            System.out.println("Acabei de trabalhar com o cliente " + i++);
            inCli.close();
            outCli.close();
        }
        cli.close();
    }
}
