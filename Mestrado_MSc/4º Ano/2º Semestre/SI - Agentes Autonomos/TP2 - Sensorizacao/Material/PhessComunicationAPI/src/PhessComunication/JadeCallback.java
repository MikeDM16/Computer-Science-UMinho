package PhessComunication;

public class JadeCallback {
	
	private static final int JADE_WAIT = 10000;

	private Object results;
	private boolean completed;
	
	public JadeCallback(){
		results = new Object();
		this.completed = false;
	}
	
	public synchronized void setResults(Object o){
		this.results = o;
		this.completed = true;
		this.notifyAll();
	}
	
	public synchronized Object getResults() throws InterruptedException, PhessTimeoutException{
		if (!this.completed){
			this.wait(JADE_WAIT);
		}
		if (!this.completed){
			throw new PhessTimeoutException();
		}
		return results;
	}
}
