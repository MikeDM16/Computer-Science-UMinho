
/**
 * Escreva a descrição da classe Soma aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Soma
{
    public static void servidorSoma(String args){
        ServerSocket ss = new ServerSocket(9999);
        int quant, soma;
        soma = quant = 0; 
        Socket cs = null;
        cs = ss.accept();
        
        PrintWriter out = new PrintWriter( cs.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( cs.getInputStream() ));
        
        String current;
        while( (current = in.readLine()) != null){
            try{
                //insert current != null
                soma += current.parseInt(current);
                quant++;
                out.print(soma);
            }catch (NumberrformatException e){}
        }
        
        if(quant > 0){ out.println("média = " + soma/quant);}
        in.close();
        out.close();
        cs.close();
        ss.close();
        
    }
}
