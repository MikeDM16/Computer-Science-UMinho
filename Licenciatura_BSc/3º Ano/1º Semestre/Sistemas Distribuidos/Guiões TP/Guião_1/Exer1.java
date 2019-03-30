
/**
 * Write a description of class Exer1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.concurrent.*;
import java.lang.*;
public class Exer1 implements Runnable
{
    private static int i = 100;
    private static int n = 10;
    public Exer1(){}
    
    public void run(){
        for(int j = 0; j<i; j++){
            System.out.println("Thread imprime numero "+ j + ".");
        }
    }
    
    public static void main(){
        Thread[] t = new Thread[10];
        int j;
        try{
            for(j = 0; j<n; j++){   t[j] = new Thread(new Exer1());}
            for(j = 0; j<n; j++){   t[j].start();}
            for(j = 0; j<n; j++){   t[j].join();}
        }catch(InterruptedException e){}
    }
}
