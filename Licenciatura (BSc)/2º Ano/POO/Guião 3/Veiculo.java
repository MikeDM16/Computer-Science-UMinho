
/**
 * Escreva a descrição da classe Veiculo aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.Scanner;
import java.text.DecimalFormat;
import java.lang.String;
import java.math.BigDecimal;
public class Veiculo
{
    //Variaveis da instancia
    private int kmTotais, kmParcial, capacidadeMAX, conteudo; 
    private double consumoMedio;
    private String matricula;
  
    //construtores da instancia
    public Veiculo(){ this("00-00-00",0,0,0,0,0); }
    public Veiculo(String matricula, int kmT, int kmP, int capA, int capM, double con){
        this.matricula = matricula;
        kmTotais = kmT; kmParcial = kmP;
        conteudo = capA; capacidadeMAX = capM; 
        consumoMedio = con;
    }
    public Veiculo clone(){
        Veiculo novoC = new Veiculo();
        novoC.setMatricula(this.matricula);
        novoC.setkmsTotal(this.kmTotais);
        novoC.setkmParcial(this.kmParcial);
        novoC.setCapMAX(this.capacidadeMAX);
        novoC.setConteudo(this.conteudo);
        novoC.setConsumo(this.consumoMedio);
        return novoC;
    }
    
    
    //metodos da instancia
    public String getMatricula(){ return matricula; }
    public int getKmsTotal() { return kmTotais; }
    public int getKmsParcial() { return kmParcial; }
    public int getCapacidade() { return capacidadeMAX; }
    public int getConteudo() { return conteudo; }
    public double getConsumoMedio() {return consumoMedio; }
    
    public void setMatricula(String matricula){ this.matricula = matricula; }
    public void setkmsTotal(int kmTotais) { this.kmTotais = kmTotais; }
    public void setkmParcial(int kmParcial) { this.kmParcial = kmParcial; }
    public void setCapMAX(int capacidadeMAX) { this.capacidadeMAX = capacidadeMAX; }
    public void setConteudo(int capacidadeAtual) { this.conteudo = capacidadeAtual; }
    public void setConsumo(double consumoMedio) {this.consumoMedio = consumoMedio; }
    
    public boolean equals(Veiculo v){
        if(v==null) {return false; }
        if(v==this){ return true; }
        if(v.getClass()!=this.getClass()){return false; }
        return (v.getMatricula() == this.matricula && v.getKmsTotal()==this.kmTotais &&
        v.getKmsParcial()==this.kmParcial && v.getCapacidade()==this.capacidadeMAX &&
        v.getConteudo()==this.conteudo && v.getConsumoMedio()==this.consumoMedio);
    }
    
    public String toString(){
        String s = new String();
        s = "A matricula do veiculo é: "+this.matricula;
        return s; 
    }
    
    public void abastecer(int litros){
        int i;
        for(i=0; (i<litros) && (conteudo<capacidadeMAX) ; i++){
            conteudo++;
        }
    }
    public void resetKms(){
        kmParcial = 0;
        consumoMedio = 0;
    }
    public double autonomia(){
        return ( (conteudo*100) / consumoMedio );
    }
    public void registarViagem(int kms, double consumo){
        kmTotais += kms;
        if (((conteudo - consumo))<0){
            conteudo = 0;
        }else {
            conteudo -= consumo;
        }
        consumoMedio = (consumo*100) / kms;
    }
    public boolean naReserva(){
        return (conteudo < 10 );
    }
    public double totalCombustivel(double custoLitro){
        return ( kmTotais*custoLitro*consumoMedio)/100;
    }
    public double custoMedioKm(double custoLitro){
        return (consumoMedio * custoLitro)/100; 
    }
}
