
/**
 * Write a description of class MainBanco here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainBanco_exe2 implements Runnable
{
    private Banco banco;
    
    public MainBanco_exe2(Banco b){
        this.banco = b;
    }
    
    public void run(){
        int i = 10000;
        for(int j = 0; j!=i; j++){ banco.credito(2,1);}
        /*adicionar 1 U.M. na conta numero 2.*/
    }
    public static void main(){
        int n = 1000;
        Banco b = new Banco(100); /*Banco com 100 contas */
        Thread[] t = new Thread[n];
        try{
            for(int j = 0; j!=n; j++){t[j] = new Thread(new MainBanco_exe2(b)); }
            for(int j = 0; j!=n; j++){t[j].start(); }
            for(int j = 0; j!=n; j++){t[j].join(); }
        }catch(InterruptedException e){}
        
        System.out.println("Valor final da conta 2: " + b.getSaldo(2));
    }
    
}
