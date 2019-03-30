package servidor;


public class Viagem {

    protected String usernameDriver;
    protected String usernamePassenger;
    protected Coordenadas driverOrigin;
    protected Coordenadas departure;
    protected Coordenadas arrival;

    public Viagem(){}

    public Viagem(String usernameDriver, String usernamePassenger, Coordenadas driverOrigin, Coordenadas departure, Coordenadas arrival) {
        this.usernameDriver = usernameDriver;
        this.usernamePassenger = usernamePassenger;
        this.driverOrigin = driverOrigin;
        this.departure = departure;
        this.arrival = arrival;
    }

    public Viagem(Viagem v){
        this(v.getUsernameDriver(),v.getUsernamePassenger(), v.getDriverOrigin(), v.getDeparture(), v.getArrival());
    }

    public String getUsernamePassenger() {
        return usernamePassenger;
    }

    public void setUsernamePassenger(String usernamePassenger) {
        this.usernamePassenger = usernamePassenger;
    }

    public Coordenadas getDriverOrigin() {
        return driverOrigin.clone();
    }

    public void setDriverOrigin(Coordenadas driverOrigin) {
        this.driverOrigin = driverOrigin.clone();
    }


    public String getUsernameDriver() {
        return usernameDriver;
    }

    public void setUsernameDriver(String usernameDriver) {
        this.usernameDriver = usernameDriver;
    }

    public Coordenadas getDeparture() {
        return departure.clone();
    }

    public void setDeparture(Coordenadas departure) {
        this.departure = departure.clone();
    }

    public Coordenadas getArrival() {
        return arrival.clone();
    }

    public void setArrival(Coordenadas arrival) {
        this.arrival = arrival.clone();
    }

    public int getTripDuration(Double v){
        return this.getDeparture().getTravelDuration(this.getArrival(),v);
    }

    public int getDurationDriverToDeparture(Double v){
        return this.getDeparture().getTravelDuration(this.getArrival(),v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Viagem viagem = (Viagem) o;

        if (!usernameDriver.equals(viagem.usernameDriver)) return false;
        if (!usernamePassenger.equals(viagem.usernamePassenger)) return false;
        if (!driverOrigin.equals(viagem.driverOrigin)) return false;
        if (!departure.equals(viagem.departure)) return false;
        return arrival.equals(viagem.arrival);

    }

    @Override
    public int hashCode() {
        int result = usernameDriver.hashCode();
        result = 31 * result + usernamePassenger.hashCode();
        result = 31 * result + driverOrigin.hashCode();
        result = 31 * result + departure.hashCode();
        result = 31 * result + arrival.hashCode();
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Viagem{");
        sb.append("usernameDriver='").append(usernameDriver).append('\'');
        sb.append(", usernamePassenger='").append(usernamePassenger).append('\'');
        sb.append(", driverOrigin=").append(driverOrigin.neatToString());
        sb.append(", departure=").append(departure.neatToString());
        sb.append(", arrival=").append(arrival.neatToString());
        sb.append('}');
        return sb.toString();
    }

    public Viagem clone(){
        return new Viagem(this);
    }

}
