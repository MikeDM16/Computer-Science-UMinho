
/**
 * Write a description of class VOR here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.lang.*;
import java.util.*;
import java.io.*;
import Exception.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.joining;
 
public class VOR
{
   private Map<String, Equipa> equipas;
   
   public Barco getBarco(String idEquipa, String idBarco){
      Barco b = null;
       for(Map.Entry<String, Equipa> e: equipas.entrySet()){
         if(e.getKey().equals(idEquipa)){
            b = e.getValue().getBarcos().get(idBarco).clone();
         }
      }
      return b; 
   }
   
   public List<Barco> getBarcos(String idEquipa, double milhas) {
       Map<String, Barco> barcos = null;
       Barco baux = null;
       List<Barco> res = null;
       for(Map.Entry<String, Equipa> e: this.equipas.entrySet()){
           if(e.getKey().equals(idEquipa)==true){
               barcos = e.getValue().getBarcos();
               for(Map.Entry<String, Barco> b: barcos.entrySet()){
                   baux = b.getValue();
                   if(baux.getMilhas()>= milhas) { res.add(baux.clone()); }
                }
               break;
            }
        }
       return res; 
   }
   /*
   public List<Barco> getBarcos(String idEquipa, double milhas) {
       Map<String, Barco> barcos = null;
       Barco baux = null;
       List<Barco> res = null;
       for(Map.Entry<String, Equipa> e: this.equipas.entrySet()){
           if(e.getKey().equals(idEquipa)==true){
               res = e.getValue().getBarcos()
                                 .filter(m -> this.getValue().getMilhas() >= milhas)
                                 .collect(Collectors.toMap());
                }
            }
       return res;
   }*/
   public void removeBarco(String idEquipa, String idBarco){
       
       for(Map.Entry<String, Equipa> e: this.equipas.entrySet() ){
           if(e.getKey().equals(idEquipa)){
               e.getValue().removeBarco(idBarco);
            }
        }
    }
   
   
}
