
/**
 * Escreva a descrição da classe exe4 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.lang.Math;
import java.util.Scanner;
import java.lang.Integer;
public class exe4
{
    public static void main(String args[]){
        Scanner ler = new Scanner(System.in);
        double x; 
        System.out.println("quantos digitos vai introduzir?");
        int i = ler.nextInt();
        while(i>0){
            x = ler.nextInt();
            System.out.println("a raiz de " + x + " é " + Math.sqrt(x));
            i--;
        }
    }
}