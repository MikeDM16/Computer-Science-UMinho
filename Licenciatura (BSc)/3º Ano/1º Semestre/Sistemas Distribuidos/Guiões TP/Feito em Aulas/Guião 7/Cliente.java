
/**
 * Escreva a descrição da classe Cliente aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
public class Cliente
{
    private PrintWriter out;
    private BufferedReader in;
    private int soma, quant;
    ReentrantLock lockCli; 
    
    public Cliente(PrintWriter out, BufferedReader in){
        this.out = out; this.in = in; 
        soma = 0; quant = 0;
        lockCli = new ReentrantLock();
    }
    
    public PrintWriter getPrintewriter(){ return this.out; }
    public BufferedReader getBufferedReader() {return this.in; }
    
    public void addSoma( int s){ this.soma += s; quant++; }
    public int getSoma(){ return this.soma; }
    
    public float getMedia(){
        if( quant > 0){
            return this.soma/this.quant;
        }
        return 0;
    }
}
