package cliente;

import servidor.Utilizador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Cliente {

    private static final Pattern patternCords = Pattern.compile("[(]?[ ]*[+-]?[0-9]+[, ;]+[+-]?[0-9]+[ ]*[)]?");
    private static String usernameLogged;
    private static ROLE currentRole = ROLE.CUSTOMER;
    private static Socket socket;
    private static PrintWriter serverWriter;
    private static BufferedReader serverReader;

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean connected = false;
        int attempts = 0, port = 12345, secsNewTry = 5;
        String host = "localhost";

        while(!connected){
            System.out.println("Trying to connect to " + host + " at port " + port + "(Attempt "+ ++attempts +  ")");

            try {
                socket = new Socket(host, port);
                System.out.println("Connected to the server!");
                connected = true;
                serverWriter = new PrintWriter(socket.getOutputStream());
                serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                menuHUB(MENU.MAIN);
                socket.close();
            }catch(SocketException e){
                System.out.println("Could not connect. Maybe the server is off? " + e.getMessage());
                System.out.println("Trying to connect again in " + secsNewTry + " seconds");
                Thread.sleep(secsNewTry*1000);
            }

        }
    }

    private static void menuHUB(MENU menuDestino) throws IOException {
        MENU menu = menuDestino;

        while(menu != MENU.QUIT){

            switch(menu){
                case MAIN:
                    menu = mainMenu();
                    break;
                case LOGIN:
                    menu = loginMenu();
                    break;
                case REGISTER:
                    menu = registerMenu();
                    break;
                case DRIVER:
                    menu = driverMenu();
                    break;
                case CUSTOMER:
                    menu = customerMenu();
                    break;
                case PROFILE:
                    menu  = profileMenu();
                    break;
                case EDIT_USER:
                    menu = editUserMenu();
                    break;
                case REQUEST_TRIP:
                    menu = requestTripMenu();
                    break;
                case ANNOUNCE_POSITION:
                    menu = announcePositionMenu();
                    break;
                case QUIT: System.exit(0); break;
            }


        }

    }

    private static MENU mainMenu(){
        int escolha=0;
        boolean escolhaOk = false;

        System.out.println("--------------------------");
        System.out.println("UBER > MAIN MENU");
        System.out.println("--------------------------");
        System.out.println("    1- Login");
        System.out.println("    2- Sign Up");
        System.out.println("--------------------------");
        System.out.println("9 - Quit");
        System.out.println("--------------------------");

        escolha = readChoice();

        switch(escolha){
            case 1: return MENU.LOGIN;
            case 2: return MENU.REGISTER;
            case 9: return MENU.QUIT;
            default: return MENU.MAIN;
        }

    }

    private static MENU loginMenu() throws IOException {
        int choice = 0;
        String username, password, serverAnswer;
        boolean loginOk = false, canBeDriver = false;
        Scanner sc;

        System.out.println("-----------------------------");
        System.out.println("UBER > LOGIN");
        System.out.println("-----------------------------");

        while (!loginOk) {
            sc = new Scanner(System.in);
            System.out.print("    Username: ");
            username = sc.nextLine();
            System.out.print("    Password: ");
            password = sc.nextLine();

            serverWriter.println("l" + "|" + username + "|" + password);
            serverWriter.flush();
            serverAnswer = serverReader.readLine();

            if (serverAnswer.equalsIgnoreCase("OK")) {
                loginOk = true;
                usernameLogged = username;
                serverWriter.println("gname" + "|" + usernameLogged);
                serverWriter.flush();
                serverAnswer = serverReader.readLine();
                System.out.println("\t-- WELCOME " + serverAnswer + " --");
                System.out.println("-----------------------------------------------------");

                serverWriter.println("c" + "|" + username);
                serverWriter.flush();
                serverAnswer = serverReader.readLine();

                if(serverAnswer.equalsIgnoreCase("OK")) {
                    canBeDriver = true;
                    System.out.println("1 - Login as Customer | 2 - Login as Driver");
                }else{
                    System.out.println("1 - Login as Customer");
                }

                System.out.println("0 - Main Menu | 9 - Quit");
                System.out.println("-----------------------------------------------------");

                choice = readChoice();

                switch (choice) {
                    case 0: return MENU.MAIN;
                    case 1:
                        currentRole = ROLE.CUSTOMER;
                        return MENU.CUSTOMER;
                    case 2:
                        if(canBeDriver){
                            currentRole = ROLE.DRIVER;
                            return MENU.DRIVER;}
                        else return MENU.CUSTOMER;
                    case 9:
                        return MENU.QUIT;
                    default:
                        return MENU.LOGIN;
                }
            } else {
                System.out.println("The username does not exist or password is wrong.");
                System.out.println("-----------------------------------------------------");
                System.out.println("1 - Try again           ");
                System.out.println("0 - Main menu | 9 - Quit");
                System.out.println("-----------------------------------------------------");

                choice = readChoice();

                switch (choice) {
                    case 0: return MENU.MAIN;
                    case 1: return MENU.LOGIN;
                    case 9: return MENU.QUIT;
                    default: return MENU.LOGIN;
                }
            }
        }
        return MENU.LOGIN;
    }

    private static MENU registerMenu() throws IOException {
        int escolha=0;
        Utilizador newUser;
        String name=null, username=null, password=null, licencePlate=null, model=null, serverAnswer;
        boolean usernameOk=false;
        Scanner sc;

        System.out.println("-----------------------------");
        System.out.println("UBER > SIGN UP");
        System.out.println("-----------------------------");

        while(!usernameOk){

            sc = new Scanner(System.in);
            System.out.print  ("    Username: ");
            username = sc.nextLine();

            serverWriter.println("cu" + "|" + username);
            serverWriter.flush();
            serverAnswer = serverReader.readLine();

            if(serverAnswer.equalsIgnoreCase("TRUE")){
                System.out.println("Username already exists. Pick another one please.");
            }else{
                usernameOk = true;
            }

        }

        sc = new Scanner(System.in);
        System.out.print  ("    Name: ");
        name = sc.nextLine();
        System.out.print  ("    Password: ");
        password = sc.nextLine();
        System.out.print  ("    License Plate: ");
        licencePlate = sc.nextLine();
        System.out.print  ("    Model: ");
        model = sc.nextLine();

        if(!licencePlate.equals("") && !model.equals("")){
            serverWriter.println("r|" + name + "|" + username + "|" + password + "|" + licencePlate + "|" + model);
        }else{
            serverWriter.println("r|" + name + "|" + username + "|" + password);
        }

        serverWriter.flush();
        serverAnswer = serverReader.readLine();

        if(serverAnswer.equalsIgnoreCase("OK")){
            System.out.println("Succefully signed up!");
        }else{
            System.out.println("Erro no registo.");
        }

        System.out.println("-----------------------------");
        System.out.println("0 - Previous Menu | 9 - Quit");
        System.out.println("-----------------------------");

        escolha = readChoice();

        switch(escolha){
            case 0: return MENU.MAIN;
            case 9: return MENU.QUIT;
            default: return MENU.REGISTER;
        }
    }

    private static MENU customerMenu() throws IOException {
        int choice=0;
        String serverAnswer;

        System.out.println("--------------------------");
        System.out.println("UBER > CUSTOMER AREA");
        System.out.println("--------------------------");
        System.out.println("    1 - Request Trip");
        System.out.println("    2 - Change to driver");
        System.out.println("    3 - Edit info");
        System.out.println("    4 - Profile");
        System.out.println("--------------------------");
        System.out.println("0 - Logout | 9 - Quit");
        System.out.println("--------------------------");

        choice = readChoice();

        switch(choice){
            case 0: return MENU.MAIN;
            case 1: return MENU.REQUEST_TRIP;
            case 2:
                serverWriter.println("c" + "|" + usernameLogged);
                serverWriter.flush();
                serverAnswer = serverReader.readLine();
                if (serverAnswer.equalsIgnoreCase("OK")) {
                    currentRole = ROLE.DRIVER;
                    return MENU.DRIVER;
                } else {
                    System.out.println("\n >> Please update your car's info! <<\n");
                    return MENU.CUSTOMER;
                }
            case 3: return MENU.EDIT_USER;
            case 4: return MENU.PROFILE;
            case 9: return MENU.QUIT;
            default: return MENU.CUSTOMER;
        }
    }

    private static MENU driverMenu(){
        int choice=0;

        System.out.println("--------------------------");
        System.out.println("UBER > DRIVER AREA");
        System.out.println("--------------------------");
        System.out.println("    1 - Announce Position");
        System.out.println("    2 - Change to customer");
        System.out.println("    3 - Edit info");
        System.out.println("    4 - Profile");
        System.out.println("--------------------------");
        System.out.println("0 - Main menu | 9 - Quit");
        System.out.println("--------------------------");

        choice = readChoice();

        switch(choice){
            case 0: return MENU.MAIN;
            case 1: return MENU.ANNOUNCE_POSITION;
            case 2:
                currentRole = ROLE.CUSTOMER;
                return MENU.CUSTOMER;
            case 3:return MENU.EDIT_USER;
            case 4:return MENU.PROFILE;
            case 9: return MENU.QUIT;
            default: return MENU.DRIVER;
        }

    }

    private static MENU requestTripMenu() throws IOException {
        String strOr, strDest, serverAnswer;
        String parse[];
        boolean orOK = false, destOK = false;
        int xOr=0, xDest=0, yOr=0, yDest=0;
        int choice;
        Matcher matcherOr, matcherDest;
        StringTokenizer strTok;
        Scanner sc;

        System.out.println("-----------------------------");
        System.out.println("UBER > REQUEST TRIP");
        System.out.println("-----------------------------");

        while(!orOK) {
            sc = new Scanner(System.in);
            System.out.print("    Where are you? ");
            strOr = sc.nextLine();
            matcherOr = patternCords.matcher(strOr);
            if (matcherOr.matches()) {
                orOK=true;
                strTok = new StringTokenizer(strOr, "() ,;\n\r");
                xOr = Integer.parseInt(strTok.nextToken());
                yOr = Integer.parseInt(strTok.nextToken());
            }else{
                System.out.println("Invalid Position. Try again.");
            }
        }

        while(!destOK) {
            sc = new Scanner(System.in);
            System.out.print("    Where do you want to go? ");
            strDest = sc.nextLine();
            matcherDest = patternCords.matcher(strDest);
            if (matcherDest.matches()) {
                destOK=true;
                strTok = new StringTokenizer(strDest, "() ,;\n\r");
                xDest = Integer.parseInt(strTok.nextToken());
                yDest = Integer.parseInt(strTok.nextToken());
            }else{
                System.out.println("Invalid Position. Try again.");
            }
        }

        serverWriter.println("p|" + usernameLogged + "|" + xOr + "," + yOr + "|" + xDest + "," + yDest);
        serverWriter.flush();

        System.out.println("> Request sent. Looking for driver... <");

        serverAnswer = serverReader.readLine();
        parse = serverAnswer.split("[|,]");
        System.out.println("Driver found!");
        System.out.println(" \tDriver's name: " + parse[0]);
        System.out.println(" \tEstimated time of arrival: " + parse[1]);
        System.out.println(" \tLicense Plate: " + parse[2]);
        System.out.println(" \tModel: " + parse[3]);

        serverAnswer = serverReader.readLine();
        System.out.println("> Driver arrived at departure <");

        serverAnswer = serverReader.readLine();
        parse = serverAnswer.split("[|]");

        System.out.println("> Trip finished. Fare: " + parse[1] + " <");
        System.out.println("-----------------------------");
        System.out.println("0 - Customer Area | 9 - Quit");
        System.out.println("-----------------------------");

        choice = readChoice();

        switch(choice){
            case 0: return MENU.CUSTOMER;
            case 9: return  MENU.QUIT;
            default: return MENU.REQUEST_TRIP;
        }

    }

    private static MENU announcePositionMenu() throws IOException {
        String strOrigem, serverAnswer, parse[];
        boolean coordsOK=false;
        int x=0, y=0, escolha;
        Matcher matcher;
        StringTokenizer strTok;
        Scanner sc;


        System.out.println("-----------------------------");
        System.out.println("UBER > ANNOUNCE POSITION");
        System.out.println("-----------------------------");
        System.out.print  ("    Where are you? ");

        while(!coordsOK) {

            sc = new Scanner(System.in);
            strOrigem = sc.nextLine();
            matcher = patternCords.matcher(strOrigem);
            if(matcher.matches()){
                coordsOK=true;
                strTok = new StringTokenizer(strOrigem, "() ,;\n\r");
                x = Integer.parseInt(strTok.nextToken());
                y = Integer.parseInt(strTok.nextToken());
            }else{
                System.out.println("Invalid Position. Try again.");
            }

        }

        serverWriter.println("a" + "|" + usernameLogged + "|" + x + "," +y);
        serverWriter.flush();
        serverAnswer = serverReader.readLine();
        parse = serverAnswer.split("[|]");
        System.out.println("Trip assigned! ");
        System.out.println("Customer: "+ parse[0]);
        System.out.println("Departure: ("+ parse[1] + ")");
        System.out.println("Destination: ("+ parse[2] + ")");

        serverAnswer = serverReader.readLine();
        System.out.println("> Driver arrived at departure <");

        serverAnswer = serverReader.readLine();
        parse = serverAnswer.split("[|]");

        System.out.println("> Trip finished. Fare: " + parse[1] + " <");

        System.out.println("-----------------------------");
        System.out.println("0 - Driver Area | 9 - Quit");
        System.out.println("-----------------------------");

        escolha = readChoice();

        switch(escolha){
            case 0: return MENU.DRIVER;
            case 9: return MENU.QUIT;
            default: return MENU.ANNOUNCE_POSITION;
        }

    }

    private static MENU editUserMenu() throws IOException {
        int choice=0;
        String serverAnswer;
        String currentName, currentPassword, currentLPlate, currentModel;
        String name, username, password, licensePlate, model;
        Scanner sc;

        sc = new Scanner(System.in);
        System.out.println("--------------------------");
        System.out.println("UBER > EDIT PROFILE");
        System.out.println("--------------------------");
        System.out.println("    1 - Change Name");
        System.out.println("    2 - Change Username");
        System.out.println("    3 - Change Password");
        System.out.println("    4 - Change License Plate");
        System.out.println("    5 - Change Model");
        System.out.println("--------------------------");
        System.out.println("0 - Previous Menu | 9 - Quit");
        System.out.println("--------------------------");

        choice = readChoice();

        switch(choice){
            case 0:
                if(currentRole == ROLE.DRIVER) return MENU.DRIVER;
                else return MENU.CUSTOMER;
            case 1:
                sc = new Scanner(System.in);
                currentName = sendServerRequest("gname|" + usernameLogged);
                System.out.println("Your current name is: "+ currentName);
                System.out.print  ("Insert new name: ");
                name = sc.nextLine();
                serverAnswer = sendServerRequest("cname|" + usernameLogged + "|" + name);
                if (serverAnswer.equalsIgnoreCase("OK")) {
                    currentName = sendServerRequest("gname|" + usernameLogged);
                    System.out.println("Name changed! You are now called " + currentName);
                } else {
                    System.out.println("Error: Some error when changing the name.");
                }
                break;
            case 2:
                sc = new Scanner(System.in);
                System.out.println("Your current username is: "+ usernameLogged);
                System.out.print  ("Insert new username: ");
                username = sc.nextLine();
                serverAnswer = sendServerRequest("cusername|" + usernameLogged + "|" + username);
                if(serverAnswer.equalsIgnoreCase("OK")){
                    usernameLogged = username;
                    System.out.println("Username changed! You are now " + username);
                }else{
                    System.out.println("Error: Username already in use.");
                }
                break;
            case 3:
                sc = new Scanner(System.in);
                System.out.println("Insert the old password: ");
                currentPassword = sc.nextLine();
                System.out.println("Insert the new password: ");
                password = sc.nextLine();
                serverAnswer = sendServerRequest("cpassword|" + usernameLogged + "|" + currentPassword + "|" + password);
                if(serverAnswer.equalsIgnoreCase("OK")){
                    System.out.println("Password changed.");
                }else{
                    System.out.println("Old password wrong.");
                }
                break;
            case 4:
                sc = new Scanner(System.in);
                currentLPlate = sendServerRequest("gmat|" + usernameLogged);
                System.out.println("Your current license plate is: "+ currentLPlate);
                System.out.print  ("Insert your new license plate: ");
                licensePlate = sc.nextLine();
                serverAnswer = sendServerRequest("cmatricula|" + usernameLogged + "|" + licensePlate);
                if(serverAnswer.equalsIgnoreCase("OK")){
                    System.out.println("License plate changed.");
                }else{
                    System.out.println("An error ocurred. License not chnged.");
                }

                break;
            case 5:
                sc = new Scanner(System.in);
                currentModel = sendServerRequest("gmod|" + usernameLogged);
                System.out.println("Your current model is: "+ currentModel);
                System.out.print  ("Insert your new model: ");
                model = sc.nextLine();
                serverAnswer = sendServerRequest("cmodelo|" + usernameLogged + "|" + model);
                if(serverAnswer.equalsIgnoreCase("OK")){
                    System.out.println("Model changed.");
                }else{
                    System.out.println("An error ocurred. Model not chnged.");
                }
                break;
            case 9: return MENU.QUIT;
            default: return MENU.EDIT_USER;
        }

        System.out.println("----------------------------");
        System.out.println("0 - Previous Menu | 9 - Quit");
        System.out.println("----------------------------");

        choice = readChoice();

        switch(choice){
            case 0: if(currentRole == ROLE.DRIVER) return MENU.DRIVER;
                    else return MENU.CUSTOMER;
            case 9: return MENU.QUIT;
            default: return MENU.EDIT_USER;
        }

    }

    private static MENU profileMenu() throws IOException {
        String serverAnswer;
        int choice;

        System.out.println("--------------------------");
        System.out.println("UBER > PROFILE");
        System.out.println("--------------------------");

        serverAnswer = sendServerRequest("gname|" + usernameLogged);
        System.out.println("Name: " + serverAnswer);
        System.out.println("Username: " + usernameLogged);
        serverAnswer = sendServerRequest("gmat|" + usernameLogged);
        System.out.println("License Plate: " + serverAnswer);
        serverAnswer = sendServerRequest("gmod|" + usernameLogged);
        System.out.println("Model: " + serverAnswer);

        System.out.println("----------------------------");
        System.out.println("0 - Previous Menu | 9 - Quit");
        System.out.println("----------------------------");

        choice = readChoice();

        switch(choice){
            case 0: if(currentRole == ROLE.DRIVER) return MENU.DRIVER;
            else return MENU.CUSTOMER;
            case 9: return MENU.QUIT;
            default: return MENU.PROFILE;
        }

    }

    public static String sendServerRequest(String request) throws IOException {
        serverWriter.println(request);
        serverWriter.flush();
        return serverReader.readLine();
    }

    private static int readChoice(){
        boolean escolhaOk = false;
        int escolha=0;

        System.out.print(" Choose an option: ");
        while(!escolhaOk){

            try {
                Scanner sc  = new Scanner(System.in);
                escolha = sc.nextInt();
                escolhaOk = true;
            }catch (InputMismatchException e){
                System.out.println("Invalid option. Please insert a new one: ");
            }

        }

        return escolha;
    }

    enum ROLE {DRIVER, CUSTOMER}

    enum MENU {MAIN, LOGIN, REGISTER, DRIVER, CUSTOMER, EDIT_USER, PROFILE,
        REQUEST_TRIP, ANNOUNCE_POSITION, QUIT}

}
