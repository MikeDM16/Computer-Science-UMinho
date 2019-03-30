/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_3;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author migue
 */
public class Mensagens {
    private ReentrantLock lock;
    private Condition esperar;
    private ArrayList<String> mensagens;
    
    public Mensagens(){
        this.lock = new ReentrantLock();
        this.esperar = lock.newCondition();
        this.mensagens = new ArrayList<>();
    }
    public void add(String s){
        lock.lock();
        mensagens.add(s);
        esperar.signalAll();
        lock.unlock();
    }
    
    public String getMensagem() throws InterruptedException{
        lock.lock();
        while(mensagens.size()==0){
            esperar.await();
        }
        String res = mensagens.get(0);
        mensagens.remove(0);
        lock.unlock();
        return res;
    }
    public void esperar() throws InterruptedException { 
        this.esperar.await();
    }
    public void getLock(){ this.lock.lock(); }
    public void getUnlock(){ this.lock.unlock(); }
    
}
