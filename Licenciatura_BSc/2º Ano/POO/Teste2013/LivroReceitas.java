
/**
 * Write a description of class LivroReceitas here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;
public class LivroReceitas
{
    private String NomeLivro;
    private Map<String, Receita> receitas; 
    
    /*public double totalCalorias(String nomeReceita) throws ReceitaInexistenteException{
        try{
            this.receitas.get(nomeReceita).calorias();
        }catch (ReceitaInexistenteException e){
            return 0;
        }
    }*/
    public double totalCalorias(String nomeReceita){
        if(this.receitas.containsKey(nomeReceita))
            this.receitas.get(nomeReceita).calorias();
        return 0.0;
    }
    
    public Map<String, Set<String>> receitasPorIngrediente(){
        Receita receita = null;
        Set<String> totalIngredientes = new TreeSet<String>();
        Map<String, Set<String>> lista = new TreeMap<String, Set<String>>();
        Set<String> aux = null;
        for(Map.Entry<String, Receita> r: this.receitas.entrySet()){
            receita = r.getValue();
            for(Ingrediente i: receita.getIngredientes()){
                if(totalIngredientes.contains(i)==false){
                    totalIngredientes.add(i.getNome());
                }
            }
        }
        for(String i: totalIngredientes){
            aux = new TreeSet<String>();
            for(Map.Entry<String, Receita> r: this.receitas.entrySet()){
                receita = r.getValue();
                if(receita.getIngredientes().contains(i)){
                    aux.add(receita.getNome());
                }
            }
            lista.put(i, aux);
        }
        return lista; 
    } 
    
    public void gravaObj(String file, double calorias)throws FileNotFoundException, IOException
   ,ClassNotFoundException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        for(Map.Entry<String, Receita> r: this.receitas.entrySet()){
            if(r.getValue().calorias()>= calorias){
                oos.writeObject(r.getValue());
            }
        }
        oos.flush();
        oos.close();
    }
}
