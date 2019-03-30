package SPB;

import java.util.Vector;

public class Posicao {
	private int x, y;
	
	public Posicao(int x, int y) {
		this.x = x; 
		this.y = y;
	}

	public void deslocar(Posicao d) {
		int xd = d.getX(), yd = d.getY();
		
		if(xd > this.x) this.x += 1;
		if(xd < this.x) this.x -= 1;
		
		if(yd > this.y) this.y += 1;
		if(yd < this.y) this.y -= 1;
		
	}
	/*Métodos getters e setters necessários para o contexto */
	public int getX() {		return this.x;	}
	public int getY() {		return this.y;	}
	
	public void setX(int x) {	this.x = x;	}
	public void setY(int y) {	this.y = y;	}
	
	public double distancia(Posicao p) {
		int x1 = p.getX(), y1 = p.getY();
		int x2 = this.x, y2 = this.y;

		return Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2, 2));
	}
	
}
