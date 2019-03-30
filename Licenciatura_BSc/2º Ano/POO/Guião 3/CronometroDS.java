
/**
 * Escreva a descrição da classe CronometroDS aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
public class CronometroDS
{
    //variavies da instancia
    private GregorianCalendar IniTime, F1Time, S2Time;
    private int count = 0;
    
    //Construtores da classe
    public CronometroDS(){
        IniTime = new GregorianCalendar();
        count = 0;
    }
    
    //metodos da classe
    
    public void parar(){
        switch (count){
            case 0: F1Time = new GregorianCalendar();
                        count++;
                        break;
            case 1: S2Time = new GregorianCalendar();
                        break;
        }
    }
    public String difTime(GregorianCalendar time0, GregorianCalendar time1){
        DecimalFormat df = new DecimalFormat("0.0");
        double tempoMilli = time1.getTimeInMillis()- time0.getTimeInMillis();
        double tempoMin = tempoMilli / (60*1000) ; 
        int minutos = (int) tempoMin; // parte inteira tempo total em minutos
        
        double tempoSeg = tempoMin - minutos;// parte decimal tempo total em minutos
        tempoSeg *= 60; // converter parte decimal do tempo (MIN) em segundos
        int segundos = (int) tempoSeg; // parte inteira dos segundos
        
        tempoMilli = tempoSeg - segundos; //parte decimal do tempo em segundos;
        tempoMilli *= 1000; // converter parte decimal tempo (SEG) em milisegundos
        int milisegundos = (int) tempoMilli;// parte inteira dos milisegundos
        
        String min, seg, miliseg;
        min = Integer.toString(minutos); seg = Integer.toString(segundos); 
        miliseg = Integer.toString(milisegundos);
        //System.out.println(min+":"+seg+"'"+miliseg);
        return (min+":"+seg+"'"+miliseg);
    }
    public String primeiraParagem(){
        return (difTime(IniTime, F1Time));
    }
    public String segundaParagem(){
        return (difTime(IniTime, S2Time));
    }
    public String tempoSplit(){
        return (difTime(F1Time, S2Time));
    }
}

    /*
       double minutos = tempo / (1000*60); //passar tempo para minutos;
        System.out.println("minutos double "+minutos);
        int min = (int) minutos; //parte inteira dos minutos;
        //System.out.println("minutos inteiro "+min);
        double segundos = minutos - min;// parte decimal dos minutos
        segundos *=60; //passar parte decimal dos minutos para segundos
        System.out.println("segundos double "+segundos);
        int seg = (int) segundos; // parte inteira em segundos
        //System.out.println("minutos int "+seg);
        double miliseg = seg - segundos; //parte decimal segundos
        miliseg *=100; //passar parte decimal segundos para milisegundos
        String s = Double.toString(minutos)+":"+ Double.toString(segundos)+"'"+Double.toString(miliseg);
        return s;
        */
