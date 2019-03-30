package d1team;

import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.RoundEndedEvent;
import robocode.*;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;
import java.awt.geom.Point2D;
import java.text.*;
import java.awt.Color;
import java.io.*;


public class Alfredo extends AdvancedRobot {

    private LinkedHashMap<String, Meco> obstaculos;
    private ArrayList<Meco> caminho;
    private double coordX=30;
    private double coordY=30;
    private DistanceThread counter;
	private static double racio1;
	private static double racio2;
	private static int nrounds;

    public void run() {

        caminho = new ArrayList<Meco>();
        
        setAdjustRadarForRobotTurn(true);
        pimpTheRobot();
        esperar(100);

        vaiPara(coordX, coordY);
		this.counter = new DistanceThread(this);
        this.counter.start();
        
        scan();
        calcularCaminho();
        resultados();
    }

    public void pimpTheRobot(){
        setBodyColor(new Color(52, 73, 94));
        setGunColor(new Color(52, 73, 94));
        setRadarColor(new Color(52, 73, 94));
        setBulletColor(new Color(52, 73, 94));
        setScanColor(new Color(52, 73, 94));
    }

    public void scan(){
        obstaculos = new LinkedHashMap<String, Meco>();
        turnRadarRight(360);
        obstaculos.put("PFinal", new Meco((int) coordX, (int) coordY, "PFinal"));
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getVelocity() == 0) {
            double angulo = Math.toRadians((getHeading() + e.getBearing() % 360));
            double pX = (getX() + Math.sin(angulo) * e.getDistance());
            double pY = (getY() + Math.cos(angulo) * e.getDistance());
            Meco o = new Meco((int) Math.round(pX), (int) Math.round(pY), e.getName());
            obstaculos.put(e.getName(), o);
        }
    }

    private void resultados() {   
        ArrayList<Point2D> aux = new ArrayList<Point2D>();
        for(Meco m : this.obstaculos.values()){
            aux.add(new Point2D.Double(m.getX(), m.getY()));
        } 
        
        double perimetroTriangulo = 0;
        double perimetroLosango = 0;
        double perimetroLosangoObs = 0;
        counter.kill();
        double distancia = counter.getDistance();

        if(aux.size() == 4){
            perimetroTriangulo += aux.get(0).distance( aux.get(1) );
            perimetroTriangulo += aux.get(1).distance( aux.get(2) );
            perimetroTriangulo += aux.get(2).distance( aux.get(0) );
        }
        
        if(aux.size() == 4){
            perimetroLosango +=  aux.get(3).distance( aux.get(0) );
            perimetroLosango += aux.get(0).distance( aux.get(1) );
            perimetroLosango += aux.get(1).distance( aux.get(2) );
            perimetroLosango += aux.get(2).distance( aux.get(3) );
        }
        if(aux.size() == 5){
            perimetroLosangoObs += aux.get(0).distance( aux.get(1) );
            perimetroLosangoObs += aux.get(1).distance( aux.get(2) );
            perimetroLosangoObs += aux.get(2).distance( aux.get(3) );
            perimetroLosangoObs += aux.get(3).distance( aux.get(0) );
        }        
        
        DecimalFormat df = new DecimalFormat("#.##");
        if(aux.size() == 4){
            System.out.println("============================================================");
            System.out.println("Distância percorrida: " +  df.format(distancia));
            System.out.println("Perímetro triângulo: " + df.format(perimetroTriangulo));
            System.out.println("Perímetro losango: " + df.format(perimetroLosango));
            System.out.println("Rácio 1 (triângulo): " + df.format(perimetroTriangulo/distancia));
            System.out.println("Rácio 2 (losango): " + df.format(perimetroLosango/distancia));
			racio1+=perimetroTriangulo/distancia;
			racio2+=perimetroLosango/distancia;
			nrounds++;
            System.out.println("============================================================");
        }
        else{
            System.out.println("============================================================");
            System.out.println("Distância percorrida: " +  df.format(distancia));
            if(aux.size()==5){
                System.out.println("Perímetro entre obstáculos: " + df.format(perimetroLosangoObs));
                System.out.println("Rácio: " + df.format(perimetroLosangoObs/distancia));
            }
            System.out.println("============================================================");
        }
   }

    private void vaiPara(double x, double y) {
        boolean chegou = false;
        while (!chegou) {
            goTo(x, y);
            execute();
            if (getX() == x && getY() == y) {
                chegou = true;
                stop();
            }
        }
    }

    private void vaiParaMargem(double x, double y) {
        boolean chegou = false;
        while (!chegou) {
            goTo(x, y);
            execute();
            if (Math.abs(x - getX()) < 0.001 && Math.abs(y - getY()) < 0.001) {
                chegou = true;
                stop();
            }
        }
    }

    private void goTo(double x, double y) {
        double dx = x - getX();
        double dy = y - getY();
        double angleToTarget = Math.atan2(dx, dy);
        double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());
        double distance = Math.hypot(dx, dy);
        double turnAngle = Math.atan(Math.tan(targetAngle));
        setTurnRightRadians(turnAngle);
        if (targetAngle == turnAngle) {
            setAhead(distance);
        } else {
            setBack(distance);
        }
    }

    private void esperar(int turns) {
        for (int i = 0; i < turns; i++) {
            doNothing();
        }
    }

    private void calcularCaminho() {

        ArrayList<Meco> ordenado = new ArrayList<Meco>(obstaculos.values());
        ordenado.sort(new ClockWiseComparator((int) coordX, (int) coordY));
        
        for (int i = 0; i < obstaculos.size() - 1; i++) {
            Meco obstaculo = ordenado.get(i);
            Meco proximoObstaculo = ordenado.get(i + 1);
           
            int diagonal = calculaDiagonal(obstaculo, proximoObstaculo);

            if(diagonal==1){
                vaiParaMargem(obstaculo.getX() - 37, obstaculo.getY() + 37);
                virar(90);
                if (proximoObstaculo.getY() < obstaculo.getY() && proximoObstaculo.getX() < getX()) {
                    ajustarDirecao(180);
                }
            }
            if(diagonal==2){
                vaiParaMargem(obstaculo.getX() - 37, obstaculo.getY() + 37);
            }
            if(diagonal==3){
                vaiParaMargem(obstaculo.getX() + 37, obstaculo.getY() - 37);
                vaiParaMargem(obstaculo.getX() - 37, obstaculo.getY() - 37);
            }
            if(diagonal==4){
                if (getY() - 38 < obstaculo.getY()) {
                    vaiParaMargem(obstaculo.getX() - 37, obstaculo.getY() + 37);
                    virar(90);
                    virar(180);
                } 
                else {
                    vaiParaMargem(obstaculo.getX() + 37, obstaculo.getY() + 37);
                    virar(180);
                }
            }
        }    
        ajustarDirecao(270);
        vaiPara(coordX, coordY);
    }

    private int calculaDiagonal(Meco obstaculo, Meco proximoObstaculo) {

        if (proximoObstaculo.getX() < obstaculo.getX() && proximoObstaculo.getY() >= obstaculo.getY()) {
            return 3;
        }
        if (proximoObstaculo.getX() < obstaculo.getX()) {
            return 4;
        }
        if (obstaculo.getY() <= proximoObstaculo.getY()) {
            return 2;
        } else {
            return 1;
        }
    }

    private void virar(int direcao) {
        boolean done = false;
        while (!done) {
            if (getHeading() > direcao) {
                turnLeft(getHeading() - direcao);
            } else {
                turnRight(direcao - getHeading());
            }
            if (getHeading() == direcao) {
                done = true;
            }
        }
        ahead(74);
    }

    private void ajustarDirecao(int direcao) {
        boolean done = false;
        while (!done) {
            if (getHeading() > direcao) {
                turnLeft(getHeading() - direcao);
            } else {
                turnRight(direcao - getHeading());
            }
            if (getHeading() == direcao) {
                done = true;
            }
        }

    }

    /*public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
            turnRight(e.getBearing());
            back(10);
        } else {
            turnRight(20);
            ahead(10);
        }
    }*/

    public void onHitRobot (HitRobotEvent e) {
        turnRight(45);
        ahead(-200);
    }
	
public void onBattleEnded(BattleEndedEvent event){

   PrintStream w = null;
   DecimalFormat df = new DecimalFormat("#.##");
		try {
			w = new PrintStream(new RobocodeFileOutputStream(getDataFile("rockquad.txt")));

			w.println("Rácio1: " + df.format(racio1/nrounds) + " Rácio2: "+ df.format(racio2/nrounds));

			if (w.checkError()) {
				out.println("I could not write the count!");
			}
		} catch (IOException e) {
			out.println("IOException trying to write: ");
			e.printStackTrace(out);
		} finally {
			if (w != null) {
				w.close();
			}
		}
}

}
