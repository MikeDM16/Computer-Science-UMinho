
/**
 * Escreva a descrição da classe exe13 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Math;
import java.lang.Integer;
import java.text.DecimalFormat;

public class exe13
{
    public static void triangulo(){
        Scanner ler = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00000");
        float base=1.1f, altura, ladohipotenusa, perimetro, area;
        System.out.println("Digite a base do triangulo: ");
        base = ler.nextFloat();
        while(base!=0){
            System.out.println("Digite a altura do triangulo: ");
            altura = ler.nextFloat(); 
            
            area = (base * altura) ; 
            double a1 = (double) altura;
            double b1 = (double) base/2;
            double ladoh = Math.sqrt( Math.pow(a1,2) + Math.pow(b1,2) );
            ladohipotenusa = (float) ladoh;
            perimetro = base + 2*ladohipotenusa;
            
            System.out.println("O valor da área é " + df.format(area) + " e do perimetro é " + df.format(perimetro));
            
            System.out.println("Digite a base do triangulo: ");
            base = ler.nextFloat();
        }
        
    }
    public static void main(String args[]){
        exe13.triangulo();
    }
}
