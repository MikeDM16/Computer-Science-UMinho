package SPB.Agente_Estacao;

import java.util.*;
import jade.core.AID;
import java.lang.Math;
import java.text.DecimalFormat;

import SPB.Posicao;

public class Estacao {
	private Posicao localizacao;
	private int area_proximidade, nr_bicicletas_atual, capacidade_maxima;
	private double ocupacao_ideal, lucro;
	private String nome;
	private AID agentID;
	private int nr_reservas = 0, nr_entregas = 0; 
	private int props_aceites = 0, props_rejects = 0, entregas_excecionais = 0;
	
	// Construtor da Classe
	public Estacao(String nome, AID agentID, Posicao pos, int raio, int bici_atual, int capacidade_max, 
									int reservas, int entregas, int pa, int pr, double lucro, int excep) {
		this.nome = nome;									this.agentID = agentID;
		this.localizacao = pos;								this.area_proximidade = raio;
		this.nr_bicicletas_atual = bici_atual;				this.capacidade_maxima = capacidade_max;
		this.nr_reservas = reservas;						this.nr_entregas = entregas;
		this.props_aceites = pa; 							this.props_rejects = pr;
		Random rand = new Random();
		int max = 85, min = 75;
		int oc = rand.nextInt(max - min) + min;
		this.ocupacao_ideal = (oc / 100.0) ;				this.lucro = lucro;
		this.entregas_excecionais = excep;
	}
	public Estacao(String nome, AID agentID, Posicao pos, int raio, int bici_atual, int capacidade_max) {
		this.nome = nome;									this.agentID = agentID;
		this.localizacao = pos;								this.area_proximidade = raio;
		this.nr_bicicletas_atual = bici_atual;				this.capacidade_maxima = capacidade_max;
		Random rand = new Random();
		int max = 85, min = 75;
		int oc = rand.nextInt(max - min) + min;
		this.ocupacao_ideal = (oc / 100.0) ;				this.lucro = 0.0;
	}
	
	public Estacao(AID agent) {
		this.agentID = agent;
	}

	
	/* Método utilizado para gerar os parametros de uma estação aleatoriamente
	 * dentro do contexto do sistema a desenvolver */
	public static Estacao gera_Estacao() {
		Random rand = new Random();
		
		// Determinar o raio de proximidade da estação
		int r_min = 15, r_max = 35;
		int raio = rand.nextInt( r_max + 1 - r_min) + r_min;
		
		// Determinar uma localização para a estação 
		int x = rand.nextInt(100); 
		int y = rand.nextInt(100);
		Posicao pos = new Posicao(x, y); 
		
		/* Um estação terá pelo menos 15 bicicletas para alugar
		 * Pode variar de 5 a 5, até a um máximo de 35 */
		int n = rand.nextInt(5);
		int bici_atual = 15 + (n * 5);
	
		// A capacidade máxima será de pelo menos 10% superior à capacidade inicial
		int c_min = 10, c_max = 30;
		n = rand.nextInt(c_max + 1 - c_min) + c_min;
		int capacidade_max = bici_atual + ((int) ((n * bici_atual) / 100) );
		 
		return ( new Estacao("", null, pos, raio, bici_atual, capacidade_max, 0,0,0,0, 0.0, 0) );
	}

	/* Metodo que determina se para cada elemento da lista de estações do 1º arg, alguma
	 * delas é vizinha da estação passada no 2º argumento*/
	public static boolean existe_vizinhanca(List<Estacao> estacoes, Estacao e) {
		boolean cond = false;
		
		for (Estacao est : estacoes) {
			int x1 = est.getLocalizacao().getX(), y1 = est.getLocalizacao().getY();
			int r1 =  est.getArea_proximidade();
			
			int x2 = e.getLocalizacao().getX(), y2 = e.getLocalizacao().getY();
			
			/* Se a localização de uma das estações estiver contida na primeira metade da área de
			 * proximidade da outra estação, então as estações estão demasiado proximas para coixistirem*/
			cond = (Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2)) <= (r1/2));

			if (cond) {	break; }
		}
		
		return cond;
	}
	
	public boolean entrou_area_proximidade(Posicao p) {
		int x1 = this.localizacao.getX(), y1 = this.localizacao.getY();
		int r = this.area_proximidade;
		
		int x2 = p.getX(), y2 = p.getY();
		
		return (Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2)) <= (r));
	}
	
	/* Método utilizado para retirar os valores existentes numa string.
	 * Após parti-la, retira dela os valores dos atributos de uma estação */
	public static Estacao str_to_Estacao(String[] valores, AID agentID) {
		Estacao e = null;
		if (valores.length >= 24) {
			String nome = valores[2];
			int x = Integer.parseInt(valores[4]);
			int y = Integer.parseInt(valores[6]);
			Posicao p = new Posicao(x, y);
			
			// raio, nr bicicletas atual e capacidade maxima
			int raio = Integer.parseInt(valores[8]);
			int bici_atual = Integer.parseInt(valores[10]);
			int cap = Integer.parseInt(valores[12]);
			
			// nr de reservas e entregas na estação
			int r = Integer.parseInt(valores[14]);
			int et =  Integer.parseInt(valores[16]);
			
			// nr propostas aceites e rejeitadas
			int pa = Integer.parseInt(valores[18]);
			int pr = Integer.parseInt(valores[20]);
			
			double l = Double.parseDouble(valores[22]);
			int excep =  Integer.parseInt(valores[24]);
			e = new Estacao(nome, agentID, p, raio, bici_atual, cap, r, et, pa, pr, l, excep);
		}
		return e; 
	}

	/* Método utilizado para passar os campos de uma estação para uma string, de forma a enviar
	 * estes dados no conteudo de uma mensagem ACL. */
	public String toMsg() {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		
		if(this != null) {
			sb.append("Nome " + this.nome + " ");
			sb.append("posX " + this.localizacao.getX() + " ");
			sb.append("posY " + this.localizacao.getY() + " ");
			sb.append("area " + this.area_proximidade + " ");
			sb.append("bici_atual " + this.nr_bicicletas_atual + " ");
			sb.append("capacidade " + this.capacidade_maxima + " ");
			sb.append("nr_Reservas " + this.nr_reservas + " ");
			sb.append("nr_entregas " + this.nr_entregas + " ");
			sb.append("aceites " + this.props_aceites + " ");
			sb.append("rejeitadas " + this.props_rejects + " ");
			sb.append("lucro " + this.lucro + " ");
			sb.append("entregas_excecionais " + this.entregas_excecionais + " ");
		}
		return sb.toString();
	}
	
	public double distancia(Posicao p) {
		double dist = 100000;
		if(this != null) {
			int x1 = p.getX(), y1 = p.getY();
			int x2 = this.localizacao.getX(), y2 = this.localizacao.getY();
			
			dist = Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2) );
		}
		
		return dist;
	}
	
	public void inc_Lucro(double dist) {
		this.lucro = Math.round( (this.lucro + dist) * 100.0) / 100.0;
	}
	public boolean is_Sobrelotada() {
		return (this.getOcupacao_atual() >= 1.0);
	}
	public boolean is_Underlotada() {
		return (this.getOcupacao_atual() < this.getOcupacao_ideal());
	}
	
	public void inc_Propostas_Aceites() {
		this.props_aceites++;
	}
	public void inc_Propostas_Rejeitadas() {
		this.props_rejects++;
	}
	
	/*Métodos getters e setters necessários ao contexto*/
	public void inc_Nr_Reservas() { this.nr_reservas++; }
	public void inc_Nr_Entregas() { this.nr_entregas++; }
	public void inc_Nr_Bicicletas() { 
		this.nr_bicicletas_atual ++;
		//if(this.nr_bicicletas_atual < 0) this.nr_bicicletas_atual = 0; 
	}
	public void dec_Nr_Bicicletas() { this.nr_bicicletas_atual--; }
	public void setNr_entregas(int e) {
		this.nr_entregas = e;
	}
	public void inc_Entregas_Excecionais(){	
		this.entregas_excecionais++;
		//this.nr_bicicletas_atual--;
	}
	public int get_Entregas_Excecionais() {	return this.entregas_excecionais; }
	public void setLocalizacao(Posicao localizacao) {		
		this.localizacao = localizacao;	
	}
	public void setNr_reservas(int nr_reservas) {
		this.nr_reservas = nr_reservas;
	}
	public void setArea_proximidade(int area_proximidade) {	
		this.area_proximidade = area_proximidade;	
	}
	public void setNr_bicicletas_atual(int nr_bicicletas_atual) {
		this.nr_bicicletas_atual = nr_bicicletas_atual;
	}
	public void setCapacidade_maxima(int capacidade_maxima) {
		this.capacidade_maxima = capacidade_maxima;
	}
	public void setOcupacao_ideal(double ocupacao_ideal) {
		this.ocupacao_ideal = ocupacao_ideal;
	}
	public void setLucro(double lucro) {	this.lucro = lucro;		}
	public void setNome(String nome)   {	this.nome = nome;		}
	public void setAgentID(AID agentID){	this.agentID = agentID;	}
	
	public double getOcupacao_atual() { 
		double oc = ((double) (this.nr_bicicletas_atual)) / 
									((double) this.capacidade_maxima);
		double roundOff = Math.round(oc * 100.0) / 100.0;
		return (roundOff);
	}
	public void setProps_aceites(int props_aceites) {
		this.props_aceites = props_aceites;
	}
	public void setProps_rejects(int props_rejects) {
		this.props_rejects = props_rejects;
	}


	public int getNr_entregas() { 	return nr_entregas; }
	public int getNr_reservas() {		return nr_reservas;	}
	public Posicao getLocalizacao()  {	return localizacao;			}
	public int getArea_proximidade() {	return area_proximidade;	}
	public int getNr_bicicletas_atual() {	return nr_bicicletas_atual;	}
	public int getCapacidade_maxima() {	return capacidade_maxima;	}
	public double getOcupacao_ideal() {	return this.ocupacao_ideal; }
	public double getLucro() {	return lucro;	}
	public String getNome()  {	return nome;	}
	public AID getAgentID()  {	return agentID;	}
	public int getProps_aceites() {		return props_aceites;	}
	public int getProps_rejects() {		return props_rejects;	}
	
	
	
}
