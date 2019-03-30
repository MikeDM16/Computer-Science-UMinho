
/**
 * Write a description of class Albuns here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Albuns extends Multimédia
{
    private String nomeArtista;
    private int numeroFaixas;
    
    public Albuns(String id, String titulo, String nome, int faixas, double  duracao,boolean p, String comentario){
       super(id, titulo, duracao, p, comentario);
        this.nomeArtista = nome; this.numeroFaixas = faixas; 
    }
    public Albuns(Albuns a){
        super(a);
        this.nomeArtista = a.getNomeArtista();
        this.numeroFaixas = a.getNumeroFaixas();
    }
   
    public String getNomeArtista(){return this.nomeArtista;}
    public int getNumeroFaixas(){return this.numeroFaixas; }
    
    public Albuns clone(){
       return new Albuns(this);
    }
}
