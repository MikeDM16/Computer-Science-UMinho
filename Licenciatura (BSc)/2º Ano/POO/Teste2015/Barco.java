
/**
 * Write a description of class Barco here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;

public class Barco{
    private String id; 
    private double milhas; 
    private String categoria; 
    private double autonomia; 
    private Pessoa skipper; 
    private Set<Pessoa> tripulantes;
    private List<RegistoEtapa> etapas; 
    
    public Barco(){ this("n/a", 0.0, "n/a", 0.0, null, null); }
    public Barco(String id, double m, String cat, double aut, Pessoa p, Set<Pessoa> trip){
        this.id = id; this.milhas = m; this.categoria = cat; this.autonomia = aut;
        this.skipper = p; this.tripulantes = trip;
    }
    public Barco(Barco b){
        this(b.getId(), b.getMilhas(), b.getCategoria(), b.getAutonomia(), 
        b.getSkipper(), b.getTripulantes());
    }
    
    public void setId(String id){this.id = id;}
    public void setMilhas(double d) {this.milhas = d; }
    public void setCategoria(String cat) {this.categoria = cat; }
    public void setAutonomia(double d) {this.autonomia = d; }
    public void setSkipper(Pessoa p ) {this.skipper = p; }
    public void setTripulantes(Set<Pessoa> p){this.tripulantes = p; }
   
       public String getId(){ return this.id; }
    public double getMilhas(){ return this.milhas; }
    public String getCategoria(){ return this.categoria; }
    public double getAutonomia(){ return this.autonomia; }
    public Pessoa getSkipper() { return this.skipper; }
    public List<RegistoEtapa> getRegisto(){ return this.etapas; }
    public Set<Pessoa> getTripulantes() {
        Set<Pessoa> aux = new TreeSet<Pessoa>();
        if(this.tripulantes!=null){
            for(Pessoa p: tripulantes){
                aux.add(p.clone());
            }
        }
        return aux;
    }
    
    public Barco clone(){ return new Barco(this.getId(), this.getMilhas(), 
        this.getCategoria(), this.getAutonomia(), this.getSkipper(), this.getTripulantes());
    }
    public boolean equals(Object o){ 
        if(o==this) return true; 
        if( o==null || o.getClass()!= this.getClass()) return false; 
        else {
             Barco b = (Barco) o;
             return ( this.id.equals(b.getId()) && this.milhas==b.getMilhas() &&
             this.categoria.equals(b.getCategoria()) && this.autonomia==b.getAutonomia() &&
             this.skipper.equals(b.getSkipper()) && compara(b.getTripulantes()) );
            }
    }
    private boolean compara (Set<Pessoa> p) {
        if (p.size() != this.tripulantes.size() ) return false;
        for(Pessoa aux : p){
            if(this.tripulantes.contains(aux)==false) return false; 
        }
        return true;
    }
    
    public double totalProva(){
        double res = 0.0;
        for(RegistoEtapa r: etapas) {
            res += r.difTempo();
        }
        return res; 
    }
} 
