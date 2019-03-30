
/**
 * Write a description of class Empresa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.lang.*;
import java.util.*;
import java.io.*;
public class Empresa extends Contribuinte
{
   private Map<String, Empregado> empregados;
    public boolean existeEmpregado(String cod){
        try{ 
            return this.empregados.containsKey(cod);
        }catch (NullPointerException e){
            return false; 
        }
    }
    
    public Empregado getEmpregado(String cod){
        Empregado e = null;
        if(this.empregados.containsKey(cod)){
            return this.empregados.get(cod).clone();
        }
        return e;
    }
    
    public void addEmpregado(Empregado e){
        if(this.empregados.containsValue(e)==false){
            this.empregados.put(e.getCodigo(), e);
        }
    }
    
   /* public Iterator<Empregado> listaPorOrdemDecrescenteSalario(){
        Iterator<Empregado> lista = new Iterator(); 
        new comparaSalario()  como usarr????
        for(Map.Entry<String, Empregado> e: this.empregados.entrySet()){
            lista.add(e.getValue());
        }
        return lista;         
    }*/
}
