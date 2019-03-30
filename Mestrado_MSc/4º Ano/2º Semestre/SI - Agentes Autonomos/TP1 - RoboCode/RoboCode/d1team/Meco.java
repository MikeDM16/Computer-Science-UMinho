package d1team;

public class Meco {
    
    private int posicaoX;
    private int posicaoY;
    private String nome;

    public Meco(int x, int y, String nome) {
        this.posicaoX = x;
        this.posicaoY = y;
        this.nome=nome;
    }

    public int getX() {
        return posicaoX;
    }

    public int getY() {
        return posicaoY;
    }
}
