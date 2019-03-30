
/**
 * Escreva a descrição da classe Ecran aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class Ecran
{
    private static int N,M;
    private Pixel[][] matriz = new Pixel[N][M]; 
    
    public static void SetDimensoes(int x,int y){ N=x; M=y; }
    
    public Ecran(){ this(0); }
    public Ecran(int cor){
        int i,j;
        for(i=0;i!=N;i++)
           for(j=0;j<M;j++){ matriz[i][j]= new Pixel(i,j,cor);}
       
    }
    public Ecran clone(){
        Ecran clone = new Ecran();
        int i,j;
        for(i=0;i!=N;i++)
            for(j=0;j!=M;j++){
                //Pixel p = Pmatriz(i,j);
                clone.getPixel(i,j).mudarCor( matriz[i][j].getCor() ); 
                //mudar cor e getCor sao metodos de instancia da classe Pixel
            }
        return clone;
    }
    
    public Pixel getPixel(int x, int y){ return matriz[x][y]; }
    
    public void mudaCor(int x, int y, int cor){
        if(x<=N && y<=M && cor<=15 && cor>=0) {
            matriz[x][y].mudarCor(cor); //metodo de instancia da classe Pixel
        }
    }
    public int obterCor(int x, int y){
        return matriz[x][y].getCor(); //metodo de instancia da classe Pixel
    }
    public boolean equals(Ecran o){
        if(o==null) {return false; }
        if(this==o) {return true; }
        if(this.getClass() != o.getClass() ) {return false; }
        int i, j;
        for (i=0;i!=N;i++)
            for(j=0; j!=M; j++)
              if(this.getPixel(i,j).getCor() != o.getPixel(i,j).getCor()){ return false;}
        return true; 
    }
    public String toString(){
        return "A matriz tem tamanho "+N+" por "+M+".\n"; 
    }
}
