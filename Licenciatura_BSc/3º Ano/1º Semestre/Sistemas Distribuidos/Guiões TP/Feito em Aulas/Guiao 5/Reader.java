
/**
 * Escreva a descrição da classe Reader aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class Reader implements Runnable
{
   RWLock rw; 
   
   public Reader(RWLock rw){  this.rw = rw;   }
   
   public void ler() throws InterruptedException {
       while(rw.nEscritores != 0){
          rw.leitores.await();
        }
       rw.readLock();
       rw.nLeitores++;
       rw.ler();
       rw.nLeitores--;
       rw.readUnlock();
       
       if(rw.nLeitores == 0 ){
           rw.escritores.signal();
       }     
   }
   
   public void run(){}
}
