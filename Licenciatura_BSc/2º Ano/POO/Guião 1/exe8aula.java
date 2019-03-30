
/**
 * Escreva a descrição da classe exe8aula aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.util.GregorianCalendar;

public class exe8aula
{
   public static void main(String args[]){
       String diassem[]=(new java.text.DateFormatSymbols()).getWeekdays();
       GregorianCalendar d1 = new GregorianCalendar();
       System.out.println(d1);
       System.out.println(GregorianCalendar.DAY_OF_WEEK);
       System.out.println(diassem[d1.get(GregorianCalendar.DAY_OF_WEEK)]);
    }
}
