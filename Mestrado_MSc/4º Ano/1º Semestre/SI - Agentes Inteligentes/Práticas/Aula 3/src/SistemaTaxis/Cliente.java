package SistemaTaxis;

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

public class Cliente extends Agent {
	public AID cliente;
	public int x, y;
	
	public Cliente() { }
	
	protected void setup() {
		super.setup();
		
		//Registo do agente como sendo do tipo cliente
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("Cliente");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
		
		Random r = new Random();
		this.x = r.nextInt(100); 
		this.y = r.nextInt(100);
		System.out.println("Cliente " + this.getLocalName() + " a começar"
				+ " nas coordenadas x= " + this.x + " e y= " + this.y + ".");
		
		this.addBehaviour(new Chamar_Taxi(this.x, this.y, this));
		this.addBehaviour(new Esperar_Taxi());
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println("Cliente " + this.getLocalName() + " a morrer.");
		
		try { DFService.deregister(this);}
		catch (FIPAException e) {	e.printStackTrace();	}
	}
	
	private class Chamar_Taxi extends SimpleBehaviour {
		private boolean finished;
		private int x, y;
		private Cliente c;
		
		public Chamar_Taxi(int x, int y, Cliente c) {
			this.x = x; this.y = y; this.c = c;
			this.finished = false;
		}
		
		public void action() {
			//Procurar pelo Manager disponivel
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("Manager");
			template.addServices(sd);
			
			DFAgentDescription[] disponiveis;
			try {
				disponiveis = DFService.search(myAgent, template);
				if(disponiveis.length > 0 ) {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(disponiveis[0].getName());
					msg.setContent("" + this.x + " " + this.y);
					send(msg);
				}				
				
			}catch (FIPAException e) {	e.printStackTrace();	}
			this.finished = true;
		}	
		
		public boolean done() { return finished; }
	};
	
	private class Esperar_Taxi extends CyclicBehaviour{			
		public void action() {
			ACLMessage msg = receive();
			if(msg!=null) {
				System.out.println("Cliente " + this.myAgent.getLocalName() + ": " + msg.getContent());
				myAgent.doDelete();
			}				
		}
	};
}
