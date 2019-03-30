
/**
 * Write a description of class HoteisIncList here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import static java.util.stream.Collectors.toMap;

public class HoteisIncList
{
    /** Nome da cadeia */
    private String nome;
        /** Data de início de actividade */
    private Date inicio;
        /** Mapeamento de código de hotel para hotel */
    private List<Hotel> hoteis;
    //Construtores da classe
    /**
     * Construtor vazio
     */
    public HoteisIncList() {
        nome = "HoteisIncList";   inicio = new Date();  // VALIDAR
        hoteis = new ArrayList<Hotel>();
    }
    /**
     * Construtor por cópia
     * @param c 
     */
    public HoteisIncList(HoteisIncList c) {
        this.nome = c.getNome(); this.inicio = c.getInicio();  this.hoteis = c.getHoteis();
    }
    /**
     * Construtor por parâmetro
     * @param hoteis 
     */
    public HoteisIncList(String nome, Date inicio, List<Hotel> hoteis) {
        this.nome = nome;           // String são imutáveis
        this.inicio = inicio;       // Dates são imutáveis
        this.hoteis = new ArrayList<Hotel>();   
        setHoteis(hoteis);
    }
    //metodos da classe 
    public void setHoteis(List<Hotel> lH){
        Iterator<Hotel> it = lH.iterator();
        for(Hotel h = null; it.hasNext();){ h = it.next().clone();this.hoteis.add(h);  }
    }
    public void setNome(String nome) { this.nome = nome; }
    public void setDate(Date d) {this.inicio = d; }
    public Date getInicio(){return this.inicio; }
    public String getNome(){ return this.nome; }
    public List<Hotel> getHoteis(){
        List<Hotel> res = new ArrayList<Hotel>();  Hotel h= null;
        Iterator<Hotel> it = this.hoteis.iterator();
        while(it.hasNext()){
            h = it.next().clone();
            res.add(h);
        }
        return res; 
    }
    
    public boolean existeHotel(String codigo){
        Iterator<Hotel> it = this.hoteis.iterator(); Hotel h = null;
        for(; it.hasNext();){
            h = it.next();
            if(h.getCodigo().equals(codigo)){return true; }
        }
        return false; 
    }
    public int quantos(){ return this.hoteis.size(); }
    public int quantos(String tipo){ return 0;}
    
    public Hotel getHotel(String cod){
        Iterator<Hotel> it = this.hoteis.iterator(); Hotel h = null; 
        Hotel res = null;
        for(; it.hasNext();){
           h = it.next();
           if(h.getCodigo().equals(cod)){ res = h.clone(); break;}
        }
        return res; 
    }
    public void adiciona(Hotel h){
        this.hoteis.add(h);
    }
    
}
