
/**
 * Escreva a descrição da classe Testar aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
public class PutValue implements Runnable
{
    public BoundedBuffer buf;
    
    public PutValue(BoundedBuffer b){this.buf = b; }
    
    public void run(){
        int v, tries;
        Random rand = new Random();
        for(tries = 0; tries<10; tries++){
            v = rand.nextInt(100);
            System.out.println("Adicionou o valor "+ v + ".");
            buf.put(v);
        }
    }
    
}
