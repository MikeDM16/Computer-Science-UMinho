
/**
 * Escreva a descrição da classe exe2 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe2
{
    public static void main(String args[]){
        int i; int j;
        Scanner ler=new Scanner(System.in);
        System.out.println("valor do primeiro inteiro");
        i=ler.nextInt();
        System.out.println("valor do segundo inteiro");
        j=ler.nextInt();
        if (i>j) {
           System.out.println("Valores por ordem decrescente: " + i+  " "+ j + ". ");
           System.out.println("Media dos valores " + ((i+j)/2));
           
    }
        else {
           System.out.println("Valores por ordem decrescente: " + j+ " "+ i + ". ");
           System.out.println("Media dos valores " + ((i+j)/2));
        }
}
}
