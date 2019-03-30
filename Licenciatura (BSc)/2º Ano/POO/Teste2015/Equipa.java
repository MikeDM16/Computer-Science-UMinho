
/**
 * Write a description of class Equipa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class Equipa
{
    private String nome;
    private Map<String, Barco> barcos; 
    
    public Equipa(){this("n/a", null); }
    public Equipa(String nome, Map<String, Barco> barcos){ this.nome = nome; this.barcos = barcos;}
    public Equipa(Equipa e){ this(e.getNome(), e.getBarcos()); }
    
    public void setNome(String n){ this.nome = n; }
    public void setBarcs(Map<String, Barco> b){ this.barcos = b; }
    
    public String getNome(){ return this.nome; }
    public Map<String, Barco> getBarcos(){
        Map<String, Barco> aux = new TreeMap<String, Barco>();
        for(Map.Entry<String, Barco> b: this.barcos.entrySet()){
            aux.put(b.getKey(), b.getValue());
        } 
        return aux; 
    }
    
    public void removeBarco(String idBarco){
        this.barcos.remove(idBarco);
    }
    
    public double registoMaisLongo(){
       double tempo, regLongo = 0.0;
       for(Map.Entry<String, Barco> b: this.barcos.entrySet()){
           tempo = totalEmProva(b.getKey());
           if(tempo > regLongo) regLongo = tempo; 
           
       }
       return regLongo; 
    }
    
    public double totalEmProva(String idBarco){
        /*Barco b = barcos.get(idBarco); 
        return b.totalProva();
        */
       GregorianCalendar ini,fim;
       double tempoTotal = 0.0;
       Barco b = barcos.get(idBarco);
       List<RegistoEtapa> reg = b.getRegisto();
       for(RegistoEtapa r: reg){
           ini = r.getInicio();
           fim = r.getfim();
           tempoTotal += 
               (double) ( (fim.getTimeInMillis() - ini.getTimeInMillis()) / (60*60*1000) );
        }
       return tempoTotal; 
    }
   
    public Equipa clone(){ return new Equipa(this.getNome(), this.getBarcos()); }
    public boolean equals(Object o){
    if(o==this) return true; 
    if( o==null || o.getClass()!= this.getClass()) return false; 
    else {
         Equipa e = (Equipa) o;
         return ( e.getNome().equals(this.nome) && compara(e.getBarcos()) );
        }
    }
    private boolean compara(Map<String, Barco> barcos){
        for(Map.Entry<String, Barco> b: barcos.entrySet()){
            Barco aux = b.getValue();
            if(this.barcos.containsValue(aux)==false) return false; 
        }
        return true; 
    }
    
}
