
/**
 * Escreva a descrição da classe Banco aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;

public class Banco
{
    public Map<Integer, Conta> banco;
    public ReentrantLock lockBanco;
    public int nrConta; 
    
    public Banco(){
        banco = new HashMap<>();
        lockBanco = new ReentrantLock();
        nrConta = 0; 
    }
    
    public int createAccount(float inicialBalance){
        int nr; Conta newConta = null;
        
        // Lock ao banco para ver o numero da conta a adicionar
        lockBanco.lock();
        nr = nrConta;
        nrConta++;
        lockBanco.unlock();
        
        // Criar a nova conta sem ter o banco bloqueado para si
        newConta = new Conta(inicialBalance); 
        
        //Lock ao banco para adicionar a nova conta criada
        lockBanco.lock();
        banco.put(nr, newConta);
        lockBanco.unlock();
        
        //Devolde o número da conta criada
        return nr;
    }
    
    float closeAccount(int id) throws InvalidAccount{
        float saldo = 0;
        Conta c = null;
        
        //Lock ao banco para aceder á conta
        lockBanco.lock();
        if(banco.containsKey(id)){
            try{ 
                c = banco.get(id);
                //Lock para aceder á conta que queremos remover               
                c.getLockConta();
                saldo = c.getSaldo();
                
                //Tendo o lock da conta e previamente do banco, podemos remover
                banco.remove(id);
            }finally {
                // o finally faz sempre o unlock mesmo que haja returns no try 
                c.getUnlockConta(); 
                lockBanco.unlock();
            }
        }else {throw new InvalidAccount("nao existe a conta com esse id."); }
        
        return saldo; 
    }
    
    void transfer(int from, int to, float amount) throws InvalidAccount, NotEnoughtFounds{
        Conta destino = null; Conta origem = null;
        
        //Lock ao banco para aceder ás contas
        lockBanco.lock();
        if( (banco.containsKey(from)==false) || (banco.containsKey(to)==false) ){
            throw new InvalidAccount("O número de uma das contas fornecidas nao existe.");
            
        }
       
        //Obter o lock das contas depois de ter o lock do banco
        //if(from < to){
            try{
                origem = banco.get(from);
                origem.getLockConta();
                destino = banco.get(to);
                destino.getLockConta();
            
                //Depois de ter os locks das contas, liberto locks do banco
                lockBanco.unlock();
            
                if(origem.getSaldo() < amount){ 
                    throw new NotEnoughtFounds("A conta de origem nao tem saldo suficiente.");
                }
            
                origem.retira(amount);
                origem.getUnlockConta();
                destino.adiciona(amount);
                destino.getUnlockConta();
            }finally {
                origem.getUnlockConta();
                destino.getUnlockConta();
            }
        //}else {}
    }
    
    float totalBalance(int accounts[]){
        //garantir que todos os ids de conta existem no banco!!!
        int i; float balanco = 0;
        int[] arrayNrContas = accounts;
        Arrays.sort(arrayNrContas);
        
        Conta[] arrayContas = new Conta[accounts.length];
               
        //Lock para o banco
        lockBanco.lock();
        
        for(i = 0; i!=accounts.length; i++){
            //Passar para o array de contas as contas com os ids passados no argumento
            arrayContas[i] = banco.get( arrayNrContas[i] );
        }
        for(i = 0; i!=accounts.length; i++){
            //Ciclos para ir buscar lock ás contas
            arrayContas[i].getLockConta();
        }
        //depois de ter o lock das contas libertar o banco
        lockBanco.unlock();
        
        for(i=0; i!=accounts.length; i++){
            //tendo os locks das contas fazemos o seu balanco
            balanco += arrayContas[i].getSaldo();
            //logo depois de ter acedido á informaçao podemos libertar a conta i 
            arrayContas[i].getUnlockConta();
        }
        
        return balanco;
    }
}
