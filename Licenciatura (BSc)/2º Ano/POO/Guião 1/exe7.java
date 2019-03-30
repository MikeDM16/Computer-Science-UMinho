
/**
 * Escreva a descrição da classe exe7 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Math;
import java.lang.Integer;
import java.util.GregorianCalendar;
public class exe7
{
    public static void main(String args[]){
        GregorianCalendar antes = new GregorianCalendar();
        exer6.fatorial(1000000);
        GregorianCalendar depois = new GregorianCalendar();
        double dif = depois.getTimeInMillis() - antes.getTimeInMillis();
        System.out.println("O tempo de execuçao do fatorial de 1000000 foi de "+dif+" milisegundos");
    }
}
