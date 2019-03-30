
/**
 * Escreva a descrição da classe exe3 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Arrays;
public class RWLock
{
    ReentrantLock leitura;
    ReentrantLock escrita;
    Condition leitores; 
    Condition escritores;
    
    int nEscritores, nLeitores; 
    public RWLock(){
        leitura = new ReentrantLock();
        escrita = new ReentrantLock();
        leitores = leitura.newCondition();
        escritores = escrita.newCondition();
        nEscritores = nLeitores = 0; 
    }
    public void readLock(){  leitura.lock();   }
    public void readUnlock(){ leitura.unlock();   }
    public void writerLock(){   escrita.lock();   }
    public void writerUnlock(){  escrita.unlock();  }
    
    public void ler(){
        /*le coisa */
    }
    public void escrever(){
        /*Escreve coisas */
    }
    
    
    
}
