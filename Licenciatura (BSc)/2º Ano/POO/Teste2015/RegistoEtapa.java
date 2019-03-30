
/**
 * Write a description of class RegistoEtapa here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.joining;

public class RegistoEtapa
{
    private GregorianCalendar inicio, fim ;
    
    public double difTempo(){
        return (double) 
        ( (fim.getTimeInMillis() - inicio.getTimeInMillis()) / (60*60*1000) );
    }
    
    public GregorianCalendar getInicio(){ return this.inicio; }
    public GregorianCalendar getfim(){ return this.fim; }
}
