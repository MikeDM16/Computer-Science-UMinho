package d1team;

import robocode.Robot;
import java.awt.geom.Point2D;


 
public class DistanceThread extends Thread{
    
    private Point2D old_position;
    private Point2D new_position;
    private Double distance;
    private int rate = 1000;
    private int frame_rate = 1000 / rate; 
    private Robot robot;
    private boolean alive;
    
    private boolean error_show;

    public DistanceThread(Robot r) {
        this.robot = r;
        this.alive = true;
        
        this.old_position = new Point2D.Double(r.getX(), r.getY());
        this.distance = 0.0;
        
        this.error_show = true;
    }
    
    /**
     * Apos criar o objeto correr metodo ".Start()"
     */
    @Override
    public void run() {
        while(true) {
            if (this.alive) {
                try {
                    this.move();
                } catch (robocode.exception.DisabledException d) {
                    if (this.error_show) {
                        System.out.println("Erro");
                        this.error_show = false;
                    }
                }

                try {
                    Thread.sleep(this.frame_rate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else break;
        }
    }
    
    /**
     * fim da batalha
     */
    public void kill() {
        this.alive = false;
    }

    public double getDistance() {
        synchronized(this.distance) {
            return this.distance;
        }
    }

    public void move() {
        synchronized(this.distance) {
            this.update_new_position();
            this.update_distance();
            this.update_old_position();
        }
    }
    
    public void update_new_position() {
        this.new_position = new Point2D.Double(this.robot.getX(), this.robot.getY());
    }
    
    public void update_distance() {
        double euclidian = Math.sqrt(Math.pow((this.old_position.getX() - this.new_position.getX()), 2) + Math.pow((this.old_position.getY() - this.new_position.getY()), 2));
        this.distance += euclidian;
    }
    
    public void update_old_position() {
        this.old_position = new Point2D.Double(this.new_position.getX(), this.new_position.getY());
    }
}
