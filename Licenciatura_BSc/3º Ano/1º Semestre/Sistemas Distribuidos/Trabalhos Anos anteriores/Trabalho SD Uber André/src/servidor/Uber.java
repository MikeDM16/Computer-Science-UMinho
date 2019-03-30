package servidor;

import exceptions.ExistentUserException;
import exceptions.InexistentUserException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Uber {

    private class UtilizadorUber extends Utilizador{
        public Condition noPassenger;

        public UtilizadorUber() {
            super();
            this.noPassenger = lockUber.newCondition();
        }

        public UtilizadorUber(String name, String username, String password) {
            super(name, username, password);
            this.noPassenger = lockUber.newCondition();
        }

        public UtilizadorUber(String name, String username, String password, String licensePlate, String model) {
            super(name, username, password, licensePlate, model);
            this.noPassenger = lockUber.newCondition();
        }

        public UtilizadorUber(Utilizador u) {
            super(u);
            this.noPassenger = lockUber.newCondition();
        }

        public Utilizador toRegularUser(){
            return new Utilizador(name, username, password, licensePlate, model);
        }
    }

    private class TripUber extends Viagem{
        int id;
        public Condition driverNotAtDeparture;
        public Condition passengerDidntConfirm;
        public Condition didntArriveAtDestination;


        public TripUber(int id) {
            super();
            this.driverNotAtDeparture =  lockUber.newCondition();
            this.passengerDidntConfirm = lockUber.newCondition();
            this.didntArriveAtDestination = lockUber.newCondition();
            this.id = id;
        }

        public TripUber(int id, String usernameDriver, String usernamePassenger, Coordenadas driverOrigin, Coordenadas departure, Coordenadas arrival) {
            super(usernameDriver, usernamePassenger, driverOrigin, departure, arrival);
            this.driverNotAtDeparture =  lockUber.newCondition();
            this.passengerDidntConfirm = lockUber.newCondition();
            this.didntArriveAtDestination = lockUber.newCondition();
            this.id = id;
        }

        public TripUber(Viagem v, int id) {
            super(v);
            this.driverNotAtDeparture =  lockUber.newCondition();
            this.passengerDidntConfirm = lockUber.newCondition();
            this.didntArriveAtDestination = lockUber.newCondition();
            this.id = id;
        }

        public Viagem toRegularTrip(){
            return new Viagem(usernameDriver, usernamePassenger, driverOrigin, departure, arrival);
        }

        public int getId() {
            return id;
        }
    }

    private int contadorIdTrip = 1;
    private Lock lockUber = new ReentrantLock();
    private Condition semCondutor = this.lockUber.newCondition();

    private Map<String, UtilizadorUber> usersDB = new HashMap<>();
    private Map<String, Coordenadas> driversPositions = new HashMap<>();
    private Map<String, TripUber> tripsAssigned = new HashMap<>();
    private Map<Integer, TripUber> tripsDB = new HashMap<>();

    public Uber() {
    }

    public void registaUtilizador(Utilizador u) throws ExistentUserException {
        lockUber.lock();
        try{
            if(usersDB.containsKey(u.getUsername()))
                throw new ExistentUserException("User " + u.getUsername() + " is already registered!");
            else
                usersDB.put(u.getUsername(), new UtilizadorUber(u));

        }finally {
            lockUber.unlock();
        }

    }


    public Utilizador getUtilizador(String username){
        Utilizador result;
        lockUber.lock();
        result = usersDB.get(username).clone();
        lockUber.unlock();
        return result;
    }

    public boolean utilizadorExiste(String username) {
        boolean result;
        lockUber.lock();
        result = usersDB.containsKey(username);
        lockUber.unlock();
        return result;
    }


    public boolean changeUsername(String username, String newUsername) {
        boolean changeOK = false;
        lockUber.lock();
        if (!this.usersDB.containsKey(newUsername)) {
            this.usersDB.put(newUsername, usersDB.remove(username));
            changeOK = true;
        }
        lockUber.unlock();
        return changeOK;
    }

    public boolean changeName(String username, String newName) {
        boolean changeOK = false;
        lockUber.lock();
        if (utilizadorExiste(username)) {
            this.usersDB.get(username).setName(newName);
            changeOK = true;
        }
        lockUber.unlock();
        return changeOK;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        boolean changeOK = false;
        lockUber.lock();
        if (this.usersDB.get(username).getPassword().equals(oldPassword)) {
            this.usersDB.get(username).setPassword(newPassword);
            changeOK = true;
        }
        lockUber.unlock();
        return changeOK;
    }

    public boolean changeLicensePlate(String username, String licensePlate) {
        boolean changeOK = false;
        lockUber.lock();
        if (utilizadorExiste(username)) {
            this.usersDB.get(username).setLicensePlate(licensePlate);
            changeOK = true;
        }
        lockUber.unlock();
        return changeOK;
    }

    public boolean changeCarModel(String username, String model) {
        boolean changeOK = false;
        lockUber.lock();
        if (utilizadorExiste(username)) {
            this.usersDB.get(username).setModel(model);
            changeOK = true;
        }
        lockUber.unlock();
        return changeOK;
    }

    public boolean passwordCorrecta (String username, String password) throws InexistentUserException {
        boolean result;
        lockUber.lock();
        if(usersDB.containsKey(username)){
            Utilizador u = usersDB.get(username);
            result = u.getPassword().equals(password);
        }else{
            result = false;
        }
        lockUber.unlock();
        return result;
    }

    public int requestTravel(String usernamePassenger, Coordenadas departure, Coordenadas arrival) throws InterruptedException {
        int idTrip;
        lockUber.lock();
        while(driversPositions.isEmpty())
            semCondutor.await();

        idTrip = this.contadorIdTrip++;
        TripUber trip = new TripUber(idTrip);
        tripsDB.put(idTrip,trip);
        trip.setUsernamePassenger(usernamePassenger);
        trip.setDeparture(departure.clone());
        trip.setArrival(arrival.clone());
        String driver = getNearestDriver(trip.getDeparture());
        trip.setUsernameDriver(driver);
        trip.setDriverOrigin(driversPositions.get(driver));
        tripsAssigned.put(driver, trip);
        usersDB.get(driver).noPassenger.signal();
        return idTrip;
    }

    public int announce(String driverUsername, Coordenadas position) throws InterruptedException {
        UtilizadorUber driverUser;
        TripUber trip;
        int idTrip;
        lockUber.lock();
        this.driversPositions.put(driverUsername, position);
        semCondutor.signal();
        driverUser = usersDB.get(driverUsername);
        while(tripsAssigned.get(driverUsername)==null)
            driverUser.noPassenger.await();
        driversPositions.remove(driverUsername);
        trip = tripsAssigned.get(driverUsername);
        tripsAssigned.remove(driverUsername);
        idTrip = trip.getId();
        lockUber.unlock();
        return idTrip;
    }

    /*
    PASSAGEIRO
     */
    public void esperaCondutor(int idTrip){
        TripUber trip = tripsDB.get(idTrip);
        try {
            trip.driverNotAtDeparture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lockUber.unlock();
        }
    }

    public void confirmDeparture(int idTrip){
        lockUber.lock();
        TripUber trip = tripsDB.get(idTrip);
        trip.passengerDidntConfirm.signalAll();

        try {
            trip.didntArriveAtDestination.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lockUber.unlock();
        }
    }

    /*
    CONDUTOR
     */
    public void arrivedAtDeparture(int idTrip){
        lockUber.lock();
        TripUber trip = tripsDB.get(idTrip);
        trip.driverNotAtDeparture.signalAll();
        try {
            trip.passengerDidntConfirm.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lockUber.unlock();
        }
    }

    public void endTravel(int idTrip){
        lockUber.lock();
        TripUber trip = tripsDB.get(idTrip);
        trip.didntArriveAtDestination.signalAll();
        lockUber.unlock();

    }

    public Viagem getTripFromId(int id){
        Viagem result;
        lockUber.lock();
        result = tripsDB.get(id).toRegularTrip();
        lockUber.unlock();
        return result;
    }

    public Coordenadas getDriverCoords(String username){
        Coordenadas result;
        lockUber.lock();
        result = driversPositions.get(username).clone();
        lockUber.unlock();
        return result;
    }

    private String getNearestDriver(Coordenadas coordinates){
        double minDistance = Double.MAX_VALUE;
        double currentDistance;
        String nearestDriver = null;

        for(Map.Entry<String,Coordenadas> entry: driversPositions.entrySet()) {
            currentDistance = coordinates.manhattanDistance(entry.getValue());
            if (currentDistance<minDistance) {
                minDistance = currentDistance;
                nearestDriver = entry.getKey();
            }
        }

        return nearestDriver;
    }

}
