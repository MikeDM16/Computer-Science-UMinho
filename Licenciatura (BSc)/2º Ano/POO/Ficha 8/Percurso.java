
/**
 * Write a description of class Percurso here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.lang.String;
public class Percurso
{
    private String nomePercurso;
    private Stack<String> locsVisitadas;
    private Stack<String> locsPorVisitar;
    
    public Percurso(Collection<String> localidades){
        this.locsPorVisitar = new Stack<String>();
        try{
            for(String loc: localidades){
            this.locsPorVisitar.push(loc);
           }
        }catch (NullPointerException e){
            System.out.println("Não inseriu nenhuma localidade");
        }
    }
    
    public String locAVisitar(){
        try{
            return this.locsPorVisitar.peek();
        }catch (EmptyStackException e){
            System.out.println("Não tem localidades a visitar");
            return null;
        }
    }
    
    public void proxLoc(){
        String s = new String();
        try {
            s = this.locsPorVisitar.pop();
            this.locsVisitadas.push(s);
        }catch (EmptyStackException e){
             System.out.println("Não tem localidades a visitar");
        }
    }
    
    public void removeLocalidade(String loc){
        try {
            if(this.locsVisitadas.contains(loc)==false){
                this.locsPorVisitar.remove(loc);
            }else {System.out.println("Essa localidade já foi visitada");}
        }catch (EmptyStackException e){
            System.out.println("Não tem localidades a visitar");
        }
    }
}
