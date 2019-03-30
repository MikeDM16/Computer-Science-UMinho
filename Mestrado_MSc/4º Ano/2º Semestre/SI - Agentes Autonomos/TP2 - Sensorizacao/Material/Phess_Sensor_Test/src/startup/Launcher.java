package startup;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


public class Launcher {
	Runtime rt;
	ContainerController container;
	
	public void initRemoteContainer(String host, String port){
		System.out.println("Connecting to Remote JADE platform...");
		this.rt = Runtime.instance();
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "false");
		this.container = rt.createAgentContainer(prof);
		rt.setCloseVM(true);
	}
	
	public void startAgentInPlatform(String name, String classpath){
		 try {
			 AgentController ac = container.createNewAgent(name, classpath, new Object[0]);
             ac.start();
         } catch (Exception e) {
             e.printStackTrace();
         }
	}

	// Launch Remote connections
    public static void main(String args[]) {
    	Launcher mc = new Launcher();
    	mc.initRemoteContainer( "192.168.1.21", "1099");
    	mc.startAgentInPlatform("SensorName", "agents.IslabSound");
    }
}
