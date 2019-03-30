package testes;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe apenas para testar expressoes regulares
 */
class TesteRegex {

    public static void main(String[] args) {
        //Pattern correspondente a coordenadas.
        //Considera-se coordenadas dois numeros separados por virgula, espaço ou ponto e virgula, eventualmente rodeados por ()
        Pattern patternCord = Pattern.compile("[(]?[ ]*[+-]?[0-9]+[, ;]+[+-]?[0-9]+[ ]*[)]?");

        ArrayList<String> possibilidades= new ArrayList<>();
        //Possibilidades que deverao fazer match
        possibilidades.add("1,2");
        possibilidades.add("(1,2)");
        possibilidades.add("(-1 , -2)");
        possibilidades.add("1;2");
        possibilidades.add("1 2");
        possibilidades.add("( -1 , -2 )");
        //Possibilidades que nao deverao fazer match
        possibilidades.add("1,2,3");
        possibilidades.add("13");

        //Ciclo de debug que imprime informacoes das strings
        //Vê se cada uma faz match e em caso afirmativo, divide-a em 2 tokens, o x e y das coordenadas.
        for(String str:possibilidades){
            Matcher m = patternCord.matcher(str);
            System.out.print("String original: " + str);
            System.out.print(" | Matches? " + m.matches());

            if(m.matches()) {
                StringTokenizer strTok = new StringTokenizer(str, "() ,;\n\r");
                System.out.println(" | Forma normalizada: (" + strTok.nextToken() + "," + strTok.nextToken() + ")");
            }
            else
                System.out.print("\n");
        }

    }
}
