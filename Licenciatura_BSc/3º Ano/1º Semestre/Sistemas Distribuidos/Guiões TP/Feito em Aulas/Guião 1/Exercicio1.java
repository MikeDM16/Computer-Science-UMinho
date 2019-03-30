
/**
 * Escreva a descrição da classe Exercicio1 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Exercicio1 implements Runnable
{
    private int i = 15;
    public void run(){
        for(int j=1; j!=i; j++) {System.out.println("número " + j ); }
    }
    
    public static void main(int args){
        Thread arr[] = new Thread[args];
        System.out.println("antes de criar as Threads");
        int j;
        /*Criaçao de uma Thread pronta a correr em cada posiçao do array*/
        for(j=0; j!=args; j++){
            arr[j] = new Thread(new Exercicio1());
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
        System.out.println("Fim programa principal");
    }
}
