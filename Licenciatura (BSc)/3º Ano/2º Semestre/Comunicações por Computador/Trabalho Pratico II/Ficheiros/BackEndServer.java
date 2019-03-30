
import java.net.*;
import java.util.concurrent.locks.*;
import java.util.*;

/**
 *
 * @author migue
 */
public class BackEndServer implements Runnable {
    private int idBackEnd, porta, nrCliAtv, nrHellos, nrPerdas;     
    private String designacao;               
    private InetAddress endIP;
    private Lock lockVars;                                          
    private int enviaPDU, enviaProbes;
    /*-----------------------------------------------------------
    Variaveis para controlo de envio de PDU  */
    private int nrSeqPDU, nrPDUsEnviados, nrPDUsLost;
    private double lastPDUTime;
    private int nrPDUsRecebidos; //usado pelo Reverse
    private double RTTChegadaProbes;
    /*-----------------------------------------------------------*/
    /*-----------------------------------------------------------
    Variaveis para controlo do probing usadas no Reverse */
    private int nrSeqProbing, nrProbsEnviados, nrProbesLost;
    private int nrProbesRecebidos;
    private double lastProbTime; private double RTTChegadaPDU; 
    /*-----------------------------------------------------------*/
    

    /*
    Construtor da classe recebendo todos os parametros
    */
    public BackEndServer(int idBackEnd, String designacao, int porta, int nrSeq, long tRececao, int nrCliAtv, InetAddress endIP) {
        this.idBackEnd = idBackEnd;                 this.porta = porta;
        this.nrCliAtv = nrCliAtv;
        this.designacao = designacao;               this.endIP = endIP;
        this.nrHellos = this.nrPerdas =0;           this.RTTChegadaPDU = 0.0;
        this.lockVars = new ReentrantLock();        this.nrSeqPDU = nrSeq;
        
        //Controlo rececao PDUs
        this.nrPDUsRecebidos = 0; this.lastPDUTime = tRececao; this.nrPDUsLost = 0;
        //controlo do probing
        this.nrProbesLost = 0; this.nrProbesRecebidos = 0;
        this.nrSeqProbing = 0; this.nrProbsEnviados = 0; this.lastProbTime = 10000;   
        this.RTTChegadaProbes = 0;
    }
    public BackEndServer(int idBackEnd, String designacao, InetAddress endIP, int porta){
        this.idBackEnd = idBackEnd;                 this.porta = porta;
        this.designacao = designacao;               this.endIP = endIP;
        this.nrHellos = this.nrPerdas = 0;          this.RTTChegadaPDU = 0.0;
        this.lockVars = new ReentrantLock();        this.RTTChegadaProbes = 0.0;
        this.enviaPDU = 1; 
        this.enviaProbes = 1;
        //Controlo de Cliente ativos
        this.nrCliAtv = 0;

        //controlo do probing
        this.nrSeqPDU = 1; this.nrPDUsEnviados = 0; this.lastPDUTime = 0.0; 
    }

    public static void main(String args[]){   

        Scanner sc = new Scanner(System.in);
        int escolha = (-1);
        InetAddress ipAddress = null;
        try{ 
            //ipAddress = InetAddress.getByName("localhost");
            ipAddress = InetAddress.getLocalHost();
            System.out.println(ipAddress.toString());
        }catch(Exception e){
            System.out.println("Não é possivel determinar o IP da maquina");
            System.out.println(e);
        }
        
        while(escolha != 0){
            System.out.println("(1) - Adicionar Servidor BackEnd ");
            System.out.println("(0) - Sair");

            escolha = sc.nextInt();
            if(escolha == 1){    
                System.out.println("Atribuir id único ao servidor:");
                int idBackEnd = sc.nextInt();  
                /*  Arrancar com servidor de BackEnd   */
                String designacao = "ServerBackEnd"+ idBackEnd;
                Thread tEnviar = new Thread(new BackEndServer(idBackEnd, designacao, ipAddress, 5555));
                tEnviar.start();
                escolha = 0;
                
            }
        }
        System.out.println("Configuração arranque do servidor terminado");
    }    

    /*Método run, chamado ao executar o backEnd*/
    public void run(){
        Scanner sc = new Scanner(System.in);
        DatagramSocket dsPDU = null;
        ServerSocket socketTCP = null;    
        try{
            dsPDU = new DatagramSocket(5555);
            /*  Arrancar com thread BackEnd  para envio de PDU */
            Thread tMonitorPDU = new Thread(new MonitorHelloPDU(this, dsPDU));
            tMonitorPDU.start(); 

            Thread tControlProbing = new Thread(new ControlProbing(this, dsPDU));
            tControlProbing.start();

            socketTCP = new ServerSocket(9999);
            Thread tControlTCP = new Thread(new ControlTCP(socketTCP));
            
            int escolha = 1;
            
            while(escolha != 0){
                System.out.println("(1) - Parar envio de PDUs");
                System.out.println("(2) - Parar respostas de Probing");
                System.out.println("(3) - Retomar envio de PDUs");
                System.out.println("(4) - Retomar respostas de Probing");
                System.out.println("(0) - Sair");
                
                //while(sc.hasNextLine()){
                    escolha = sc.nextInt();
                
                //this.lockVars.lock();
                if(escolha == 1){ 
                    this.enviaPDU = 0; 
                }
                if(escolha == 2){ 
                    this.enviaProbes = 0;
            
                }
                if(escolha == 3){ 
                    if(this.enviaPDU == 0){ 
                        this.enviaPDU = 1;
                        tMonitorPDU = new Thread(new MonitorHelloPDU(this, dsPDU));
                        tMonitorPDU.start(); 
                    }
                }
                if(escolha == 4){ 
                    if(this.enviaProbes == 0){ 
                        this.enviaProbes = 1; 
                        tControlProbing = new Thread(new ControlProbing(this, dsPDU));
                        tControlProbing.start(); 
                    }
                }
            }

            tMonitorPDU.join();
            tControlProbing.join();
            tControlTCP.join();
            if(dsPDU != null) dsPDU.close();
            if(socketTCP != null) socketTCP.close();
        }catch (Exception e){
                System.out.println("Erro BackendServer: " + e);
        }

    }
    
    public void atualizar(double tEnvio, long tRececao, int nrCliAtv, int nrSeq){
        this.lockVars.lock();
        this.nrCliAtv = nrCliAtv;
        
        this.nrPDUsRecebidos ++; 
        if( nrSeq > (this.nrSeqPDU + 1) ){ 
            this.nrPerdas += (nrSeq - nrSeqPDU - 1);
            this.nrPDUsLost++;
        }
        this.nrSeqPDU = nrSeq;

        this.lastPDUTime = ((double) tRececao) - tEnvio;
        this.RTTChegadaPDU = (this.RTTChegadaPDU + lastPDUTime)/ 2;
        this.lockVars.unlock();
    }
    
    public void atualizaDadosProbing( int nrSeq, double tEnvio, long tRececao ){
        this.lockVars.lock();
        this.nrProbesRecebidos++;
        if( nrSeq > (this.nrSeqProbing + 1) ){ 
            this.nrPerdas += (nrSeq - nrSeqProbing - 1);
            this.nrProbesLost++;
        }
        this.nrSeqProbing = nrSeq;

        this.lastProbTime = ((double) tRececao) - tEnvio;
        this.RTTChegadaProbes = (this.RTTChegadaProbes + lastProbTime)/ 2;

        this.lockVars.unlock();
    }

    public void getLock(){ this.lockVars.lock(); }
    public void getUnlock(){ this.lockVars.unlock(); }
    
    /* 
    Metodos setters
    */

    public void setIdBackEnd(int idBackEnd) {   this.idBackEnd = idBackEnd;    }
    public void setPorta(int porta) {   this.porta = porta;    }
    public void setNrCliAtv(int nrCliAtv) { this.nrCliAtv = nrCliAtv;    }
    public void setDesignação(String designação) {  this.designacao = designação;    }
    public void setEndIP(InetAddress endIP) {   this.endIP = endIP;    }
    public void setNrHellos(int nrHellos) {     this.nrHellos = nrHellos;   }
    public void setRTTChegadaPDU(double RTTChegadaPDU) {  this.RTTChegadaPDU = RTTChegadaPDU;   }
    
    /*
    Metodos getters
    */
    public int getIdBackEnd() { return this.idBackEnd;   }
    public int getNrPerdas(){ return this.nrPerdas; }
    public int getPorta() { return this.porta;    }
    public int getNrCliAtv() {  return this.nrCliAtv;    }
    public String getDesignacao() { return this.designacao;    }
    public InetAddress getEndIP() { return this.endIP;    }
    public int getNrHellos() {  return this.nrHellos;    }
    public double getRTTChegadaPDU() {   return this.RTTChegadaPDU;    } 


    /*---------------------------------------------------------
        Metodos usados no controlo de atendimento Cliente   
    -------------------------------------------------------------*/
    public void incNrCliAtv(){
        this.lockVars.lock();   this.nrCliAtv++;   this.lockVars.unlock();
    }
    public void decNrCliAtv(){
        this.lockVars.lock();   this.nrCliAtv--;   this.lockVars.unlock();
    }
    public double getIndiceDisponibilidade(){
        double r = this.RTTChegadaProbes*0.3 + this.RTTChegadaPDU*0.1 +
                   this.nrPDUsLost*0.5       + this.nrCliAtv*0.1;
        return r;
    }
    /*---------------------------------------------------------
        Metodos usados no envio de PDU pelo MonitorHelloPDU    
    -------------------------------------------------------------*/
    public int getNrSeqPDU(){ return this.nrSeqPDU; }
    public void setLastPDUTime(long time){
        this.lockVars.lock();
        this.lastPDUTime = time; 
        this.lockVars.unlock();
    }
    public void incPDUsEnviados(){
        this.lockVars.lock();
        this.nrPDUsEnviados++;
        this.nrSeqPDU++;
        this.lockVars.unlock();
    }
    public void setEnviaPDU(int o){
        this.lockVars.lock();
        this.enviaPDU = o; 
        this.lockVars.unlock();
    }
    public void setEnviaProbe(int o){
        this.lockVars.lock();
        this.enviaProbes = o; 
        this.lockVars.unlock();
    }

    public synchronized int getEnviaPDU(){ return this.enviaPDU; }
    public synchronized int getEnviaProbe(){ return this.enviaProbes; }
    /*------------------------------------------------------------
    -------------------------------------------------------------*/

    /*---------------------------------------------------------
        Metodos usados no processo de probing no ReverseProxy       */
    public int getNrSeqProbing(){ return this.nrSeqProbing; }
    public double getLastProbeTime(){ return this.lastProbTime; }
    
    public boolean testaAtividade(){ 
        int r = nrProbsEnviados - nrProbesRecebidos;
        return r<4;
    }
    public void incProbesEnviados(){
        this.lockVars.lock();
        this.nrProbsEnviados++;
        this.nrSeqProbing++;
        this.lockVars.unlock();
    }
    public void setLastProbeTime(long time){
        this.lockVars.lock();
        this.lastProbTime = time; 
        this.lockVars.unlock();
    }
    public int getNrPerdasProbes(){ return this.nrProbesLost; }
    public int getNrProbesEnviados(){ return this.nrProbsEnviados; }
    /*------------------------------------------------------------
    -------------------------------------------------------------*/
}
