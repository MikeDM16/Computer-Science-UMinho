
/**
 * Escreva a descrição da classe exe4 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;

public class exe4
{
    private static int[] subArray(int[] arr, int i, int f){
        int[] vect = new int [f-i+1];
        int j;
        for(j=0; i<f+1; i++,j++){
            vect[j]=arr[i];
        }
        return vect;
    }
    public static int[] escrevearray(int n){
        int[] r = new int[n];
        int i;
        Scanner ler = new Scanner(System.in);
        for(i=0; i<n;i++){
            System.out.println("digite o elemento do array");
            r[i] = ler.nextInt();
            
        }
        for(int a:r) {System.out.print(a+" ");}
        System.out.println(".\n");
        return r;
    }
    public static void main(){
        Scanner ler = new Scanner(System.in);
        System.out.println("digite o tamanho do seu array");
        int n = ler.nextInt();
        int[] arr = escrevearray(n);
        System.out.println("digite o indice de corte inical (entre 0.."+(n-1)+").");
        int i= ler.nextInt();
        System.out.println("digite o indice de corte final (entre 0.."+(n-1)+").");
        int f= ler.nextInt();
        
        int[] res = exe4.subArray(arr, i, f);
        for(int a:res) {System.out.print(a + " ");}
        System.out.println(".\n");}
        
    }
   
