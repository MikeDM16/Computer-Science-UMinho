
/**
 * Write a description of class ficha8 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Scanner;
public class ficha8
{
    public static void funcaoExe1(){
        int pares, impares, somaPares, x;  Scanner scanner = new Scanner(System.in);
        pares = impares = somaPares = 0;
        System.out.println("\nDigite um número. Termine com 0");
        x = scanner.nextInt();
        while(x!=0){
            if( (x%2)==0) {pares++; somaPares += x;} else {impares++;}
            System.out.print("Proximo numero:");  x = scanner.nextInt();
        }
        System.out.println("Escreveu "+pares+" números pares e "+impares+" números impares");
        try {
            System.out.println("A média dos valores pares que escreveu é: "+(somaPares/pares) );
        }
        catch (ArithmeticException e){
            System.out.println("Não é possivel calcular a média");
        }
    }
    
    public static void funcaoExe2(int entradas, int idade){
        Scanner scanner = new Scanner(System.in); System.out.println("\nMedia idades\n");
        int soma, res, total, x, i;     soma = res = total = 0;
        for(i=0; i!=entradas; i++){
            System.out.print("Digite uma idade:");  x = scanner.nextInt(); soma += x; total++;
            if(x>=idade) {res++;   System.out.println("A sua idade é superior a " + idade); }
        }
        System.out.println("A media das idade digitadas foi de " + (soma/total) ); 
        System.out.println("foram escritas " + res + " idades superiores a " + idade + "anos.");
    }
    
    public static int funcaoExe3(int N, int arr[]){
        int i, t=0;
        for(i=0; i!=N; i++)
            if(arr[i++]%2==0){t++;}
        return t;    
    }
    
    public static void funcaoExe4(char[] pal, char[] subPal){
        int i, j, it1, count = 0;
        for(i=0; i!=pal.length; i++){
            j = 0;
            if(pal[i]==subPal[j]){
                it1 = i;
                while(it1< pal.length && j<subPal.length && pal[it1]==subPal[j]){
                    it1++; j++; 
                }
            }
            if(j==subPal.length){ count++; }
        }
        System.out.println("O array  contem "+count+" vezes a palavra ");
    }
}
