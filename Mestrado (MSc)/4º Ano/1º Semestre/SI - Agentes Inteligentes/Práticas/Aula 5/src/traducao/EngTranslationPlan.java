package traducao;

import java.util.HashMap;

import jadex.bdi.runtime.IExpression;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;
import jadex.bridge.fipa.SFipa;

public class EngTranslationPlan extends Plan{
		private String engWord; 
		private IExpression aceder_dicionario;
		
		public EngTranslationPlan() {
			System.out.println("Plano " + this + " criado.");
		}
		
		public void body() {
			IMessageEvent msg = waitForMessageEvent("pedido_traducao");
			
			String words = (String) msg.getParameter(SFipa.CONTENT).getValue().toString();
			String[] tokens = words.split(" ");
			
			int count = ((Integer) getBeliefbase().getBelief("contador").getFact());
			
			//tokens[0] tem a palavra "traduzir" 
			this.engWord = tokens[1]; 
			System.out.println(" Pedido de tradução recebido: " + engWord);
			
			this.aceder_dicionario = getExpression("query_epword");
			String traducao = (String) this.aceder_dicionario.execute("$eword", engWord);
			
			IMessageEvent resp;
			if(traducao != null) {
				resp = createMessageEvent("inform");
				resp.getParameter(SFipa.CONTENT).setValue("Tradução: " + traducao);
			}else {
				resp = createMessageEvent("failure");
				resp.getParameter(SFipa.CONTENT).setValue("Falha. Palavra com erro ou nao existe no dicionario");
			}			
			//Incrementar nr de pedidos de tradução
			getBeliefbase().getBelief("contador").setFact(count++);
		
			resp.getParameterSet(SFipa.RECEIVERS).addValue(msg.getParameter(SFipa.SENDER));
			sendMessage(resp);
		}
		
		public void passed() {}
		public void failed() {}
		public void aborted() {}
}
