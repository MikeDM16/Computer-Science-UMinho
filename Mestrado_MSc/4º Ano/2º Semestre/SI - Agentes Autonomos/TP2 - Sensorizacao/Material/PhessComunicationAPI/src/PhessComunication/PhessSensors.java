package PhessComunication;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.List;
import java.util.Map;


import Agents.Status;

public class PhessSensors {

	private Runtime rt;
	private ContainerController container;
	private AgentController agentControler;
	private PhessComunication pComunication;
	
	public PhessSensors(){
	}
	
	public void connectToPHESS(String host, String port, String user){
		this.rt = Runtime.instance();
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "false");
		this.container = rt.createAgentContainer(prof);

		try {
			
			this.agentControler = this.container.createNewAgent(user, Status.class.getName(), new Object[0]);
			this.agentControler.start();
			pComunication = agentControler.getO2AInterface(PhessComunication.class);
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}	
		
	}
	
	public List<String> getTotalAgents() throws InterruptedException, PhessTimeoutException {
		JadeCallback j = new JadeCallback();
		this.pComunication.getTotalAgents(j);
		return (List<String>) j.getResults();
	}

	public List<String> getSensorsAgentServices(String agent) throws InterruptedException, PhessTimeoutException {
		JadeCallback j = new JadeCallback();
		this.pComunication.getSensorsAgentServices(agent, j);
		return (List<String>) j.getResults();
	}

	public Map<String,String> getSensorValueFromAgent(String agent) throws InterruptedException, PhessTimeoutException {
		JadeCallback j = new JadeCallback();
		this.pComunication.getSensorValueFromAgent(agent, j);
		return (Map<String,String>) j.getResults();
	}
	
	public void disconnect() throws StaleProxyException, InterruptedException, PhessTimeoutException{
		JadeCallback j = new JadeCallback();
		this.pComunication.disconnect(j);
		j.getResults();
		this.agentControler.kill();
		this.container.kill();
		this.rt.shutDown();
	}
	
}
