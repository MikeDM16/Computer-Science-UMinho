package PhessComunication;

import java.util.List;


public interface PhessComunication {

	//discover agents and services on the sensor layer
	public void getTotalAgents(JadeCallback j);
	public void getSensorsAgentServices(String agent, JadeCallback j );
	
	
	//get value from agent
	public void getSensorValueFromAgent(String Agent, JadeCallback j);
	
	//disconnect
	public void disconnect(JadeCallback j);
	
}
