
/**
 * Write a description of class TestaBarreira here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class TestaBarreira implements Runnable
{
    private Barreira b;
    private int t;
    public TestaBarreira(Barreira b, int t){this.b = b; this.t = t;}
    
    public void run(){
        Random rand = new Random();
        int i = 0;
        try{
            while(true){
                i++;
                System.out.println("A thread " + t + " iniciou o estágio " + i);
                Thread.sleep(rand.nextInt(5000));
                System.out.println("A thread " + t + " acabou o estágio " + i);
                b.esperar();
            }
        }catch (InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}
