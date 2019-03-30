
/**
 * Write a description of class Banco here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Banco
{
    private Conta[] contas;
    
    public Banco(int n){ /* n = numero de contas do banco*/
        this.contas = new Conta[n];
        for(int j = 0; j!=n; j++){
            contas[j] = new Conta();
        }
    }
    
    public synchronized void credito(int to, int valor){
        contas[to].credito(valor);
    }
    public synchronized void debito(int to, int valor){
        contas[to].debito(valor);
    }
    public synchronized void transfer(int to, int from, int value){
        contas[to].debito(value);
        contas[from].credito(value);
    }
    public int getSaldo(int c){ return contas[c].consulta();}
    public int nrContas(){ return contas.length;}
}
