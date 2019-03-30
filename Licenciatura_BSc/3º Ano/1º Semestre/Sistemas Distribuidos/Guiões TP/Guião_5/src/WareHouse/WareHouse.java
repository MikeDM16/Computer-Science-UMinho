/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WareHouse;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author migue
 */
public class WareHouse {
    private Map<String, Item> items;
    private ReentrantLock lockArmazem;
    
    
public WareHouse(){
    this.items = new TreeMap<String, Item>();
    this.lockArmazem = new ReentrantLock();
}   

public void suply(String item, int quantity){
    lockArmazem.lock();
    Item novo = null;
    if(!items.containsKey(item)){
        novo = new Item(item, quantity);
        items.put(item, novo);
        lockArmazem.unlock();
    }else{
        novo = items.get(item);
        try{
            novo.getLock();
            novo.addQuantidade(quantity);
            novo.getUnlock();
            novo.getSignal();
        }finally{
            novo.getUnlock();
            lockArmazem.unlock();
        }
    }
}

public void consume(String[] produtos) throws InterruptedException{
    lockArmazem.lock();
    int i;
    Item item = null;
    String id = null;
    for(i = 0; i<produtos.length; i++){
        id = produtos[i];
        item = items.get(id);
        item.getLock();
        while(item.getQuantidade() < 0){
            lockArmazem.unlock();
            item.esperaStock();
            lockArmazem.lock();
        }
        item.consume();
        item.getUnlock();       
    }
    lockArmazem.unlock();
}
}
