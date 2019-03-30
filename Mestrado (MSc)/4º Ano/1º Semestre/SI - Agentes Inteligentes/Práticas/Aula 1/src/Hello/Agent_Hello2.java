package Hello;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class Agent_Hello2 extends Agent{
	int counter[];
	int i = 0;;
	protected void setup() {
		this.counter = new int[100];
		
		super.setup();
		
		System.out.println(this.getLocalName() + " a começar.");
		
		ParallelBehaviour b = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ALL){
			public int onEnd() {
				System.out.println("Finalizou");
				//myAgent.doDelete();
				return 0;
			}
		};
		
		this.addBehaviour(b);
		b.addSubBehaviour(new Behaviour());
		b.addSubBehaviour(new Behaviour());
		b.addSubBehaviour(new Behaviour());
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	private class Behaviour extends SimpleBehaviour{
		private boolean finished;
		
		public Behaviour() {	this.finished = false;	}
		
		public void action() {
			System.out.println("Olá do paralelo " + i);
			counter[i] = i;
			i++;
		}
		
		public boolean done() {	
			if(i > 5) {
				return true;
			}else return false;
		}
	}
} 
