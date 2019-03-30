
/**
 * Write a description of class Lugar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lugar
{
    //variaveis de instancia
    /** Matricula do veiculo alocado */
    private String matricula;
    /** Nome do proprietario */
    private String nome;
    /** Tempo atribuido ao lugar, em minutos */
    private int minutos;
    /** Indica se o lugar é permanente, ou de aluguer */
    private boolean permanente; 
    
    // Construtores da instancia
    public Lugar(){ this(null, null, 0, false);}
    public Lugar(String matricula, String nome, int minutos, boolean permanente){
        this.matricula = matricula;        this.nome = nome;
        this.minutos = minutos;      this.permanente = permanente; 
    }
    public Lugar(Lugar l){ this(l.getMatricula(), l.getNome(), l.getMinutos(), l.getPermanente() );}
    /** metodos get */
    public String getMatricula(){ return this.matricula; }
    public String getNome(){ return this.nome; }
    public int getMinutos(){ return this.minutos; }
    public boolean getPermanente(){ return this.permanente; }
    /** metodos set */
    public void setMatricula(String m){ this.matricula = m; }
    public void setNome(String n){ this.nome = n; }
    public void setMinutos(int m){this.minutos = m; }
    public void setPermanente(boolean b){this.permanente = b; }
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("O Lugar tem a matricula"+this.matricula+" associada.\n");
        s.append("O lugar está em nomde de "+this.nome+".");
        if(this.permanente == false){ s.append("O lugar é reservado permanentemente.\n");}
        else { s.append("O lugar nao está reservado permanentemente.\n");}
        return s.toString();
    }
    public Lugar clone(){ return new Lugar(this); }
    public boolean equals(Lugar l){
        if(l==null) {return false; }
        if(this == l) {return true; }
        if( l.getClass() != this.getClass() ) {return false; }
        return ( this.getMatricula().equals(l.getMatricula()) && this.getNome().equals(l.getNome())
        && this.getMinutos()==l.getMinutos() && this.getPermanente()==l.getPermanente() );
    }
    
    
}
