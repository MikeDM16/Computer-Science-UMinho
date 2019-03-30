
/**
 * Escreva a descrição da classe JustHello aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.io.*;
import java.net.*;
public class JustHello
{
    public static void main(String args[]) throws IOException{
        ServerSocket ss = new ServerSocket(9999);
        
        Socket cs = ss.accept();
        
        //out whith autoflash for printline
        PrintWriter out = new PrintWriter( cs.getOutputStream(), true );
        
        String hello = "Hello World";
        out.println(hello);
        
        out.close();
        cs.close();
        ss.close();
        
        //telnet -r 127.0.0.1 9999
        //ping 193.93
    }
}
