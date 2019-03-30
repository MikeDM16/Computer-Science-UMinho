
/**
 * Escreva a descrição da classe exe15 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;

public class exe15
{
    public static int primo(int p){
        if( (p>2) && (p%2==0)) return 1; // excepto o nº2, nenhum par é nº impar.
        int i;
        for(i=2;i<p; i++){
            if(p%i==0) return 1;
        }
        return 0;
    }
    public static void primos(int n){
        System.out.print("valores primos: ");
        for(int i=0; i<n; i++){
            if (primo(i)==0) {
                System.out.print(" "+i);
            }
           if(i==n-1){ System.out.println(".");}
        }
    }
    public static void main(){
        Scanner l = new Scanner(System.in);
        System.out.println("\nDigite o seu número");
        int n = l.nextInt();
        exe15.primos(n);
    }
}
