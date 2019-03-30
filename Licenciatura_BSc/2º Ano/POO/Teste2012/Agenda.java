
/**
 * Write a description of class Agenda here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class Agenda
{
   private String titular; // nome do dono da agenda 
   private ArrayList<Tarefa> tarefas; //lista das tarefas agendadas
   
   public void addTarefa(Tarefa t){
      if(tarefas.contains(t)==false){
           tarefas.add(t);
      }
   }
   
   public Iterator<Tarefa> tarefasActivas(Comparator<Tarefa> ct){
       Iterator<Tarefa> aux;
       for(Tarefa t: this.tarefas){
           if(t.getTerminada()==false){aux.add(t);}
        }
       return aux; 
    }
   
}
