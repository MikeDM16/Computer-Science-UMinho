
/**
 * Escreva a descrição da classe Writer aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Writer implements Runnable
{
    RWLock rw;
    
    public Writer(RWLock rw){ this.rw = rw; }
    
    public void escrever() throws InterruptedException {
        while( rw.nEscritores >0 || rw.nLeitores > 0 ){
            rw.escritores.await();
        }
        rw.writerLock();
        rw.nEscritores++;
        rw.escrever();
        rw.nEscritores--;
        rw.writerUnlock();
        rw.escritores.signalAll();
        rw.leitores.signalAll();
       
    }
    
    public void run(){}
}
