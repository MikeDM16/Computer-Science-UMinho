
/**
 * Write a description of class Pessoa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pessoa
{
    private String nome;
    
    public Pessoa(){nome = new String(); nome = "N/a"; }
    public Pessoa(String nome){ this.nome = nome; }
    public Pessoa(Pessoa p) {nome = p.getNome(); }
    
    public String getNome(){ return this.nome; }
    public void setNome(String nome) {this.nome = nome; }
    
    public Pessoa clone(){
        return new Pessoa(this.nome);
    }
    
    public boolean equals(Object o){
        if(o==this) return true; 
        if( o==null || o.getClass()!= this.getClass()) return false; 
        else {
         Pessoa p = (Pessoa) o;
         return (p.getNome().equals(this.nome));
        }
    }
}
