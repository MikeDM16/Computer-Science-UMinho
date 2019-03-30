
/**
 * Escreva a descrição da classe Conta aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.concurrent.locks.ReentrantLock;
public class Conta
{
    float saldo;
    ReentrantLock lockConta; 
    public Conta(float saldo){
        this.saldo = saldo;
        lockConta = new ReentrantLock();
    }
    
    public void adiciona(float valor) { this.saldo += valor; }
    public int retira(float valor) { 
        if(saldo-valor < 0){ return 1;}
        saldo-=valor;
        return 0;        
    }
    
    public float getSaldo(){ return this.saldo; }
    public void getLockConta(){this.lockConta.lock(); }
    public void getUnlockConta(){ this.lockConta.unlock(); }
}
