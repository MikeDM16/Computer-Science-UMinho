
/**
 * Escreva a descrição da classe Playlist aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
public class Playlist
{
   //variaveis de instancia: 
   private ArrayList<Faixa> playlist;
   //private int tamanho; 
   
   //Construtores da instancia: 
   public Playlist(){ playlist = new ArrayList<Faixa>(); /*tamanho = 0;*/}
   public Playlist(Faixa f){ playlist = new ArrayList<Faixa>();
       playlist.add(f.clone() ); //tamanho = 1;
   }
   public Playlist clone(){
       Playlist cloneM = new Playlist();
       Faixa adic = new Faixa();
       for(Faixa p: this.playlist){
           adic = p.clone(); 
           cloneM.addFaixa(adic);
       }
       return cloneM;
   }
   
   //metodos da instancia:
   public int numFaixas(){ return (this.playlist.size());  /*return tamanho;*/ }
   public void addFaixa(Faixa f){this.playlist.add(f); /*tamanho++;*/}
   public void removeFaixa(Faixa m){ playlist.remove(m);}
   public void adicionar(List<Faixa> faixas){
       /* 
        for(Faixa f: faixas){
           this.playlist.add(f.clone());
        }*/
       Iterator<Faixa> it; Faixa p = new Faixa(); 
       for(it = faixas.iterator(); it.hasNext();){
           p = it.next().clone();
           this.playlist.add(p);
        }
   }
   public void adicionarF(ArrayList<Faixa> faixas){
       faixas.forEach(f -> this.addFaixa(f.clone()));
    }
   
   public int classificacaoSuperior(Faixa f){
       int c = f.getClassificacao(), res = 0;
       Iterator<Faixa> it ; Faixa p = new Faixa();
       for(it = this.playlist.iterator(); it.hasNext();){
           p = it.next();
           if(p.getClassificacao() >c) {res ++; }
        }
        return res; 
    }
   public int classificacaoSuperiorF(Faixa fa){
       return (int) playlist.stream().filter(f -> f.getClassificacao()>fa.getClassificacao()).count();
       
    }
   
   public boolean duracaoSuperior(double d){
       Iterator<Faixa> it;
       for(it = this.playlist.iterator(); it.hasNext();){
           if(it.next().getDuracao() > d ) {return true;}
        }
        return false; 
    }
   public boolean duracaoSuperiorF(double d){
       return playlist.stream().anyMatch(f -> f.getDuracao()>d);
   }
   
   public List<Faixa> getCopiaFaixas(int n){
      ArrayList<Faixa> novo = new ArrayList<Faixa>();
      
      Iterator<Faixa> it; Faixa p = new Faixa();
      for(it = this.playlist.iterator(); it.hasNext();){
          p = it.next().clone();
          p.setClassificacao(n);
          novo.add(p);
        }
       return novo;
    }
   public List<Faixa> getCopiaFaixasF(int n){
       /*ArrayList<Faixa> novo = new ArrayList<Faixa>();
       //playlist.forEach(f -> novo.add( f.clone()) );
       novo.forEach(f -> f.setClassificacao(n) );
       playlist.stream().map(Faixa::clone);
       //novo.stream().map( setClassificacao(n) );
       return novo;*/
       
       return playlist.stream().map(f -> {return f.clone();}).map(f->{f.setClassificacao(n); return f;}).collect(Collectors.toList());
    }
   
   public double duracaoTotal(){
    double t = 0.0;
    Iterator<Faixa> it;
    for(it = this.playlist.iterator(); it.hasNext();){
        t += it.next().getDuracao();
    }
    return t; 
    }
   public double duracaoTotalF(){
       double r = 0.0; 
       r = playlist.stream().mapToDouble(Faixa::getDuracao).sum();
       return r;
   }
   
   public void removeFaixas(String autor){
       Iterator<Faixa> it; Faixa p = new Faixa();
       for(it = this.playlist.iterator(); it.hasNext();){
           p = it.next();
           if(p.getAutor()==autor){this.playlist.remove(p); }
        }
    }
   public void removeFaixasF(String autor){
       List<Faixa> arem = new ArrayList<Faixa>();
       arem = this.playlist.stream().filter(f-> (f.getAutor().equals(autor))).collect(Collectors.toList());
       arem.forEach(f -> this.playlist.remove(f));
   }
   
   public List<Faixa> getFaixas(){
       ArrayList<Faixa> novo = new ArrayList<Faixa>();
       Iterator<Faixa> it; Faixa p = new Faixa();
       for(it = this.playlist.iterator(); it.hasNext();){
           p = it.next();
           novo.add( p.clone() );
        }
       return novo; 
    }

}
