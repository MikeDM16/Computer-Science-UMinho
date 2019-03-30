
/**
 * Write a description of class Discount here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Discount extends Hotel
{
   /** Taxa de ocupacao */
   private double ocupacao;
   
   /**
    * construtor vazio
    */
   public Discount(){
       super();
       ocupacao = 0.0;
    }
   
    /**
     * Construtor por cópia
     * @param c 
     */
    public Discount(Discount c) {
        super(c);
        this.ocupacao = c.getOcupacao();
    }
    /**
     * Construtor por parâmetro
     * @param codigo
     * @param nome
     * @param localidade
     * @param precoQuarto
     * @param epocaAlta 
     */
    public Discount(String codigo, String nome, String localidade
                        , double precoQuarto, int nquartos, double ocupacao) {
        super(codigo, nome, localidade, precoQuarto, nquartos);
        this.ocupacao = ocupacao;
    }
    
    /**
     * Calcula o preço de uma noite no hotel
     * @return 
     */
    public double precoQuarto() {
        double r = super.getPrecoQuarto();
        return 0.5*r;
    }
    
    public void setOcupacao(double c ){ this.ocupacao = c;}
    public double getOcupacao(){ return this.ocupacao; }
    
    public Discount clone(){ return new Discount(this); }
    
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Discount o = (Discount) obj;
        return super.equals(o) && o.getOcupacao() == this.ocupacao;
    }
    /**
     * Retorna representação textual
     * @return 
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Preço final: ").append(precoQuarto()).append("€");        
        return sb.toString();
    }
}
