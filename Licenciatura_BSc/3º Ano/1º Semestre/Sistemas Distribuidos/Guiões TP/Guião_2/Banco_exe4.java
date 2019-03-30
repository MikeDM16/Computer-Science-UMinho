
/**
 * Write a description of class Banco_exe4 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Banco_exe4{
     private Conta_exe4[] contas;
    
    public Banco_exe4(int n){ /* n = numero de contas do banco*/
        this.contas = new Conta_exe4[n];
        for(int j = 0; j!=n; j++){
            contas[j] = new Conta_exe4();
        }
    }
    
    public void credito(int to, int valor){
        contas[to].credito(valor);
    }
    public void debito(int to, int valor){
        contas[to].debito(valor);
    }
    public void transfer(int to, int from, int value){
        contas[to].debito(value);
        contas[from].credito(value);
    }
    public int getSaldo(int c){ return contas[c].consulta();}
    public int nrContas(){ return contas.length;}
}
