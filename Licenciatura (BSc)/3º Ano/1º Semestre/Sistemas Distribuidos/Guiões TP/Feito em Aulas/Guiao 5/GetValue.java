
/**
 * Escreva a descrição da classe GetValue aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class GetValue implements Runnable
{
    public BoundedBuffer buf;
    public GetValue(BoundedBuffer b){ this.buf = b; }
    
    public void run(){
        int tries;
        
        for(tries = 0; tries<100; tries++){
            int r = buf.get();
            System.out.println("devolveu "+r+".");
        }
    }
}
