package traducao;

import jadex.bdi.runtime.IExpression;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;
import jadex.bridge.fipa.SFipa;
import jadex.commons.Tuple;

public class AdicionaTraducao extends Plan {
	public AdicionaTraducao() {
		System.out.println("Plano " + this + " criado.");		
	}
	
	public void body() {
		IMessageEvent msg = waitForMessageEvent("adicionar_traducao");
		String words = (String) msg.getParameter(SFipa.CONTENT).getValue().toString();
		String[] tokens = words.split(" ");
		//tokens[0] tem a palavra "adicionar" 
		String engWord = tokens[1], ptWord = tokens[2];
		System.out.println("Pedido de adição: " + words);
		
		IExpression aceder_dicionario = getExpression("query_epword");
		Object teste = aceder_dicionario.execute("$eword", engWord);
		
		if(teste == null) {
			Tuple tuplo = new Tuple(engWord, ptWord);
			this.getBeliefbase().getBeliefSet("epwords").addFact(tuplo);
			
			System.out.println("Adicionou tuplo ("+engWord+", "+ptWord+").");
		}else {
			System.out.println("Par ("+engWord+", "+ptWord+") já existe no dicionario.");
		}
		
		
	}
	
	public void passed() {}
	public void failed() {}
	public void aborted() {}
}
