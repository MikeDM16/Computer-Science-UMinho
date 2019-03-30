package agents;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;


import pt.islab.phess.CurrentAgentServices;
import pt.islab.phess.CurrentSensorValue;
import pt.islab.phess.ObtainSensorValues;
import pt.islab.phess.ObtainSensorsDescription;
import pt.islab.phess.PhessOntology;
import pt.islab.phess.Sensor;
import pt.islab.phess.SubscribeAgentSensors;
import pt.islab.phess.db.Sensors;
import pt.islab.phess.db.facade.SensorFacade;
import pt.islab.phess.impl.DefaultCurrentAgentServices;
import pt.islab.phess.impl.DefaultCurrentSensorValue;
import pt.islab.phess.impl.DefaultSensor;

public class IslabSound extends SensorAgentTemplate {

	private static final String[] SENSORS = { "SOUND", "SENSOR" };
	private static final String[] LOCATION = { "ISLAB", "ISLAB:LAB" };
	private static final String TYPE = "SENSOR";
	
	private Codec codec = new SLCodec(); 
	private Ontology ontology = PhessOntology.getInstance();

	private Map<String,Sensor> sensorDefinition;
	private HashMap<String, String> sensors;
	private static final int SAMPLE_PERIOD = 10000;
	
	private HashSet<String> subscribed;
	
	private ByteArrayOutputStream out;
	private AudioFormat format;
	private DataLine.Info info;
	private TargetDataLine line;
	private static final double CALIBRATION_VALUE = -80;
	double mMaxValue;

	@Override
	public void setup() {
		initAgentTemplate();
		setupSystem();
		registerSensorsInDatabase();
		startTimedSensorData(SAMPLE_PERIOD);
		startMessageReceiver();
		notifyDF(concat(SENSORS, LOCATION));
	}

	private void setupSystem() {
		getContentManager().registerLanguage(codec); 
		getContentManager().registerOntology(ontology);
		
		try{
			this.sensors = new HashMap<String, String>();
			this.sensors.put("IslabSound", String.valueOf(0));
			this.sensorDefinition = new HashMap<String,Sensor>();
			
			this.subscribed = new HashSet<String>();
			
			this.out = new ByteArrayOutputStream();
			float sampleRate = 16000;
			int sampleSizeInBits = 8;
			int channels = 1;
			boolean signed = true;
			boolean bigEndian = true;
			this.format = new AudioFormat(sampleRate, sampleSizeInBits, channels,
					signed, bigEndian);
			this.info = new DataLine.Info(TargetDataLine.class, format);
			this.line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			this.mMaxValue = 0;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public void registerSensorsInDatabase(){
//		SensorFacade sf = new SensorFacade();
//		Sensors a = sf.findSensor("IslabSound");
//		if ( a == null){
//			a = new Sensors();
//			a.setRefreshRate("10000");
//			a.setName("IslabSound");
//			a.setStrategy("Monitor");
//			a.setSensing("SOUND");
//			a.setAgentName("IslabSensor");
//			sf.createSensor(a);
//			a = sf.findSensor("IslabSound");
//		}
//		Sensor sound = new DefaultSensor();
//		sound.setSensorId(a.getId());
//		sound.setSensorName(a.getName());
//		sound.setSensorRefreshRate(Integer.valueOf(a.getRefreshRate()));
//		sound.setSensorType(a.getSensing());
//		sensorDefinition.put(a.getName(), sound);
//	}
	
	public void registerSensorsInDatabase(){
		Sensor sound = new DefaultSensor();
		sound.setSensorId(1);
		sound.setSensorName("IslabSound");
		sound.setSensorRefreshRate(Integer.valueOf("10000"));
		sound.setSensorType("SOUND");
		sensorDefinition.put("IslabSound", sound);
	}

	public void handleTimedSensorEvent() {
		double splValue = 0.0;
		double rmsValue = 0.0;

		int bufferSize = line.getBufferSize() * 2; 
		byte buffer[] = new byte[bufferSize];
		double P0 = 0.000002;

		int counted = line.read(buffer, 0, buffer.length);

		for (int i = 0; i < bufferSize - 1; i++) {
			rmsValue += (short) buffer[i] * (short) buffer[i];
		}

		rmsValue = rmsValue / bufferSize;
		rmsValue = Math.sqrt(rmsValue);

		splValue = 20 * Math.log10(rmsValue / P0);
		splValue = splValue + CALIBRATION_VALUE; 
		splValue = Math.round(splValue * 100) / 100;

		if (mMaxValue < splValue) {
			mMaxValue = splValue;
		}
		this.sensors.put("IslabSound", String.valueOf(splValue));
		System.out.println("db : " + splValue);
		
//		handleDatabaseStorage();
		handleSubscriptions();
	}

	public boolean handleMessage() {
		boolean res = true;
		ACLMessage message = null;
		MessageTemplate phessRequest = MessageTemplate.MatchOntology(this.ontology.getName());		
		if ((message = this.receive(phessRequest)) != null) {
			ContentElement ce = null;
			try {
				ce = getContentManager().extractContent(message);
			} catch (CodecException | OntologyException e1) {
				e1.printStackTrace();
			}
			if (ce instanceof Action) {
				Action a = (Action) ce;
				Concept c = a.getAction();
				if (c instanceof ObtainSensorsDescription){
					handleObtainSensorsDescription(message, c);					
				} else if ( c instanceof ObtainSensorValues){
					handleObtainSensorValues(message, c);
				} else if (c instanceof SubscribeAgentSensors){
					handleSubscribeAgentSensors(message, c);
				}
			}
		} else {
			res = false;
		}
		return res;
	}
	
	protected void takedown(){
		try {
			this.line.close();
			this.out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] concat(String[] A, String[] B) {
		String[] C = new String[A.length + B.length];
		System.arraycopy(A, 0, C, 0, A.length);
		System.arraycopy(B, 0, C, A.length, B.length);
		return C;
	}

	private void handleObtainSensorValues(ACLMessage message, Concept concept){
		ACLMessage reply = message.createReply();

		ContentElementList cet = new ContentElementList();
		
		for (String name : this.sensors.keySet()) {
			CurrentSensorValue csv = new DefaultCurrentSensorValue();
			csv.setSelectedSensor( sensorDefinition.get(name) );
			csv.setSensorValue( sensors.get(name) );
			csv.setTimesStamp( String.valueOf(System.currentTimeMillis()) );
			cet.add(csv);
		}
		reply.setPerformative(ACLMessage.INFORM);
		try {
			getContentManager().fillContent(reply, cet);
			send(reply);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}
	
	private void handleObtainSensorsDescription(ACLMessage message, Concept concept){
		ObtainSensorsDescription osd = (ObtainSensorsDescription) concept;
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		CurrentAgentServices cas = new DefaultCurrentAgentServices();
		for(String e : this.SENSORS){
			cas.addListOfServices(e);
		}
		try {
			getContentManager().fillContent(reply, cas);
		} catch (OntologyException | CodecException e1) {
			e1.printStackTrace();
		} 
		send(reply);
	}
	
	private void handleSubscribeAgentSensors(ACLMessage message, Concept concept){
		SubscribeAgentSensors sA = (SubscribeAgentSensors) concept;
		this.subscribed.add(sA.getSubscriberAgent().getLocalName());
	}
	
	private void handleSubscriptions(){
		for(String e: this.subscribed){	
			for (String name : this.sensors.keySet()) {
				AID receiver = new AID();
				receiver.setLocalName(e);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(receiver);
				msg.setLanguage(codec.getName());
				msg.setOntology(ontology.getName());
				
				CurrentSensorValue cs = new DefaultCurrentSensorValue();
				cs.setSelectedSensor(this.sensorDefinition.get(name));
				cs.setSensorValue(this.sensors.get(name));
				cs.setTimesStamp( String.valueOf(System.currentTimeMillis()) );
				try {
					getContentManager().fillContent(msg, cs);
				} catch (OntologyException | CodecException e1) {
					e1.printStackTrace();
				}
				
				send(msg);
				System.out.println("sent to : " + e );
			}
		}
	}
	
//	private void handleDatabaseStorage(){
//		AID r = new AID();
//		r.setLocalName("PhessDatabase");
//
//		ContentElementList cet = new ContentElementList();
//		
//		for (String name : this.sensors.keySet()) {
//			CurrentSensorValue csv = new DefaultCurrentSensorValue();
//			csv.setSelectedSensor( sensorDefinition.get(name) );
//			csv.setSensorValue( sensors.get(name) );
//			csv.setTimesStamp(String.valueOf( System.currentTimeMillis()));
//			cet.add(csv);
//		}
//		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
//		message.setLanguage(codec.getName());
//		message.setOntology(ontology.getName());
//		message.addReceiver(r);
//		try {
//			getContentManager().fillContent(message, cet);
//			send(message);
//		} catch (CodecException | OntologyException e) {
//			e.printStackTrace();
//		}
//		
//	}

}
