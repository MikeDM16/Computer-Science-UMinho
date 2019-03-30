
/**
 * Escreva a descrição da classe exe3 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.util.Arrays;
public class exe3
{
    public static int[] lerArrayInt(int N){
        Scanner s = new Scanner(System.in);
        int[] v = new int[N];
        int m; int j;
        for(int i=0;i<N;i++){
            System.out.println("introduza o seu numero:");
            m = s.nextInt();
            for(j=0;j<=i;j++){
                System.arraycopy(v,j,v,j+1,i-j);
                break;
            
            }
            v[j]=m;
        }
        s.close();
        return v;
        }
   
        public static void main(String args[]){
        Scanner read = new Scanner(System.in);
        System.out.println("\nQuantos elementos terá o array?");
        int N = read.nextInt();
        int[] array = exe3.lerArrayInt(N);
        int i;
        System.out.print("Array: ");
        for (i=0; i<N; i++){
        System.out.print(array[i] + " ");
    }
        System.out.println(".\n");
    }
    
    }

