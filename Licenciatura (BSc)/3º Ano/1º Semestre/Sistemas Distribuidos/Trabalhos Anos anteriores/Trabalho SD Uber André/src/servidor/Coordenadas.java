package servidor;


public class Coordenadas{

    private double x;
    private double y;

    public Coordenadas(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Coordenadas(Coordenadas c){
        this.x = c.x;
        this.y = c.y;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double manhattanDistance(Coordenadas to){
        return Math.abs(this.x - to.getX()) + Math.abs(this.y -to.getY());
    }

    public int getTravelDuration(Coordenadas c, Double v){
       return (int)(manhattanDistance(c)/v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordenadas that = (Coordenadas) o;

        if (this.x != that.getX()) return false;
        return this.y == that.getY();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getX());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coordenadas{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    public String neatToString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("(").append(x).append(",").append(y).append(')');
        return sb.toString();
    }

    public Coordenadas clone(){
        return new Coordenadas(this);
    }

}
