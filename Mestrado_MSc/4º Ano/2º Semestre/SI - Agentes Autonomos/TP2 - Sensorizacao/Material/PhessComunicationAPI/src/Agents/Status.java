package Agents;

//import agents.OneShotBehaviour;
import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.islab.phess.CurrentAgentServices;
import pt.islab.phess.CurrentSensorValue;
import pt.islab.phess.ObtainSensorValues;
import pt.islab.phess.ObtainSensorsDescription;
import pt.islab.phess.PhessOntology;
import pt.islab.phess.Sensor;
import pt.islab.phess.impl.DefaultObtainSensorValues;
import pt.islab.phess.impl.DefaultObtainSensorsDescription;
import pt.islab.phess.impl.DefaultSensor;
import PhessComunication.JadeCallback;
import PhessComunication.PhessComunication;

public class Status extends Agent implements PhessComunication {
	
	private Codec codec = new SLCodec(); 
	private Ontology ontology = PhessOntology.getInstance();

	public Status() {
		registerO2AInterface(PhessComunication.class, this);
	}
	
	protected void setup(){
		getContentManager().registerLanguage(codec); 
		getContentManager().registerOntology(ontology);
	}

	@Override
	public void getTotalAgents(JadeCallback j) {
		final JadeCallback je = j;

		OneShotBehaviour b = new OneShotBehaviour() {

			@Override
			public void action() {
				List<String> res = new ArrayList<String>();

				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("SENSOR");
//				dfd.addServices(sd);

				DFAgentDescription[] result;
				try {
					result = DFService.search(myAgent, dfd);
					if (result.length > 0) {
						for (DFAgentDescription desc : result) {
							res.add(desc.getName().getLocalName());
						}
					}
					je.setResults(res);
				} catch (FIPAException e) {

				}
			}
		};
		this.addBehaviour(b);
	}

	@Override
	public void getSensorsAgentServices(String agent, JadeCallback j) {
		final String ag = agent;
		final JadeCallback jc = j;
		OneShotBehaviour b = new OneShotBehaviour() {
			
			@Override
			public void action() {
				try {
					//send message
					AID receiver = new AID();
					receiver.setLocalName(ag);
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.setLanguage(codec.getName());
					msg.setOntology(ontology.getName());
					msg.addReceiver(receiver);
					ObtainSensorsDescription osd = new DefaultObtainSensorsDescription();
					osd.setSAgent(getAID());
					Action a = new Action(receiver,osd);
					a.setActor(receiver);
					getContentManager().fillContent(msg, a);
					send(msg);
				
					//receive response
					boolean received = false;
					ACLMessage message = null;
					
					MessageTemplate template = MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
							MessageTemplate.MatchSender(receiver));
					
					
					while( !received ){
						if ((message = myAgent.receive(template)) != null) {
							System.out.println("test received");
							ContentElement ce = getContentManager().extractContent(message);
							List<String> mm = new ArrayList<String>();
							if ( ce instanceof CurrentAgentServices){
								CurrentAgentServices ser = (CurrentAgentServices) ce;
								Iterator it = ser.getAllListOfServices();
								while(it.hasNext()){
									String resp = (String) it.next();
									mm.add(resp);
								}
							}
							jc.setResults(mm);
														
							received = true;
						} else {
							block();
						}
					}
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		this.addBehaviour(b);
	}

	@Override
	public void getSensorValueFromAgent(String agent, JadeCallback j) {
		final String ag = agent;
		final JadeCallback jc = j;
		
		OneShotBehaviour b = new OneShotBehaviour() {

			@Override
			public void action() {
				try {
					//send message
					AID receiver = new AID();
					receiver.setLocalName(ag);
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					
					msg.setLanguage(codec.getName());
					msg.setOntology(ontology.getName());
					
					ObtainSensorValues osv = new DefaultObtainSensorValues();
					osv.setSAgent(receiver);
					Action a = new Action(receiver,osv);
					a.setActor(receiver);
					getContentManager().fillContent(msg, a);
					
					
					msg.addReceiver(receiver);
					send(msg);
				
					//receive response
					MessageTemplate template = MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
							MessageTemplate.MatchSender(receiver));
					
					boolean received = false;
					ACLMessage message = null;
					
					while (!received){
						if ((message = myAgent.receive(template)) != null) {
							Map<String, String> mm = new HashMap<String, String>();
							ContentElement ce = getContentManager().extractContent(message);
							if (ce instanceof ContentElementList){
								ContentElementList cel = (ContentElementList) ce;
								for(ContentElement c : cel.toArray()){
									if ( c instanceof CurrentSensorValue){
										CurrentSensorValue sensorValue = (CurrentSensorValue) c;
										mm.put(sensorValue.getSelectedSensor().getSensorName() , sensorValue.getSensorValue() );
									}
								}
							} else if (ce instanceof CurrentSensorValue){
								CurrentSensorValue sensorValue = (CurrentSensorValue) ce;
								mm.put(sensorValue.getSelectedSensor().getSensorName() , sensorValue.getSensorValue() );
							}
							jc.setResults(mm);
							
							received = true;
				
						} else {
							block();
						}
					}
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		};
		this.addBehaviour(b);
	}
	
	public void getSubscriptionAgent(String agent, JadeCallback j) {
		final String ag = agent;
		final JadeCallback jc = j;
		
		OneShotBehaviour b = new OneShotBehaviour() {

			@Override
			public void action() {
				try {
					//send message
					AID receiver = new AID();
					receiver.setLocalName(ag);
					ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
					msg.addReceiver(receiver);
					send(msg);
				
					//receive response
					MessageTemplate template = MessageTemplate.and(
							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
							MessageTemplate.MatchSender(receiver));
					
					boolean received = false;
					ACLMessage message = null;
					
					while (!received){
						if ((message = myAgent.receive(template)) != null) {
						Object obj = message.getContentObject();
						
						if (obj instanceof Map<?,?>) {
							jc.setResults(obj);
						}
						received = true;
						
						} else {
							block();
						}
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				} 
			}
		};
		this.addBehaviour(b);
	}

	@Override
	public void disconnect(JadeCallback j) {
		final JadeCallback jc = j;
		OneShotBehaviour b = new OneShotBehaviour() {

			@Override
			public void action() {
				takeDown();
				jc.setResults("Agent Killed");
			}
		};
		this.addBehaviour(b);
	}

}
