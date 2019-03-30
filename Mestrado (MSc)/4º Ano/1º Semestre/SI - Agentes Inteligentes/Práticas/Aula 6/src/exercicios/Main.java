package exercicios;

import jess.*;

public class Main {
	public static void main(String args[]) {
		try {
			Rete engine = new Rete();
			//Value v = engine.eval("");
			//System.out.print(v);
			
			engine.batch("exercicios/exe2.clp");
			engine.reset();
			engine.executeCommand("(facts)");
			engine.run();
			
		}catch(JessException ex) {
			System.err.println(ex);
		}
	}
}
