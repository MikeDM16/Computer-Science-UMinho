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
public class ServidorBanco {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       ServerSocket servidor = new ServerSocket(9999);
       Socket cliente = null;
       Banco bank = new Banco();
       
       while((cliente = servidor.accept())!=null){
           Thread t = new Thread(new ClienteBanco(cliente, bank));
           t.start();
       }
       cliente.close();
       servidor.close();
    }
    
}
