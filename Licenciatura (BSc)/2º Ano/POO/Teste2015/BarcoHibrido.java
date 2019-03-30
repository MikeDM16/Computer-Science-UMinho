
/**
 * Write a description of class BarcoHibrido here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class BarcoHibrido extends Barco 
{
    /*               kWh          kW     horas     */
    private double capacidade, potencia, autonomia; 
    
    public BarcoHibrido(String id, double m, String cat, double aut, Pessoa p, Set<Pessoa> trip,
    double capacidade, double potencia, double autonomia){
        super(id,m,cat,aut,p,trip);
        this.capacidade = capacidade;
        this.potencia = potencia;
        this.autonomia = autonomia; 
    }
    
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(super.toString());
        s.append("capacidade kWh: " + this.capacidade);
        s.append("autonomia horas: " + this.autonomia);
        s.append("potencia kW: " + this.potencia);
        return s.toString();
    }
    
    public double getAutonomia(){
        return ( (double) this.autonomia + super.getAutonomia() ); 
    }
    
}
