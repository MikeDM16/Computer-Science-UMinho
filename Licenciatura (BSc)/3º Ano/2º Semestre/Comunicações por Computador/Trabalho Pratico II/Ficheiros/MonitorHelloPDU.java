import java.net.*;
import java.util.*;

/**
 *
 * @author migue
 */
public class MonitorHelloPDU implements Runnable{
    
    private BackEndServer b;
    private DatagramSocket dataSocket;
    
    public MonitorHelloPDU(BackEndServer b, DatagramSocket dsPDU){
        this.b = b; this.dataSocket = dsPDU;
    }
    public void run(){
        Random r = new Random();
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        int enviarPDU = 1;
        try {
           InetAddress IPReverse = InetAddress.getByName("10.1.1.1");
           System.out.println("Thread do server de BackEnd arrancou envio de PDU hellos para o " + IPReverse);
           while(enviarPDU == 1){
                b.getLock();
                int idBackEnd = b.getIdBackEnd();
                String designacao = b.getDesignacao(); 
                InetAddress IPbackend = b.getEndIP();
                int porta = b.getPorta();
                int nrCliAtv = b.getNrCliAtv();
                int nrSeq = b.getNrSeqPDU();
                enviarPDU = b.getEnviaPDU();
                long time = System.currentTimeMillis(); 
                b.setLastPDUTime(time);

                b.getUnlock();

                PDUHello hello = new PDUHello(idBackEnd, designacao, porta, nrSeq, time, nrCliAtv, IPbackend);
                sendData = hello.getPDU().getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPReverse, 5555);
                dataSocket.send(sendPacket);

                System.out.println("Enviou PDU com # Sequência = " + nrSeq);
                b.incPDUsEnviados();            
                Thread.sleep(5000);
           }
            
        }catch(Exception e){
            System.out.println("Erro no MonitorHelloPDU do BackEndServer: " + e);
        }
    }
    
    
}
