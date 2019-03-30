import java.net.*;
import java.util.*;
import java.io.*;

public class ControlTCP implements Runnable{
	private ServerSocket socketTCP;

	ControlTCP(ServerSocket socketTCP){ 
		this.socketTCP = socketTCP; }

	public void run(){
		Socket atender;
		try{
			while((atender = socketTCP.accept()) != null){
				PrintWriter out = new PrintWriter(atender.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(atender.getInputStream()));

				String pedido;
				while((pedido = in.readLine())!= null ){
					out.println("Recebeu backend Pedido: " + pedido);
				}

				out.close();
				in.close();
			}
		}catch(Exception e){
			System.out.println("Erro ControlTCP backend: " + e);
		}/*
		System.out.println("Á espera de atender clientes...");
		String pedidocliente;
		try{
			Socket socketTCP = new Socket("10.1.1.1", 9999);
			BufferedReader socketReverse = new BufferedReader( new InputStreamReader( socketTCP.getInputStream()));
			PrintWriter resultadoReverse = new PrintWriter(socketTCP.getOutputStream(), true);

			while((pedidocliente = socketReverse.readLine()) != null){
				resultadoReverse.println(pedidocliente);
				System.out.println("Respondeu ao pedido: " + pedidocliente);
			}			
		}catch(Exception e){
                System.out.println("Erro ControlTCP backendServer: " + e);
            } */
	}

}