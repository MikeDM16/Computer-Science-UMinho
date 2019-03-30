
/**
 * Escreva a descrição da classe Banco aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Banco
{
    int contas[];
    
    public Banco(){
        contas = new int[20];
        for(int i=0; i!=20; contas[i++] = 0);
        
    }
    public int getContas() { 
        int saldo = 0;
        for(int i=0; i!=contas.length; saldo +=contas[i++]);
        return saldo;
    }
    public int getNumeroContas(){return contas.length;}
    
    public void put(int f, int q) { contas[f]+= q; }
    public void take(int f, int q){ contas[f]-= q; }
    
}
