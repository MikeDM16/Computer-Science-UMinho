
/**
 * Write a description of class Conta_exe4 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Conta_exe4
{
    private int valor; 
    
    public Conta_exe4(){this.valor = 0; }
    
    public synchronized void credito(int v){ this.valor = this.valor + v;}
    public synchronized void debito(int v){ this.valor = this.valor - v;}
    public synchronized int consulta(){ return this.valor; }
}
