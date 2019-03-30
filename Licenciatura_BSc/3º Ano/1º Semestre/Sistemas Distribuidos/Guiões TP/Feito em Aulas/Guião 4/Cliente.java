
/**
 * Escreva a descrição da classe Barreira aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
public class Cliente implements Runnable
{
    Barrier b;
    int n;
    public Cliente(Barrier b, int n){
        this.b = b; this.n = n;
    }
    
    public void run(){
        Random rand = new Random();
        int i = 0;
        try{
            while(true){
                i++;
                System.out.println("Thread "+ n +" started stage "+i);
                Thread.sleep(rand.nextInt(5000));
                System.out.println("Thread "+ n +" finished stage "+i);
                b.esperar();
            }
        }catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}
