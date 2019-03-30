package d1team;

import java.util.Comparator;

public class ClockWiseComparator implements Comparator<Meco>  {
    
    private int pX;
    private int pY;
    
    public ClockWiseComparator(int x,int y) {
        this.pX=x;
        this.pY=y;
    }

    public int compare(Meco o1, Meco o2) {
        double angle1 = Math.atan2(o1.getY() - pY, o1.getX() - pX);
        double angle2 = Math.atan2(o2.getY() - pY, o2.getX() - pX);
        if(angle1 < angle2) return 1;
        else if (angle2 < angle1) return -1;
        return 0;
    }
    
}