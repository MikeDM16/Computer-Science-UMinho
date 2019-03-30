
/**
 * Escreva a descrição da classe exe3 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe3
{
    public static void main(String args[]){
        String arr1;
        int i, x, c=0;
        Scanner ler =new Scanner(System.in);
        System.out.println("Digite os seus 10 números");
        for(i=0; i<9; i++){
            x = ler.nextInt();
            if (x>5) {
               c++;
            }
        }
        System.out.println("Existem " + c + " valores superiores a 5");
    }
}
