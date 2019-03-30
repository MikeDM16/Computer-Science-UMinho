package tutorial;

import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

/**
 * A simple agent to be used as a basis for own developments.
 */
@Agent
@Description("The hello agent. <br> Empty agent that can be loaded and started.")

public class HelloAgent {
	/**
	 * Called when the agent is started.
	 */
	@AgentBody
	public IFuture<Void> executeBody() {
		System.out.println("Hello world!");
		return IFuture.DONE;
	}
}
