 

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Classe Utilizador
 *
 * @author Octavio Maia
 * @version 1.0
 */
public class Utilizador {

    //variaveis de instancia
    private String email;
    private String pw;
    private boolean condutor;
    private boolean activ;
    private boolean ocupado;
    private Veiculo v;
    private Local loc;
    private Local dest;
    private double custoUnitario;
    private Utilizador par;
    private BufferedReader in;
	private BufferedWriter out;
	private boolean autent;
    
    
    public Utilizador(String mail,String pw) {
        this.email = mail;
        this.pw = pw;
        this.condutor=false;
        this.activ=false;
        this.ocupado=false;
        this.v= null;
        this.loc = null;
        this.dest= null;
        this.custoUnitario = -1;
        this.par=null;
        this.in=null;
        this.out=null;
        this.autent=false;
    }
    
    public synchronized void logout(){
        this.condutor=false;
        this.activ=false;
        this.v= null;
        this.loc = null;
        this.dest= null;
        this.par=null;;
        this.ocupado=false;
        this.autent=true;
    }
    
    public synchronized void exit(){
        this.condutor=false;
        this.activ=false;
        this.v= null;
        this.loc = null;
        this.dest= null;
        this.par=null;
        this.in=null;
        this.out=null;
        this.ocupado=false;
        this.autent=false;
    }
    public synchronized boolean autenticar(String pw){
    	boolean ret =false;
    	if(this.pw.equals(pw)){
    		this.autent=true;
    		ret = true;
    	}
    	return ret;
    }

    public synchronized boolean isAutent(){
    	return this.autent;
    }
    
	public synchronized void setPw(String pw) {
		this.pw = pw;
	}

	public synchronized Veiculo getV() {
		return v;
	}

	public synchronized void setV(Veiculo v) {
		this.v = v;
	}

	public synchronized void setCondutor(boolean condutor) {
		this.condutor = condutor;
	}

    public synchronized boolean login(String pw, boolean condutor, Veiculo v,Local l, BufferedReader in, BufferedWriter out,Local dest){
    	boolean ret = false;
    	if(this.pw.equals(pw)){
    		ret = true;
    		this.condutor=condutor;
            this.activ=true;
            this.ocupado=false;
            this.v= v;
            this.loc =l;
            this.par=null;
            this.in=in;
            this.custoUnitario = -1;
            this.out=out;
            this.dest=dest;
            this.autent=true;

    	}
    	return ret;
    }
    
    public synchronized String getPw(){
    	return pw;
    }

	public synchronized boolean isCondutor() {
		return condutor;
	}

	public synchronized boolean isActiv() {
		return activ;
	}

	public synchronized void setActiv(boolean activ) {
		this.activ = activ;
	}

	public synchronized boolean isOcupado() {
		return ocupado;
	}

	public synchronized void setOcupado(boolean ocupado) {
		if(this.ocupado==false){
			this.ocupado = ocupado;
			if(this.getPar()!=null){
				this.getPar().setOcupado(ocupado);
			}
		}
	}
	
	public synchronized String getEmail(){
		return email;
	}

	public synchronized void setEmail(String email){
		this.email=email;
	}
	
	public synchronized Veiculo getVeiculo() {
		return v;
	}

	public synchronized void setVeiculo(Veiculo v) {
		this.v = v;
	}

	public synchronized Local getLoc() {
		return loc;
	}

	public synchronized void setLoc(Local loc) {
		this.loc = loc;
	}

	@Override
	public String toString() {
		return "Utilizador [email=" + email + ", pw=" + pw + ", condutor=" + condutor + ", activ=" + activ
				+ ", ocupado=" + ocupado + ", v=" + v + ", loc=" + loc + ", dest=" + dest + ", custoUnitario="
				+ custoUnitario + ", par=" + par + ", in=" + in + ", out=" + out + ", autent=" + autent + "]";
	}

	public synchronized Local getDest() {
		return dest;
	}

	public synchronized void setDest(Local dest) {
		this.dest = dest;
	}

	public synchronized double getCustoUnitario() {
		return custoUnitario;
	}

	public synchronized void setCustoUnitario(double custoUnitario) {
		this.custoUnitario = custoUnitario;
	}

	public synchronized Utilizador getPar() {
		return par;
	}
 
	public synchronized void setPar(Utilizador par) {
		if(this.par==null){
			this.par = par;
			par.setPar(this);
		}
	}

	public synchronized BufferedReader getIn() {
		return in;
	}

	public synchronized void setIn(BufferedReader in) {
		this.in = in;
	}

	public synchronized BufferedWriter getOut() {
		return out;
	}

	public synchronized void setOut(BufferedWriter out) {
		this.out = out;
	}
    
	@Override
	public synchronized int hashCode() {
		return this.email.hashCode();
	}
}
