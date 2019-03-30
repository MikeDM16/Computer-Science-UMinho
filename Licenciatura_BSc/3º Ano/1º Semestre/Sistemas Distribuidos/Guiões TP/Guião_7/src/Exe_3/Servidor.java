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

public class Servidor {
    public static void main(String args[]) throws IOException {
        ServerSocket servidor = new ServerSocket(9999);
        Socket cliente = null;
        Mensagens m = new Mensagens();
        int i = 0;
        System.out.println("Ligou-se o servidor: ");
        while((cliente = servidor.accept())!=null){
            System.out.println("Ligou-se o cliente: " + i);
            Thread t1 = new Thread(new ClienteMensagens(cliente, m, i++));
            Thread t2 = new Thread(new GeralMensagens(cliente, m));
            t1.start(); t2.start();
        }
        cliente.close();
    }
}
