
/**
 * Write a description of class MultimediaInterface here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
import java.lang.*;
import java.util.*;
import java.io.*;

public class MultimediaInterface
{
   /*Variaveis de classe*/
   private String nome;
   private Map<String, Multimédia> biblioteca;
   
   /*Construtores da classe*/
   public MultimediaInterface(){
       this.nome="Sem Nome"; 
       biblioteca = new TreeMap<String, Multimédia>();
    }
   public MultimediaInterface(String nome){
     this.nome = nome; biblioteca = new TreeMap<String, Multimédia>();
    }
   /*   Adicionar um objeto Multimédia */
   public void adicionaMultimedia(){
       Scanner scanner = new Scanner(System.in);
       int escolha;
       
       System.out.println("Escolha a seguinte opçao para adicionar: ");
       System.out.println("(1) Album       (2) Filme");
       escolha = scanner.nextInt();
       switch (escolha){
           case 1: adicionaAlbum();
                   break;
           case 2: adicionaFilme();
                   break;
           default: break; 
        }
   }
   private void adicionaAlbum(){
       Scanner scanner = new Scanner(System.in); String titulo, nome, comentario;
       int faixas, r; double duracao; boolean p;
       System.out.println("Digite o titulo do album: "); titulo = scanner.next();
       System.out.println("Digite o nome do artista/banda: "); nome = scanner.next();
       System.out.println("Digite o numero de musicas do album: "); faixas = scanner.nextInt();
       System.out.println("Digite a duracao do album: "); duracao = scanner.nextDouble();
       System.out.println("Possui este album? (0)Sim (1)Não"); r = scanner.nextInt();
       if(r==0){p = true;} else{ p=false; }
       System.out.println("Digite o seu comentário sobre o album"); comentario = scanner.next();
       /*Considero o titulo do album como o seu identificador unico*/
       Albuns a = new Albuns(titulo, titulo, nome, faixas, duracao, p, comentario);
       this.biblioteca.put(titulo, a);
   }
   private void adicionaFilme(){
       Scanner scanner = new Scanner(System.in); String titulo, realizador, tempo, comentario;
       double duracao; int r = 0; ArrayList<String> atores = new ArrayList<String>();
       boolean p;
       System.out.println("Digite o titulo do filme: "); titulo = scanner.next();
       System.out.println("Digite o nome do realizador do filme: "); realizador = scanner.next();
       System.out.println("Digite a duração total do filme: "); duracao = scanner.nextDouble();
       while(r==0){
           System.out.println("Digite um atore do filme: "); atores.add(scanner.next());
           System.out.println("Deseja adicionar mais atores: (0)Sim  (1)Não  ?");
           r = scanner.nextInt();
       }
       System.out.println("Possui este filme? (0)Sim (1)Não"); r = scanner.nextInt();
       if(r==0){p = true;} else{ p=false; }
       System.out.println("Digite o seu comentário sobre o filme"); comentario = scanner.next();
       Filmes m = new Filmes(titulo, titulo, realizador, duracao, atores, p, comentario);
       /*Considero o titulo do file como o seu identificador unico*/
       this.biblioteca.put(titulo, m);
   }
   
   public Multimédia procuraMultimedia(String nome){
       Multimédia m = null;
       if(this.biblioteca.containsKey(nome)==true){
              m = this.biblioteca.get(nome).clone();
       }
       return m; 
   }
   
   public List<String> listAtor(String nome){
       List<String> filmes = new ArrayList<String>();
       Filmes f = null;
       for(Map.Entry<String, Multimédia> m: this.biblioteca.entrySet()){
           if(m.getValue() instanceof Filmes){
               f = (Filmes) m.getValue().clone();
               if(f.getAtores().contains(nome)){filmes.add(f.getTitulo());}
            }
        }
       return filmes;
   }
   
   public void gravaObj(String file)throws FileNotFoundException, IOException{
     ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
     oos.writeObject(this); oos.flush(); oos.close();
    }
   public static MultimediaInterface leObje(String file)throws FileNotFoundException, IOException
   ,ClassNotFoundException{
       ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
       MultimediaInterface m = (MultimediaInterface) ois.readObject();
       ois.close(); return m; 
    }
   public void log(String f, boolean ap) throws IOException {
       FileWriter fw = new FileWriter(f, ap);
       fw.write("\n-------------- Log ----------------- \n");
       fw.write(this.toString());
       fw.write("\n-------------- Log ----------------- \n");
       fw.flush(); fw.close();
    }
}
