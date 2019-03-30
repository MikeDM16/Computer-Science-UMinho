
/**
 * Escreva a descrição da classe metodo_increment aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class metodo_increment implements Runnable
{
    int i = 100000; // N Threads
    static Counter x;
    public void run(){
        for(int j =0; j!=i; j++){
            x.increment();
        }
    }
    
    public static void main(int arg){
        int j;
        x = new Counter();
        Thread arr[] = new Thread[arg]; 
        for(j=0; j!= arg; j++){
            arr[j] = new Thread(new metodo_increment() ); 
        }
        
        for(j=0; j!=arg; j++){
            arr[j].start();
        }
        
        for(j=0; j!=arg; j++){
            try{
                arr[j].join();
            }catch (InterruptedException e){}
        }
        
        System.out.println("Valor final de x = " + x.contador);
    }
}
