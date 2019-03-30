package testing;

import jade.wrapper.StaleProxyException;

import java.util.List;
import java.util.Map;

import PhessComunication.PhessSensors;
import PhessComunication.PhessTimeoutException;

public class Test {

	 public static void main(String args[]){
		 PhessSensors s = new PhessSensors();
		 s.connectToPHESS("islab.di.uminho.pt","1099", "test");
		 try {
			System.out.println("List of Agents: ");
			List<String> res = s.getTotalAgents();
			for(String e : res){
				System.out.println(e);
			}
			System.out.println("List of Services: ");
			res = s.getSensorsAgentServices("IslabSensor");
			for(String e : res){
				
				System.out.println(e);
			}
			System.out.println("Sensor value: ");
			Map<String,String> resM = s.getSensorValueFromAgent("PhessSound");
			for(String e : resM.keySet()){
				System.out.println(e + " : " + resM.get(e));
			}
			System.out.println("Sensor value: ");
//			resM = s.getSensorValueFromAgent("IslabSensor2");
//			for(String e : resM.keySet()){
//				System.out.println(e + " : " + resM.get(e));
//			}
//			System.out.println("Sensor value: ");
//			resM = s.getSensorValueFromAgent("IslabSensor3");
//			for(String e : resM.keySet()){
//				System.out.println(e + " : " + resM.get(e));
//			}
		} catch (InterruptedException | PhessTimeoutException e) {
			e.printStackTrace();
		} 
		 finally {
			 try {
				s.disconnect();
			} catch (StaleProxyException | InterruptedException | PhessTimeoutException e) {
				e.printStackTrace();
			}
		 }
		 
	 }
}
