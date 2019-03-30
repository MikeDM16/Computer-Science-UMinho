
/**
 * Escreva a descrição da classe exe17 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.lang.Math;
import java.util.Random;

public class exe17
{
    public static void jogaRandom(){
        double a,b, x, dist;
        Random gerador = new Random();
        Scanner read = new Scanner(System.in);
        a = gerador.nextInt(100);
        /*while( (a>100) || (a<1) ){ 
            a=gerador.nextInt();
        } */
        b = gerador.nextInt(100);
        while ( /*(b>100) || (b<1) || */ (b==a) ) {
            b=gerador.nextInt(100);
        }
        int i;
        System.out.print("O jogo vai começar. \nTem 5 tentativas para adivinhar um dos dois números gerados\n");
        for(i=0;i<5;i++){
            System.out.println("Digite a sua "+(i+1)+"º tentativa");
            x = read.nextDouble();
            if( (x==a) || (x==b)){
                System.out.println("PARABÉNS!\nAcertou á "+(i+1)+"º tentativa.");
                break;
            }
            if ( Math.abs(a-x)>Math.abs(b-x) ) {dist = Math.abs(b-x);} else { dist = Math.abs(a-x);};
            System.out.println("A sua tentativa está a "+dist+" de distantia ao numero mais proximo");
            
        }
        System.out.println("elementos gerados: "+a+b);
        if (i==5) {System.out.println("Você não acertou em nenhum dos números. Tente novamente");}
    }
    
    public static void main(){
        System.out.print("\n1.- Jogar \n2.- Sair \n");
        Scanner o = new Scanner(System.in);
        System.out.println("Escolha uma opção ( 1 ou 2 ): ");
        int x = o.nextInt();
        while(x!=2){
            jogaRandom();
            System.out.print("\n1.- Jogar \n2.-Sair \n");
            System.out.print("Escolha uma opção ( 1 ou 2 ) ");
            x = o.nextInt();
        }
    }
}
