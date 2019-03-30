
/**
 * Escreva a descrição da classe exercicio1 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class ExercicioTeste implements Runnable
{
    public void run(){
        System.out.println("Ola vindo da Thread");
    }
    
    public static void main(String args[]){
        //(new Thread(new ExercicioTeste())).start();
       
        ExercicioTeste e1 = new ExercicioTeste();
        Thread t1 = new Thread(e1);
        t1.start();
    }
    
}
