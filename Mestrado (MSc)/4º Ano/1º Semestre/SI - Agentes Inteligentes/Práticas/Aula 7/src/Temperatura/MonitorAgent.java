package Temperatura;

import java.util.Random;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jess.*;
public class MonitorAgent extends Agent{
	private Rete jess_engine;
	
	public void setup() {
		//Registo do agente como sendo do tipo Monitor de sensores
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("MonitorAgent");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
		System.out.println("Monitor: " + this.getLocalName() + " começou.");
		
		this.jess_engine = new Rete();
		try {
			/* Ecxecutar o ficheiro com as regras, funções, templates e factos iniciais */
			this.jess_engine.batch("Temperatura/confs.clp");
			this.jess_engine.reset();
			
			// O Agente regista-se como um facto no ambiente de execução jess
			this.jess_engine.store("AGENT",this);
			this.jess_engine.executeCommand("(assert (Agent (agent (fetch AGENT))))");
			this.jess_engine.run();
			
			//Visualizar as regras e factos iniciais do sistema jess
			this.jess_engine.executeCommand("(facts) (rules)");
			this.jess_engine.run();
			
		} catch (JessException e) {	e.printStackTrace(); }
		
		this.addBehaviour(new ReceberNotificacoes(this));
		this.addBehaviour(new Notificacao(this, 15000));
	}
	
	// Métodos getters e Setters necessários no contexto
	public Rete getJessEngine() {	return this.jess_engine; }
	
	
	/*
	 * Behaviour utilizado para receber as mensagens vindas dos sensosres dos
	 *departamentos. As msg são registas na integra no sistema jess e é dentro dele
	 *que o seu conteudo é processa e são tomadas decisões baseadas nos factos
	 */
	private class ReceberNotificacoes extends CyclicBehaviour{
		private MonitorAgent monitor;
		private int count = 0;
		public ReceberNotificacoes(Agent a) {
			this.monitor = (MonitorAgent) a;
		}
		
		public void action() {
			ACLMessage msg = receive();
			Rete jess_engine = this.monitor.getJessEngine();
			
			if(msg!=null) {String[] tokens = msg.getContent().split(" ");
				try {
				/*	Possivelmente util para o futuro!
				  	float temp = Float.parseFloat( tokens[1] );
				 	String name = msg.getSender().getLocalName().toString();
				  	Fact f = new Fact("sensor", jess_engine);
					f.setSlotValue("id_name", new Value(name, RU.STRING));
					f.setSlotValue("temperature", new Value(temp, RU.FLOAT));
					f.setSlotValue("time", new Value(this.count++, RU.INTEGER));
					jess_engine.assertFact(f);
				*/
					jess_engine.store("MESSAGE", msg);
					jess_engine.executeCommand("(assert (Message (fetch MESSAGE)))");
					jess_engine.run();
				}catch (JessException e) {	e.printStackTrace();	}
			}			
		}		
	} // Fim da classe ReceberNotificacoes
	
	/*
	 * Behaviour utilizado para chamar uma função definida no ambiente de execução jess,
	 *responsável por imprimir os parametros number of times the cooling/heating systems 
	 *were activated and the Mean and Standard Deviation of the temperatures of 
	 *all Departments 
	 **/
	private class Notificacao extends TickerBehaviour{
		private MonitorAgent monitor;
		
		public Notificacao(Agent a, long timeOut) {
			super(a, timeOut);
			this.monitor = (MonitorAgent) a;
		}
		
		public void onTick() {
			try {
				Rete jess_engine = this.monitor.getJessEngine();
				jess_engine.executeCommand("(informacoes)");
				jess_engine.run();
			}catch(JessException ex) {	System.err.println(ex);	}
		}		
	}// fim da class Notificacao	
}
