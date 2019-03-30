
/**
 * Write a description of class Exer2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Acesso_Direto implements Runnable
{
    private static Counter c;
    public Acesso_Direto(Counter c){ this.c = c; }
    
    public void run(){
        int i = 10000;
        for(int j = 0; j!= i; j++){
            synchronized(c){c.valor++; }
            /*usar a clausula synchronized aqui. Com este alteração já nao ocorrem race Conditions*/
        }
    }
    
    public static void main(){
        Counter c = new Counter();
        int n = 1000;
        try{
            Thread[] t = new Thread[n];
            for(int j = 0; j!=n; j++){t[j] = new Thread(new Acesso_Direto(c));}
            for(int j = 0; j!=n; j++){t[j].start();}
            for(int j = 0; j!=n; j++){t[j].join(); }    
        }catch(InterruptedException e){}
        
        System.out.println( "Valor final: " + c.getValor());
    }
}
