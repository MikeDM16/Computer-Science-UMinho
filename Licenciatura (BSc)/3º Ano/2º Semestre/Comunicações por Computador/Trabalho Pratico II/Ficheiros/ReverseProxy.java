import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author miguel
 */
public class ReverseProxy {
    
    public static void main(String args[]){
        //Iniciar a variavel que vai conter a tabela com os dados de todos os servidores backEnd
        GestaoMonitores gestMon = new GestaoMonitores();
        
        try{
        // iniciar o socket para receber os PDU hello e controlo de Probing
        DatagramSocket dsPDU = new DatagramSocket(5555);

        /* Criar a thread que ficara a correr em backgroud, recebendo os hellos dos servidores de backEnd
        e atualizando a tabela com as informações */
        Thread tMonitorRevServer = new Thread(new MonitorRevServer( dsPDU, gestMon ));
        tMonitorRevServer.start();   

        /* Criar a thread que ficara a correr em backgroud, percorrendo tabela 
        com as informações e enviando probing aos servidores de backEnd*/
        Thread tProbeRequests = new Thread(new ProbeRequest( dsPDU, gestMon ));
        tProbeRequests.start();

        //Iniciar socket TCP para receber pedidos cliente na porta 80
        ServerSocket ssocket = new ServerSocket(80);
        Thread tRecebeClientes = new Thread(new RecebeClientes( ssocket, gestMon ));
        tRecebeClientes.start();
        
        }catch(Exception e){
            System.out.println("Erro no ReverseProxy: " + e);
        }    
        
        int escolha = 1;
        Scanner sc = new Scanner(System.in);
        
        while(escolha != 0){
            System.out.println("(1) - Listar chegadas PDU Hello ");
            System.out.println("(2) - Listar Pedidos de Probe");
            System.out.println("(3) - Listar monitoriação");
            System.out.println("(3) - Parar listagens");
            System.out.println("(5) - Listar servidores BackEnd");
            System.out.println("(0) - Sair");

            escolha = sc.nextInt();
            if(escolha == 1)    gestMon.setListar(1);
            if(escolha == 2)    gestMon.setListar(2);
            if(escolha == 3)    gestMon.setListar(3);
            if(escolha == 4)    gestMon.setListar(0);
            if(escolha == 5){
                                gestMon.setListar(0);
                                gestMon.getLock(); int s = gestMon.nrServers(); gestMon.getUnlock();
                                if( gestMon.nrServers()==0)
                                    System.out.println("Sem servidores de BackEnd");
                                else gestMon.listarServidores();
                            }
            if(escolha == 0)    {gestMon.getLock(); 
                                gestMon.setReverseRunning(0); 
                                gestMon.getUnlock();};
            
        }
        System.out.println("Terminou a execução do Servidor reverse");
      
    }
    

}
