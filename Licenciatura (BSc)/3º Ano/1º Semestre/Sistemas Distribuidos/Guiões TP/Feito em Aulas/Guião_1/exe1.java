
/**
 * Escreva a descrição da classe exe1 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class exe1 implements Runnable
{
    int i = 10; // N threads
    
    public void run(){
        for(int j=1; j!=i; j++){
            System.out.println("número " + j);
        }
    }
    
    public static void main(int arg){
        Thread arr[] = new Thread[arg];
        System.out.println("Antes de criar as Threads");
        int j;
        for(j = 0; j!= arg; j++){
            arr[j] = new Thread(new exe1());
        }
        
        for(j = 0; j!= arg; j++){
            arr[j].start();
        }
        System.out.println("Depois da criaçao das Threads");
        
        for(j = 0; j!= arg; j++){
            try{
                arr[j].join();
            }catch( InterruptedException e){}
        }
        
        System.out.println("Fim programa principal");
    }
}
