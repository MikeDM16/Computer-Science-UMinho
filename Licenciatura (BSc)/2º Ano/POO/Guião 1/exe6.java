
/**
 * Escreva a descrição da classe exe6 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class exe6
{
    /* public static int fatorial(int n){
        if (n==0){
            return 1;
        }
        return n*fatorial(n-1);
    }*/
    public static double fatorial(int valor){
        double v=1;
        while(valor >1){
            v*=valor;
            valor--;
        }
        return v;
    }
    public static void main(String args[]){
        for(String a:args){
            System.out.println("o fatorial de "+a+" é "+ exe6.fatorial(Integer.valueOf(a)));
    }
}
}

