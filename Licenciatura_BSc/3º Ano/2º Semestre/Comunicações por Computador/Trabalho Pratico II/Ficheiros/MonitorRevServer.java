
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author migue
 */
public class MonitorRevServer implements Runnable{
    private GestaoMonitores gestMon;
    private DatagramSocket serverSocketUDP;
    /*
    Construtor da classe
    */
    MonitorRevServer(DatagramSocket dsPDU, GestaoMonitores gestMon){
        this.gestMon = gestMon; this.serverSocketUDP = dsPDU;
    }
    /*
    Thread que ficará sempre a correr, recebendo os Hellos e atualizando os dados na 
    variavel global que representa a tabela com as informações dos servidores de backEnd
    */
    public void run(){
        byte[] receiveBuf = new byte[1024];
        DatagramSocket serverSocketUDP = null;
        try{
            System.out.println("Monitor do Reverse Proxy ligado"); 
            int isReverseRunning = 1;
            while(isReverseRunning == 1){
                DatagramPacket recebido = new DatagramPacket(receiveBuf, receiveBuf.length);
                this.serverSocketUDP.receive(recebido);
                
                String dados = new String( recebido.getData());
                atualizaDados( dados );
            }
        }catch(IOException e){
            System.out.println("Erro do MonitorRevServer no ReverseProxy: " + e);
        }   
        serverSocketUDP.close(); 
    }

    
    /*
    Função que realiza o parsing do PDU (em formato string) recebido pelo socktet e atualiza
    os dados da variavel global, que representa a tabela com  as informações dos servidores de backEnd
    */
    public void atualizaDados( String dados ) throws UnknownHostException{
        long tempoRececao = System.currentTimeMillis();

        String delim = " ";
        String parse[] = dados.split(delim);
        int control = 1;
        String designacao = "---";
        int idBackEnd = (-1), nrSeq = (-1);
        double tempoEnvio = 0;  

        //Recebeu resposta a probe
        if(parse[0].equals("ProbeAnswer")){

            idBackEnd   = Integer.parseInt( parse[1] );
            tempoEnvio      = Double.parseDouble( parse[2] );
            nrSeq       = Integer.parseInt( parse[3] );
            control = 0;
            gestMon.atualizaDadosProbing(idBackEnd, nrSeq, tempoEnvio, tempoRececao );
        }

        //Recebeu PDU 
        if(parse[0].equals("PDU")){
            idBackEnd = 0; int porta = 0, nrCliAtv = 0;         
            InetAddress endIP = null;
            control = 0;
            idBackEnd      = Integer.parseInt( parse[1] );
            designacao     = parse[2];
            porta          = Integer.parseInt( parse[3] );
            nrSeq          = Integer.parseInt( parse[4] );
            tempoEnvio     = Double.parseDouble( parse[5] );
            nrCliAtv       = Integer.parseInt( parse[6] );
            endIP          = InetAddress.getByName( parse[7] );

            gestMon.atualizaDados(idBackEnd, designacao, porta, nrSeq, tempoEnvio, tempoRececao, nrCliAtv, endIP);  
        }
        int listar = gestMon.getListar();
        
        if((listar==1) || (listar == 3)){
            if(parse[0].equals("PDU")){
                System.out.println("Recebeu um UDP Hello de " + designacao + 
                                                " com # de sequência: " + nrSeq);
            }
        }
        if((listar==2) || (listar == 3)){
            if(parse[0].equals("ProbeAnswer")){

                System.out.println("Recebeu um ProbeAnswer de backEnd" + idBackEnd 
                                                     + " com nr Sequência = " + nrSeq);
            }
        }
        if((listar == 1) || (listar==2) || (listar == 3)){
            if(control == 1)
                System.out.println("Parametros recebidos incorretos");
                /*for(int i = 0; i< parse.length; i++){
                    System.out.println(parse[i]);
                }*/
        }
    }

}
