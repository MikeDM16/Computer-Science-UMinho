
/**
 * Escreva a descrição da classe A aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;
public class Servidor implements Runnable{
    private int somaTotal;
    ReentrantLock lock;
    Cliente cliente;
    public Servidor(Cliente c){
        this.somaTotal = 0; 
        this.cliente = c; 
        this.lock = new ReentrantLock();
    }
    
    public void run(){  
        int quant, soma;  soma = quant = 0;
        
        PrintWriter out = cliente.getPrintewriter();  
        BufferedReader in = cliente.getBufferedReader();
        String current = null;
        try{
            while( (current = in.readLine() ) != null){
                try{                
                    cliente.addSoma(Integer.parseInt(current));
                    lock.lock(); 
                    somaTotal += soma; 
                    lock.unlock();
                                
                    out.print(cliente.getSoma());
                }catch (NumberFormatException e){}
            }
            
            out.println("média = " + cliente.getMedia() ); 
            
            in.close();
            out.close();
        }catch (IOException e){}
    }
    
    public static void main(String args){
        //Iniciar servidor()
        try{
            ServerSocket ss = new ServerSocket(9999);
            Socket cs = null;
            PrintWriter out;  BufferedReader in;
            //Esperar por clientes()
            while((cs = ss.accept()) != null){
                out = new PrintWriter( cs.getOutputStream(), true);
                in  = new BufferedReader( new InputStreamReader( cs.getInputStream() ));
                Thread cli = new Thread(new Servidor(new Cliente(out, in)) );
                cli.start();
                //cli.join();
            }
            //Fechar servidor
            //cs.close();
            //ss.close();
        }catch (IOException e){}
    }
}
