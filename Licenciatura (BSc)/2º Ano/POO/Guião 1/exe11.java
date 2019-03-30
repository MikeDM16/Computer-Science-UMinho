
/**
 * Escreva a descrição da classe exe11 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.lang.Integer;
import java.util.Scanner;
public class exe11
{
    public static void frequenciaNotas(String n[]){
        float nota;
        int c1, c2, c3, c4;
        c1=c2=c3=c4=0;
        for(String a: n){
           nota = (float) Integer.valueOf(a);
           if ( (nota>=0) && (nota<5) ) { c1++; }
           if ( (nota>=5) && (nota<10) ) { c2++; }
           if ( (nota>=10) && (nota<15) ) { c3++; }
           if ( (nota>=15) && (nota<21) ) {c4++; }
           System.out.println("Existem "+c1+" classificações entre [0,5[");
           System.out.println("Existem "+c2+" classificações entre [5,10[");
           System.out.println("Existem "+c3+" classificações entre [10,15[");
           System.out.println("Existem "+c4+" classificações entre [15,20]");
    }
}
    public static void main(String args[]){
        exe11.frequenciaNotas(args);
        
    }
}
