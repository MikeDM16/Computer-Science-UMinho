
/**
 * Write a description of class Barreira here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class Barreira
{    
   private final int aguardarN;
   private int emEspera;
   private int estagio;
   private ReentrantLock lockBarreira;
   private Condition esperar;
   
   public Barreira(int n){
       this.aguardarN = n;
       this.emEspera = 0; this.estagio = 0;
       this.lockBarreira = new ReentrantLock();
       this.esperar = lockBarreira.newCondition();
   }
   
   public synchronized void esperar(){
       //lockBarreira.lock();
       int fase = this.estagio;
       emEspera++;
       if(emEspera < aguardarN){
           //lockBarreira.unlock();
           while(fase == this.estagio){
               try{
                   wait();
               }catch(InterruptedException e){}
           }
       }else{
           this.emEspera = 0;
           this.estagio++;
           //lockBarreira.unlock();
          notifyAll();
        }
   }
}