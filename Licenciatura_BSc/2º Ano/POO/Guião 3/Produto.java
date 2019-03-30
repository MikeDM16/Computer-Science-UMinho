
/**
 * Escreva a descrição da classe Produto aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.lang.String;
public class Produto
{
   //variaveis da instancia
   private String codigo, nome;
   private int stock, minimo;
   private double buyPrice, sellPrice; 
   
   //Construtores de instancia
   public Produto(){ this("xxxxxxxx","NoName",0,0,0,0); }
   public Produto(String codigo, String nome, int stock, int minimo, double buyPrice, double sellPrice){
       this.nome = nome; this.codigo = codigo;
       this.stock = stock; this.minimo = minimo; this.buyPrice = buyPrice; this.sellPrice = sellPrice;
    }
  
   public Produto clone(){
       return new Produto(this.codigo, this.nome, this.stock, this.minimo, this.buyPrice, this.sellPrice);
       
   }
   
    //metodos da instancia
   public String getCodigo(){ return codigo; }
   public String getNome(){ return nome; }
   public int getStock(){ return stock; }
   public int getMinimo(){ return minimo; }
   public double getBuyPrice(){ return buyPrice; }
   public double getPrecoVenda(){ return sellPrice; }
   
   public void setCodigo(String codigo){ this.codigo = codigo; }
   public void setNome(String nome){ this.nome = nome; }
   public void setStock(int stock){ this.stock = stock; }
   public void setMinimo(int minimo){ this.minimo = minimo; }
   public void setPrecoVenda(double sellPrice){ 
       if(sellPrice>=buyPrice){ this.sellPrice = sellPrice; }
   }
   public void setbuyPrice(double sellPrice){ this.buyPrice = buyPrice;  }
   
   public boolean equals(Produto p){
       if(p==null){ return false;} 
       if(p==this){return true; }
       if(p.getClass()!=this.getClass()){return false; }
       return ( p.getCodigo()==this.codigo && p.getNome()==this.nome && p.getStock()==this.stock &&
       p.getMinimo()==this.minimo && p.getPrecoVenda()==this.sellPrice && p.getBuyPrice()==this.buyPrice); 
   }
   
   public String toString(){
       String s = new String();
       s = "Existem "+this.stock+" produtos em stock.";
       return s;
    }
   
   public void modificaStock(int stock){
       int s = this.stock + stock;
       if(s>=0){
           this.stock+= stock;
        }
   }
   public void alteraCodigo(String codigo){
       if( codigo.length() >=8){ this.codigo = codigo; }
   }
   public void defineMargemLucro(double percentagem){ 
       sellPrice = ( (1 + 0.01*percentagem)*buyPrice );
   }
   public void efectuaCompra(double valor){
       double p = (valor/sellPrice);
       p = stock - p;
       int x = (int) p;
       if(x>=0){ stock = x;}
       
   }
   public double lucroTotal(){
       return ( stock*(sellPrice) ); //o lucro devia ser custosCompra - receitasVenda -.-"
    }
   public double precoTotal(int encomenda){
       return (encomenda*sellPrice);
   }
   public boolean abaixoValor(){
       return (stock<minimo);
   }
}
