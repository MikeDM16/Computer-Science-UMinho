
/**
 * Write a description of class Conta here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Conta
{
    private int valor; 
    
    public Conta(){this.valor = 0; }
    
    public void credito(int v){ this.valor = this.valor + v;}
    public void debito(int v){ this.valor = this.valor - v;}
    public int consulta(){ return this.valor; }
}
