
/**
 * Escreva a descrição da classe teste aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class first_thread_exemplo implements Runnable
{
    public void run(){
        System.out.println("ola vindo da thread");
    }
    
    public static void main(String args[]){
        Thread t = new Thread(new first_thread_exemplo() );
        t.start();
    }
}
