package Biblioteca;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Librarian extends Agent {
	String[] books; 
	int[] stock;
	
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " a começar.");
		
		this.books = new String[] {"b1", "b2", "b3", "b4"};
		
		//variavel para guardar o nr de cópias de cada livro disponivel
		//Por omissão só existe uma cópia de cada livro
		this.stock = new int[books.length];
		for(int i = 0; i != stock.length; stock[i++]= 1);
		stock[1] = 0; // Forçar que nao exista o livro b2; 
		
		this.addBehaviour(new RecebePedidos(this));
	}
	
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a morrer.");
	}
	
	public void repor(String b) {
		for(int i = 0; i != books.length; i++)
       		if(books[i].equals(b)) 
       			//Aumentar em 1 unidade o stock daquele livro devolvido
       			stock[i]++;
	}
	
	public void retirar(String b) {
		for(int i = 0; i != books.length; i++)
       		if(books[i].equals(b)) 
       			//Aumentar em 1 unidade o stock daquele livro devolvido
       			stock[i]--;
	}
	
	public String[] getBooks() { return this.books; }
	public int[] getStock() { return this.stock; }
	
		
	private class RecebePedidos extends CyclicBehaviour{
		Librarian l; 
		
		public RecebePedidos(Librarian l) {	this.l = l; }
		
		public void action() {
			int encontrado = 0;
			
			ACLMessage msg= receive();
			if (msg!=null) {
				if(msg.getPerformative() == ACLMessage.INFORM) {
				//Se a mensagem for um pedido de requisito - INFORM 
					
					String livro_pedido = msg.getContent();
	               	ACLMessage resp = msg.createReply();
	               	System.out.println("Lib: Recebeu um pedido de requisito do livro: " + 
	               				livro_pedido);
	               	
	               	String[] books = l.getBooks();
	               	int[] stock = l.getStock();
	               	
	               	for(int i = 0; i != books.length; i++) {
	               		if(books[i].equals(livro_pedido)) {
	               			encontrado = 1;
	               			if(stock[i] > 0) {
	                       		resp.setContent("Sucesso");
	                       		// Decrementar o nr daquele livro em stock para requisitos futuros
	                       		l.retirar(livro_pedido);                       		
	                       		
	               			}else {	
	               				resp.setContent("Reservado"); 
	               			}
	               		}
	               	}
	               	
	               	if(encontrado == 0) { resp.setContent("Inexistente"); }               	
	               	send(resp);
	            }
				
				if(msg.getPerformative() == ACLMessage.CANCEL) {
					//Se a mensagem for uma devolução - CANCEL
					String livro_devolvido = msg.getContent();
					l.repor(livro_devolvido);
					System.out.println("Lib: Recebeu devolução do livro: " + livro_devolvido + ".");
				}
			}               	
            block();
        }
	};
}
