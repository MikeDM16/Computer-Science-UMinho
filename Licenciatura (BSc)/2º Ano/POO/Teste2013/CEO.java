
/**
 * Write a description of class CEO here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CEO extends Empregado
{
    private double salario;
    public CEO(CEO e){
        super(e);
        this.salario = e.getSalario();
    }
    
    public double getSalario(){return this.salario;}
    public void salario(){
        this.salario = 3*getSalDia();
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Nome"+this.getNome());
        return s.toString();
    }
    public CEO clone(){
        return new CEO(this);
    }
}
