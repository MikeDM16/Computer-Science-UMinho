
/**
 * Escreva a descrição da classe exe8 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.util.Random;
public class exe8
{
    public static int elem(int n, int[] arr){
        int r=0;
        for(int x:arr){
            if(x==n){
                r = 1;
                break;
            } 
        }
        return r;
    }
    public static void euromilhoes(){
        int[] numbers = new int[5];
        int[] numbersp = new int[5];
        int[] stars = new int[2];
        int[] starsp = new int[2];
        int i, p, qnumbers, qstars;
        qnumbers=qstars=0;
        Scanner read = new Scanner(System.in);
        Random gerador = new Random();
        System.out.println("\nJOGO DO EUROMILHÔES\n");
        //Chave gerada aleatoriamente
        for(i=0; i<2; i++){
            p = gerador.nextInt(9);
            while((exe8.elem(p,stars)==1) ) { p= gerador.nextInt(9); }
            stars[i]=p;
        }
        for(i=0; i<5; i++){
            p = gerador.nextInt(50);
            while((exe8.elem(p,numbers)==1) ) { p= gerador.nextInt(50); }
            numbers[i]=p;
        }
        // chaver utilizador : 
        System.out.println("Digite os seus 5 números sem repetiçoes");
        for(i=0; i<5; i++){
            System.out.print("Digite o seu "+(i+1)+"º número (1..50)");
            p = read.nextInt();
            while( (p<0) || (p>50) || (exe8.elem(p,numbersp)==1)){ 
                System.out.print("O seu número nao respeita as condições.\nDigite o seu "+(i+1)+"º número (1..50)");
                p = read.nextInt();
            }
            numbersp[i]=p;
        }
        System.out.println("\nDigite agora as suas 2 estrelas sem repetiçoes");
        for(i=0; i<2; i++){
            System.out.print("Digite a sua "+(i+1)+"º nestrela (1..9)");
            p = read.nextInt();
            while( (p<0) || (p>9) || (exe8.elem(p,starsp)==1)){ 
                System.out.print("O seu némero nao respeita as condições.\n Digite a sua "+(i+1)+"º estrela (1..9)");
                p = read.nextInt();
            }
            starsp[i]=p;
        }
        System.out.println(".");
        
        for(int a:numbersp){
            if(elem(a, numbers)==1){ qnumbers++; }
        }
        for(int a:starsp){
            if(elem(a,stars)==1){ qstars++; }
        }
        
        // imprimir chave gerada:
        System.out.print("Numeros da chave gerada:");
        for(int a:numbers){System.out.print(a+" ");}
        System.out.print(".\nEstrelas da chave gerada:");
        for(int a:stars){System.out.print(a+" ");}
        System.out.println(".");
        //imprimir chave utilizador: 
        System.out.print("Numeros da chave introduzida pelo jogador:");
        for(int a:numbersp){System.out.print(a+" ");}
        System.out.print(".\nEstrelas da chave introduzida pelo jogador:");
        for(int a:starsp){System.out.print(a+" ");}
        System.out.print(".\n");
        System.out.println("Acertou em "+qnumbers+" números e em "+qstars+" estrelas.");
        }
        
    public static void main(String args[]){
        exe8.euromilhoes();
    }
}
