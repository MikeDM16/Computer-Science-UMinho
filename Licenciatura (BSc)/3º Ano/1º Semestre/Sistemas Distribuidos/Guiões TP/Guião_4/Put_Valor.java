
/**
 * Write a description of class Put_Valor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Put_Valor implements Runnable
{
    private BoundedBuffer buffer;
    
    public Put_Valor(BoundedBuffer b){
        this.buffer = b;        
    }
    
    public void run(){
        int n = 10;
        for(int i =0; i!= n; i++){
            System.out.println("Colocou o valor: " + i);
            buffer.put(i);
        }
    }
}
