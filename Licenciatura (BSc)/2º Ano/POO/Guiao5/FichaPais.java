
/**
 * Write a description of class FichaPais here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FichaPais
{
    // instance variables
    private double populacao; 
    private String nome, continente;
    
    //construtores da instancia
    public FichaPais(){this.populacao = 0; nome = "NoName"; continente = "NoName"; }
    public FichaPais(String nome, String continente, double populacao){
        this.populacao = populacao; this.nome = nome; this.continente = continente; 
    }
    public FichaPais clone(){
        FichaPais novo = new FichaPais(this.nome, this.continente, this.populacao);
        return novo;
    }
    
    public String getNome(){return this.nome;}
    public String getContinente(){return this.continente; }
    public double getPopulacao(){return this.populacao; }
    public void setPopulacao(double p){this.populacao = p;}
    public void setNome(String n){this.nome = n;}
    public void setcontinente(String c){this.continente = c; }
}

