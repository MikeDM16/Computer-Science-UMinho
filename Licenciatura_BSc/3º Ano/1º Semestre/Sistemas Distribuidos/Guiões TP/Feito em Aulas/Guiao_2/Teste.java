
/**
 * Escreva a descrição da classe Teste aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
public class Teste implements Runnable
{
    public static Banco b;
    private int i; /// i = N threads
    
    public void run(){
        Random rand = new Random();
        int ncontas = b.getNContas();
        int from, to, tries;
        
        for(tries = 0; tries < 100000; tries++){
            from = rand.nextInt(ncontas); // get an acount
            while((to=rand.nextInt(ncontas)) == from); //get another acount
            b.transferir(from, to, 10);
        }
        
    }
    
    public Teste(){
        this.b = new Banco();
    }
       
    public static void main(int arg){
        int i = arg;
        Thread[] arr = new Thread[i];
        System.out.println("Antes de executar");
        for(int j = 0; j!=i; j++){
            arr[j] = new Thread( new Teste() );
        }
        
        for(int j = 0; j!=i; j++){
            arr[j].start();
        }
        System.out.println("Depois de executar");
        try{
            for(int j = 0; j!=i; j++){
                arr[j].join();
            }
        }catch (InterruptedException e){}
        System.out.println("fim de executar");
        System.out.println("Total monerário contas = " + b.getValorTotal());
    }
}
