package agents;

import java.io.IOException;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;

public abstract class SensorAgentTemplate extends Agent{
	
	private static Agent myAgent;
	private static String myAgentName;

	public static final MessageTemplate request = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	
	//required methods to implement
	public abstract void setup();
	
	//optional methods
	public void handleTimedSensorEvent(){;}
	public boolean handleMessage(){return false;}
	public void handleDfNotification(DFAgentDescription[] notification) {;};
	
	//abstract implementations
	public void initAgentTemplate(){
		myAgent = this;
		myAgentName = this.getAID().getLocalName();
	}
	
	public void notifyDF(final String[] services){
		System.out.println("DF notified by: " + myAgentName);
		OneShotBehaviour notify = new OneShotBehaviour(this) {
			 public void action() {
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.setName(myAgent.getAID());
				for(String n: services){
					ServiceDescription sd = new ServiceDescription();
					sd.setName(myAgentName);
					sd.setType(n);
					dfd.addServices(sd);
				}
				
		        try {
		        	DFAgentDescription list[] = DFService.search( myAgent, dfd );
					if ( list.length>0 ) {
		            	DFService.deregister(myAgent);
					}
					DFService.register(myAgent,dfd);
				}
		        catch (FIPAException fe) { 
		        	fe.printStackTrace();
		        }
			 }
		};
		this.addBehaviour(notify);
	}
	
	public void unNotifyDF(){
		OneShotBehaviour unNotify = new OneShotBehaviour(this) {
			 public void action() {
				try {
					DFService.deregister(myAgent);
				} catch (FIPAException e) {
					e.printStackTrace();
				}
			 }
		};
		this.addBehaviour(unNotify);
	}
	
	public void subscribe(final String[] services){
        DFAgentDescription template = new DFAgentDescription();
        for(String n: services){
        	ServiceDescription sd = new ServiceDescription();
            sd.setType(n);
            template.addServices(sd);
        }
        Behaviour subscribe = new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, null)) {

            @Override
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] resultados = DFService.decodeNotification(inform.getContent());
                    handleDfNotification(resultados);
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        };
        addBehaviour(subscribe);
        System.out.println("DF services subscrition: " + myAgentName);
	}	

	public void startTimedSensorData(int sample) {
		TickerBehaviour timer = new TickerBehaviour(this,sample) {
	    	
			@Override
			protected void onTick() {
				handleTimedSensorEvent();
			}
	    };
	    this.addBehaviour(timer);
	}
	
	public void startMessageReceiver(){
		CyclicBehaviour messageReceiver = new CyclicBehaviour(this) {
			
			@Override
			public void action() {
				if(!handleMessage()){
					block();
				}
			}
		};
		addBehaviour(messageReceiver);
	}
	
	
	public void sendDataToAgents(String agent) {
		AID receiver = new AID();
		receiver.setLocalName(agent);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);
//			msg.setContentObject(s);
		msg.addReceiver(receiver);
		send(msg);
	}
	
}
