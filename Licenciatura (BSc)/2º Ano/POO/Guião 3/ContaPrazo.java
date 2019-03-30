
/**
 * Escreva a descrição da classe ContaPrazo aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.GregorianCalendar; 
public class ContaPrazo
{
    // variaveis da instancia
    private String codigo, titular;
    private GregorianCalendar dataIni, prazo;
    private double capitalDep, juro;
    
    //construtores da instancia
    public ContaPrazo(String codigo, String titular, GregorianCalendar dataIni, double capitalDep, 
    GregorianCalendar prazo, double juro){
        this.codigo = codigo; this.titular = titular; this.capitalDep = capitalDep;
        this.dataIni = dataIni; this.prazo = prazo; this.juro = juro;
    }
    public ContaPrazo clone(){
        return (new ContaPrazo(this.codigo, this.titular, this.dataIni, this.capitalDep, this.prazo,
        this.juro));
    }
    //metodos da instancia
    public String getCodigo(){ return codigo; }
    public String getTitular(){ return titular; }
    public double getCapital(){ return capitalDep; }
    public double getTaxa(){ return juro; }
    public GregorianCalendar getDataIni(){ return dataIni; }
    public GregorianCalendar getPrazo(){return prazo; }
    
    public void setCodigo(String codigo){this.codigo = codigo; }
    public void setDataIni(GregorianCalendar dataIni){ this.dataIni = dataIni; }
    public void setPrazo(GregorianCalendar prazo) {this.prazo = prazo; }
    
    public boolean equals(ContaPrazo p){
        if(p==null){ return false; }
        return (p.getCodigo()==this.codigo && p.getTitular()==this.titular && p.getCapital()==this.capitalDep &&
        p.getTaxa()==this.juro && p.getDataIni()==this.dataIni && p.getPrazo()==this.prazo);
    } 
    
    public String toString(){
        String s = new String();
        s = "O codigo do cliente é :"+ this.codigo +". \n";
        return s; 
    }
    
    public void alterarTaxa(double juro){ this.juro = juro; }
    public void alterarTitular(String titular){ this.titular = titular; }
    public int diasPassados(){
        GregorianCalendar atual = new GregorianCalendar();
        double dif = atual.getTimeInMillis()- dataIni.getTimeInMillis();
        dif = dif / (24*60*60*1000); //passar milisegundos para dias; 
        return ((int) dif);
    }
    public void actualizarJuros(GregorianCalendar novaData){
        prazo = novaData;
        capitalDep += ( capitalDep*juro );
    }
    public boolean verificarDiaJuros(){
        int dia, mes, ano;
        dia = prazo.get(GregorianCalendar.DAY_OF_MONTH);
        mes = prazo.get(GregorianCalendar.MONTH);
        ano = prazo.get(GregorianCalendar.YEAR);
        GregorianCalendar atual = new GregorianCalendar();
        int day, month, year;
        day = atual.get(GregorianCalendar.DAY_OF_MONTH);
        month = atual.get(GregorianCalendar.MONTH);
        year = atual.get(GregorianCalendar.YEAR);
        return ( (dia==day) && (mes==month) && (ano==year) );
        
    }
    public double fecharConta(){
        //if(verificarDiaJuros()){
            capitalDep += ( (capitalDep*juro) );
        //} //  nao devia só acrescentar juro se fosse a dato do prazo do seu vencimento? 
        double r = capitalDep;
        capitalDep = 0; 
        return r;
    }
}


