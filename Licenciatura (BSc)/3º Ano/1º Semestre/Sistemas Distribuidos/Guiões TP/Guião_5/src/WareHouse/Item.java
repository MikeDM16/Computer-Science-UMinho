/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WareHouse;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author migue
 */
public class Item {
    private String id;
    private int quantity;
    private ReentrantLock lockItem;
    private Condition esgotado;
    
    public Item(String id, int quantaty){
        this.quantity = quantity;
        this.id = id;
        this.lockItem = new ReentrantLock();
        this.esgotado = lockItem.newCondition();
    }
    
    public void getSignal(){
        esgotado.signalAll();
    }
    public void esperaStock() throws InterruptedException{
        esgotado.await();
    }
    public void consume(){this.quantity--; }
    public void addQuantidade(int quantidade){
        this.quantity += quantidade; 
    }
    public int getQuantidade(){return this.quantity; }
    public void getLock(){
        this.lockItem.lock();
    }
    public void getUnlock(){
        this.lockItem.unlock();
    }
    
}