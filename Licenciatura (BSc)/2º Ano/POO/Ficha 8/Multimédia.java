
/**
 * Abstract class Multimédia - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Multimédia
{
    /* o identificador unico de um objeto multimedia é o seu titulo*/
    private String identificador, titulo, comentario; 
    private double duracao;
    private boolean possui; 
    
    public Multimédia(String id, String titulo, double duracao, boolean p, String comentario){
        this.identificador = id; this.titulo = titulo; this.duracao = duracao; 
        this.possui = p; this.comentario = comentario; 
    }
    public Multimédia(Multimédia m){
        this.identificador = m.getIdentificador();
        this.titulo = m.getTitulo();
        this.comentario = m.getComentario();
        this.duracao = m.getDuracao(); this.possui = m.getPossui();
    }
    public boolean getPossui(){return this.possui; }
    public double getDuracao(){return this.duracao; }
    public String getComentario(){return this.comentario; }
    public String getTitulo(){return this.titulo; }
    public String getIdentificador(){return this.identificador; }
    public abstract Multimédia clone();
}
