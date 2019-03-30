
/**
 * Escreva a descrição da classe Conta aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Conta
{
    private int saldo;
    
    public Conta(int v){this.saldo = v; }
    
    public  void adiciona(int v){this.saldo += v;}
    public void remove(int v){this.saldo -= v;}
    
    public int saldo(){ return this.saldo; }
}
