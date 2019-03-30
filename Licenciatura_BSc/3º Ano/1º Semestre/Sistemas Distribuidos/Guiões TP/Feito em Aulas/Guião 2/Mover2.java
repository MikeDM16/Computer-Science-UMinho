
/**
 * Escreva a descrição da classe Mover2 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
class Mover2 implements Runnable
{
    public static Banco2 b;
    
    public Mover2(Banco2 b) { this.b = b; }
    public void run()
    {
        Random rand = new Random();
        int nContas = b.getNumeroContas2();
        int f, t, tries;
        
        for(tries = 0; tries <1000000; tries++){
            f = rand.nextInt(nContas); // get an acount
            while((t=rand.nextInt(nContas))==f); // get a distint number for t;
            b.take2(f,10); 
            b.put2(t,10);
        }
        
         }
    
    public static void main(String args[]){
        Thread t[] = new Thread[102];
        for(int i=0; i!=102; t[i++] = new Thread(new Mover2(new Banco2())));
        for(int i=0; i!=102; t[i++].start());
        System.out.println("Antes");
        try{
            for (int i=0; i!=102; t[i++].join());
        } catch (InterruptedException e){}
        System.out.println("Saldo total final Contas = " + b.getContas2());
   
    }
}
