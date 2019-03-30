package Hello;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Agent_Receiver extends Agent {
	
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " a começar.");
		addBehaviour(new ReceberResponder(this) );
	}
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	private class ReceberResponder extends CyclicBehaviour {
		
		public ReceberResponder(Agent_Receiver a) {
			super(a);
		}
		
		public void action() 
        {
			ACLMessage msg= receive();
           if (msg!=null) {
               	System.out.println("The agent Bob receive the following message: \n " 
                + msg.getContent() + " from : " + msg.getSender());
        		
               	ACLMessage resp = msg.createReply();
               	
               	if(msg.getContent().equals("Ola")) {
               		resp.setContent("Ola do receptor");
               		resp.setPerformative(ACLMessage.INFORM);
               	}else {
               		resp.setContent("Não percebi... ");
               		resp.setPerformative(ACLMessage.NOT_UNDERSTOOD);
               	}
               	
               	send(resp);
            }
           block();                      
        }
    
	};

}
