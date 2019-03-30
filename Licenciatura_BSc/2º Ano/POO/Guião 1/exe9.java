
/**
 * Escreva a descrição da classe exe9 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.util.GregorianCalendar;
import java.lang.Math;

public class exe9
{
    public static int weekday(int dia, int mes, int ano){
        GregorianCalendar data;
        data = new GregorianCalendar(ano,mes, dia, 12,00);
        /* if(mes ==2) { data = new GregorianCalendar(ano,FEBRUARY, dia, 12,00);}
        if(mes ==3) { data = new GregorianCalendar(ano,data.MARCH, dia, 12,00);}
        if(mes ==4) { data = new GregorianCalendar(ano,data.APRIL, dia, 12,00);}
        if(mes ==5) { data = new GregorianCalendar(ano,data.MAY, dia, 12,00);}
        if(mes ==6) { data= new GregorianCalendar(ano,data.JUNE, dia, 12,00);}
        if(mes ==7) { data = new GregorianCalendar(ano,data.JULY, dia, 12,00);}
        if(mes ==8) { data = new GregorianCalendar(ano,data.AUGUST, dia, 12,00);}
        if(mes ==9) { data = new GregorianCalendar(ano,data.SEPTEMBER, dia, 12,00);}
        if(mes ==10) { data = new GregorianCalendar(ano,data.OCTOBER, dia, 12,00);}
        if(mes ==11) { data = new GregorianCalendar(ano,data.NOVEMBER, dia, 12,00);}
        if(mes ==12) { data = new GregorianCalendar(ano,data.DECEMBER, dia, 12,00);} */
        return(data.get(data.DAY_OF_WEEK));
        
        
    }
    
    public static void main(String args[]){
        Scanner ler = new Scanner(System.in);
        int dia, mes, ano;
        System.out.println("digite a o dia (1..31): ");
        dia = ler.nextInt();
        System.out.println("digite a o mês (1..12): ");
        mes = ler.nextInt();
        System.out.println("digite a o ano (1900..2100): ");
        ano = ler.nextInt();
        int r= exe9.weekday(dia, mes, ano);
        if (r==7){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Sábado");}
        if (r==1){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Domingo");}
        if (r==2){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Segunda");}
        if (r==3){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Terça");}
        if (r==4){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Quarta");}
        if (r==5){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era Quinta");}
        if (r==6){ System.out.println("O dia da semana em " +dia+"/"+mes+"/"+ano+ " era sexta");}
    }
}
