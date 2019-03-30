/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

/**
 *
 * @author joaosilva
 */
public class Local {
    private int x;
    private int y;

    public Local(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
     public Local(String s){
        String[] spv = s.split("(,)");
        this.x= Integer.parseInt(spv[0].replace("(", ""));
        this.y= Integer.parseInt(spv[1].replace(")", ""));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ')';
    }
   
    
    public int distancia(Local l){
        return (Math.abs(this.getX()-l.getX()) + Math.abs(this.getY()-l.getY()));
    }
    
}
