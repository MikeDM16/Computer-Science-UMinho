
/**
 * Write a description of class BoundedBuffer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class BoundedBuffer
{
    private ArrayList<Integer> buffer;
    private int tamanho;
    private ReentrantLock lockBuffer;
    private Condition vazio, cheio;
    public BoundedBuffer(){
        this.tamanho = 150;
        this.buffer = new ArrayList(tamanho);
        this.lockBuffer = new ReentrantLock();
        this.cheio = lockBuffer.newCondition();
        this.vazio = lockBuffer.newCondition();
    }
   
    public void put(int v){
        lockBuffer.lock();       
        while(tamanho == buffer.size()){
            try{
                cheio.await();
            }catch(InterruptedException e){}
        }
        buffer.add(v);
        vazio.signal();
        lockBuffer.unlock();
    }
    
    public int get(){
        lockBuffer.lock();
        while(buffer.size() <= 0){
            try{
                vazio.await();
            }catch(InterruptedException e){}
        }
        int r = buffer.get(0);
        buffer.remove(0);
        cheio.signal();
        lockBuffer.unlock();
        return r;
    }
   
    public void valores(){
        for(int i =0; i!= buffer.size(); i++){
            System.out.println("Posição i "+ i + " tem " + buffer.get(i));
        }
    }
}
