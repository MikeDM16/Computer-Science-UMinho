
/**
 * Escreva a descrição da classe complexos1 aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.lang.Math;
public class Complexo
{
    // variaveis da instancia
    private double a,b;
    
    //construtores de instancia
    public Complexo(){ this(0.0, 0.0); }
    public Complexo(double x, double y){ a = x; b = y; }
    public Complexo clone(){ 
        return new Complexo(this.a, this.b);
        /*
        Complexo clone = new Complexo();
        clone.setA(this.a);
        clone.setB(this.b);
        return clone;  
        */
    }
    
    
    //metodos de instancia
    public double getA(){  return a; }
    public double getB(){  return b; }
    
    public void setA(double x) { this.a = x; }
    public void setB(double y) { this.b = y; }
    public boolean equals(Complexo p){
        if(p==null) { return false;}
        if(p==this) { return true; }
        if(this.getClass()!=p.getClass()){ return false; }
        return ( (this.a == p.getA()) && (this.b == p.getB()) );
        
    }
    public String toString(){
        String valor = new String();
        valor = "valor de A = "+this.a+", valor de B = "+this.b+".";
        return valor; 
    }
    public Complexo conjugado(){
        Complexo conjugado = new Complexo();
        conjugado.b = (-1)*this.b;
        conjugado.a = this.a;
        return conjugado;
    }
    public Complexo soma(Complexo complexo){
        Complexo somaC = new Complexo();
        somaC.a = (this.a + complexo.getA());
        somaC.b = (this.b + complexo.getB());
        return somaC;
    }
    public Complexo produto(Complexo complexo){
        Complexo prodC = new Complexo();
        prodC.a = (this.a*complexo.getA() - this.b*complexo.getB());
        prodC.b = (this.b*complexo.getA() + this.a*complexo.getB());
        return prodC;
    }
    public Complexo reciproco(){
        Complexo recpC = new Complexo();
        recpC.a = ( this.a / (Math.pow(this.a,2) + Math.pow(this.b,2)) ); 
        recpC.b = (-1)*( this.b / (Math.pow(this.a,2) + Math.pow(this.b,2)));
        return recpC;
    }
}
