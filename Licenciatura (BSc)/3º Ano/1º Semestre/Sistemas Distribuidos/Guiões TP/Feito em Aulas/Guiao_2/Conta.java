
/**
 * Escreva a descrição da classe Conta aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Conta 
{
    private float saldo;
    // ao aplicar a clausula synchronized nos metodos importantes já nao ocorrem corridas em zonas criticas
    
    public Conta(){
        this.saldo = 0;
    }
    
    public synchronized void credito(float q) {
        this.saldo += q;
    }
    public synchronized void debito(float q){
        this.saldo -= q;
    }
    
    public  float consulta(){ return this.saldo; }
    
}
