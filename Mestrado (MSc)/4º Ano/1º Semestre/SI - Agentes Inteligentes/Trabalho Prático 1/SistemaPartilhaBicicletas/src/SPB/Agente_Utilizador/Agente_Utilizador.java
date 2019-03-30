package SPB.Agente_Utilizador;


import java.util.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.df;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import SPB.*;
import SPB.Agente_Estacao.Agente_Estacao;
import SPB.Agente_Estacao.Estacao;

public class Agente_Utilizador extends Agent {
	private List<Estacao> estacoes; 
	private int nr_estacoes; 
	private Utilizador user; 
	private int desconto = 0;
	
	public Agente_Utilizador() {
		this.estacoes = new ArrayList<Estacao>();	
	}
	
	public void setup(){
		System.out.println(Tempo.get_time() + this.getLocalName() + " iniciou.");
		
		// Registo do agente, no directory facilitator,  como sendo do tipo utilizador
		regista_Agente("Utilizador");
		
		// Comportamento para receber mensagens
		this.addBehaviour(new Recebe_Notificacoes(this));
				
		// Descobrir as estações, para escolher aleatoriamente uma origem e destino
		descobre_estacoes();
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(Tempo.get_time() + this.getLocalName() + " terminou.");
	}
	
	/* Método utilizado para comunicar com todos os agentes estação registados no DF
	 * de forma a descobrir as suas informacoes */
	private void descobre_estacoes() {
		//Procurar pelas estações iniciadas
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd 		= new ServiceDescription();
		sd.setType("Estacao");
		template.addServices(sd);
		
		DFAgentDescription[] disponiveis;
		try {
			disponiveis = DFService.search(this, template);
			// Comportamento paralelo para comunicar em parelelo com as diferentes estações
			ParallelBehaviour pb = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL) {
				public int onEnd() {
					return 0;
				}
			};	
			this.addBehaviour(pb);
			for(int i = 0; i < disponiveis.length; i++) {
				pb.addSubBehaviour(new Envia_mensagem(this, disponiveis[i].getName()));	
			}				
			int nr_estacoes = disponiveis.length;
			this.nr_estacoes = nr_estacoes; 
			
			this.estacoes = new ArrayList<Estacao>(nr_estacoes);
		}catch (FIPAException e) {	e.printStackTrace();	}
	}
	
	private class Envia_mensagem extends SimpleBehaviour{
		private Agente_Utilizador a_user;
		private boolean finished = false; 
		private AID destino;
		
		public Envia_mensagem(Agente_Utilizador u, AID destino) {
			this.a_user = u; 
			this.destino = destino;
		}
		
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(this.destino);
			msg.setContent("informar ");
			send(msg);		
			this.finished = true;			
		}
		
		public boolean done() {return this.finished; }
	}// Fim da classe Envia_mensagem usada no parallel behaviour
	
	/*------------------------------------------------------------------------------------------------ 
	 * Classe Recebe_Notificações utilizada para fazer o tratamentos das mensagens recebidas
	 * por parte de um utilizador */
	private class Recebe_Notificacoes extends CyclicBehaviour{
		private Agente_Utilizador a_user; 
		private List<Estacao> estacoes; 
		private int recebidas = 0; 
		private Map<AID, Negociacao> negociacoes;
		
		public Recebe_Notificacoes(Agente_Utilizador a) {
			this.a_user = a;
			this.estacoes = a.estacoes;
			this.negociacoes = new HashMap<AID, Negociacao>();
		}
		
		public void action() {
			ACLMessage msg = receive();
			if(msg!=null) {
				//System.out.println(this.a_user.getLocalName() + "Recebeu msg "+ msg.getContent());
				int perf = msg.getPerformative();
				
				/* Com o método descobre_estacoes, envia-se uma mensagem a cada um das estacoes 
				 * registadas no DF. Cada estação vai responder a essas mensagems com as suas 
				 * informaçoes, dando-se a conhecer ao utilizador. 
				 * Isto só é feito 1x, quando se arranca com o utilizador */
				if( perf == ACLMessage.INFORM) {
					String[] valores = msg.getContent().split(" ");					
					Estacao e = Estacao.str_to_Estacao(valores, msg.getSender()); 
					if (e != null) {
						if( this.recebidas < this.a_user.nr_estacoes ) {
							this.estacoes.add(recebidas, e);
							this.recebidas++;
						}
					}
					/* Depois de recebidas todas as respostas por parte dos agentes estacao, é possivel
					 * determinar uma localização inicial e final através das estações que se deram a conhecer */
					if(this.recebidas == this.a_user.nr_estacoes) {
						this.a_user.estacoes = this.estacoes;
						this.a_user.addBehaviour(new Inicia_Utilizador(this.a_user));
					}
				}
				
				/* O utilizador pode receber propostas das estações 
				 * O utilizador, caso faca contra propostas, pode receber: 
				 * agrees, CFP e rejects proposals */
				if( (perf == ACLMessage.PROPOSE) || (perf == ACLMessage.AGREE) || 
						(perf == ACLMessage.CFP) || (perf == ACLMessage.REJECT_PROPOSAL)){
					regista_negociacao(msg.getSender(), msg);
				}
				

				if(msg.getPerformative() == ACLMessage.REQUEST) {
					String[] valores = msg.getContent().split(" ");
					if(valores[0].equals("terminar")) {
						// Fim da simulação
						this.a_user.doDelete();
					}
				}
			}
		}
		
		private void regista_negociacao(AID estacao, ACLMessage msg) {
			Utilizador user = this.a_user.user;
			Posicao destino = user.getFim();
			int perf = msg.getPerformative();
			
			if((!this.negociacoes.containsKey(estacao)) && (perf == ACLMessage.PROPOSE)) {
				inicia_negociacao(estacao, msg);
			}
			
			// Agree, Reject e CFP são respostas a proposal e CFP já trocadas
			if((this.negociacoes.containsKey(estacao)) && (perf == ACLMessage.AGREE)) {
				String tokens[] = msg.getContent().split(" ");
				int desconto = Integer.parseInt(tokens[4]);
				int x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]);
				destino.setX(x); destino.setY(y);
				Posicao new_dest = new Posicao(x,y);
				this.a_user.user.setFim(new_dest);
				this.a_user.user.getDestino().setLocalizacao(new_dest);
				this.a_user.user.getDestino().setAgentID(estacao);
				
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + ": " + estacao.getLocalName() + " aceitou CFP, com desconto de " + desconto + ".");
				this.negociacoes.get(estacao).add_Msg(msg.getContent());
				this.negociacoes.get(estacao).set_Desconto(desconto);
				this.negociacoes.get(estacao).set_Terminada(true);
				this.a_user.desconto = desconto;
			}
			if((this.negociacoes.containsKey(estacao)) && (perf == ACLMessage.REJECT_PROPOSAL)) {
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + ": " + estacao.getLocalName() + " rejeitou CFP, com desconto de" + desconto + ".");
				this.negociacoes.get(estacao).add_Msg(msg.getContent());
				this.negociacoes.get(estacao).set_Desconto(0);
				this.negociacoes.get(estacao).set_Terminada(true);
				this.a_user.desconto = 0;
			}
			if((this.negociacoes.containsKey(estacao)) && (perf == ACLMessage.CFP)) {
				if(this.negociacoes.get(estacao).get_Nr_propostas_trocadas() > 5){
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
					reply.setContent("rejeita");
					this.negociacoes.get(estacao).set_Terminada(true);
					this.negociacoes.get(estacao).set_Desconto(0);
					this.a_user.desconto = 0;
				}else {
					inicia_negociacao(estacao, msg);
					this.negociacoes.get(estacao).inc_Nr_propostas_trocadas();
				}
			}
		}
		
		private void inicia_negociacao(AID estacao, ACLMessage msg) {
			String tokens[] = msg.getContent().split(" ");
			int desconto = Integer.parseInt(tokens[4]);

			List<String> l = new ArrayList<String>();
			Estacao est = new Estacao(estacao);
			Negociacao n = new Negociacao(this.a_user.user, est, l);
			Random rand = new Random();
			int c = rand.nextInt(100);
			ACLMessage reply = msg.createReply();
			if(c<15) { // Situacoes em que rejeita a proposta
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				reply.setContent("rejeita");
				n.set_Terminada(true);
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + ": rejeita negociação com" + estacao.getLocalName() + ", com desconto de " + desconto);
				desconto = 0;
			}
			if((c>=15)&&(c<40)) { // situações em que faz contra proposta
				desconto += 10;
				tokens[4] = "" + desconto; 
				reply.setPerformative(ACLMessage.CFP);
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < tokens.length;) {
					sb.append(tokens[i++] + " ");
				}
				reply.setContent(sb.toString());
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + ": realiza CFP À " + estacao.getLocalName() + ", com desconto de " + desconto);
				
				
			}
			if(c>=40){ // situações em que aceita
				int x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]);
				Posicao new_dest = new Posicao(x,y);
				Posicao destino = this.a_user.user.getFim(); 
				this.a_user.user.getDestino().setLocalizacao(new_dest);
				this.a_user.user.getDestino().setAgentID(msg.getSender());
				
				//destino.setX(x); destino.setY(y);
				//this.a_user.user.getDestino().setLocalizacao(destino);
				//this.a_user.user.getDestino().setAgentID(estacao);
				
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + ": aceitou  " + estacao.getLocalName() + " com desconto de " + desconto);
				destino.setX(x); destino.setY(y);
				reply.setPerformative(ACLMessage.AGREE);
				reply.setContent(msg.getContent());
				n.set_Terminada(true);
			
			}
			send(reply);
			n.add_Msg(msg.toString());
			n.add_Msg(reply.toString());
			n.inc_Nr_propostas_trocadas();
			n.set_Desconto(desconto);
			if(!this.negociacoes.containsKey(estacao)) {
				this.negociacoes.put(estacao, n);
			}else {
				this.negociacoes.get(estacao).add_Msg(msg.getContent());
				this.negociacoes.get(estacao).add_Msg(reply.getContent());
				this.negociacoes.get(estacao).inc_Nr_propostas_trocadas();
				this.negociacoes.get(estacao).set_Desconto(desconto);
			}
			this.a_user.desconto = desconto;
			
		}
		
	} // fim da classe Recebe notificações 
	
	private class Update_Deslocacao extends TickerBehaviour{
		private Agente_Utilizador a_user;
		
		public Update_Deslocacao(Agente_Utilizador a, long timout) {
			super(a, timout);
			this.a_user = a; 
		}
		
		public void onTick() {
			Posicao atual = this.a_user.user.getAtual();
			Posicao origem = this.a_user.user.getInicio();
			Posicao destino = this.a_user.user.getFim();
			
			if( (atual.getX() != destino.getX()) || (atual.getY() != destino.getY())) {
				atual.deslocar(destino);
				this.a_user.user.setAtual(atual);
			}
			if( (atual.getX() == destino.getX()) && (atual.getY() == destino.getY())) {
				Estacao e = this.a_user.user.getDestino();
				// Notificar a estação final que o cliente chegou ao destino
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(e.getAgentID());
				msg.setContent("cliente end " + this.a_user.user.toMsg() + " " + this.a_user.desconto);
				send(msg);
				System.out.println(Tempo.get_time() + this.a_user.getLocalName() + " chegou ao destino");
				this.a_user.doDelete();
				}
		}		
		
	}
	
	private class Notifica_Estacoes extends TickerBehaviour{
		private Agente_Utilizador a_user;
		private List<Estacao> comunicadas;
		
		public Notifica_Estacoes(Agente_Utilizador a, long timeout) {
			super(a, timeout);
			this.a_user = a;
			this.comunicadas = new ArrayList<Estacao>();
		}
		
		public void onTick() {
			List<Estacao> estacoes = this.a_user.estacoes;
			Posicao pos = this.a_user.user.getAtual();
			
			for(Estacao est : estacoes) {
				if((est.entrou_area_proximidade(pos)) && (!comunicadas.contains(est))) {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(est.getAgentID());
					msg.setContent("cliente enter " + this.a_user.user.toMsg());
					send(msg);
					this.comunicadas.add(est);
				}
			}
		}
	}
	
	/*------------------------------------------------------------------------------------------------
	 * Classe inicia_utilizador representa o comportamento chamado quando já se conhecem as estações 
	 * registadas no DF e se vão escolher aleatoriamente duas para seres a origem e destino do 
	 * utilizador 	 */
	private class Inicia_Utilizador extends SimpleBehaviour{
		private boolean finished = false; 
		private List<Estacao> estacoes; 
		private Agente_Utilizador a_utilizador; 
		
		public Inicia_Utilizador(Agente_Utilizador a) {
			this.estacoes = a.estacoes; 
			this.a_utilizador = a; 
		}
		
		public void action() {
			if(this.a_utilizador.nr_estacoes < 2) {
				System.out.println(Tempo.get_time() + this.a_utilizador.getLocalName() + ": não existe um número minimo de duas estação ativas.");
				this.a_utilizador.doDelete();
				return;
			}
			Random rand = new Random();
			// Escolher um indice para selecionar uma estacao origem, com bicicletas disponiveis
			int o = rand.nextInt(estacoes.size());
			if(estacoes.get(o).getNr_bicicletas_atual() <= 0 ) {
				System.out.println(Tempo.get_time() + this.a_utilizador.getLocalName() + ": selecionou a  " + 
						estacoes.get(o).getNome() + " que nao tem stock disponivel!");
				this.a_utilizador.doDelete();
				return;
			}
			// Escolher indice para selecionar estacao destino =/= origem
			int d = o;
			while(d == o) {
				d = rand.nextInt(estacoes.size());
			}
			
			Estacao origem = this.estacoes.get(o);
			Estacao destino = this.estacoes.get(d);
			
			this.a_utilizador.user = new Utilizador(origem, origem, destino);
			// Notificar a estação inicial que o cliente fez uma reserva na mesma
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(origem.getAgentID());
			msg.setContent("cliente start");
			send(msg);		
			System.out.println(Tempo.get_time() + this.a_utilizador.getLocalName() + ": destino " + this.a_utilizador.user.getDestino().getNome() + ".");
			this.a_utilizador.addBehaviour(new Update_Deslocacao(this.a_utilizador, 500));
			this.a_utilizador.addBehaviour(new Notifica_Estacoes(this.a_utilizador, 2000));
			this.finished = true; 
		}
		
		public boolean done() { return this.finished;  }
	}// fim da classe Inicia_Utilizador
	
	/*------------------------------------------------------------------------------------------------*/
	
	/* Função utilizada para registar no directory facilitator um agente, associando-o 
	 * a uma determinada funçao */
	private void regista_Agente(String tipo) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd  = new ServiceDescription();
		sd.setType( tipo );
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
	}
}
