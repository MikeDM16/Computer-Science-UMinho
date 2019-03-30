package SPB;

import SPB.Agente_Estacao.*;
import SPB.Tempo;
import SPB.Agente_Utilizador.Agente_Utilizador.*;
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

import java.time.LocalDateTime;
import java.util.*;

public class Agente_Interface_SPB extends Agent {
	private List<Estacao> estacoes; 
	private int nr_estacoes; 
	
	public Agente_Interface_SPB() {		
		this.estacoes = new ArrayList<Estacao>();
	}
	
	public void setup() {
		System.out.println(this.getLocalName() + ": iniciado.");
		
		// Registo do agente, no directory facilitator,  como sendo do tipo interface
		regista_Agente("Interface");
		
		//Inicia os parametros iniciais das N estações criadas no mainContainer
		inicia_estacoes();
		
		this.addBehaviour(new Recebe_Informacoes(this) );
		this.addBehaviour( new Questiona_Estacoes(this, 5000) );
		
	}
	
	private class Recebe_Informacoes extends CyclicBehaviour{
		private Agente_Interface_SPB a_interface; 
		private List<Estacao> estacoes; 
		private int recebidas = 0; 
		
		public Recebe_Informacoes(Agente_Interface_SPB a) {
			this.a_interface = a;
			this.estacoes = this.a_interface.estacoes; 
		}
		
		public void action() {
			ACLMessage msg = receive();
			if(msg!=null) {
				String[] valores = msg.getContent().split(" ");
				if(msg.getPerformative() == ACLMessage.INFORM){
					
					Estacao e = Estacao.str_to_Estacao(valores, msg.getSender()); 
					if (e != null) {
						if( this.recebidas < this.a_interface.nr_estacoes ) {
							this.estacoes.add(recebidas, e);
							this.recebidas++;
						}
					}
					
					if(this.recebidas == this.a_interface.nr_estacoes) {
						imprimir(estacoes);
						this.recebidas = 0;
						this.estacoes.clear();
						this.a_interface.estacoes.clear();
					}
				}
				if(msg.getPerformative() == ACLMessage.REQUEST) {
					if(valores[0].equals("terminar"));
					this.a_interface.doDelete();
				}
			}
		}
		
		private void imprimir(List<Estacao> estacoes ) {
			int i = 0; 
			TableBuilder tb = new TableBuilder();
			
			tb.addRow("Nome","Px","Py","Raio","C atual","C max", "Reservas", "Entregas", "Oc ideal", 
								"Oc atual", "Prop A", "Prop R", "% Aceites", "Prejuizos","Lucro");
			tb.addRow("----","--","--","----","-------","-----", "--------", "--------", "--------", 
								"--------", "------", "------", "---------", "---------", "-----");
			for (Estacao e: estacoes) {
				String n = e.getNome().split("Agente_")[1];
				String x = "" + e.getLocalizacao().getX();
				String y = "" + e.getLocalizacao().getY();
				String r = "" + e.getArea_proximidade();
				String b = "" + e.getNr_bicicletas_atual();
				String c = "" + e.getCapacidade_maxima();
				String rs = "" + e.getNr_reservas();
				String et = "" + e.getNr_entregas();
				String oa = "" + e.getOcupacao_atual();
				String oi = "" + e.getOcupacao_ideal();
				String pa = "" + e.getProps_aceites();
				String pr = "" + e.getProps_rejects();
				String perc = "" + Math.round((e.getProps_aceites() / (e.getProps_aceites() + e.getProps_rejects()+ Math.pow(1,-10))) * 100.0) / 100.0;
				String l = "" + e.getLucro();
				String excep = "" + e.get_Entregas_Excecionais(); 
				tb.addRow(n, x, y, r, b, c, rs, et, oi, oa, pa, pr, perc, excep, l);
			}
			System.out.println(tb.toString() );
		}
	}
	
	/* ------------------------------------------------------------------------------------------------
	 * Classe Questiona_Estacoes vai simplesmente determinar quais os agentes do tipo estação, 
	 * a partir do DF, e soliciar a cada uma delas as suas informacoes de utilização mais recentes.  */
	private class Questiona_Estacoes extends TickerBehaviour{
		private Agente_Interface_SPB a_interface; 
		
		public Questiona_Estacoes(Agente_Interface_SPB a, long timeout) {
			super(a, timeout);
			this.a_interface = a;
		}
		
		public void onTick() {
			//Procurar pelas estações iniciadas
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("Estacao");
			template.addServices(sd);
			
			
			DFAgentDescription[] disponiveis;
			try {
				disponiveis = DFService.search(this.a_interface, template);
				// Comportamento paralelo para comunicar em parelelo com as diferentes estações
				ParallelBehaviour pb = new ParallelBehaviour(this.a_interface, ParallelBehaviour.WHEN_ALL) {
					public int onEnd() {
						return 0;
					}
				};
				this.a_interface.addBehaviour(pb);
				
				for(int i = 0; i < disponiveis.length; i++) {
					//pb.addSubBehaviour(new Envia_mensagem(this.a_interface, disponiveis[i].getName()));	
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(disponiveis[i].getName());
					msg.setContent("informar");
					send(msg);	
				}	
				
				int nr_estacoes = disponiveis.length;
				this.a_interface.nr_estacoes = nr_estacoes; 
				this.a_interface.estacoes = new ArrayList<Estacao>(nr_estacoes);
				
			}catch (FIPAException e) {	e.printStackTrace();	}
		}
		
		private class Envia_mensagem extends SimpleBehaviour{
			private Agente_Interface_SPB a_interface;
			private AID destino;
			private boolean finished = false;
			
			public Envia_mensagem(Agente_Interface_SPB a, AID destino) {
				this.a_interface = a;
				this.destino = destino;
			}
			
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(this.destino);
				msg.setContent("informar");
				send(msg);	
			}
			
			public boolean done() { 	return this.finished; }
		}
		
	}// Fim da classe Questiona_Estacoes
	
	/* ------------------------------------------------------------------------------------------------*/
	
	private void inicia_estacoes(){
		//Procurar pelas estações iniciadas
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd 		= new ServiceDescription();
		sd.setType("Estacao");
		template.addServices(sd);
		
		DFAgentDescription[] disponiveis;
		try {
			disponiveis = DFService.search(this, template);
			if(disponiveis.length == 0 ) {
				System.out.println(this.getLocalName() + ": Não foram inicializadas estacoes.");
			}
			for( int i=0; i< disponiveis.length; i++ ) {
				// Gerar dados iniciais de uma estação
				Estacao e = Estacao.gera_Estacao();
				
				/* Garantir que a localização da nova estação nao é demasiado próxima
				 * de uma das estações já conhecidas */
				while(Estacao.existe_vizinhanca(this.estacoes, e)) {
					e = Estacao.gera_Estacao();
				}
				
				e.setNome(disponiveis[i].getName().getLocalName());
				e.setAgentID(disponiveis[i].getName());
				this.estacoes.add(e);
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(disponiveis[i].getName());
				/* enviar uma mensagem inform, cujo conteudo inicia por "iniciar" para
				 * que a estação rereptora conheça que o conteudo da mensagem contem os
				 * seus parametors de inicialização */
				msg.setContent("iniciar " + e.toMsg());
				send(msg);				
			}
			
			this.estacoes.clear();
		}catch (FIPAException e) {	e.printStackTrace();	}
		
	}
	
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
	
	
	public static void main(String[] args) {
		MainContainer mc = new MainContainer();
		mc.initMainContainerInPlatform("localhost", "8888", "MainContainer");

		System.out.print(Tempo.get_time() + "Quantas estações de aluguer deseja iniciar: ");
		Scanner in = new Scanner(System.in);
		int nr_estacoes = in.nextInt();
		
		// Inicialização, dentro do MainContainer do agent, das N estações de aluguer
		for(int i = 0; i!= nr_estacoes; i++) {
			mc.startAgentInPlatform("Agente_Estacao_"+i, "SPB.Agente_Estacao.Agente_Estacao");
		}
		
		mc.startAgentInPlatform("Agente_Interface", "SPB.Agente_Interface_SPB");
		
		Random rand = new Random();
		try {
			Thread.sleep( rand.nextInt(2000) );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(Tempo.get_time() + "Quantos utilizadores deseja criar: ");
		int nr_utilizadores = in.nextInt();
		
		for(int i =0; i <= nr_utilizadores; i++) {
			mc.startAgentInPlatform("Agente_Utilizador_"+i++, "SPB.Agente_Utilizador.Agente_Utilizador");
			mc.startAgentInPlatform("Agente_Utilizador_"+i++, "SPB.Agente_Utilizador.Agente_Utilizador");
			mc.startAgentInPlatform("Agente_Utilizador_"+i++, "SPB.Agente_Utilizador.Agente_Utilizador");
			mc.startAgentInPlatform("Agente_Utilizador_"+i++, "SPB.Agente_Utilizador.Agente_Utilizador");
			mc.startAgentInPlatform("Agente_Utilizador_"+i++, "SPB.Agente_Utilizador.Agente_Utilizador");			
			mc.startAgentInPlatform("Agente_Utilizador_"+i, "SPB.Agente_Utilizador.Agente_Utilizador");
			try {
				Thread.sleep( rand.nextInt(2000) );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		

	}
		
}
