package sensors;

public class Phidgets {

	private InterfaceKitPhidget ik;
	private RFIDPhidget rfid;

	public boolean connect() {
		boolean resp = true;
		try {
			ik = new InterfaceKitPhidget();
			ik.open(116679);
			ik.waitForAttachment();
			System.out.println("InterfaceKit instatiation complete!");
		} catch (PhidgetException e) {
			System.err.println("Error on the interfaceKit instatiation!");
		}
		
		ik.addSensorChangeListener(new SensorChangeListener() {
		public void sensorChanged(SensorChangeEvent se) {
			if (se.getIndex() == 2) {
				if (se.getValue() == 0){
					sensors.put("IslabTouch", "ON");
					System.out.println("touch");
				} else if (se.getValue() == 1) {
					sensors.put("IslabTouch", "OFF");
				}
			}
		}
	});
		return resp;
	}
	
	public double captureTemperature(){
		try {
		double temp = (ik.getSensorValue(1) * 0.2222) - 61.111;
		
	} catch (PhidgetException e) {
		e.printStackTrace();
	}
	}
	
	public double captureLuminosity(){
		try {
		double lum = ik.getSensorValue(0);
		
	} catch (PhidgetException e) {
		e.printStackTrace();
	}
	}
	
	public void close(){
		boolean resp = true;
		
		return resp;
	}
	

}
