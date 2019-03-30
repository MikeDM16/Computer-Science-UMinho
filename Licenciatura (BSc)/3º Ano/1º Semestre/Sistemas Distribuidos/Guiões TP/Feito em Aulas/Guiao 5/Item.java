
/**
 * Escreva a descrição da classe Item aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
public class Item
{
    private int quantity;
    private String nome;
    ReentrantLock lock;
    public Item(String s, int n){
        this.quantity = n; 
        this.nome = s;
        lock = new ReentrantLock();
    }
    
    public String getNome(){return this.nome;}
    public int getQuantidade(){return this.quantity;}
    
    public void addQuantidade(int q){this.quantity+=q;}
    
    public void getLock(){this.lock.lock();}
    public void getUnlock(){this.lock.unlock();}
    
}
