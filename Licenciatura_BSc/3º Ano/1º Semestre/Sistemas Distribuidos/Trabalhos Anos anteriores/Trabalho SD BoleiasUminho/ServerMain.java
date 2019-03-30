/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Octavio
 */
public class ServerMain {
    
	private static Scanner in = new Scanner(System.in);
	private static int lerint() throws IOException{
		Integer ret =0;
		
		System.out.println("Erro ao ler porto.");
		System.out.println("Indique um porto válido: ");
		String inp = in.nextLine();
		try{
			ret = Integer.parseInt(inp);
		}catch(Exception e){
			ret = lerint();
		}
		return ret;
	}
	
	
    public static void main(String[] args) throws Exception {
    	int port;
    	try{
    		port = Integer.parseInt(args[0]);
    	}catch(Exception e){
    		port = lerint();
    	}
    	Servidor s  = new  Servidor(port);
    	System.out.println("Vou inicicar o servidor na porta " + port);
    	s.startServer();
    }
    
}
