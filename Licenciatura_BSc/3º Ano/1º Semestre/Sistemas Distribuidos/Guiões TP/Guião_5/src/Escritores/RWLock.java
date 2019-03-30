/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Escritores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author miguel
 */
public class RWLock {
    private ReentrantLock lockLeitura, lockEscrita;
    Condition ler, escrever;
    int nEscritores, nLeitores, querEscrever;
    
    public RWLock(){
        this.lockLeitura = new ReentrantLock();
        this.lockEscrita = new ReentrantLock();
        this.ler = lockLeitura.newCondition();
        this.escrever = lockEscrita.newCondition();
        nEscritores = nLeitores = querEscrever = 0;
    }
    
    public void readLock(){ lockLeitura.lock();}
    public void writeLock(){ lockEscrita.lock();}
    public void readUnlock(){ lockLeitura.unlock();}
    public void writeUnlock(){ lockEscrita.unlock();}           
}
