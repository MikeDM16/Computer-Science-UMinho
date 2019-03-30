
/**
 * Escreva a descrição da classe LLStrings aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
public class LLStrings
{
    //implementaçao com conceito de listas ligadas
    private int tamanho;
    private LLStringsAux listaLig;
    
    public class LLStringsAux {
        private String string;
        private LLStringsAux next; 
        
        public LLStringsAux(String s){
            string = s;
            next=null;
        }
        public LLStringsAux getNext(){ return next; }
        public String getStr(){if(this==null){return null; }   return string; }
        public void setNext(LLStringsAux c){ next = c; }
    }
    public LLStringsAux getLista(){ return listaLig; }
    public void imprimir(){
        LLStringsAux iterar = listaLig;
        for(int i=0; i!=tamanho; i++){
            System.out.print("elemento "+(i+1)+" "+iterar.getStr()+", ");
            iterar = iterar.getNext();
        }
    }
    public LLStrings(){
        tamanho=0;
        listaLig = null;
    }
    public int tamanho(){ return tamanho; }
    public boolean vazia(){ return (tamanho==0); }
    public void adicionar(String s){
        if(listaLig == null) {listaLig = new LLStringsAux(s); }
        else{
            LLStringsAux caixa = new LLStringsAux(s);
            LLStringsAux iterar = listaLig;
            while(iterar.getNext() != null ) { iterar = iterar.getNext(); }
            iterar.setNext(caixa);
        }
        tamanho++;
    }
    public void inserir(int i, String s){
        if(i>=tamanho){adicionar(s); }
        else{
            LLStringsAux caixa = new LLStringsAux(s);
            LLStringsAux iterar = listaLig;
            for(int j=0; j!=i-1; j++) { iterar = iterar.getNext(); }
            caixa.setNext(iterar.getNext() );
            iterar.setNext(caixa);
            tamanho++;
        }
    }
    public String get(int i){
        LLStringsAux iterar = listaLig;
        for(int j=0;j!=i;j++){ iterar = iterar.getNext(); }
        return iterar.getStr();
    }
    public void esvaziar(){ tamanho=0; listaLig=null;}
    public boolean equals(LLStrings s){
        if(s==null) {return false; }
        if(this==s){return true; }
        if(this.getClass() != s.getClass()){ return false; }
        if(this.tamanho != s.tamanho){ return false; }
        LLStringsAux x1 = this.getLista(); 
        LLStringsAux x2 = s.getLista();
        for(int i=0; i!=tamanho; i++) {
            if (x1.getStr() != x2.getStr()) {return false; } 
            x1 = x1.getNext(); x2 = x2.getNext();
        }
        return true;
    }
    public String toString(){return "A lista de Strings tem "+tamanho+" elementos."; }
    public LLStrings clone(){
        LLStrings clone = new LLStrings();
        //clone.tamanho = this.tamanho;
        LLStrings iterar = clone;
        for(int i=0; i!=tamanho; i++){
            iterar.adicionar(this.get(i));
        }
        //iterar.adicionar(null);
        return clone;
    }
}
