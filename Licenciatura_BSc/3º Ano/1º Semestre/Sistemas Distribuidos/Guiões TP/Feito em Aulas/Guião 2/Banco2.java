
/**
 * Escreva a descrição da classe Banco2 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Banco2
{
    int contas[];
    
    public Banco2(){
        contas = new int[20];
        for(int i=0; i!=20; contas[i++] = 0);
        
    }
    public int getContas2() { 
        int saldo = 0;
        for(int i=0; i!=contas.length; saldo +=contas[i++]);
        return saldo;
    }
    public int getNumeroContas2(){return contas.length;}
    
    public synchronized void put2(int f, int q) { contas[f]+= q; }
    public synchronized void take2(int f, int q){ contas[f]-= q; }
    
}
