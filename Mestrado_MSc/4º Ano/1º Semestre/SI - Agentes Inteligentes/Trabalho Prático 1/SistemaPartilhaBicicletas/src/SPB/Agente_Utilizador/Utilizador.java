package SPB.Agente_Utilizador;

import SPB.Agente_Estacao.Estacao;
import SPB.Posicao;

public class Utilizador {
	private Estacao origem, destino;
	private Posicao inicio, atual, fim; 
	
	public Utilizador(Estacao origem, Estacao atual, Estacao destino) {
		this.origem = origem;
		this.destino = destino;
		
		this.inicio = new Posicao( origem.getLocalizacao().getX(), origem.getLocalizacao().getY());
		this.atual  = new Posicao( origem.getLocalizacao().getX(), origem.getLocalizacao().getY());;
		this.fim	= new Posicao( destino.getLocalizacao().getX(), destino.getLocalizacao().getY());
		
	}
	
	public Utilizador( Posicao o, Posicao a, Posicao d) {
		this.inicio = o; this.fim = d; this.atual = a; 
	}
	
	public String toMsg() {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(this != null) {
			sb.append("Ox " + this.getInicio().getX() + " ");
			sb.append("Oy " + this.getInicio().getY() + " ");
			sb.append("Ax " + this.getAtual().getX() + " ");
			sb.append("Ay " + this.getAtual().getY() + " ");
			sb.append("Dx " + this.getFim().getX() + " ");
			sb.append("Dy " + this.getFim().getY() );
		}
		return sb.toString();
	}
	
	public static Utilizador str_to_Utilizador(String msg) {
		String[] valores = msg.split(" ");
		Utilizador u = null;
		if(valores.length >= 13) {
			int x = Integer.parseInt(valores[3]);
			int y = Integer.parseInt(valores[5]);
			Posicao origem = new Posicao(x, y);
			
			x = Integer.parseInt(valores[7]);
			y = Integer.parseInt(valores[9]);
			Posicao atual = new Posicao(x, y);
			
			x = Integer.parseInt(valores[11]);
			y = Integer.parseInt(valores[13]);
			Posicao destino = new Posicao(x, y);
			
			u = new Utilizador(origem, atual, destino);		
		}
		
		return u;
		
	}
	
	/* Métodos getters e setters necesários ao contexto */
	public Estacao getOrigem() {	return origem;	}
	public Estacao getDestino() {	return destino;	}
	public Posicao getInicio() {	return inicio;	}
	public Posicao getAtual() {	return atual;		}
	public Posicao getFim() {	return fim;	}
	
	public void setOrigem(Estacao origem) {		this.origem = origem;	}
	public void setDestino(Estacao destino) {		this.destino = destino;	}
	public void setInicio(Posicao inicio) {		this.inicio = inicio;	}
	public void setAtual(Posicao atual) {		this.atual = atual;	}
	public void setFim(Posicao fim) {		this.fim = fim;	}

	
}
