
/**
 * Escreva a descrição da classe Banco aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Banco
{
    Conta[] banco;
    private int nContas = 50;
    
    public Banco(){
        this.banco = new Conta[nContas];
        for(int i=0; i!=nContas; i++){ 
            this.banco[i] = new Conta(0);
        }
    }
    
    public int getNumeroContas(){
        return this.banco.length;
    }
    
    public void adicionaV(int conta, int valor){
        this.banco[conta].adiciona(valor);
    }
    public void removeV(int conta, int valor){
        this.banco[conta].remove(valor);
    }
}
