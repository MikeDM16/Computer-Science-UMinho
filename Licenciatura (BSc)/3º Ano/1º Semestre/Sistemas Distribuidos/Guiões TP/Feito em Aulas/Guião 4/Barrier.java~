
/**
 * Escreva a descrição da classe Barrier aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Barrier
{
   final int N;
   int proc = 0;
   int stage = 0;
   
   public Barrier(int n){this.N = n; }
   
   public synchronized void esperar() throws InterruptedException 
   {
       int local = stage;
       proc++;
       if(proc < N){
           while(local == stage){
               wait();
            }
       }else{
           stage++;
           proc = 0;
           notifyAll();
        }
   }
   
    /*private int N;
    private int nEspera;
    public Barrier(int N){this.N = N; nEspera = 0; }
    
    public synchronized void esperar(){
        while(nEspera < N ){
            try{
                nEspera++;
                wait();
            }catch (InterruptedException e){}
        }
        notifyAll();
        nEspera = 0;
       
    }
    */
   
}
