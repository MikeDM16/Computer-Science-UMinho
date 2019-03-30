
/**
 * Escreva a descrição da classe exe8 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Math;
import java.lang.Integer;

public class exe8
{
    public static int weekday(int d, int m, int a){
        int a1;
        a1 = (a-1900);
        a1+= (a-1900)/4;
        if((a1%4==0) && ((m==1) || (m==2)) ) {a1--;}
        a1+= d;
        return (a1/7);
    }
    public static void main(String arg[]){
        int dia, mes, ano;
        Scanner ler = new Scanner(System.in);
        System.out.println("Digite o dia do ano (1..31)");
        dia = ler.nextInt();
        System.out.println("Digite o mês do ano (1..12)");
        mes = ler.nextInt();
        System.out.println("Digite o ano (1900...2100)");
        ano = ler.nextInt();
        int r = exe8.weekday(dia, mes, ano);
        if(r==0){System.out.println("O dia da semana é Domingo");}
        if(r==1){System.out.println("O dia da semana é Segunda");}
        if(r==2){System.out.println("O dia da semana é Terça");}
        if(r==3){System.out.println("O dia da semana é Quarta");}
        if(r==4){System.out.println("O dia da semana é Quinta");}
        if(r==5){System.out.println("O dia da semana é Sexta");}
        if(r==6){System.out.println("O dia da semana é Sabado");}
        
    }
}
