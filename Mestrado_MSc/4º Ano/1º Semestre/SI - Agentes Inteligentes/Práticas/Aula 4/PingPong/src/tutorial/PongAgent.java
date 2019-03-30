package tutorial;

import java.util.Map;

import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.future.IFuture;
import jadex.micro.MicroAgent;

public class PongAgent extends MicroAgent {
	private int enviadas = 5;
	
	public void messageArrived(Map msg, MessageType mt) {		
		if( ((String) msg.get(SFipa.PERFORMATIVE)).equals("query-if")) {
			System.out.println(this.getAgentName() + ": recebeu uma msg. ");
			
			if ( ( ((String) msg.get(SFipa.CONTENT)).equals("Ping!") ) && (this.enviadas > 0)){
				Map reply = createReply(msg, mt);
				reply.put(SFipa.CONTENT, "Pong!");
				reply.put(SFipa.PERFORMATIVE, "inform");
				reply.put(SFipa.SENDER,  getComponentIdentifier());
				sendMessage(reply, mt);				
				this.enviadas--;
				System.out.println(this.getAgentName() + ": Enviou uma mensagem de Pong.");
			}
		}
	}
	
}
