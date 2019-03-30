
/**
 * Escreva a descrição da classe exer6 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Integer;
import java.lang.Math;
public class exer6
{
  public static double fatorial(int a){
      double i = 1;
      double r=a;
      while(i<a){
          r*=i;
          i++;
        }
      return r;
    }
    /** números grandes dá 0 */
  public static void main(String args[]){
      for(String a:args){
          System.out.println("o fatorial de " +a+ " é " + exer6.fatorial(Integer.valueOf(a)));
        }
  }
}
