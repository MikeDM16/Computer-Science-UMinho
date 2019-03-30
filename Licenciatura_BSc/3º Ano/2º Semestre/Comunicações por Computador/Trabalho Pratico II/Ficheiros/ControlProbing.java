import java.net.*;
import java.util.*;

/**
 *
 * @author migue
 */
public class ControlProbing implements Runnable{
    
    private BackEndServer b;
    private DatagramSocket dsocket;
    
    public ControlProbing(BackEndServer b, DatagramSocket ds){
        this.b = b; this.dsocket = ds;
    }

    public void run(){
    	byte[] sendData = new byte[1024];
    	byte[] receiveBuf = new byte[1024];
        int enviarProbe = 1; 
        try{
            System.out.println("Rececao de Probings ligado:"); 
            while(enviarProbe == 1){
            	DatagramPacket recebido = new DatagramPacket(receiveBuf, receiveBuf.length);
               	this.dsocket.receive(recebido);
                String pedido = new String( recebido.getData());
                String delim = " ";
        		String parse[] = pedido.split(delim);
                enviarProbe = b.getEnviaProbe();

        		if(parse[0].equals("ProbeRequest")){
        			int idProbe = Integer.parseInt(parse[1]);
        			int nrSeq = Integer.parseInt(parse[2]);
        			ProbeAnswerPck pacoteProbe = new ProbeAnswerPck(idProbe, nrSeq);
    				sendData = pacoteProbe.getPacket().getBytes();
        			//if(id == idProbe){ System.out.println("ok"); }

    				int porta = dsocket.getLocalPort();
    				InetAddress IPReverse = InetAddress.getByName("10.1.1.1");	
    				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPReverse, porta);
    				dsocket.send(sendPacket);

                    System.out.println("Respondeu ao Probing com nr sequência = " + nrSeq);
        		}
            }
        }catch(Exception e){
            System.out.println("Erro no ControlProbing do BackEndServer: " + e);
        }    

    }

    private class ProbeAnswerPck{
    	private int id, nrSeq;

    	ProbeAnswerPck(int id, int nrSeq){ this.id =id; this.nrSeq = nrSeq; }

    	public String getPacket(){
    		StringBuilder s = new StringBuilder();
            s.append("ProbeAnswer" + " ");
            s.append(id + " ");
            s.append(System.currentTimeMillis() + " ");
            s.append(nrSeq + " ");
            return s.toString();
    	}
    }
}