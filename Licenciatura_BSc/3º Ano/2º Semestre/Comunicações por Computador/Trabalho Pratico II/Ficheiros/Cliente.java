import java.io.*;
import java.net.*;
import java.util.concurrent.locks.*;
import java.util.*;

class Cliente {

  public static void main(String argv[]) {
    try{

      InetAddress IPReverse = InetAddress.getByName("10.1.1.1");
      Socket clientSocket = new Socket(IPReverse, 80);

      Thread tEscuta = new Thread(new CliEscuta(clientSocket));
      Thread tPedido = new Thread(new CliPedido(clientSocket));
      tPedido.start();
      tEscuta.start();

      tPedido.join();
      tEscuta.join();
      clientSocket.close();

        }catch(Exception e){
          System.out.println("Erro Cliente: " + e);
        }
   }

   public static class CliEscuta implements Runnable{
       private Socket cliSocket;

       CliEscuta(Socket cli){ this.cliSocket = cli; }
       
       public void run(){
          try{   
            BufferedReader br = new BufferedReader( new InputStreamReader( cliSocket.getInputStream() ));
            String respostaReverse;

            while((respostaReverse= br.readLine())!= null){
              System.out.println("resposta: " + respostaReverse);
            }
          }catch(Exception e){
          System.out.println("Erro CliEscutaTCP: " + e);
        }

       }
   }

   public static class CliPedido implements Runnable{
       private Socket cliSocket;

       CliPedido(Socket cli){ this.cliSocket = cli; }
       
       public void run(){
          try{ 
            PrintWriter inCli = new PrintWriter( cliSocket.getOutputStream(), true );
            Scanner inputCli = new Scanner(System.in);

            String pedido;
            while((pedido = inputCli.nextLine()) != null){
              System.out.println("cliente pediu: " + pedido);
              inCli.println(pedido);
            }
            inCli.close();
          }catch(Exception e){
            System.out.println("Erro CliEscutaTCP: " + e);
          }
       }
   }
   
}