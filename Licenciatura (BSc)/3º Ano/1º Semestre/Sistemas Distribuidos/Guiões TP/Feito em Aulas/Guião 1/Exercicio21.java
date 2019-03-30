
/**
 * Escreva a descrição da classe Exercicio21 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Exercicio21 implements Runnable
{
    private int i = 15;
    static Counter21 c; 
    public void run(){
        for(int j=0; j!=i; j++) {c.x++; }
    }
    
    public static void main(int args){
        Thread arr[] = new Thread[args];
        System.out.println("antes de criar as Threads");
        int j; c = new Counter21();
        
        /*Criaçao de uma Thread pronta a correr em cada posiçao do array*/
        for(j=0; j!=args; j++){
            arr[j] = new Thread(new Exercicio21());
        }
        /*Iniciar cada uma das threads. Coloca-las a correr; */
        for(j=0; j!=args; j++){
            arr[j].start();
        }
        System.out.println("Depois da criaçao das Threads");
        /*Esperar pelo fim de cada Thread*/
        for(j=0; j!=args; j++){
            try{
                arr[j].join();
            }catch( InterruptedException e){}
        }
        System.out.println("Fim programa principal " + c.x);
        
    }
}
