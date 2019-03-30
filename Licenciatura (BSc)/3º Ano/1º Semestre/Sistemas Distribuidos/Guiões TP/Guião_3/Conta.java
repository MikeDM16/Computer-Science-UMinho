
/**
 * Write a description of class conta here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Arrays;

public class Conta
{
    private float saldo;
    private ReentrantLock lockConta; 
    public Conta(float f){
        this.saldo = f;
        lockConta = new ReentrantLock();
    }
    
    public void credito(float v){saldo += v;}
    public void debito(float v){saldo -= v; }
    
    public float getSaldo(){return this.saldo; }
    
    public void getLock(){this.lockConta.lock();}
    public void getUnlock(){this.lockConta.unlock();}
}
