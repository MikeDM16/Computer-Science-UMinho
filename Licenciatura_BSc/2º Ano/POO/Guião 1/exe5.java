
/**
 * Escreva a descrição da classe exe5 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.lang.Math;
import java.util.Scanner;
import java.lang.Integer;
public class exe5
{
    public static void sequenciaSum(){
        Scanner ler = new Scanner(System.in);
        int max, min, sum=0;
        System.out.println("Inicie a sua sequencia de número (terminada com o valor 0):");
        int i = ler.nextInt();
        max=min=i; sum+=i;
        for(; i!=0; i = ler.nextInt()){
            if(i>max){max=i;}
            if(i<min) {min=i;}
            sum+=i;
        }
        System.out.println("A sua sequencia tem como valor maximo " + max + ", valor minimo " +min+ " e somatótio " + sum);
        
    }
    public static void main(String args[]){
        sequenciaSum();
    }
}
