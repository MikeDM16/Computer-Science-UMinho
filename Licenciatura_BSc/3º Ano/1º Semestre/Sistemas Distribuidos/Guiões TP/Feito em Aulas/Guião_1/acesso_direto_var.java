
/**
 * Escreva a descrição da classe exe2 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class acesso_direto_var implements Runnable
{
    int i=100000; // N Threads
    static Counter c; 
    public void run(){
        for(int j=0; j!=i; j++){
            c.contador++;
        }
    }
    
    public static void main(int arg){
        Thread arr[] = new Thread[arg];
        int j;
        c = new Counter();
        for(j=0; j!=arg; j++){
            arr[j] = new Thread(new acesso_direto_var());
        }
        
        for(j=0; j!= arg; j++){
            arr[j].start();
        }
        
        for(j=0; j!=arg; j++){
            try{
                arr[j].join();
            }catch (InterruptedException e){}
        }
              
        System.out.println("Valor do contador = " + c.contador);
    }
   
}
