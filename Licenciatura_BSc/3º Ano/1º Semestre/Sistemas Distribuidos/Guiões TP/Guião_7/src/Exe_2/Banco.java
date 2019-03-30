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

public class Banco
{
    private Map<Integer, Conta> contas;
    private int idConta;
    private ReentrantLock lockBanco;
    
    public Banco(){
        this.contas = new TreeMap<Integer, Conta>();
        lockBanco = new ReentrantLock();
    }
    
    public int createAccount(float i){
        /*Lock para obter o numero da proxima conta a adicionar*/
        lockBanco.lock();
        int nConta = idConta++;
        lockBanco.unlock();
        
        /*Lock para colocar a nova conta no sistema do banco*/
        Conta c = new Conta(i);
        lockBanco.lock();
        contas.put(nConta, c);
        lockBanco.unlock();
        
        return nConta;
    }
    
    public float closeaccount(int id) throws InvalidAccount{
        float saldo = 0;
        Conta c = null;
        lockBanco.lock();
        try{
            if(contas.containsKey(id)){
                c = contas.get(id);
                c.getLock();
                saldo = c.getSaldo();
                c.getUnlock();
                
                contas.remove(id);
                lockBanco.unlock();
            }else {throw new InvalidAccount("O id nao corresponde a nenhuma conta"); }
        }finally{ lockBanco.unlock(); }
        
        return saldo; 
    }
    
    public void transfer(int from, int to, float value) throws InvalidAccount, NotEnoughtFounds{
        lockBanco.lock();
        Conta f = null, t = null;
        try{
            
            if(contas.containsKey(from) && contas.containsKey(to)){
                f = contas.get(from);                
                f.getLock(); /*lock à conta origem, tendo já o lock do banco*/
                try{
                    if(f.getSaldo()>value){
                    t = contas.get(to); /*tenho o lock do banco*/
                    t.getLock(); /*depois de ter o lock das duas contas desbloquio o banco*/
                    lockBanco.unlock();
                     
                    f.debito(value);
                    f.getUnlock(); /*depois de operar na conta origem, liberto-a*/
                    t.credito(value);
                    t.getUnlock();/*assim que opero na conta destino, liberto-a*/
                        
                   }else{ throw new NotEnoughtFounds("Saldo da conta origem nao disponivel");}   
                }finally{f.getUnlock();}
                
            }else{throw new InvalidAccount("Uma das contas nao existe no sistema");}   
            
        }finally{
            lockBanco.unlock();
        }
    }
    
    public float totalBalance(int accounts[]){ /*array com os ids das contas*/
        float res = 0;
        Arrays.sort(accounts);
        Conta[] contasBalanco = new Conta[accounts.length];
        int i;
        
        lockBanco.lock();
        try{
            for(i = 0; i<contasBalanco.length; i++){ contasBalanco[i] = contas.get( accounts[i] ); }
            /*array com todas as contas para realizar o balanco*/
            
            for(i = 0; i<contasBalanco.length; i++){contasBalanco[i].getLock(); }
            /*depois de ter o lock de todas as contas, libeto o lock ao banco*/
            lockBanco.unlock();
            
            for(i = 0; i<contasBalanco.length; i++){
                res += contasBalanco[i].getSaldo();
                contasBalanco[i].getUnlock();
            }
             /*reunir o saldo de todas as contas*/
             /*depois de realizar a operaçao pertendida libertar todas a contas*/
             
             /*
             for(i = 0; i<contasBalanco.length; i++){contasBalanco[i].getUnlock(); }
             /*depois de realizar a operaçao pertendida libertar todas as contas
             */
        }finally{lockBanco.unlock();}
        
        return res;
    }
    
}
