
/**
 * Escreva a descrição da classe Banco aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
class Mover implements Runnable
{
    public static Banco b;
    
    public Mover(Banco b) { this.b = b; }
    public void run()
    {
        Random rand = new Random();
        int nContas = b.getNumeroContas();
        int f, t, tries;
        
        for(tries = 0; tries <1000000; tries++){
            f = rand.nextInt(nContas); // get an acount
            while((t=rand.nextInt(nContas))==f); // get a distint number for t;
            synchronized (b) { 
                b.take(f,10); 
                b.put(t,10);
            }
        }
        
         }
    
    public static void main(String args[]){
        Thread t[] = new Thread[102];
        for(int i=0; i!=102; t[i++] = new Thread(new Mover(new Banco())));
        for(int i=0; i!=102; t[i++].start());
        System.out.println("Antes");
        try{
            for (int i=0; i!=102; t[i++].join());
        } catch (InterruptedException e){}
        System.out.println("Saldo total final Contas = " + b.getContas());
   
    }
}
