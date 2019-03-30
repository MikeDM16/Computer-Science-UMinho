package servidor;

import exceptions.ExistentUserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RunnerServidor extends Thread {

    InputStreamReader isReader;
    private Uber ub;
    private BufferedReader in;
    private PrintWriter out;
    private int threadID;

    public RunnerServidor(Uber ubS, Socket socket, int threadID) throws InterruptedException, IOException {
        this.ub = ubS;
        this.isReader = new InputStreamReader(socket.getInputStream());
        this.in = new BufferedReader(isReader);
        this.out = new PrintWriter(socket.getOutputStream());
        this.threadID = threadID;
    }


    public int getThreadID() {
        return threadID;
    }


    public void run(){
        String line=null;

        boolean finished = false;

        while(!finished) {
            try {
                line = in.readLine();
            } catch (Exception e) {
                line = null;
                System.out.println(">> Connection " + threadID + " lost! <<");
            }

            if(line==null){
                finished = true;
            }else{
                try {
                    requestHandler(line, out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    finished=true;
                }
            }
        }
    }

    public void requestHandler(String line, PrintWriter out) throws InterruptedException {
        String[] data = line.split("[|]");
        String[] coords, origem, dest;
        String name, newName, username, usernameC, newUsername, newPassword, newLicense, newModel, password, matricula, modelo;
        Coordenadas driverPosition, o, d;
        Utilizador driver;
        Double ox,oy,dx,dy, v= 10.0;
        int travelID;
        Viagem trip;
        Boolean condutorOK, loginP, userExists, changeOK;
        Utilizador user;
        int time, travelTime;

        switch(data[0]){
            case "r":
                name = data[1];
                username = data[2];
                System.out.println("["+ threadID + ":REGISTER]" + ">> Received register request. Attempting to register...");
                int length = data.length;
                if (length == 4) {
                    password = data[3];
                    user = new Utilizador(name, username, password);
                }
                else {
                    password = data[3];
                    matricula = data[4];
                    modelo = data[5];
                    user = new Utilizador(name, username, password, matricula, modelo);
                }
                try {
                    ub.registaUtilizador(user);
                    out.println("OK");
                    System.out.println("["+ threadID + ":REGISTER]" + "\tUser has been succefully registered!" +
                                            "Data :" + user);
                }catch(ExistentUserException e){
                    out.println("NOK");
                    System.out.println("["+ threadID + ":REGISTER]" + "\tError! User " + username + " is already registered!");
                }
                out.flush();
                break;
            case "l":
                username = data[1];
                password = data[2];
                System.out.println("["+ threadID + ":LOGIN] >> Attempting login."+
                                    "[username: " + username + " password: " + password + " ]");
                loginP = ub.passwordCorrecta(username,password);
                if (loginP) {
                    out.println("OK");
                    out.flush();
                    System.out.println("["+ threadID + ":LOGIN] \tUser " + username + " logged succefully!");
                }
                else {
                    out.println("NOK");
                    out.flush();
                    System.out.println("["+ threadID + ":LOGIN] \tError! User already exists or password is incorrect!");
                }
                break;
            case "p":
                username = data[1];
                origem = data[2].split(",");
                dest = data[3].split(",");
                o = new Coordenadas(Integer.parseInt(origem[0]), Integer.parseInt(origem[1]));
                d = new Coordenadas(Integer.parseInt(dest[0]), Integer.parseInt(dest[1]));
                System.out.println("["+ threadID + ":TRIP_REQUEST] >> Travel request from " + o.neatToString() + " to " + d.neatToString());
                travelID = ub.requestTravel(username, o, d);
                trip = ub.getTripFromId(travelID);
                driver = ub.getUtilizador(trip.getUsernameDriver());
                time = trip.getDurationDriverToDeparture(v);
                System.out.print  ("["+ threadID + ":TRIP_REQUEST] \tDriver " + trip.getUsernameDriver() + " assigned. ");
                System.out.println("It will take aprox." + time + " secs to arrive at the customer's location");
                out.println(driver.getUsername()+"|"+time+"|"+driver.getLicensePlate()+"|"+driver.getModel());
                out.flush();
                ub.esperaCondutor(travelID);
                System.out.println("["+ threadID + ":TRIP_REQUEST] \tDriver is on spot");
                out.println("OK");
                out.flush();
                ub.confirmDeparture(travelID);
                travelTime = trip.getTripDuration(v);
                out.println("OK|"+(0.85*travelTime));
                out.flush();
                System.out.println("["+ threadID + ":TRIP_REQUEST] \tCustomer has arrived at destination. Fare: " + 0.85 * travelTime);
                break;
            case "a":
                username = data[1];
                coords = data[2].split(",");
                driverPosition = new Coordenadas(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                System.out.println("["+ threadID + ":DRIVER_ANNOUNCE] >> Request to announce availability at"
                                        + driverPosition.neatToString() + " drive from user " + username);
                travelID = ub.announce(username, driverPosition);
                trip = ub.getTripFromId(travelID);
                usernameC = trip.getUsernamePassenger();
                ox = trip.getDeparture().getX();
                oy = trip.getDeparture().getY();
                dx = trip.getArrival().getX();
                dy = trip.getArrival().getY();
                out.println(usernameC + "|" + ox + "," + oy + "|" + dx + "," + dy);
                out.flush();
                trip = ub.getTripFromId(travelID);
                System.out.println("["+ threadID + ":DRIVER_ANNOUNCE] " +
                                        "\tAssigned Trip" +trip.getDeparture().neatToString()+"->"+trip.getArrival().neatToString()+
                                        "Now going to departure location...");
                time = trip.getDurationDriverToDeparture(v);
                sleep(time*1000);
                System.out.println("["+ threadID + ":DRIVER_ANNOUNCE] \tAt departure. Waiting passenger to confirm...");
                ub.arrivedAtDeparture(travelID);
                System.out.println("["+ threadID + ":DRIVER_ANNOUNCE] \tPassenger confirmed the trip. Starting trip..");
                out.println("OK");
                out.flush();
                travelTime = trip.getDurationDriverToDeparture(v);
                sleep(travelTime*1000);
                System.out.println("["+ threadID + ":DRIVER_ANNOUNCE] \tArrived at destination");
                ub.endTravel(travelID);
                out.println("OK|" + (0.85 * travelTime));
                out.flush();

                break;
            case "c":
                username = data[1];
                condutorOK = ub.getUtilizador(username).condutor();
                System.out.println("["+ threadID + ":CAN_BE_DRIVER] >> Received request to verify if " + username + " can be a driver...");
                if (!condutorOK) {
                    out.println("NOK");
                    System.out.println("["+ threadID + ":CAN_BE_DRIVER] \t "+username +" can't be driver :(");
                }
                else {
                    out.println("OK");
                    System.out.println("["+ threadID + ":CAN_BE_DRIVER] \t "+username +" can be driver :)");
                }
                out.flush();
                break;
            case "cu":
                username = data[1];
                userExists = ub.utilizadorExiste(username);
                System.out.println("["+ threadID + ":USER_EXISTS] >> Received request to check " + username + " existence...");
                if (userExists) {
                    out.println("TRUE");
                    System.out.println("["+ threadID + ":USER_EXISTS] \t" + username + "exists!");
                } else {
                    out.println("FALSE");
                    System.out.println("["+ threadID + ":USER_EXISTS] \t" + username + "does not exist!");
                }
                out.flush();
                break;
            case "gname":
                username = data[1];
                System.out.println(">> Received name request to user " + username);
                System.out.println("Name: " + ub.getUtilizador(username).getName());
                out.println(ub.getUtilizador(username).getName());
                out.flush();
                break;
            case "gmat":
                username = data[1];
                System.out.println(">> Received license plate request to user " + username);
                System.out.println("License plate: " + ub.getUtilizador(username).getLicensePlate());
                out.println(ub.getUtilizador(username).getLicensePlate());
                out.flush();
                break;
            case "gmod":
                username = data[1];
                System.out.println(">> Received car model request to user " + username);
                System.out.println("Car model: " + ub.getUtilizador(username).getModel());
                out.println(ub.getUtilizador(username).getModel());
                out.flush();
                break;
            case "cusername":
                username = data[1];
                newUsername = data[2];
                System.out.println(">> Received request to change username from " + username + " to " + newUsername);
                changeOK = ub.changeUsername(username, newUsername);
                if (changeOK) {
                    out.println("OK");
                    System.out.println("Username succefully changed!");
                } else {
                    out.println("NOK");
                    System.out.println("Error! There's already an user registered with the new username!");
                }
                out.flush();
                break;
            case "cname":
                username = data[1];
                newName = data[2];
                System.out.println(">> Received request to change " + username + "'s name to " + newName);
                changeOK = ub.changeName(username, newName);
                if (changeOK) {
                    out.println("OK");
                    System.out.println("Name succefully changed!");
                } else {
                    out.println("NOK");
                    System.out.println("Error while attempting to change user's name!");
                }
                out.flush();
                break;
            case "cpassword":
                username = data[1];
                password = data[2];
                newPassword = data[3];
                System.out.println(">> Received request to change " + username + "'s password from " + password + " to " + newPassword);
                changeOK = ub.changePassword(username, password, newPassword);
                if (changeOK) {
                    out.println("OK");
                    System.out.println("Password succefully changed!");
                } else {
                    out.println("NOK");
                    System.out.println("Error! Wrong password!");
                }
                out.flush();
                break;
            case "cmatricula":
                username = data[1];
                newLicense = data[2];
                System.out.println(">> Received request to change " + username + "'s license plate to " + newLicense);
                changeOK = ub.changeLicensePlate(username, newLicense);
                if (changeOK) {
                    out.println("OK");
                    System.out.println("License plate succefully changed!");
                } else {
                    out.println("NOK");
                    System.out.println("Error! Something went wrong!");
                }
                out.flush();
                break;
            case "cmodelo":
                username = data[1];
                newModel = data[2];
                System.out.println(">> Received request to change " + username + "'s car model to " + newModel);
                changeOK = ub.changeCarModel(username, newModel);
                if (changeOK) {
                    out.println("OK");
                    System.out.println("Car model succefully changed!");
                } else {
                    out.println("NOK");
                    System.out.println("Error! Something went wrong!");
                }
                out.flush();
                break;
            default:
                System.out.println(">> Ups! Something went wrong!");
                out.println("NOK");
                out.flush();
        }
    }
}
