package SistemaTaxis;

import java.text.DecimalFormat;
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

public class Taxi extends Agent{
	private int x, y, nr_Pedidos;
	private double klms_Precorridos;
	
	protected void setup() {
		super.setup();
				
		// Registo do agente como sendo do tipo Taxi
		DFAgentDescription dfd 	= new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd 	= new ServiceDescription();
		sd.setType("Taxista");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
		
		Random r = new Random();
		this.x = r.nextInt(100); 
		this.y = r.nextInt(100);
		this.nr_Pedidos = 0; this.klms_Precorridos = 0;
		
		System.out.println("Taxi " + this.getLocalName() + " a começar"
				+ " nas coordenadas x= " + this.x + " e y= " + this.y + ".");
		
		this.addBehaviour(new Receber_Pedidos(this));
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println("Taxi " + this.getLocalName() + " a morrer.");
		
		try { DFService.deregister(this);}
		catch (FIPAException e) {	e.printStackTrace();	}
	}
	
	public int	getX() 		{ return this.x;	}
	public int 	getY() 		{ return this.y; }
	public int 	get_Pedidos() { return this.nr_Pedidos; }
	public double get_kms() { return this.klms_Precorridos; }
	public void setX(int x) { this.x = x;	}
	public void setY(int y) { this.y = y; }
	public void incrementa_Nr_Pedidos() 	{	this.nr_Pedidos++; }
	public void incrementaKlms(double dist) {	this.klms_Precorridos += dist; }
	
	private class Receber_Pedidos extends CyclicBehaviour {
		private Taxi t;
	
		public Receber_Pedidos(Taxi t) {	this.t = t;	}
		
		public void action() {
			ACLMessage msg= receive();
			if (msg!=null) {
				// ACLMessage.INFORM usada para soliciar a distancia do taxista ao cliente
				if(msg.getPerformative() == ACLMessage.INFORM) {
					String[] tokens = msg.getContent().split(" ");
					int x_Cli = Integer.parseInt(tokens[0]);
					int y_Cli = Integer.parseInt(tokens[1]);
					double dist = Math.sqrt(Math.pow((t.getX() - x_Cli),2) + 
											Math.pow((t.getY() - y_Cli),2));
					dist = (double)Math.round(dist * 100d) / 100d;
					ACLMessage reply = msg.createReply();
					reply.setContent("" + dist);
					
					// O taxista faz uma "proposta" com a distancia que ele tem que percorrer para servir o cliente
					reply.setPerformative(ACLMessage.PROPOSE);
					send(reply);
				}
				
				// ACLMessage.Request usada para indicar ao taxista que vai atender o cliente X
				if(msg.getPerformative() == ACLMessage.REQUEST) {
					this.t.addBehaviour(new Transporta_Cliente(this.t, msg.getContent()));
				} 
				
				//ACLMessage.Confirm indica que passaram 20s e deve dar report das estatisticas 
				if(msg.getPerformative() == ACLMessage.CONFIRM) {
					System.out.println("Taxi " + this.t.getLocalName() + 
							": dist precorrida " + this.t.get_kms() + "km " +
							"| nr pedidos atendidos " + this.t.get_Pedidos() + ".");
					myAgent.doDelete();
				}
				
			}
		}
	}
			
	private class Transporta_Cliente extends SimpleBehaviour{
		private Taxi t;
		private int x_Cli, y_Cli;
		private boolean finished;
		private String nome_cli;
		
		public Transporta_Cliente(Taxi t, String pedido) {
			this.t = t;
			String[] tokens = pedido.split(" ");
			this.nome_cli = tokens[0];
			this.x_Cli = Integer.parseInt(tokens[1]);
			this.y_Cli = Integer.parseInt(tokens[2]);
			this.finished= false;
		}
				
		public void action() {
			this.t.incrementa_Nr_Pedidos();
			
			double dist = Math.sqrt(Math.pow((this.t.getX() - x_Cli),2) + 
									Math.pow((this.t.getY() - y_Cli),2));
			dist = (double)Math.round(dist * 100d) / 100d;
			this.t.incrementaKlms(dist);
			
			// As coordenadas do taxi passam a ser as do cliente que ele serviu
			this.t.setX(this.x_Cli); 
			this.t.setY(this.y_Cli);
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			AID recetor = new AID();
			recetor.setLocalName(this.nome_cli);
			msg.addReceiver(recetor);
			msg.setContent("Chegou ao seu destino");
			send(msg);		
			
			this.finished = true;
		}
								
		public boolean done() { return finished; }
	}
}

