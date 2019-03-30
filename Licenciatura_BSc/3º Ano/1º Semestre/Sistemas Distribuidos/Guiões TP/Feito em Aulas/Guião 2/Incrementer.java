
/**
 * Escreva a descrição da classe lol aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Incrementer implements Runnable
{
    Contador c;
    Incrementer(Contador c1){
        c = c1;
    }
    public void run(){
        for(long i =0; i < 10000000; i++)
            c.inc();
        /*
           removendo o Synchronizer da class contador!
           for(long i =0; i < 10000000; i++)
            synchronized (c) { c.inc(); }
           */
    }
}
