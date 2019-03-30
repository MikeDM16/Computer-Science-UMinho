package servidor;


import java.net.*;
import java.io.*;
public class Servidor {


    public static void main(String[] args) throws IOException, InterruptedException {
        int threadID = 1;
        Uber ub = new Uber();
        ub.registaUtilizador(new Utilizador("Mariana Carvalho", "mariana", "12"));
        ub.registaUtilizador(new Utilizador("Andre Santos", "andre", "12"));
        ub.registaUtilizador(new Utilizador("Sofia Martins", "sofia", "12"));
        ub.registaUtilizador(new Utilizador("Joao Semedo", "joao", "12", "AB-65-EJ", "CIVIC X"));
        ub.registaUtilizador(new Utilizador("Alexandra Teixeira", "alexandra", "12", "WS-76-UT", "COROLA"));
        ub.registaUtilizador(new Utilizador("Luis Cardoso", "luis", "12", "ZX-12-HY", "AUDI"));
        ub.registaUtilizador(new Utilizador("Pedro Osvaldo", "pedro", "12", "10-12-RE", "POPOCAR"));


        ServerSocket server = new ServerSocket(12345);
        System.out.println("Porta 12345 aberta!");
        while (true) {
            Socket c = server.accept();
            System.out.print("New connection: " + c.getInetAddress().getHostAddress());
            Thread t = new Thread(new RunnerServidor(ub, c, threadID));
            System.out.println(" Assigned to thread " + threadID);
            threadID++;
            t.start();
        }

    }
}

