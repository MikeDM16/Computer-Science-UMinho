/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_2;

import java.net.*;
import java.io.*;

/**
 *
 * @author migue
 */
public class Servidor_exe2 {
    
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(9999);
        Socket cliente = null;
        PrintWriter outCli = null; BufferedReader inCli = null;
        String recebe = null; 
        int valor = 0, ntries, i=0;
        while((cliente = servidor.accept())!=null){
            i++;
            outCli = new PrintWriter(cliente.getOutputStream(), true);
            inCli = new BufferedReader( new InputStreamReader( cliente.getInputStream()));
            valor = 0; ntries = 1;
            System.out.println("recebi um novo cliente");
            while((recebe = inCli.readLine())!=null){
                if(recebe.equals("Fim")){
                    outCli.println("A sua média de valores é :" + (valor/ntries));
                    System.out.println("O cliente  " + i + " terminou com média" + (valor/ntries));
                    break;
                }else{
                    valor += (int) Integer.parseInt(recebe);
                    outCli.println("A sua soma de valores atual é :" + (valor));
                    ntries++;
                }         
            }            
            outCli.close(); 
            inCli.close(); 
            cliente.close();
        }
    }
    
    
}
