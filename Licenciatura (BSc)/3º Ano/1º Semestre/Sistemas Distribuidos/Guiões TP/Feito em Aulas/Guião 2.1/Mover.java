
/**
 * Escreva a descrição da classe Mover aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
public class Mover implements Runnable
{
    public static Banco b;
    
    public Mover(Banco b){ this.b = b;}
    public void run(){
        Random rand = new Random();
        int nContas = b.getNumeroContas();
        int f,t,tries;
        
        for(tries = 0; tries != 1000000; tries++){
            f = rand.nextInt(nContas);
            while((t=rand.nextInt(nContas))==f);
            synchronized (b.banco[t]){
                synchronized (b.banco[f]){
                    b.removeV(t, 10);
                    b.adicionaV(f, 10);
                }
            }
        }
    }
    
    public static void main(String args[]){
        Thread t[] = new Thread[10];
        for(int i = 0; i!=10; t[i++] = new Thread(new Mover(new Banco())));
        System.out.println("antes da execuçao");
        for(int i = 0; i!=10; t[i++].start());
        try{
            for(int i = 0; i!=10; t[i++].join());
        }catch (InterruptedException e){}
        System.out.println("Depois da execuçao");
    }
}
