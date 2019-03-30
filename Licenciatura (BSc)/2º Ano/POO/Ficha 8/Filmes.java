
/**
 * Write a description of class Filmes here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*; 
public class Filmes extends Multimédia
{
    private String realizador;
    private List<String> atores;
    
    public Filmes(String id, String titulo, String realizador, double duracao, List<String> perso, boolean p, String comentario){
        super(id,titulo, duracao, p, comentario); 
        this.realizador = realizador; 
        for(String s: perso){
             this.atores.add(s);
            }
    }
    public Filmes(Filmes f){
        super(f);
        this.realizador = f.getRealizador();
    }
    
    public String getRealizador(){return this.realizador;}
    public List<String> getAtores(){
        ArrayList<String> aux = new ArrayList<String>();
        for(String s: this.atores){aux.add(s); }
        return aux;
    }
    
    public Filmes clone(){ return new Filmes(this); }
}
