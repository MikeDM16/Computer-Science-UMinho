package SistemaTaxis;

import java.util.Random;
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

public class Manager extends Agent{
	protected void setup() {
		super.setup();
		System.out.println("Manager " + this.getLocalName() + " a começar.");
		
		//Registo do agente como sendo do tipo Manager
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("Manager");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
		
		this.addBehaviour(new Receber_Pedidos(this));
		this.addBehaviour(new Terminar(this, 5000));
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println("Manager " + this.getLocalName() + " a morrer.");
		
		try { DFService.deregister(this);}
		catch (FIPAException e) {	e.printStackTrace();	}
	}
	
	private class Receber_Pedidos extends CyclicBehaviour{
		private Manager m;
		public Registo[] distancias; 
		public int it, nr_Taxis;
		private Cliente cli;
		private boolean ocupado;
		
		public Receber_Pedidos(Manager m) { 
			this.m = m;		this.cli = new Cliente();
			this.it = 0; 	this.nr_Taxis = 0;	
			ocupado = false;
		}
		
		public void action() {
			
			ACLMessage msg= receive();
			if (msg!=null) {			 
				//Receber pedidos de cliente
				if(msg.getPerformative() == ACLMessage.INFORM) {
					if(ocupado == false) {
						ocupado = true;
						atender_Cliente(msg);
					}else {
						ACLMessage reply = msg.createReply();
						reply.setContent("Manager Ocupado");
						send(reply);
					}					
				}
				
				//Receber mensagens dos taxis, em resposta ao comportamento paralelo
				if(msg.getPerformative() == ACLMessage.PROPOSE) {
					double dist = Double.parseDouble(msg.getContent());
					AID taxi = msg.getSender();
					if(it < distancias.length ) {
						distancias[it] = new Registo(dist, taxi);
						it++;
					}
				}
				
				if( it == nr_Taxis ) {
					Registo r = new Registo();
					for(int i = 0; i < distancias.length; i++) {
						if(distancias[i].distancia < r.distancia) {
							r.distancia = distancias[i].distancia;
							r.taxi 		= distancias[i].taxi;
						}
					}
					System.out.println("Escolheu o taxi " + r.taxi.getName() + " para o " + cli.cliente.getLocalName());
					ACLMessage msgTaxi = new ACLMessage(ACLMessage.REQUEST);
					msgTaxi.addReceiver(r.taxi);
					msgTaxi.setContent(cli.cliente.getLocalName() + " " + cli.x + " " + cli.y);
					send(msgTaxi);	
					it = 0; nr_Taxis = 0; ocupado = false;
				}				
			}
		}
		
		private void atender_Cliente(ACLMessage msg) {
			String[] tokens = msg.getContent().split(" ");
			cli.cliente = msg.getSender();
			cli.x = Integer.parseInt(tokens[0]);
			cli.y = Integer.parseInt(tokens[1]);
			
			//Reunir todos os taxistas disponiveis
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("Taxista");
			template.addServices(sd);
			DFAgentDescription[] disponiveis;
			
			ParallelBehaviour b = new ParallelBehaviour(m, ParallelBehaviour.WHEN_ALL) {
				public int onEnd() {
					return 0;
				}
			};					
			m.addBehaviour(b);
			
			try {
				disponiveis = DFService.search(this.m, template);
				this.nr_Taxis 	= disponiveis.length; 
				this.distancias = new Registo[nr_Taxis];
				for(int i = 0; i < nr_Taxis; i++) {		
					AID taxi = disponiveis[i].getName();
					b.addSubBehaviour(new Questiona_Taxi(msg.getContent(), taxi, 1));
				}					
				
			}catch (FIPAException e) {	e.printStackTrace();	}
		}
	};
	
	private class Registo{
		public double distancia;
		public AID taxi;
		
		public Registo() {
			this.distancia = 2000;		this.taxi = null;
		}	
		public Registo(double d, AID t) {
			this.distancia = d;		this.taxi = t;
		}
	};
	
	private class Questiona_Taxi extends SimpleBehaviour{
		private boolean finished;
		private String msg; 
		private AID dest; 
		private int op;
		
		public Questiona_Taxi(String msg, AID dest, int op) {
			this.finished = false;		this.msg = msg; this.dest = dest; 
			this.op = op;
		}
		
		public void action() {
			ACLMessage msgTaxi = null;
			if(op == 1)
				msgTaxi = new ACLMessage(ACLMessage.INFORM);
			if(op == 2)
				msgTaxi = new ACLMessage(ACLMessage.CONFIRM);
			
			msgTaxi.addReceiver(this.dest);
			msgTaxi.setContent(this.msg);
			send(msgTaxi);
		
			this.finished = true;
		}
		
		public boolean done() {
			return this.finished; 
		}
	};
	
	private class Terminar extends TickerBehaviour{
		
		public Terminar(Agent a, long timeout) {
			super(a, timeout);
		}
		
		public void onTick() {
			//Reunir todos os taxistas disponiveis
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("Taxista");
			template.addServices(sd);
			DFAgentDescription[] disponiveis;
			
			ParallelBehaviour t = new ParallelBehaviour(this.myAgent, ParallelBehaviour.WHEN_ALL) {
				public int onEnd() {
					//Matar ? Não Matar ?
					myAgent.doDelete();
					return 0;
				}
			};					
			this.myAgent.addBehaviour(t);
			
			try {
				disponiveis = DFService.search(this.myAgent, template);
				for(int i = 0; i < disponiveis.length; i++) {		
					AID taxi = disponiveis[i].getName();
					t.addSubBehaviour(new Questiona_Taxi("", taxi, 2));
				}				
			}catch (FIPAException e) {	e.printStackTrace();	}
		}
	};
}
