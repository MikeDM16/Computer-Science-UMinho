
/**
 * Escreva a descrição da classe Produto aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class Warehouse2
{
    private Lock l = new ReentrantLock();
    private Map<String, Produto> m = new HashMap<String, Produto>();
    
    private class Produto{
        int q = 0;
        Condition c = l.newCondition();
    }
    
    private Produto get(String s){
        Produto p = m.get(s);
        if( p!= null ) return p;
        p = new Produto();
        m.put(s,p);
        return p; 
    }
    
    public void suply(String s, int q) throws InterruptedException {
        l.lock();
        try{
            Produto p = get(s);
            p.q += q;
            p.c.signalAll();
        }finally {
            l.unlock();
        }
    }
    
    public void consume(String[] a)throws InterruptedException {
        l.lock();
        try{
            for(int i = 0; i<a.length; )
            {
                Produto p = get(a[i]);
                i++;
                if(p.q == 0 ){
                    //Se nao houver um stock de um produto, adormece e recomeça a lista com i=0
                    p.c.await();
                    i = 0;
                }
            }
            for(String s: a){
                get(s).q--;
            }
        }finally{
            l.unlock();
        }
    }
}
