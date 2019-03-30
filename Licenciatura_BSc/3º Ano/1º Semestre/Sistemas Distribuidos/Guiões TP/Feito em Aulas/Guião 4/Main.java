
/**
 * Escreva a descrição da classe Main aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
public class Main
{
   public static void main(String args[]){
       final int N = 5;
       Barrier b = new Barrier(N);
       for(int i=0; i<N; i++){
           new Thread(new Cliente(b, i)).start();
        }
   }
}
