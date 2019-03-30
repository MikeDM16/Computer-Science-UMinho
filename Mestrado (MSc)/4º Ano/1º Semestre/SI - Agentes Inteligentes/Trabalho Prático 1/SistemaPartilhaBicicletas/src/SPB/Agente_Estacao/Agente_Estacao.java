package SPB.Agente_Estacao;

import SPB.Negociacao;
import SPB.Posicao;
import SPB.Tempo;
import SPB.Agente_Estacao.*;
import SPB.Agente_Utilizador.Utilizador;

import java.io.StringReader;
import java.util.*;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Agente_Estacao extends Agent{
	private Estacao e; 
	private Map<String, Estacao> vizinhanca; 
	
	public Agente_Estacao() {
		this.e = null;
		this.vizinhanca = new HashMap<String, Estacao>();
	}
	
	public void setup(){
		System.out.println(Tempo.get_time() + this.getLocalName() + " iniciou.");
		
		// Registo do agente, no directory facilitator,  como sendo do tipo estacao aluguer
		regista_Agente("Estacao");
		
		this.addBehaviour(new Recebe_Notificacoes(this));
		this.addBehaviour(new Informa_Estacoes(this, 5000));
	}
	
	
	/* ------------------------------------------------------------------------------------------------
	 * Classe responsável pelo envio de mensagens às outras estacao, no processo de intra comunicação
	 * entre estações  
	 * 
	 * -> Enivia msg para todas as estações. 
	 * -> Trabalho futuro: Enviar só para uma dada vizinhança
	 * */
	private class Informa_Estacoes extends TickerBehaviour{
		private Agente_Estacao a_estacao; 
		
		public Informa_Estacoes(Agent a, long timeout) {
			super(a, timeout);
			this.a_estacao = (Agente_Estacao) a; 
		}
		
		public void onTick() {
			//Procurar pelos agentes estação registados
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("Estacao");
			template.addServices(sd);
			
			DFAgentDescription[] disponiveis;
			try {
				disponiveis = DFService.search(this.a_estacao, template);
				// Comportamento paralelo para comunicar em parelelo com as diferentes estações
				ParallelBehaviour pb = new ParallelBehaviour(this.a_estacao, ParallelBehaviour.WHEN_ALL) {
					public int onEnd() {
						return 0;
					}
				};	
				this.a_estacao.addBehaviour(pb);
				
				for(int i = 0; i < disponiveis.length; i++) {
					pb.addSubBehaviour(new Envia_mensagem(this.a_estacao, disponiveis[i].getName()));
				}	
			}catch (FIPAException e) {	e.printStackTrace();	}
		}
		
		private class Envia_mensagem extends SimpleBehaviour{
			private Agente_Estacao a_estacao;
			private boolean finished = false; 
			private AID destino;
			
			public Envia_mensagem(Agente_Estacao a, AID destino) {
				this.a_estacao = a; 
				this.destino = destino;
			}
			
			public void action() {
				// condição para evitar que a estação envie msg de informacao para si mesma
				if(!this.destino.getLocalName().equals(this.a_estacao.getLocalName())) {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(this.destino);
					msg.setContent("informar " + this.a_estacao.e.toMsg());
					send(msg);		
					this.finished = true;
				}
			}
			
			public boolean done() {return this.finished; }
		}
		
	}// Fim da classe Informa_Estaoes 
	
	/* ------------------------------------------------------------------------------------------------
	 * Classe responsável pelo tratamentos das mensagens recebidas por parte da estacao */
	private class Recebe_Notificacoes extends CyclicBehaviour {
		private Agente_Estacao a_estacao;
		private Map<AID, Negociacao> negociacoes; 
		
		public Recebe_Notificacoes(Agente_Estacao agente_Estacao) {
			this.a_estacao = agente_Estacao;
			this.negociacoes = new HashMap<AID, Negociacao>();
		}
		
		public void action() {
			ACLMessage msg = receive();
			if(msg != null) {
				//System.out.println(this.a_estacao.getLocalName() + ": " + msg.getContent());
				int perf = msg.getPerformative();
				if(perf == ACLMessage.INFORM) {
					String[] tokens = msg.getContent().split(" ");
					
					// Mensagem da interface com os valores iniciais dos parametros da estação
					if(tokens[0].equals("iniciar")) {	iniciar_parametros_estacao(tokens);	}
					
					/* Mensagens de outras estações próximas que anunciam periodicamente 
					 * os seus dados de utilização */
					if(tokens[0].equals("informar")) {	atualiza_estacao(tokens, msg.getSender());	}
					
					/* Mensagens recebidas pelos clientes que realizam aluguer nesta estação */
					if(tokens[0].equals("cliente")) {
						/* Msg dos clientes que iniciam na estação e nela alugam uma bicicleta */
						if(tokens[1].equals("start")) {
							this.a_estacao.e.inc_Nr_Reservas();
							this.a_estacao.e.dec_Nr_Bicicletas();
						}
						/* Msg dos clientes que terminam na estação e entregam nela a bicicleta alugada */
						if(tokens[1].equals("end")) {
							this.a_estacao.e.inc_Nr_Entregas();
							this.a_estacao.e.inc_Nr_Bicicletas();
							int x = Integer.parseInt(tokens[3]);
							int y = Integer.parseInt(tokens[5]);
							Posicao po = new Posicao(x,y);
							x = Integer.parseInt(tokens[11]);
							y = Integer.parseInt(tokens[13]);
							Posicao pd = new Posicao(x,y);
							
							double dist_total = Math.round( (po.distancia(pd)) * 100.0) / 100.0;
							double dist_pagar = dist_total; 
							double desconto = Double.parseDouble(tokens[14]);
							
							dist_pagar = Math.round( (((100-desconto)/100) * dist_total) * 100.0) / 100.0;  
							if(desconto == 0) {
								System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": recebeu entrega do " + 
										msg.getSender().getLocalName() + " e cobrou " + dist_pagar + ".");
							}else {
								System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": recebeu entrega do " + 
										msg.getSender().getLocalName() + ". Preço " + dist_total + 
										",  com desconto de " + desconto + " Preço final :"+ dist_pagar + ".");
							}
							
							if(this.a_estacao.e.is_Sobrelotada()) {
								this.a_estacao.e.inc_Entregas_Excecionais();
								System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": deslocou excepcionalmente o equipamento entregue por " + 
										msg.getSender().getLocalName() + ".");
							}
							
							this.a_estacao.e.inc_Lucro(dist_pagar);
						}
						/* Msg dos clientes quando entram na área de proximidade da estacao */
						if(tokens[1].equals("enter")) {
							regista_negociacao( msg.getSender(), msg);
						}
					}					
				}
				
				if( (perf == ACLMessage.AGREE) ||(perf == ACLMessage.REJECT_PROPOSAL)
						|| (perf == ACLMessage.CFP)) {
					regista_negociacao( msg.getSender(), msg);
				}
				
				if(msg.getPerformative() == ACLMessage.REQUEST) {
					String[] valores = msg.getContent().split(" ");
					if(valores[0].equals("terminar")) {
						// Fim da simulação
						this.a_estacao.doDelete();
					}else {
						// Requestes enviados pela interface para solicitar as informações da estação
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent("informar " + this.a_estacao.e.toMsg());
						send(reply);
					}
				}
				
			}
		}
		
		private void regista_negociacao( AID user, ACLMessage msg) {
			//System.out.println(this.a_estacao.getLocalName() + " possivel negociacao");
			Estacao est = this.a_estacao.e;
			Posicao p_est = est.getLocalizacao();
			int perf = msg.getPerformative();
			// Se ainda nao foi feita nenhuma comunicação com o agente utilizador
			if ((!this.negociacoes.containsKey(user)) && (perf==ACLMessage.INFORM)) {
				inicia_negociacao(user, msg);
			}
			if((this.negociacoes.containsKey(user)) && (perf==ACLMessage.AGREE)) {
				this.a_estacao.e.inc_Propostas_Aceites();
				/* Adicionar a ultima mensagem recebida ao historico de msgs da negociação, e marcar a negociacao 
				 * como terminada */
				System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": " + user.getLocalName() + 
						" aceitou proposta com desconto de " + this.negociacoes.get(user).get_Desconto());
				this.negociacoes.get(user).add_Msg(msg.getContent());
				int desconto = Integer.parseInt(msg.getContent().split(" ")[4]);
				this.negociacoes.get(user).set_Desconto(desconto);
				this.negociacoes.get(user).set_Terminada(true);
			}
			if((this.negociacoes.containsKey(user)) && (perf==ACLMessage.REJECT_PROPOSAL)) {
				this.a_estacao.e.inc_Propostas_Rejeitadas();
				String last_propose = this.negociacoes.get(user).get_Last_Propose();
				String[] tokens = last_propose.split(" ");
				int x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]);
				System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": " + user.getLocalName() + " rejeitou proposta");
				

				/* Adicionar a ultima mensagem recebida ao historico de msgs da negociação, e marcar a negociacao 
				 * como terminada */
				this.negociacoes.get(user).add_Msg(msg.getContent());
				this.negociacoes.get(user).set_Terminada(true);
			}
			if((this.negociacoes.containsKey(user)) && (perf == ACLMessage.CFP)) {
				if(this.negociacoes.get(user).get_Nr_propostas_trocadas() > 5){
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
					reply.setContent("rejeita");
					this.negociacoes.get(user).set_Terminada(true);
					System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": desistiu de negociar com" + user.getLocalName());
					this.a_estacao.e.inc_Propostas_Rejeitadas();
					
				}
				System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + ": continuou negociacao com " + user.getLocalName());
				
				continua_negociacao(user, msg);
				this.negociacoes.get(user).inc_Nr_propostas_trocadas();
			}
			
		}
		
		private void continua_negociacao(AID user, ACLMessage msg) {
			String tokens[] = msg.getContent().split(" ");
			int desconto = Integer.parseInt(tokens[4]);

			Random rand = new Random();
			int c = rand.nextInt(100);
			ACLMessage reply = msg.createReply();
			if(c<15) { // Situacoes em que rejeita a proposta
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				reply.setContent("rejeita");
				this.a_estacao.e.inc_Propostas_Rejeitadas();
				this.negociacoes.get(user).set_Terminada(true);
			}
			if((c>=15)&&(c<40)) { // situações em que faz contra proposta
				desconto -= 5;
				tokens[4] = "" + desconto; 
				reply.setPerformative(ACLMessage.CFP);
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < tokens.length;) {
					sb.append(tokens[i++] + " ");
				}
				reply.setContent(sb.toString());
				this.negociacoes.get(user).inc_Nr_propostas_trocadas();
			}
			else { // situações em que aceita
				int x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]);
				reply.setPerformative(ACLMessage.AGREE);
				reply.setContent(msg.getContent());
				this.a_estacao.e.inc_Propostas_Aceites();
				this.negociacoes.get(user).set_Terminada(true);
			}
			send(reply);
			this.negociacoes.get(user).set_Desconto(desconto);
			this.negociacoes.get(user).add_Msg(msg.getContent());
			this.negociacoes.get(user).add_Msg(reply.getContent());
		}
		
		private void inicia_negociacao( AID user, ACLMessage msg) {
			Estacao est = this.a_estacao.e;
			Posicao p_est = est.getLocalizacao();
			ACLMessage prop = new ACLMessage(ACLMessage.PROPOSE);
			prop.addReceiver(user);
			int desconto = 0;
			
			Utilizador u = Utilizador.str_to_Utilizador(msg.getContent());
			Posicao p_user = u.getFim();
			Posicao p_ini = u.getInicio();
			Posicao p_fim = u.getFim();
			double dist1 = p_ini.distancia(p_fim); // distancia inicio -> fim
			double dist2 = p_ini.distancia(p_user); // distancia inicio -> pos atual
			
			// Se ainda nao percorreu 3/4 do percurso ou iniciou-se nesta mesma estação
			if( (dist2 < (3/4)*dist1) ||
				(p_ini.getX() == p_est.getX()) && (p_ini.getY() == p_est.getY())){ 
				return; 
			}
			
			if(est.is_Sobrelotada()) { // Caso a estação esteja sobrelotada
				/* Caso a estação destino do utilizador que entrou na área de proximidade seja 
				 * esta estação atualmente sobrelotada, vamos reencaminhá-lo para outra estacao */
				if( (p_user.getX() == p_est.getX()) && (p_user.getY() == p_est.getY())) {
					desconto = 30;
					Estacao new_destino = escolhe_Estacao(u.getInicio(), u.getAtual());
					if(new_destino == null) { return; }
					Posicao p = new_destino.getLocalizacao();						
					// conteudo msg: "propost px py desconto desc"
					prop.setContent("propose " + p.getX() + " " + p.getY() + " desconto " + desconto + " ");
					System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + " vai tentar deslocar o " + user.getLocalName() + " com desconto de " + desconto );
					send(prop);
					// lista l representa o histórico inicial de msg trocadas entre os agentes
					List<String> l = new ArrayList<String>();
					l.add(msg.getContent());
					l.add(prop.getContent());
					Negociacao n = new Negociacao(u, est, l, 1);
					n.set_Desconto(desconto);
					this.negociacoes.put(user, n);
				}					
			}
			
			// Caso a estação esteja com falta de equipamento
			if(this.a_estacao.e.is_Underlotada()) {
				desconto = 20;
				// conteudo msg: "propose px py desconto desc"
				prop.setContent("propose " + p_est.getX() + " " + p_est.getY() + " desconto " + desconto + " ");					
				System.out.println(Tempo.get_time() + this.a_estacao.getLocalName() + " vai tentar cativar o " + user.getLocalName() + " com desconto de " + desconto );
				send(prop);
				// lista l representa o histórico inicial de msg trocadas entre os agentes
				List<String> l = new ArrayList<String>();
				l.add(msg.getContent());
				l.add(prop.getContent());
				Negociacao n = new Negociacao(u, est, l, 0);
				n.set_Desconto(desconto);
				this.negociacoes.put(user, n);
			}
		}
		
		/* método utilizado para determinar, das estações conhecidas, qual a que é 
		 * mais proxima da sua posição atual*/
		private Estacao escolhe_Estacao(Posicao ini, Posicao p_user) {
			Estacao escolhida = null;
			double d_escolhido = 10000;
		
			Map<String, Estacao> estacoes = this.a_estacao.vizinhanca;
			for(Map.Entry<String, Estacao> entry : estacoes.entrySet()) {
				Estacao est = entry.getValue();
				Posicao p_est = est.getLocalizacao();
				
				/* Condicao para garantir que o utilizador nao é encaminhado para a estação 
				 * onde o mesmo começou */
				if((p_est.getX() != ini.getX()) || (p_est.getY() != ini.getY())) {
					if(est.distancia(p_user) < d_escolhido) {
						escolhida = est;
						d_escolhido = est.distancia(p_user);
					}
				}
				
			}
		
			return escolhida;
		}
		
		/* Método utilizado quando se recebe uma mensagem INFORM vinda de outras estação, e se
		 * atualiza as informacoes sobre essa estação na lista de estações conhecidas 	 */
		private void atualiza_estacao(String[] valores, AID agentID) {
			Map<String, Estacao> vizinhanca = this.a_estacao.vizinhanca;
			Estacao e = Estacao.str_to_Estacao(valores, agentID);
			if (e != null) {	
				if(vizinhanca.containsKey(valores[2])) {
					Estacao aux = vizinhanca.get(valores[2]);
					aux = e; 
				}else {
					vizinhanca.put(valores[2], e);
				}
			}		
		}
		
		/* Método utilizado para iniciar os parametros de uma estação com os valores indicados
		 * no conteudo de uma mensagem mandada pela interface_SPB */
		private void iniciar_parametros_estacao(String[] valores) {
			if (valores.length > 12) {
				int x = Integer.parseInt(valores[4]);
				int y = Integer.parseInt(valores[6]);
				Posicao p = new Posicao(x, y);
				
				int raio = Integer.parseInt(valores[8]);
				int bici_atual = Integer.parseInt(valores[10]);
				int cap = Integer.parseInt(valores[12]);
				String n = this.a_estacao.getLocalName();
				AID aid = this.a_estacao.getAID();
				this.a_estacao.e = new Estacao(n, aid, p, raio, bici_atual, cap);
			}		
		}
		
	} /* Fim da class RecebeNotificações - CyclicBehaviour para receber mensagens
	 ------------------------------------------------------------------------------------------------ */
	
	
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
