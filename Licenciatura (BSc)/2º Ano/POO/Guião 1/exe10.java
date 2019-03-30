
/**
 * Escreva a descrição da classe exe10 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.GregorianCalendar;
import java.lang.Math;
import java.lang.Integer;
import java.util.Scanner;
public class exe10
{
    public static void somaDatas(int d1, int h1, int m1, int d2, int h2, int m2){
        GregorianCalendar data1 = new GregorianCalendar(2016,1,d1,h1,m1);
        GregorianCalendar data2 = new GregorianCalendar(2016,1,d2,h1,m2);
        double r =data2.getTimeInMillis() - data1.getTimeInMillis();
        r = r / (1000*60*60*24); // diferença de tempo em dias 
        int dias = (int) r; // ficar com a parte inteira (dias)
        double h = dias - r; // ficar com a parte decimal que sobra para as horas
        h = h*24; 
        int horas = (int) h; // parte inteira das horas
        double m = horas - h; // ficar com a parte decimal para os minutos
        m = m*60;
        int minutos = (int) m; 
        System.out.println(dias+"D "+ horas + "H "+ minutos+"M ");
    }
    
    public static void main(String args[]){
        int dia1, hora1, min1;
        int dia2, hora2, min2;
        Scanner ler = new Scanner(System.in);
        System.out.println("Digite o dia da primeira data: ");
        dia1 = ler.nextInt();
        System.out.println("Digite a hora da primeira data: ");
        hora1 = ler.nextInt();
        System.out.println("Digite os minutos da primeira data: ");
        min1 = ler.nextInt();
        
        System.out.println("Digite o dia da segunda data: ");
        dia2 = ler.nextInt();
        System.out.println("Digite a hora da segunda data: ");
        hora2 = ler.nextInt();
        System.out.println("Digite os minutos da segunda data: ");
        min2 = ler.nextInt();
        
        exe10.somaDatas(dia1,hora1, min1, dia2, hora2, min2);
    }
}

