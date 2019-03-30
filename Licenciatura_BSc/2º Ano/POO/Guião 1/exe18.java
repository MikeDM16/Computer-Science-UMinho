
/**
 * Escreva a descrição da classe exe18 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.text.DecimalFormat;

public class exe18
{
    public static void ageInHours(){
        int ano, mes, dia; 
        Scanner read = new Scanner(System.in); 
        //DecimalFormat df = new DecimalFormat("0.000") --> faço cast para inteiro
        System.out.println("\nDigite o ano do seu nascimento:");
        ano = read.nextInt();
        System.out.println("\nDigite o mês  do seu nascimento:");
        mes = read.nextInt();
        System.out.println("\nDigite o dia do seu nascimento:");
        dia = read.nextInt();
        GregorianCalendar born = new GregorianCalendar(ano,mes,dia,0,0);
        GregorianCalendar atual = new GregorianCalendar();
        
        float dif = atual.getTimeInMillis() - born.getTimeInMillis();
        dif /=1000*60*60;
        dif = (int) dif;
        System.out.println("a sua idade atual é de "+ dif + " horas.");
        System.out.println("Esta medição foi realizada em " + atual.get(GregorianCalendar.DAY_OF_MONTH)+"/"+
        atual.get(GregorianCalendar.MONTH)+"/"+atual.get(GregorianCalendar.YEAR)+" ás " +
        atual.get(GregorianCalendar.HOUR)+"H"+ atual.get(GregorianCalendar.MINUTE)+"min .");
    }
    public static void main(){
        exe18.ageInHours();
        
    }
}
