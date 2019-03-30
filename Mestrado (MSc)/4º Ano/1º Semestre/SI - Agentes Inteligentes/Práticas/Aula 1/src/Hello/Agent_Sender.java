package Hello;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Agent_Sender extends Agent {
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " a começar.");
		
		this.addBehaviour(new EnviarMsg(this, 5000));
		this.addBehaviour(new ReceberResposta());	
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	private class EnviarMsg extends TickerBehaviour {
		public EnviarMsg(Agent a, long timeout) {
			super(a, timeout);
		}
		
		protected void onTick() {
			AID receiver = new AID();
			receiver.setLocalName("Bob_Recetor");
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			long time = System.currentTimeMillis();
			msg.setConversationId(""+time);
			msg.addReceiver(receiver);
			msg.setContent("Ola");
			send(msg);
		}
	};
	
	private class ReceberResposta extends CyclicBehaviour {
		
		public void action() 
        {
			ACLMessage msg= receive();
           if (msg!=null) {
               	System.out.println("The agent sender receive the following answer: \n " 
                + msg.getContent() + " from : " + msg.getSender());
        		
           }
           block();                      
        }
	};
}
