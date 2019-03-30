
/**
 * Abstract class Empregado - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Empregado extends Contribuinte
{
   private String codigo, nome;
   private double salDia = 50.00;
   
   public Empregado(String codigo, String nome, int salario){
       this.codigo = codigo; this.salDia = salario;
    }
   Empregado(Empregado e){
       this.codigo = e.getCodigo();
       this.nome = e.getNome();
       this.salDia = e.getSalDia();
   }
   
   public Empregado clone(){
      Empregado e = null;
      /*---*/
      return e; 
    }
   
   public String getCodigo(){ return this.codigo;  }
   public String getNome(){return this.nome; }
   public double getSalDia(){ return this.salDia; }
}
