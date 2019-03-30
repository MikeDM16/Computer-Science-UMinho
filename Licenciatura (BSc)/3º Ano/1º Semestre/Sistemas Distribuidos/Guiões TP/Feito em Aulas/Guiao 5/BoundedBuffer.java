
/**
 * Escreva a descrição da classe BoundedBuffer aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;
public class BoundedBuffer
{
    ArrayList<Integer> buffer;
    int tamanho;
    ReentrantLock lock;
    Condition cheio;
    Condition vazio;
    public BoundedBuffer(int t){
       tamanho = t;
       buffer = new ArrayList(t);
        
       lock = new ReentrantLock();
       cheio = lock.newCondition();
       vazio = lock.newCondition();
    }
    
    public synchronized void put(int v){
        while( buffer.size() == tamanho ){
            try{
                cheio.await();
            }catch(InterruptedException e){}
        }
        buffer.add(v);
        vazio.signal();
    }
    
    public synchronized int get(){
        int r;
        while( buffer.size() == 0 ){
            try{
               vazio.await();
            }catch(InterruptedException e){}
        }
        r = buffer.get(0);
        buffer.remove(0);
        
        cheio.signal();
        
        return r;
    }
}
