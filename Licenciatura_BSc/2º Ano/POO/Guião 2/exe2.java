
/**
 * Escreva a descrição da classe exe2 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe2
{
    public static int[] lerArrayInt(int N){
        int i;
        Scanner ler = new Scanner(System.in);
        int[] r = new int[N];
        for(i=0; i<N; i++){
            System.out.println("Dgite o elemento da posiçao " +i+" do array.");
            r[i]=ler.nextInt();
        }
        return r;
    }
    public static int minPos(int[] arr){
        int min, minAux, indice, i;
        min = arr[0]; indice = 0;
        
        for(i=1; i<arr.length; i++){
            if(arr[i]<min){
                min = arr[i];
                indice = i;
            }
        }
        return indice;
    }
        public static void main(String args[]){
        Scanner read = new Scanner(System.in);
        System.out.println("Quantos elementos terá o array?");
        int N = read.nextInt();
        int[] array = exe2.lerArrayInt(N);
        int pos = exe2.minPos(array);
        System.out.println("A posiçao do elemento minimo do array é:" + pos +"."); 
    
    }
}
