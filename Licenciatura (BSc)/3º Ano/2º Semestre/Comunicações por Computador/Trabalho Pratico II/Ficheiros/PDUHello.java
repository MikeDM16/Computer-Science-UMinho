import java.net.*;

/**
 *
 * @author migue
 */
public class PDUHello {
    int idBackEnd, porta, nrSeq, nrCliAtv;
    InetAddress endIP;
    double TempoEnvio; String designacao;

    public PDUHello(int idBackEnd, String designacao, int porta, int nrSeq,  double TempoEnvio, int nrCliAtv, InetAddress endIP){ 
        this.idBackEnd = idBackEnd;       this.porta = porta;
        this.nrSeq = nrSeq;               this.TempoEnvio = TempoEnvio;
        this.nrCliAtv = nrCliAtv;         this.endIP = endIP;
        this.designacao = designacao;     
    }
       
    public String getPDU(){
        StringBuilder s = new StringBuilder();
        s.append("PDU" + " ");
        s.append(idBackEnd + " ");
        s.append(designacao + " ");
        s.append(porta + " ");
        s.append(nrSeq + " ");
        s.append(TempoEnvio + " ");
        s.append(nrCliAtv + " ");
        s.append(endIP.getHostAddress().toString() + " ");
        return s.toString();
    }

    /* 
    Metodos setters
    */
    public void setIdBackEnd(int idBackEnd) {   this.idBackEnd = idBackEnd;    }
    public void setPorta(int porta) {   this.porta = porta;    }
    public void setNrSeq(int nrSeq) {   this.nrSeq = nrSeq;    }
    public void setTempoEnvio(int TempoEnvio) {  this.TempoEnvio = TempoEnvio;  }
    public void setNrCliAtv(int nrCliAtv) { this.nrCliAtv = nrCliAtv;    }
    public void setEndIP(InetAddress endIP) {     this.endIP = endIP;    }
    public void setTempoEnvio(double TempoEnvio) {  this.TempoEnvio = TempoEnvio;   }
    public void setDesignacao(String designacao) {  this.designacao = designacao;   }

    /*
    Metodos getters
    */    
    public int getIdBackEnd() { return this.idBackEnd;   }
    public int getPorta() { return this.porta;   }
    public int getNrSeq() { return this.nrSeq;   }
    public double getTempoEnvio() {    return this.TempoEnvio;  }
    public int getNrCliAtv() {  return this.nrCliAtv;    }
    public InetAddress getEndIP() { return this.endIP;   }   
    public String getDesignacao() { return this.designacao;  }   
    
}
