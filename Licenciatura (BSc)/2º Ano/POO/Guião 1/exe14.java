
/**
 * Escreva a descrição da classe exe14 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.text.DecimalFormat;


public class exe14
{
    public static int stock(){
        Scanner ler = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.0000");
        int somaT = 0, stock;
        float preco, somaP = 0f;
        System.out.println("Digite o valor do preço do seu produto: ");
        preco = ler.nextFloat();
        System.out.println("Inicia a sua sequancia de valores de stock. ");
        stock = ler.nextInt();
        while(stock!=0){
            somaT += stock;
            somaP += (stock * preco);
            System.out.println("Inicia a sua sequancia de valores de stock. ");
            stock = ler.nextInt();
        }
        System.out.println("\nExistem " + somaT + " itens do produto em stock");
        System.out.println("O valor do stock é de " + df.format(somaP) );
        return 0;
    }
    public static void main(){
        exe14.stock();
    }
}
