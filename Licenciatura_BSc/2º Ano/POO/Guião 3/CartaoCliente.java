
/**
 * Escreva a descrição da classe CartaoCliente aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class CartaoCliente
{
    // variaveis da classe
    private int pontos, valorbonus, bonusGIVEN = 0; 
    private String codigo, titular;
    private double dinheiro;
    
    //construtores da classe
    public CartaoCliente(){ this(0,0,"000000","noName",0); }
    public CartaoCliente(double dinheiro, int pontos,String codigo, String titular, int valorbonus){
        this.pontos = pontos; this.dinheiro = dinheiro; 
        this.codigo = codigo; this.titular = titular;
        this.valorbonus = valorbonus; 
    }
    public CartaoCliente(CartaoCliente p){ this(p.getDinheiro(), p.getPontos(), p.getCodigo(), p.getNome(), p.getValorBonus());}
    
    //metodos da instancia
    public int getPontos(){ return pontos; }
    public int getValorBonus(){ return valorbonus; }
    public String getNome(){return titular; }
    public String getCodigo(){ return codigo; }
    public double getDinheiro(){ return dinheiro; }
    
    public void setPontos(int pontos){ this.pontos = pontos; }
    public void setValorBonus(int valorbonus){ this.valorbonus = valorbonus; bonusGIVEN=0;}
    public void setNome(String nome){ this.titular = nome; }
    public void setCodigo(String codigo){ this.codigo = codigo; }
    public void setDinheiro(double dinheiro) { this.dinheiro = dinheiro; }
    
    public CartaoCliente clone(){
        CartaoCliente ccli = new CartaoCliente();
        ccli.setPontos(this.pontos);
        ccli.setValorBonus(this.valorbonus);
        ccli.setNome(this.titular);
        ccli.setCodigo(this.codigo);
        ccli.setDinheiro(this.dinheiro);
        return ccli;
    }
    
    public boolean equals(CartaoCliente c){
        if(c ==null){ return false; }
        if(c.getClass()!=this.getClass()){return false; }
        if(c==this){return true; }
        return (c.getPontos()==this.pontos && c.getValorBonus()==this.valorbonus &&
        c.getNome()==this.titular && c.getCodigo()==this.codigo && c.getDinheiro()==this.dinheiro);
    }
    
    public String toString(){
        String cli = new String();
        cli = "O cartão pertence ao cliente: "+this.titular+".";
        return cli;
    }
    
    public void descontar(int menu){
        switch (menu){
            case 1:  if(pontos>=10){
                     pontos -= 10;
                     break;
                    }
            
            case 2: if(pontos>=20) {
                    pontos -= 20;
                    break;
                   }
        }
        
    }
    public void descarregarPontos(CartaoCliente cartao){
        pontos += cartao.getPontos();
        cartao.setPontos(0);
    }
    public void efectuarCompra(double valor){
        dinheiro += valor;
        if(valor>5){ pontos+=2;}
        else { pontos++;}
        if( (pontos > valorbonus) && (bonusGIVEN==0) ) {pontos += 10; bonusGIVEN=1; }
    }
}
