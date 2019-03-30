import java.io.*;
import java.net.*;
import java.util.*;

public class ProbeRequest implements Runnable{
	private GestaoMonitores gestMon;
    private DatagramSocket dsUDP;
    /*
    Construtor da classe
    */
    ProbeRequest(DatagramSocket dsPDU, GestaoMonitores gestMon){
        this.gestMon = gestMon; this.dsUDP = dsPDU;
    }

    public void run(){
    	int nrSeqProbe; 
        Map<Integer, BackEndServer> servidores = new TreeMap<>();
    	byte[] sendData = new byte[1024];
    	try{
    		while(true){
    			if(gestMon.nrServers()>0){
    				servidores = gestMon.getServidores();
                    int listar = gestMon.getListar();
    				for(Map.Entry<Integer, BackEndServer> entry: servidores.entrySet()){
    					BackEndServer server = entry.getValue();
                        int id = server.getIdBackEnd();
                        if(server.testaAtividade()==true){
                             
                            //Se o servidor está ativo envia novo pedido de probing
                            nrSeqProbe = server.getNrSeqProbing() + 1;
                            InetAddress IPBackEnd = server.getEndIP();
                            int porta = dsUDP.getLocalPort();
    						ProbeRequestPck pacoteProbe = new ProbeRequestPck(id, nrSeqProbe);
    						sendData = pacoteProbe.getPacket().getBytes();

    						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPBackEnd, porta);
    						dsUDP.send(sendPacket);

    						gestMon.incProbesEnviados(id);
    						gestMon.setLastProbeTime(id, System.currentTimeMillis());
      
                            if((listar==2) || (listar == 3)){
                                System.out.println("Realizou pedido de Probing ao backend" + server.getDesignacao() 
                                                           + " com nr sequência = " + nrSeqProbe);
                            }
                        }else{
    						gestMon.remover( id ); Thread.sleep(2000);
                            System.out.println("Removeu o servidor backend" + id );
                        }    
    				}
    			}
                Thread.sleep(3000);
    		}
    	}catch(Exception e){ System.out.println("Erro no ProbeRequest do ReverseProxy: " + e); }
    }

    private class ProbeRequestPck{
    	private int id, nrSeq;

    	ProbeRequestPck(int id, int nrSeq){ this.id =id; this.nrSeq = nrSeq; }

    	public String getPacket(){
    		StringBuilder s = new StringBuilder();
            s.append("ProbeRequest" + " ");
            s.append(id + " ");
            s.append(nrSeq + " ");
            return s.toString();
    	}
    }
}

