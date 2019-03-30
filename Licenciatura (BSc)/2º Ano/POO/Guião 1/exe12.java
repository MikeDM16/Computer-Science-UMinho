
/**
 * Escreva a descrição da classe exe12 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Integer;
import java.lang.String;

public class exe12
{
    public static void temperaturas(String t[]){
        int i; float dif;
        float d1, d2, dia1=1, dia2=2, var = 0;
        float sum=Integer.valueOf(t[0]);
        float media;
        int l=0; // funcao lenght nao dá ???? 
        for(String a:t){ l++;}
        for(i=1; i<l ; i++){
            d1 = Integer.valueOf(t[i-1]);
            d2 = Integer.valueOf(t[i]);
            dif = Math.abs(d2 - d1);
            if (dif > Math.abs(var) ) {
                var = dif;
                dia1 = Integer.valueOf(t[i-1]);
                dia2 = Integer.valueOf(t[i]);
                
            }
            sum+= Integer.valueOf(t[i]);
        }
        media = sum / i ; 
        System.out.println("A média das " + i + " temperaturas é " + media + " graus.");
        System.out.println("A maior variação registou-se entre os dias "+ dia1 + " e "+ dia2 +" tendo a temperatura subido/descido "+ var + " graus.");
        
        
        }
    public static void main(String args[]){
        exe12.temperaturas(args);
    }
}
