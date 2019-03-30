
/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main_exe1
{
    public static void main(){
        BoundedBuffer buffer = new BoundedBuffer();
        int nThreads = 10;
        Thread[] threadsPut = new Thread[nThreads];
        Thread[] threadsGet = new Thread[nThreads];
        System.out.println("Inicio");
        buffer.valores();
        try{
            for(int i=0; i!= nThreads; i++){
                threadsPut[i] = new Thread(new Put_Valor(buffer));
                threadsGet[i] = new Thread(new Get_Valor(buffer));
            }
            for(int i=0; i!= nThreads; i++){
                threadsPut[i].start();
                threadsGet[i].start();
            }
            for(int i=0; i!= nThreads; i++){
                threadsPut[i].join();
                threadsGet[i].join();
            }
        }catch(InterruptedException e){}
        System.out.println("Fim");
        buffer.valores();
    }
    
    
}
