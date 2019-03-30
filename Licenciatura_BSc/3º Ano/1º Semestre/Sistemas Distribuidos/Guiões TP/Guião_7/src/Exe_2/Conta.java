/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exe_2;

/**
 *
 * @author migue
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
