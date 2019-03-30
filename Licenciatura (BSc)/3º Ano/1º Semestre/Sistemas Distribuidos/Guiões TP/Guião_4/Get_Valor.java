
/**
 * Write a description of class Get_Valor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Get_Valor implements Runnable
{
    private BoundedBuffer buffer;
    
    public Get_Valor(BoundedBuffer b){
        this.buffer = b;
    }
    
    public void run(){
        int n = 9;
        for(int i = 0; i!=n; i++){
            System.out.println("Devolveu o valor " + buffer.get());
        }
    }
    
    
}
