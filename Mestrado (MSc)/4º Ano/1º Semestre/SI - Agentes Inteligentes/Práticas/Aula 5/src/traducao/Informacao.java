package traducao;

import jadex.bdi.runtime.Plan;

public class Informacao extends Plan{
	public Informacao() {
		System.out.println("Plano " + this + " criado.");
	}
	
	public void body() {
		int count = ((Integer) getBeliefbase().getBelief("contador").getFact());
	    long alarme = (long) getBeliefbase().getBelief("alarme").getFact();
	    getBeliefbase().getBelief("alarme").setFact(alarme+10000);
	    
	    System.out.println("There have been "+count+" requests!");
	}
}
