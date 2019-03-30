
/**
 * Escreva a descrição da classe exe5 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;

public class exe5
{
    private static int[] arrayConcat(int[]arr1, int[] arr2){
        int[] novo = new int[arr1.length + arr2.length];
        int i,j;
        for(i=0; i<arr1.length; novo[i] = arr1[i++]);
        for(j=0; j<arr2.length; novo[i+j] = arr2[j++]);
        return novo;
    }
    public static void main(){
        int ind1, ind2;
        Scanner ler = new Scanner(System.in);
        
        System.out.println("Digite o tamanho do primeiro array");
        ind1 = ler.nextInt();
        int[] arr1 = exe4.escrevearray(ind1);
        
        System.out.println("Digite o tamanho do segundo array");
        ind2 = ler.nextInt();
        int[] arr2 = exe4.escrevearray(ind2);
        
        int res[] = exe5.arrayConcat(arr1, arr2);
        for(int x:res){System.out.print(x+" ");}
        System.out.println(".\n");
        
    }
}
