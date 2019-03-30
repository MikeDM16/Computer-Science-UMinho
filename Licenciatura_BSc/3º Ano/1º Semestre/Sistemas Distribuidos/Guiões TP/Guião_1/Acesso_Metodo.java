
/**
 * Write a description of class Acesso_Metodo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Acesso_Metodo implements Runnable
{
    private static Counter c;
    public Acesso_Metodo(Counter c){ this.c = c; }
    
    public void run(){
        int i = 10000;
        for(int j = 0; j!= i; j++){
            synchronized(c){ c.increment(); }
            /*ou se usa a clausula synchronized aqui, ou então na declaração do metodo increment na 
             * classe Counter. Com este alteração já nao ocorrem race Conditions*/
        }
    }
    
    public static void main(){
        Counter c = new Counter();
        int n = 1000;
        try{
            Thread[] t = new Thread[n];
            for(int j = 0; j!=n; j++){t[j] = new Thread(new Acesso_Metodo(c));}
            for(int j = 0; j!=n; j++){t[j].start();}
            for(int j = 0; j!=n; j++){t[j].join(); }    
        }catch(InterruptedException e){}
        
        System.out.println( "Valor final: " + c.getValor());
    }
    
}
