
/**
 * Escreva a descrição da classe exe7 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe7
{
   public static Scanner read = new Scanner(System.in);
    public static float[] vencimentosF(int n){
       float[] venc = new float[n];
       for(int i=0; i<n; i++){
           System.out.println("Digite o vencimento do "+(i+1)+"º funcionário.");
           venc[i] = read.nextFloat();
        }
       return venc;
    }
   public static String[] cargosF(int n){
       String[] pos = new String[n];
       System.out.println("Selecione um dos cargos para cada funcionário: Developer, Gestor, Administrador, CEO.");
       for(int i=0; i<n; i++){
           System.out.println("Digite o cargo do "+(i+1)+"º funcionário.");
           pos[i] = read.next();
        }
       return pos;
   }
   public static float[] salarios(float[] val, String[] pos){
       int i;
       double x; float s;
       float[] sal = new float[val.length];
       for(i=0; i<val.length; i++){
           if(pos[i].equals("Developer")){
               x = val[i]*1.05;
               s = (float) x;
               sal[i] = s;
            }
           if(pos[i].equals("Gestor")){
               x = val[i]*1.10;
               s = (float) x;
               sal[i] = s;
           }
           if(pos[i].equals("Administrador")){
               x = val[i]*1.20;
               s = (float) x;
               sal[i] = s;
           }
           if(pos[i].equals("CEO")){
               x = val[i]*1.40;
               s = (float) x;
               sal[i] = s;
           }
           
       }
       return sal;
   }
   public static void main(String arg[]){
       System.out.println("Quantos funcionários tem a empresa?");
       int n = read.nextInt();
       float[] vencimentos = exe7.vencimentosF(n);
       String[] cargos = exe7.cargosF(n);
       float[] salarios = exe7.salarios(vencimentos, cargos);
       System.out.println("\nVencimentos:");
       for(float x: vencimentos){System.out.print(x +" ");}
       System.out.print("\nCargos");
       for(String x:cargos ){System.out.print(x +" ");}
       System.out.print("\nSalário final : ");
       for(float x: salarios){System.out.print(x +" ");}
       System.out.println("\n");
    }
}
