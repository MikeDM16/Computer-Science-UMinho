package Temperatura;

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

public class TemperatureSensorAgent extends Agent {
	private double temperature;
	private int variacao;
	private boolean aquecer = false, arrefecer = false; 
	public TemperatureSensorAgent() {}
	
	protected void setup() {
		super.setup();
		
		//Registo do agente como sendo do tipo sensor temperatura
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd  = new ServiceDescription();
		sd.setType("TemperatureSensor");
		sd.setName(this.getLocalName());
		dfd.addServices(sd);
		
		try { DFService.register(this, dfd);}
		catch (FIPAException e) {	e.printStackTrace();	}
		
		this.temperature = 25.0;
		Random rand = new Random();
		int r = rand.nextInt(3);
		switch (r) {
			case 0 :	this.variacao = -1;
						break;
			case 2 :   	this.variacao =  1;
						break;
			default: 	this.variacao = 0;
						break; 
		}
		System.out.println("Sensor: " + this.getLocalName() + " começou.");
				
		this.addBehaviour(new UpdateSensor(this, 2000));
		this.addBehaviour(new SendTemperature(this, 3000));
		this.addBehaviour(new ReceiveInstructions(this));
	}
	
	/*
	 * Função responsavel por aquecer ou arrefecer a sala, conforme o estado do sistema
	 *de refrigeração. 
	 * Se o mesmo nao estiver ativado, a temperatura varia de forma normal, segundo o 
	 *parametro variacao
	 * O aquecimento desliga-se quando se atinge 30ª e o arrefecimento desliga-se quando 
	 *se atingem os 20ª
	 * */
	public void updateTemp() {
		if((this.aquecer == true) && (this.arrefecer == false))
			this.temperature += 1;
		else if((this.aquecer == false) && (this.arrefecer == true))
				this.temperature -= 1;
			else
				this.temperature += variacao; 
		
		if((this.temperature > 30)&& (this.aquecer == true)) {
			this.aquecer = false;
			System.out.println("Sensor: " + this.getLocalName() + " desligou aquecimento.");
		}
		if((this.temperature < 20) && (this.arrefecer == true)) {
			this.arrefecer = false;
			System.out.println("Sensor: " + this.getLocalName() + " desligou arrefecimento.");
		}
	}
	
	/* Métodos getters e setters necessários */
	public double getTemp() 			{  return this.temperature;	}
	public void setAquecer(boolean b) 	{	this.aquecer = b; 		}
	public void setArrefecer(boolean b) {	this.arrefecer = b; 	}
	
	/*
	 * Behaviour utilizado para simular a variação da temperatura  
	 * */
	private class UpdateSensor extends TickerBehaviour{
		private TemperatureSensorAgent sensor; 
		
		public UpdateSensor(Agent a, long timeOut) {
			super(a, timeOut);
			this.sensor = (TemperatureSensorAgent) a; 
		}
		
		public void onTick() {
			//Chamada do método que atualiza a temperatura do departamento
			this.sensor.updateTemp();
		}
	} // Fim do behaviour que faz update da temperatura
	
	/*
	 * Behaviour utilizado para enviar a temperatura do departamento ao agente
	 *monitor. 
	 * */
	private class SendTemperature extends TickerBehaviour{
		private TemperatureSensorAgent sensor;
		private int count = 0;
		
		public SendTemperature(Agent a, long timeOut) {
			super(a, timeOut);
			this.sensor = (TemperatureSensorAgent) a;
		}
		
		public void onTick() {
			//Procurar pelo Manager de sensores disponivel no Directory Facilitator
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd 		= new ServiceDescription();
			sd.setType("MonitorAgent");
			template.addServices(sd);
			
			DFAgentDescription[] disponiveis;
			try {
				disponiveis = DFService.search(myAgent, template);
				if(disponiveis.length > 0 ) {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(disponiveis[0].getName());
					double temp = this.sensor.getTemp();
					/* O parametro time é incluido para no agente monitor manter apenas o facto com 
					 *a mensagem mais recente do agente. 
					 * O parametro id_name é o nome do agente no sistema jade para ser possivel usar 
					 *esse campo como destino/recetor de uma ACLMessage. 
					 * */
					String content = "id_name: " + this.sensor.getLocalName() + " " + 
									 "temperature " + temp + " " + 
									 "time " + this.count++ ;
					
					msg.setContent(content);
					send(msg);
					System.out.println("Sensor: " + this.sensor.getLocalName() + " enviou uma notificaçao com temp =  " + temp);
				}						
			}catch (FIPAException e) {	e.printStackTrace();	}
		}	
	} // Fim da class SendTemperature 
	
	
	/*
	 * Behaviour responsável por receber e analisar as eventuais instruções do agente
	 *monito, para regular a temperatura do departamento */
	private class ReceiveInstructions extends CyclicBehaviour{
		private TemperatureSensorAgent sensor;

		public ReceiveInstructions(TemperatureSensorAgent a) {
			this.sensor = a;
		}
		
		public void action() {
			ACLMessage msg = receive();
			if(msg != null) {
				String acao = msg.getContent();
				if(acao.equals("Aquecer")) {
					//this.sensor.addBehaviour(new Aquecer(this.sensor, 1000));
					this.sensor.setAquecer(true);
					this.sensor.setArrefecer(false);
					System.out.println("Sensor: " + this.sensor.getLocalName() + " ativou aquecimento.");
				}
				if(acao.equals("Arrefecer")) {
					this.sensor.setArrefecer(true);
					this.sensor.setAquecer(false);
					//this.sensor.addBehaviour(new Arrefecer(this.sensor, 1000));
					System.out.println("Sensor: " + this.sensor.getLocalName() + " ativou arrefecimento.");
				}
			}
		}
	} // End of ReceiveInstructions class
}
