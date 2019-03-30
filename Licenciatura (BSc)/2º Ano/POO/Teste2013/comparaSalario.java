
/**
 * Write a description of class comparaSalario here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Comparator;
public class comparaSalario implements Comparator<Empregado>
{
   public int compare(Empregado e1, Empregado e2){
       return (int) (e1.getSalDia() - e2.getSalDia());
    } 
}
