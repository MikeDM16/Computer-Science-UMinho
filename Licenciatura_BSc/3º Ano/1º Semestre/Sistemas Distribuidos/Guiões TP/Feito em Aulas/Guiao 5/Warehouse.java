
/**
 * Escreva a descrição da classe Warehouse aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;
public class Warehouse
{
   ReentrantLock lockArmazem;
   Condition ocupado;
   Condition desocupado;
   Map<String, Item> armazem;
   
   public Warehouse(){
       lockArmazem = new ReentrantLock();
       ocupado = lockArmazem.newCondition();
       desocupado = lockArmazem.newCondition();
       
       armazem = new TreeMap();
   }
   
   public void suply(String item, int quantity){
       lockArmazem.lock();
       Item c = null;
       if(armazem.containsKey(item)){
           try{
              c = armazem.get(item);
              c.getLock();
              lockArmazem.unlock();
              c.addQuantidade(quantity);
            }finally{
               c.getUnlock();
               lockArmazem.unlock();
            }
       }else{
           armazem.put(item, new Item(item, quantity) ); 
           lockArmazem.unlock();
       }    
   }
   
   
}
