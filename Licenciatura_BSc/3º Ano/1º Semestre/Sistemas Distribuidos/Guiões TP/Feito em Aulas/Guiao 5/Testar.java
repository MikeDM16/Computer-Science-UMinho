
/**
 * Escreva a descrição da classe Testar aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Testar
{
    public void Testar(){}     
    //PAra que raio uso o construtor??
    public static void main(String args[]){
        int t = 10;
        BoundedBuffer buf = new BoundedBuffer(40);
        Thread[] t1 = new Thread[10];
        Thread[] t2 = new Thread[10];
        int i;
        for(i = 0; i != t; i++){
            t1[i] = new Thread(new PutValue(buf));
            t2[i] = new Thread(new GetValue(buf));
        }
        System.out.println("Antes da execuçao");
        for(i = 0; i!=t; i++){
            t1[i].start();
            t2[i].start();
        }
        for(i = 0; i!=t; i++){
            try{
                t1[i].join();
                t2[i].join();
            }catch (InterruptedException e){}
        }
        System.out.println("Depois da execução");
    }
}