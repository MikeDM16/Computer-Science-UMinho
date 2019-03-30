
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author migue
 */
public class RecebeClientes implements Runnable{
    private GestaoMonitores gestMon;
    private ServerSocket ssocket; //socket na porta 80 para receber clientes
    
    /*
    Construtor da classe
    */
    RecebeClientes( ServerSocket ssocket, GestaoMonitores gestMon){
        this.gestMon = gestMon; this.ssocket = ssocket;
    }

    public void run(){
        try{
            Socket cli;
            while( (cli = ssocket.accept()) != null ){
                System.out.println("Novo pedido de cliente");

                BackEndServer server = gestMon.getBestServer();
                if( server == null){
                    System.out.println("Sem servidores disponiveis para atender o pedido");
                }else{
                    InetAddress ipBackEnd = server.getEndIP();
                    int id = server.getIdBackEnd();
                    gestMon.incNrCliAtv( id );
                    System.out.println("Escolheu o servidor: " + id);
                    //Os servidores de backEnd atendem sempre clientes na 9999
                    Socket socketbackEnd = new Socket(ipBackEnd, 9999);
                    Thread tPedidoTCP = new Thread( new PedidoTCP( cli, socketbackEnd));
                    Thread tRespostaTCP = new Thread( new RespostaTCP( cli, socketbackEnd));
                    tPedidoTCP.start();
                    tRespostaTCP.start();

                    tPedidoTCP.join();
                    tRespostaTCP.join();
                  //  socketbackEnd.close();
                }
            }
            cli.close();
            ssocket.close();
        }catch(Exception e){
            System.out.println("Erro Recebe Cliente: " + e);
        }  
    }

    public class PedidoTCP implements Runnable{
        private Socket cli;
        private Socket backendServer;

        PedidoTCP(Socket cli, Socket backendServer){
            this.cli = cli; this.backendServer = backendServer;
        } 

        public void run(){
            String pedidoCliente;
            try{
                System.out.println("Entrou no pedido TCP");
                BufferedReader inSocketCli = new BufferedReader( new InputStreamReader( cli.getInputStream()));
                PrintWriter outSocketBackEnd = new PrintWriter( backendServer.getOutputStream(), true);

                while((pedidoCliente = inSocketCli.readLine()) != null){
                    // Ler do socket do cliente e redirecionar para o socket do backend que
                    //vai atender o pedido 
                    outSocketBackEnd.println(pedidoCliente);
                    System.out.println("PedidoTCP Cliente " + pedidoCliente);
                }
                //inSocketCli.close();
                //outSocketBackEnd.close();
            }catch(Exception e){
                System.out.println("Erro PedidoTCP para o BackEndServer: " + e);
            } 
            /*
            byte[] receiveBuf = new byte[1024];
            try{
                DatagramPacket recebido = new DatagramPacket(receiveBuf, receiveBuf.length);
                this.cli.receive(recebido);

                //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPBackEnd, porta);
                this.backendServer.send(recebido);
            }catch(Exception e){
                System.out.println("Erro PedidoTCP para o BackEndServer: " + e);
            }  */ 
        }
    }

    public class RespostaTCP implements Runnable{
        private Socket cli;
        private Socket backendServer;

        RespostaTCP(Socket cli, Socket backendServer){
            this.cli = cli; this.backendServer = backendServer;
        } 

        public void run(){
            String respostaBackEnd;
            try{
                 System.out.println("Entrou no resposta TCP");
                BufferedReader inSocketBackend = new BufferedReader( new InputStreamReader( backendServer.getInputStream()));
                PrintWriter outSocketCli = new PrintWriter( cli.getOutputStream(), true);

                while((respostaBackEnd = inSocketBackend.readLine()) != null){
                    outSocketCli.println(respostaBackEnd);
                }
               // inSocketBackend.close();
                //outSocketCli.close();
            }catch(Exception e){
                System.out.println("Erro RespostaTCP para o cliente: " + e);
            } 
        }
    }

}
