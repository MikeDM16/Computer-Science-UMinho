/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Escritores;

import Escritores.RWLock;
import Escritores.Leitores;
import Escritores.Escritores;

/**
 *
 * @author migue
 */
public class mainRWLock {
    
    public static void main(String[] args){
        RWLock var = new RWLock();
        int nLeitores = 10;
        int nEscritores = 10;
        Thread[] ler = new Thread[nLeitores];
        Thread[] escrever = new Thread[nEscritores];
        
        for(int i =0; i!= nLeitores; i++){
            escrever[i] = new Thread(new Escritores(var));
            ler[i] = new Thread(new Leitores(var));            
        }
         for(int i =0; i!= nLeitores; i++){
            escrever[i].start();
            ler[i].start();
        }
        try{
             for(int i =0; i!= nLeitores; i++){
                escrever[i].join();
                ler[i].join();
             }
        }catch(InterruptedException e){}
    }
    
    
}
