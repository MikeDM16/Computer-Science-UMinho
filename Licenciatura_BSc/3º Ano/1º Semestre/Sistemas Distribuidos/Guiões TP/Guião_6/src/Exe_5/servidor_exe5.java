/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_5;

import java.net.*;
import java.io.*;

/**
 *
 * @author migue
 */
public class servidor_exe5 {
    
    public static void main(String args[]) throws IOException{
        ServerSocket servidor = new ServerSocket(9999);
        Socket cliente = null;
        int i=1;
        System.out.println("Iniciei o servidor");
        while((cliente = servidor.accept())!=null){
            System.out.println("Entrada do cliente " + i++);
            Thread t = new Thread(new SomaCli(cliente));
            t.start();
        }
        //cliente.close();
        servidor.close();        
    }
    
}
