
/**
 * Escreva a descrição da classe BoundedBuffer aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class BoundedBuffer
{
    private int write, tamanho;
    private int[] buf;
    //a var for the readind position ? 
    public BoundedBuffer(int t){
        this.tamanho = t; 
        buf = new int[tamanho];
        write = 0;
    }
    
    public synchronized void put(int v){
        while(write + 1 >= tamanho){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        buf[write++] = v; //write++;
        notifyAll();
    }
    
    public synchronized int get(){
        while(write - 1 == 0 ){
            try{
                wait();
            }catch (InterruptedException e){}
        }
        notifyAll();
        int r = buf[write--];
        return r;
    }
}
