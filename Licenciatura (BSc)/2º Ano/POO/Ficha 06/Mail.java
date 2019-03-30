
/**
 * Write a description of class Mail here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.lang.String;

public class Mail
{
    //variaveis de instancia Mail
    private String remetente, dataEnvio, dataRececao, assunto, corpoEmail;
    
    //construtores da instancia mail 
    public Mail(){this("N/a","N/a","N/a","N/a","N/a"); }
    public Mail(String remetente, String dataEnvio, String dataRececao, String assunto, String corpoEmail){
        this.remetente = remetente; this.dataEnvio = dataEnvio; this.dataRececao = dataRececao;
        this.assunto = assunto; this.corpoEmail = corpoEmail; 
    }
    public Mail(Mail m) {
        this(m.getRemetente(), m.getDataEnvio(), m.getDataRececao(), m.getAssunto(), m.getCorpoEmail());
    }
    
    //metodos da instancia
    public void setRemetente(String r){this.remetente = r; }
    public void setDataEnvio(String d) { this.dataEnvio = d; }
    public void setDataRececao(String d){ this.dataRececao = d; }
    public void setAssunto(String a) {this.assunto = a;}
    public void setCorpoEmail(String e) { this.corpoEmail = e; }
    
    public String getRemetente(){return this.remetente; }
    public String getDataEnvio() { return this.dataEnvio; }
    public String getDataRececao(){ return this.dataRececao; }
    public String getAssunto() { return this.assunto;}
    public String getCorpoEmail() { return this.corpoEmail; }
    
    public boolean equals(Object o){
        if(o==null) { return false; }
        if(this==o) { return true;} 
        if(this.getClass() != o.getClass() ){ return false; }
        Mail m = (Mail) o;
        return ( (this.remetente.equals(m.getRemetente())) && (this.dataEnvio.equals(m.getDataEnvio())) 
        && (this.dataRececao.equals(m.getDataRececao())) && (this.assunto.equals(m.getAssunto())) 
        && (this.corpoEmail.equals(m.getCorpoEmail())) ); 
        
    }
    public Mail clone(){
        Mail novo = new Mail(this.getRemetente(), this.getDataEnvio(), this.getDataRececao(),
        this.getAssunto(), this.getCorpoEmail());
        return novo;
    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("O mail foi enviado por: "+this.remetente);
        s.append("Foi enviado a +"+this.dataEnvio+" e recebido em "+this.dataRececao);
        s.append("O assunto do email é +"+this.assunto);
        return s.toString();
        
    }
}
