
/**
 * Write a description of class Main_Barreira here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main_Barreira
{
    public static void main(){
        Barreira b = new Barreira(5);
        int n = 5;
        Thread[] t = new Thread[n];
        for(int i=0; i!=n; i++){
                new Thread(new TestaBarreira(b, i)).start();
        }
    }
}
