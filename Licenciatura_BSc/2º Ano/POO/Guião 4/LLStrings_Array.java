
/**
 * Escreva a descrição da classe LLStrings_Array aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class LLStrings_Array
{
    //variaveis da classe
    int tamanho;
    String[] listaLig;
    
    public LLStrings_Array(){ this(10); }
    public LLStrings_Array(int i){ tamanho=0; listaLig = new String[i]; }
    public int tamanho(){ return tamanho; }
    public boolean vazia(){ return (tamanho==0); }
    public void adicionar(String s){
        if(tamanho==listaLig.length){
            String[] novo = new String[2*tamanho];
            System.arraycopy(listaLig, 0, novo, 0, tamanho);
            listaLig = novo;
        }
        listaLig[tamanho++] = s;
    }
    public String get(int i){
        if(i>tamanho){return null;}
        return (listaLig[i]);
    }
    public void inserir(int i, String s){
        if(i>=tamanho || tamanho==listaLig.length){ adicionar(s); }
        else{ System.arraycopy(listaLig, i, listaLig, i+1, tamanho-i);}
        System.out.println("i="+i+"lista-i="+(tamanho-i));
        listaLig[i]=s; tamanho++; 
    }
    
    public void esvaziar(){ tamanho = 0; }
    public boolean equals(LLStrings_Array o){
        if(o==null){return false;}
        if(this==o){return true; }
        if(this.getClass() != o.getClass()) {return false;}
        if(this.tamanho != o.tamanho() ){return false; }
        
        int i;
        for(i=0; i!=tamanho();i++){
            if(listaLig[i] != o.get(i)){return false; }
        }
        return true;
    }
    public String toString(){ return "O tamanho da lista de strings é de"+tamanho+"."; }
    public LLStrings_Array clone(){
        LLStrings_Array clone = new LLStrings_Array(this.tamanho);
        int i;
        for(i=0;i!=this.tamanho; i++){
            clone.inserir(i, this.get(i) );
        }  
        return clone;
    }
}
