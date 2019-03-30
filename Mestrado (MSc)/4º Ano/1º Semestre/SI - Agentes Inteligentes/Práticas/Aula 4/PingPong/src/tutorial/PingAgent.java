package tutorial;

import java.util.HashMap;
import java.util.Map;

import jadex.bridge.ComponentIdentifier;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.types.message.MessageType;
import jadex.commons.SUtil;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.MicroAgent;

public class PingAgent extends MicroAgent{
	private int enviadas; 
	
	public IFuture<Void> executeBody() {
		System.out.println(PingAgent.this.getAgentName() + ": a começar.");
		PingAgent.this.enviadas = 0;
		
		final Future ret = new Future();
		
		IComponentStep step = new IComponentStep() 
		{		
			public IFuture<Void> execute(IInternalAccess ia){
				if(PingAgent.this.enviadas > 5 ) {
					System.out.println(PingAgent.this.getAgentName() + ": Não obtem respostas do Agente Pong");
				}else {
					String convID = SUtil.createUniqueId(PingAgent.this.getAgentName());
					Map msg = new HashMap();
					msg.put(SFipa.CONVERSATION_ID, convID);
					msg.put(SFipa.CONTENT, "Ping!");
					msg.put(SFipa.PERFORMATIVE, "query-if");
					ComponentIdentifier receiver = new ComponentIdentifier("Pong", getComponentIdentifier().getParent());
					msg.put(SFipa.RECEIVERS, receiver);
					PingAgent.this.sendMessage(msg, SFipa.FIPA_MESSAGE_TYPE);

					PingAgent.this.enviadas++; 
					
					System.out.println(PingAgent.this.getAgentName() + ": Enviou uma mensagem de Ping.");
				}							
				waitFor(3000, this);
				return IFuture.DONE;
			}
		};		
		
		scheduleStep(step);	
		
		return ret;
	}
	
	public void messageArrived(Map msg, MessageType mt) {
		if( ((String) msg.get(SFipa.PERFORMATIVE)).equals("query-if")) {
			System.out.println(this.getAgentName() + ": recebeu uma msg. ");
			
			if ( ((String) msg.get(SFipa.CONTENT)).equals("Pong!")){
				PingAgent.this.enviadas = 0;
				
				System.out.println(this.getAgentName() + ": Recebeu uma mensagem de Pong.");
			}
		}
	}
	
}
