package Biblioteca;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Costumer extends Agent {
	String[] books; 
	String requisitado;
	
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " a começar.");
		
		this.books = new String[] {"b1", "b2", "b3", "b4", "b5", "b6"};
		this.addBehaviour(new RealizarRequisitos(this, 8000, this));
		this.addBehaviour(new RecebeRespostas(this));
	
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	public void setRequisitado(String b){ this.requisitado = b; }
	public String getRequisitado(){ return this.requisitado; }

	
	private class RealizarRequisitos extends TickerBehaviour{
		private Costumer c; 
		
		public RealizarRequisitos(Agent a, long timeout, Costumer c) {
			super(a, timeout);
			this.c = c; 
		}
		
		public void onTick() {
			AID receiver = new AID();
			receiver.setLocalName("Bibliotecario");
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(receiver);
			long time = System.currentTimeMillis();
			msg.setConversationId(""+time);
			
			Random r = new Random();
			int b = r.nextInt(books.length);
			//O conteudo da mensagem indica o nome do livro a requisitar
			msg.setContent(books[b]);
			send(msg);
			
			System.out.println("Cli: Requisitou o livro: "+ books[b]);
			c.setRequisitado(books[b]);
		}
	};
	
	private class RecebeRespostas extends CyclicBehaviour{
		private Costumer c;
		
		public RecebeRespostas(Costumer c) {
			this.c = c; 
		}
		
		public void action() {
			ACLMessage msg= receive();
	           if (msg!=null) {
	        	   System.out.println("Cli: Recebeu a seguinte resposta: " 
	   	                + msg.getContent() + ".");
	        	   
	        	   String resposta = msg.getContent();
	        	   if(resposta.equals("Sucesso")) {
	        		   /*Ler o livro dura entre 7 a 12 segs. 
	        		     Se demorar mais de 10seg, o bibliotecário repõe o livro na 
	        		     parteleira automaticamente 
	        		     */
	        		   Random r = new Random();
	        		   int min = 7;   int max = 12;
	        		   int tempo = r.nextInt(max-min) + min;
	        		   block(tempo); 
	        		   
	        		   ACLMessage devolver = msg.createReply();
	        		   String requisitado = c.getRequisitado();
	        		   devolver.setContent("" + requisitado);
	               	   devolver.setPerformative(ACLMessage.CANCEL);
	        		   send(devolver);
	        		   System.out.println("Cli: Devolveu o livro: " + requisitado +
	        				   ", ao fim de " + tempo + " segundos.");
	        	   }
	        	   if(resposta.equals("Inexistente")){
	        		   // O Livro nao existe no conjunto de livros do bibliotecario
	        		   System.out.println("Cli: O Livro nao existe no conjunto de livros do bibliotecario");
	        	   }
	        	   if(resposta.equals("Reservado")) {
	        		   // O livro já se encontra requisitado por outro cliente
	        		   System.out.println("Cli: O Livro já está requisitado. ");
	            	   
	        	   }        		
	           }
	           System.out.println("");
	           block();             
        }
	};
}
