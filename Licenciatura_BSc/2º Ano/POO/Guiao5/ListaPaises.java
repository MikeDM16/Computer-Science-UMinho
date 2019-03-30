
/**
 * Write a description of class ListaPaises here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.ArrayList;
import java.util.Iterator;
public class ListaPaises
{
    //variaveis de instancia
    private ArrayList<FichaPais> listapaises;
    
    //construtores da classe
    public ListaPaises(){
        listapaises = new ArrayList<FichaPais>();
    }
    public ListaPaises clone(){
        ListaPaises clone = new ListaPaises();
        Iterator<FichaPais> it; FichaPais p = new FichaPais();
        for(it = this.listapaises.iterator(); it.hasNext();){
            p = it.next().clone();
            clone.getLista().add(p);
        }
        return clone;
    }
    //metodos da instancia: 
    public ArrayList<FichaPais> getLista(){return this.listapaises; }
    public void adicionar(String n, String c, double p){
        FichaPais fp = new FichaPais(n,c,p);
        listapaises.add(fp);
    }
    public int numPaises(){return listapaises.size(); }
    
    public int numPaises(String c){
        int res =0;
        Iterator<FichaPais> it; FichaPais p = new FichaPais();
        for(it = this.listapaises.iterator(); it.hasNext();){
            if(it.next().getContinente()==c){res++;}
        }
        return res;
    }
    //public int numPaisesF(String c){}
    
    public FichaPais getFicha(String nome){
        FichaPais p = new FichaPais();
        Iterator<FichaPais> it;
        for(it = listapaises.iterator(); it.hasNext();){
            p = it.next().clone();
            if(p.getNome()==nome){return p;}
        }
        return p;
    }
    //public FichaPais getFichaF(String nome){}
    
    public ArrayList<String> nomesPaises(double valor){
        ArrayList<String> s = new ArrayList<String>();
        String sn = new String();
        Iterator<FichaPais> it; FichaPais p = new FichaPais();
        for(it = listapaises.iterator(); it.hasNext();){
            p = it.next().clone();
            if(p.getPopulacao() > valor ) {
                sn = p.getNome();
                s.add(sn);
            }
        }
        return s; 
    }
    //public ArrayList<String> nomesPaisesF(double valor){}
    
    public ArrayList<String> nomesContinentes(double valor){
        ArrayList<String> c = new ArrayList<String>();
        String cn = new String();
        Iterator<FichaPais> it; FichaPais p = new FichaPais();
        for(it = listapaises.iterator(); it.hasNext();){
            p = it.next().clone();
            if(p.getPopulacao() > valor ) {
                cn = p.getContinente();
                if(c.contains(cn)==false){c.add(cn);}
                
            }
        }
        return c; 
    }
    //public ArrayList<String> nomesContinentesF(double valor){}
    
    public double somatorio(String c){
        double res = 0.0;
        Iterator<FichaPais> it; FichaPais p = new FichaPais();
        for(it = listapaises.iterator(); it.hasNext();){
            p = it.next().clone();
            if(p.getContinente()==c){ res += p.getPopulacao();}
        }
        return res; 
    }
    //public double somatorioF(String c){}
    
    
}
