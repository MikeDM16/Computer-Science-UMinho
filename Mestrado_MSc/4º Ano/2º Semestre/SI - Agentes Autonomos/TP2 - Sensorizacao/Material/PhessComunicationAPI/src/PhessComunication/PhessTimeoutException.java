package PhessComunication;

public class PhessTimeoutException extends Exception{
	
	private static final String MSG = "No response from PHESS system!";
	
	public PhessTimeoutException() {
        super(MSG);
    }
}	
