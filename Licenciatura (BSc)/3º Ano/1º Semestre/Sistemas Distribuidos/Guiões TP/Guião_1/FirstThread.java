
/**
 * Write a description of class exemplo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FirstThread implements Runnable
{
    public FirstThread(){}
    public void run(){
        System.out.println("Ola vindo da thread");
    }
    public static void main(){
        Thread t = new Thread(new FirstThread());
        t.start();
    }
}
