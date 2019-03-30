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
public class Escritores implements Runnable {

    private RWLock reg; 
    
    public Escritores(RWLock reg){this.reg = reg;}
    
    @Override
    public void run() {
        try {
            this.escrever();
        } catch (InterruptedException e) {}
    }
    
    public void escrever() throws InterruptedException{
        reg.writeLock();
        reg.querEscrever++;
        while(reg.nEscritores > 0 || reg.nLeitores > 0 ){
            try{
                reg.escrever.await();
            }catch(InterruptedException e){}
            }
        reg.querEscrever--;
        reg.nEscritores++;
        reg.writeUnlock();
        
        /*Escrever = processo demorado*/
        System.out.println("comecei a escrever");
        sleep(5);
        System.out.println("acabei de escreverr");
        
        reg.writeLock();
        reg.nEscritores--;
        if(reg.querEscrever > 0) {reg.escrever.signal();}
        else{reg.ler.signal();}
    }
}
