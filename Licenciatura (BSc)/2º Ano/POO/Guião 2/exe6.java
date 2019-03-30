
/**
 * Escreva a descrição da classe exe6 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
public class exe6
{
    public static Scanner ler = new Scanner(System.in);

    public static String[] lerArrayStrings(){
        String[] pal = new String[100];
        int i=0; int x = 0;
        System.out.println("\nDigite a sua sequencia de palavras. Termine-a com a palavra 'fim'.");
        System.out.println("Digite a sua "+(i+1)+"º palavra.");
        pal[i] = ler.next();
        for(i=1; !(pal[i-1].equals("fim")); i++){
            System.out.println("Digite a sua "+(i+1)+"º palavra.");
            pal[i] = ler.next();
        }
        //imprimir palavras array pal
        System.out.print("Sequencia de palavras:");
        for(i=0; (i<pal.length) && (!pal[i].equals("fim")) ; i++) {System.out.print(pal[i]+" ");}
        System.out.print(".\n");
        
        return pal;
    }
    
    public static void trocapalavra(String[] arr){
        System.out.println("Digite a palavra que deseja substituir:");
        String sub = ler.next();
        System.out.println("Digite a palavra que deseja introduzir em lugar da anterior:");
        String nova = ler.next();
        int i;
        for(i=0; (i<arr.length) && !(arr[i].equals(sub)) ; i++){}
        if (i>arr.length){ System.out.println("A palavra que deseja substituir nao existe");}
        arr[i]=nova;
        
    }
    
    public static void removerpalavra(String[] arr){
        System.out.println("Digite a palavra que deseja remover:");
        String remove = ler.next();
        int i;
        for(i=0; !(arr[i].equals("fim")) && !(arr[i].equals(remove)) ; i++){}
        if (arr[i].equals("fim")){ System.out.println("A palavra que deseja remover nao existe");}
        System.arraycopy(arr,i+1,arr,i,(arr.length-i-1));
    }
    
    public static void main(String arg[]){
        String[] res = exe6.lerArrayStrings();
        System.out.print("\nDigite:\n's' - Para introduzir duas palavras e trocar a 1º pela 2º no array");
        System.out.println("\n'r' - Para introduzir 1 palavra a remve-la do array");
        System.out.println("'Q' - Para sair do programa");
        String r = ler.next();
            if(r.equals("s")){
            exe6.trocapalavra(res);
        }
            else if (r.equals("r")) {
            exe6.removerpalavra(res);
        }
        System.out.print("Sequencia de palavras final:");
        for(int i=0; (!res[i].equals("fim")) ; i++) {System.out.print(res[i]+" ");}
        System.out.print(".\n");
    }
}
