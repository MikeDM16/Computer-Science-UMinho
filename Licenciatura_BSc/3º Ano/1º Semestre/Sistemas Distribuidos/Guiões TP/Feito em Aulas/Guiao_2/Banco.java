
/**
 * Escreva a descrição da classe Banco aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Banco
{
    Conta[] contas;
    int N = 20;
    public Banco(){
        this.contas = new Conta[N];
        for(int j=0; j!=N; j++){
            contas[j] = new Conta();
        }
    }
    
    public void credito(int c, float q){
        if(c <= contas.length){
            contas[c].credito(q);
        }
    }
    public void debito(int c, float q){
        if(c <= contas.length){
            contas[c].debito(q);
        }
    }
    public float consulta(int c){
        float r = 0;
        if(c <= contas.length){
            r =  contas[c].consulta();
        }
        
        return r;
    }
    public int getNContas(){ return contas.length; }
    public float getValorTotal(){
        float r = 0; 
        for(int i=0; i!= contas.length; i++){
            r += contas[i].consulta();
        }
        return r;
    }
    
    public void transferir(int from, int to, float q){
        if(from <= contas.length && to <= contas.length){
            contas[from].debito(q);
            contas[to].credito(q);
        }
    }
    /* LEVA A STARVATION 
    public void transferir(int from, int to, float q){
        if(from <= contas.length && to <= contas.length){
            synchronized (contas[from]){
                synchronized(contas[to]){
                    contas[from].debito(q);
                    contas[to].credito(q);
                }
            }
        }
    }*/
}
