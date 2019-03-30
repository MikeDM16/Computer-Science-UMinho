
/**
 * Write a description of class MainBanco_exe4 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;

public class MainBanco_exe4 implements Runnable
{
    private Banco_exe4 banco;
    
    public MainBanco_exe4(Banco_exe4 b){
        this.banco = b;
    }
    
    public void run(){
        Random rand = new Random();
        int nContas = banco.nrContas();
        int f,t,tries;
        
        for(tries = 0; tries != 100000; tries++){
            f = rand.nextInt(nContas);
            while((t=rand.nextInt(nContas))==f);
            banco.transfer(t,f,10);
        }
    }
    public static void main(){
        int n = 1000,j, saldo = 0;
        Banco_exe4 b = new Banco_exe4(100); /*Banco com 100 contas */
        Thread[] t = new Thread[n];
        try{
            for(j = 0; j!=n; j++){t[j] = new Thread(new MainBanco_exe4(b)); }
            for(j = 0; j!=n; j++){t[j].start(); }
            for(j = 0; j!=n; j++){t[j].join(); }
        }catch(InterruptedException e){}
        for(j = 0; j!= 100; j++){
            saldo += b.getSaldo(j);
        }
        System.out.println("Valor final do banco: " + saldo);
    }
}
