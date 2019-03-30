
/**
 * Write a description of class Parque here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
public class Parque
{
    // variaveis da instancia
    /** Nome do parque */
    private String nomeParque; 
    private Map<String, Lugar> lugares;
    
    public Parque(){ this("N/a");}
    public Parque(String nomeParque){ this.nomeParque = nomeParque; lugares = new HashMap<>(); }
    public Parque(Parque p){ 
        this.nomeParque = p.getNome();
        this.lugares = p.getLugares();
        
    }
    
    public String getNome(){ return this.nomeParque; } 
    public void setNome(String s) {this.nomeParque = s; }
    public void setLugares(Map<String, Lugar> l){this.lugares = l;}
    
    public Map<String, Lugar> getLugares() {
        Map<String, Lugar> novo = new HashMap<>();
        for(Map.Entry<String, Lugar> e: this.lugares.entrySet() ){
            novo.put(e.getKey(), e.getValue().clone() );
        }
        return novo;
    }
    
    public void addLugar(Lugar l){
        lugares.put(l.getMatricula(), l.clone());
    }
    
    public void alterarMinutos(String m, int min){
        lugares.get(m).setMinutos(min);
    }
    
    
    
    
}
