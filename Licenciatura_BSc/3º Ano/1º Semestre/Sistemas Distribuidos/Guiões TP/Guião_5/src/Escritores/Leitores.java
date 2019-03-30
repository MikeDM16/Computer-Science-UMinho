/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Escritores;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migue
 */
public class Leitores implements Runnable {
    
    private RWLock reg; 
    
    public Leitores(RWLock reg){this.reg = reg;}
    
    public void run(){
        try {
            this.ler();
        } catch (InterruptedException ex) {}
    }
    public void ler() throws InterruptedException{ 
        reg.readLock();
        while(reg.nEscritores > 0){
            try{
                reg.ler.await();
            }catch(InterruptedException e){}
        }
        reg.nLeitores++;
        reg.readUnlock();
        
        /*Ler = processo "demorado"*/
        System.out.println("comecei a ler");
        sleep(3);
        System.out.println("acabei de ler");
        
        reg.readLock();
        reg.nLeitores--;
        if( reg.nLeitores == 0){ reg.escrever.signal(); }
        else{ reg.ler.signalAll();}
        reg.readUnlock();       
    }
}
