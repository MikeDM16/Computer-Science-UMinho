package aula_ai5;

import jadex.bdi.runtime.Plan;

/**
 *
 * @author Rita Canavarro
 */
public class NotifyPlan extends Plan {
    public NotifyPlan(){
        getLogger().info("Created:"+this);
    }

    @Override
    public void body() {
       int cnt = ((Integer) getBeliefbase().getBelief("contador").getFact());
       long alarme = (long) getBeliefbase().getBelief("alarme").getFact();
       getBeliefbase().getBelief("alarme").setFact(alarme+10000);
       
       getLogger().info("There have been "+cnt+" requests!");

    }
}
