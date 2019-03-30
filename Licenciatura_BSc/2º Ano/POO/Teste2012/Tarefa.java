
/**
 * Write a description of class Tarefa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.*;
public class Tarefa
{
       private String descricao; 
       // descric¸a~o da tarefa 
       private float prioridade; // prioridade e´ um valor entre 0..1, sendo 1 a prioridade ma´xima 
       private ArrayList<String> participantes; //lista dos nomes dos envolvidos na tarefa 
       private GregorianCalendar inicio; //data e hora do inı´cio da tarefa 
       private GregorianCalendar fim; //data e hora do te´rmino previsto da tarefa
       private boolean terminada; //tarefa concluı´da

       
       public boolean ativa(){
           boolean p = false; GregorianCalendar atual = new GregorianCalendar();
           int horaAt = atual.get(atual.HOUR), minAt = atual.get(atual.MINUTE);
           int hora = fim.get(fim.HOUR), min = fim.get(fim.MINUTE);
           if(horaAt==hora && minAt == min){p = true;}
           return p; 
       }
       
       public boolean getTerminada(){return this.terminada; }
}
