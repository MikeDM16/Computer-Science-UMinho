
/**
 * Escreva a descrição da classe exe1 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe1
{
    public static void main(String args[]){
        String nome;
        float saldo;
        Scanner ler=new Scanner(System.in);
        System.out.println("Nome?");
        nome=ler.nextLine();
        System.out.println("Saldo?");
        saldo = ler.nextFloat();
        
        System.out.println( nome +"tem " +saldo +" de saldo ");
        
    }
}
