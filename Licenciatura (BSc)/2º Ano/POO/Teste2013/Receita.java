
/**
 * Write a description of class Receita here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class Receita
{
    private String nome, descricao;
    private Set<Ingrediente> ingredientes;
    
    public Receita(String nome, String descricao, List<Ingrediente> produtos){
        this.nome = nome; this.descricao = descricao; 
        this.ingredientes = new TreeSet<Ingrediente>();
        for(Ingrediente i: produtos){
            this.ingredientes.add(i);
        }
    }
    public String getNome(){return this.nome; }
    public double calorias(){
        double cal = 0.0;
        for(Ingrediente i: ingredientes){
            cal += i.getCalorias();
        }
        return cal; 
    }
    public Set<Ingrediente> getIngredientes(){
        Set<Ingrediente> aux = new TreeSet<Ingrediente>();
        aux.addAll(this.ingredientes);
        return aux;
    }
}
