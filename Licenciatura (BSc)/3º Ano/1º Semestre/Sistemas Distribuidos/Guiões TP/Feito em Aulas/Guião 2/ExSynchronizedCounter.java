
/**
 * Escreva a descrição da classe s aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class ExSynchronizedCounter
{
    public static void main(String args[]){
        try{
            Contador c;
            Incrementer worker;
            Thread t[] = new Thread[10];
            c = new Contador();
            worker = new Incrementer(c);
            
            for(int i = 0; i<10; i++){
                t[i] = new Thread(worker);
            }
            
            for(int i=0; i<10; i++)
                t[i].start();
            
            for(int i = 0; i<10; i++){
                t[i].join();
            }
            
            System.out.println(c.get());
        } catch(InterruptedException e){}
    }
}
