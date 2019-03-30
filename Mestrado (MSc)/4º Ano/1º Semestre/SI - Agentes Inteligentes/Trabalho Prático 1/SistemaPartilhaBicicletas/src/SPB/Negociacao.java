package SPB;

import SPB.Agente_Estacao.Estacao;
import SPB.Agente_Utilizador.Utilizador;
import java.util.ArrayList;
import java.util.List;
import jade.lang.acl.ACLMessage;

public class Negociacao {
	private Utilizador u;
	private Estacao est;
	private List<String> historico = new ArrayList<String>(); 
	private boolean terminada = false; 
	private int propostas_trocadas = 0;
	private int desconto, motivo; 

	public Negociacao(Utilizador u, Estacao est, List<String> l, int m) {
		this.u = u;
		this.est = est;
		this.desconto = 0; 
		this.historico = l;
		this.motivo = m;
	}
	
	public Negociacao(Utilizador u, Estacao est, List<String> l) {
		this.u = u;
		this.est = est;
		this.desconto = 0; 
		this.historico = l;
		this.motivo = (-1);
	}
	
	public void add_Msg(String msg) {
		this.historico.add(msg);
	}
	
	public void set_Terminada(boolean b) {
		this.terminada = b;
	}
	
	public int get_Motivo() {	return this.motivo; }
	public void set_Desconto(int d) { this.desconto  =d;}
	public int get_Desconto() {return this.desconto; }
	public void inc_Nr_propostas_trocadas() { this.propostas_trocadas++; }
	public int get_Nr_propostas_trocadas() { return this.propostas_trocadas; }
	public String get_Last_Propose() { return this.historico.get(this.historico.size()-1); }
}
