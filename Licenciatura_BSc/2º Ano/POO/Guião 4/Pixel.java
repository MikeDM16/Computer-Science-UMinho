
/**
 * Escreva a descrição da classe Pixel aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Pixel
{
    // variaveis da instancia
    private double x, y;
    private int cor;
    
    //construtores da instancia
    public Pixel(){ this(0.0,0.0,0); }
    public Pixel(double x, double y, int cor){ this.x = x; this.y = y; this.cor = cor; }
    public Pixel clone(){
        Pixel p = new Pixel();
        p.setX(this.x);
        p.setY(this.y) ;
        p.mudarCor(this.cor);
        return p; 
    }
    
    //instancias da classe
    public double getX(){ return x; }
    public double getY() { return y; }
    public int getCor(){ return cor; }
    
    public void setX(double x){ this.x = x; }
    public void setY(double y){this.y = y; }
    public void mudarCor(int cor){ if((cor>=0)&&(cor<=15)) {this.cor = cor;} }
    public void desloca(double dx, double dy){ x += dx; y += dy; }
    public String toString(){
        String cores = new String();
        cores = "A cor do pixel é: "+this.nomeCor()+".";
        return cores; 
    }
    public boolean equals(Pixel p){
        if (p==null){return false; } 
        if(p==this){ return true; }
        if(this.getClass()!=p.getClass()){return false; }
       return ((this.x == p.getX()) && (this.y == p.getY()) && (this.cor==p.getCor()));
    }
    public String nomeCor(){
        String cores = new String();
        switch (cor) {
            case 0: cores = "Preto";
                    break;
            case 1: cores = "Azul marinho";
                    break;
            case 2: cores = "Verde Escuro";
                    break;
            case 3: cores = "Azul petróleo";
                    break;
            case 4: cores = "Castanho";
                    break;
            case 5: cores = "Púrpura";
                    break;
            case 6: cores = "Verde aliva";
                    break;
            case 7: cores = "Cinza claro";
                    break;
            case 8: cores =" Cinza escuro";
                    break;
            case 9: cores = "Azul";
                    break;
            case 10: cores = "Verde";
                    break;
            case 11: cores = "Azul Turquesa";
                    break;
            case 12: cores = "Vermelho";
                    break;
            case 13: cores = "Fúscsia";
                    break;
            case 14: cores = "Amarelo";
                    break;
            case 15: cores = "Branco";
                    break; 
        }
        return cores; 
    }
}
