
/**
 * Escreva a descrição da classe exe16 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;

public class exe16
{
    public static void met1Login(){
        System.out.print("Selecionou a opçao de Login\n");
    }
    public static void met2Regist(){
        System.out.print("Selecionou a opçao de Registo\n");
    }
    public static void met3Info(){
        System.out.print("Selecionou a opçao de Infornações\n");
    }
    public static void menu(){
        Scanner m = new Scanner(System.in);
        System.out.println("\n1.- Login");
        System.out.println("2.- Registo");
        System.out.println("3.- Informações");
        System.out.println("0.- Sair");
        int opcao = m.nextInt();
        while(opcao!=0){
            if( (opcao>3) || (opcao<0) ) {System.out.println("Opção errada");}
            if( opcao == 1 ) { exe16.met1Login();}
            if( opcao == 2 ) { exe16.met2Regist();}
            if( opcao == 3 ) { exe16.met3Info();}
            opcao = m.nextInt();
        }
        
    }
    public static void main(String []arg){
       exe16.menu();
    }
}
