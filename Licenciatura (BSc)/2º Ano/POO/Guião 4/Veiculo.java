
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
    private double kmTotais, kmParcial,consumoMedio;
    private int capacidade, conteudo, tipoCombustivel;
    private String matricula;
    
    public static double Gasolina95, Gasolina98, Gasoleo; 
    //construtores da instancia
    public Veiculo(){ this("00-00-00",0,0,0,0,0,0); }
    public Veiculo(String matricula, double kmT, double kmP, int capA, int capM, double con, int tC){
        this.matricula = matricula; kmTotais = kmT; kmParcial = kmP;
        conteudo = capA; capacidade = capM; consumoMedio = con; tipoCombustivel=tC;
    }
    public Veiculo clone(){
        /* return new Veiculo(this.matricula, this.kmTotais, this.kmParcial, this.conteudo,
        this.capacidade, this.consumoMedio, this.tipoCombustivel); */
        Veiculo novoC = new Veiculo();
        novoC.setMatricula(this.matricula); novoC.setKmsTotal(this.kmTotais);
        novoC.setkmParcial(this.kmParcial); novoC.setCapMAX(this.capacidade);
        novoC.setConteudo(this.conteudo); novoC.setConsumo(this.consumoMedio);
        novoC.setCapMAX(this.capacidade);
        novoC.setTipoCombustivel(this.tipoCombustivel);
        return novoC;
    }
    
    
    //metodos da instancia
    public String getMatricula(){ return matricula; }
    public double getKmsTotal() { return kmTotais; }
    public double getKmsParcial() { return kmParcial; }
    public int getCapacidade() { return capacidade; }
    public int getConteudo() { return conteudo; }
    public double getConsumoMedio() {return consumoMedio; }
    public int getTipoCombustivel() {return tipoCombustivel; }
    public double getPrecoFUEL(int t){
        double r=0.0;
        if(t==0){ r=Gasolina95; }
        if(t==1){ r=Gasolina98; }
        if(t==2){ r= Gasoleo; }
        return r;
    }
    
    public void setMatricula(String matricula){ this.matricula = matricula; }
    public void setKmsTotal(double kmTotais) { this.kmTotais = kmTotais; }
    public void setkmParcial(double kmParcial) { this.kmParcial = kmParcial; }
    public void setCapMAX(int capacidade) { this.capacidade = capacidade; }
    public void setConteudo(int capacidadeAtual) { this.conteudo = capacidadeAtual; }
    public void setConsumo(double consumoMedio) {this.consumoMedio = consumoMedio; }
    public void setTipoCombustivel(int tipoCombustivel){this.tipoCombustivel  = tipoCombustivel; }
    
    public boolean equals(Veiculo v){
        if(v==null) {return false; }
        if(v==this){ return true; }
        if(v.getClass()!=this.getClass()){return false; }
        return (v.getMatricula() == this.matricula && v.getKmsTotal()==this.kmTotais &&
        v.getKmsParcial()==this.kmParcial && v.getCapacidade()==this.capacidade &&
        v.getConteudo()==this.conteudo && v.getConsumoMedio()==this.consumoMedio &&
        this.tipoCombustivel==getTipoCombustivel() );
    }
    public void abastecer(int litros){
        int i;  
        for(i=0; (i<litros) && (conteudo<capacidade) ; i++){
            conteudo++;
        }
    }
    public void resetKms(){ kmParcial = 0; consumoMedio = 0; }
    public double autonomia(){  return ( (conteudo*100) / consumoMedio ); }
    public void registarViagem(int kms, double consumo){
        kmTotais += kms;
        if (((conteudo - consumo))<0){
            conteudo = 0;
        }else {
            conteudo -= consumo;
        }
        consumoMedio = (consumo*100) / kms;
    }
    public boolean naReserva(){ return (conteudo < 10 ); }
    
    //AULA 4 AULA 4 AULA 4  AULA 4 AULA 4 AULA 4  AULA 4 AULA 4 AULA 4  AULA 4 AULA 4 AULA 4  
    public static double CustoMedioLtr(int tipoComb){
        double r=0.0; 
        switch (tipoComb) {
            case 0: {r = Gasolina95; break; }
            case 1: {r = Gasolina98; break; }
            case 2: {r = Gasoleo; break; }
            default: break;
        }
        return r;
    }
    public static void setCustoMedioLtr(int tipoComb, double custo){
        switch (tipoComb) {
            case 0: {Gasolina95 = custo; break; }
            case 1: {Gasolina98 = custo; break; }
            case 2: {Gasoleo = custo; break; }
            default: break;
        }
    }
    public double totalCombustivel(){ 
        double r= 0.0;
       switch (tipoCombustivel) { //variavel da classe; 
            case 0: {r= (kmTotais*Gasolina95*consumoMedio)/100; break; }
            case 1: {r= (kmTotais*consumoMedio)/100; break; }
            case 2: {r= (kmTotais*Gasoleo*consumoMedio)/100; break; }
            default: break;
        }
       return r; 
    }
    public double custoMedioKm(){
         double r= 0.0;
       switch (tipoCombustivel) { //variavel da classe; 
            case 0: {r= (Gasolina95*consumoMedio)/100; break; }
            case 1: {r= (Gasolina98*consumoMedio)/100; break; }
            case 2: {r= (Gasoleo*consumoMedio)/100; break; }
            default: break;
        }
       return r;
    }
    public String toString(){
        String s = new String();
        s = "A matricula do veiculo é: "+this.matricula + ", KmsTotais ="+this.kmTotais+
        ", KmsParcial ="+this.kmParcial+", ConsMedio="+this.consumoMedio+", Contuedo="+
        this.conteudo+", Capacidade="+this.capacidade+", tipoComb="+this.tipoCombustivel; 
        return s;}
    
}
