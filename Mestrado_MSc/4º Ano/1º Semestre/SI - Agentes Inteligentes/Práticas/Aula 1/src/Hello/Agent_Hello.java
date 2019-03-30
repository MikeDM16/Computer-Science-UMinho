package Hello;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class Agent_Hello extends Agent{
	protected void setup() {
		super.setup();
		
		System.out.println(this.getLocalName() + " a começar.");
		this.addBehaviour(new Behaviour());
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	private class Behaviour extends SimpleBehaviour{
		private boolean finished;
		
		public Behaviour() {	this.finished = false;	}
		
		public void action() {
			System.out.println("Olá do agente");
			this.finished = true;
		}
		
		public boolean done() {	return this.finished;	}
	}
} 
