/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ruifreitas
 */
public class ClienteMain implements UMinhoBoleiasIface {

	private String email;
	private boolean condutor;
	private Socket sock;
	private BufferedWriter out;
	private BufferedReader in;
	private static int sleepFactor = 10;

	private static Scanner input = new Scanner(System.in);

	public ClienteMain(String remotehost, int port) {
		try {
			sock = new Socket(remotehost, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean registaUtilizador(String user, String pass) {
		String l;
		try {
			out.write(REGISTAUTILIZADOR);
			out.newLine();
			out.write(user + ":" + pass);
			out.newLine();
			out.flush();
			l = in.readLine();
			if (l.equals(KO)) {
				System.out.println("ERRO-User name já existente");
				return false;
			}
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean autenticar(String user, String pass) {
		String l;
		try {
			out.write(AUTENTICAR);
			out.newLine();
			out.write(user + ":" + pass);
			out.newLine();
			out.flush();
			l = in.readLine();
			if (l.equals(KO)) {
				System.out.println("ERRO-Credenciais");
				return false;
			} else {
				this.email = user;
			}
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	@Override
	public String solicitarViagem(String user, Local partida, Local destino) {
		String linhaResposta;
		try {
			out.write(SOLICITARVIAGEM);
			out.newLine();
			out.write(user + ":" + partida.toString() + ":" + destino.toString());
			out.newLine();
			out.flush();

			// foi enviado o pedido para o serverconnection

			System.out.println("A espera de um condutor para efetuar a sua viagem");
			// vai ficar a espera de uma resposta por parte do servidor
			linhaResposta = in.readLine(); // user:matricula:modelo:?tempo
			String[] arr = linhaResposta.split(":");
			if (arr.length == 3) {
				// o condutar já encontra-se no de partida
				System.out.println("O Utilizador " + arr[0] + " com o carro de matricula " + arr[1] + " e modelo "
						+ arr[2] + " encontra-se disponível e já se encontra no local de partida");
			} else {
				// o condutor nao se encontra no local
				System.out.println("O Utilizador " + arr[0] + " com o carro de matricula " + arr[1] + " e modelo "
						+ arr[2] + " irá demorar " + (Double.parseDouble(arr[3]) * sleepFactor) + " segundos a apresentar-se no local de partida");
			}
			
			linhaResposta = in.readLine();

			// apos o serverconnection ter feito sleep do tempo de deslocacao do
			// condutor ate ao local de partida
			if (linhaResposta.equals(OK)) {
				if (arr.length == 4) {
					System.out.println("O Utilizador " + arr[0] + " com o carro de matricula " + arr[1] + " e modelo "
							+ arr[2] + " já chegou ao local de partida");
					System.out.println("Foi iniciada a viagem para o destino " + destino.toString());
				} else {
					System.out.println("Foi iniciada a viagem para o destino " + destino.toString());
				}
			}
			// apos o serverconnection ter fetio o sleep do tempo de viagem
			// tem de receber o custo
			linhaResposta = in.readLine();
			System.out.println("A viagem chegou ao destino, teve um custo de " + linhaResposta + "€");
		} catch (IOException ex) {
			return "Viagem nao realizada";
		}
		return "Viagem realizada com sucesso";
	}

	@Override
	public String disponivelViagem(String user, Local actual, String matricula, String modelo, double custoUnitario) {
		String linhaResposta;
		try {
			out.write(DISPONIVELVIAGEM);
			out.newLine();
			out.write(user + ":" + actual.toString() + ":" + matricula + ":" + modelo + ":" + custoUnitario);
			out.newLine();
			out.flush();

			System.out.println("A espera de solicitações de viagens");

			linhaResposta = in.readLine();
			String[] arr = linhaResposta.split(":");

			if (arr.length == 3) {
				// tem uma viagem para comprir apartir do local de onte está
				System.out.println("O Utilizador " + arr[0] + " encontra-se  no local em que se encontra " + arr[1]
						+ " a espera de boleia para o destino " + arr[2]);
			} else {
				// o condutor nao se encontra no local de partida
				System.out.println("O Utilizador " + arr[0] + " encontra-se  no local " + arr[1]
						+ " a espera de boleia para o destino " + arr[2]);
			}

			linhaResposta = in.readLine();
			
			// apos o serverconnection ter feito sleep do tempo de deslocacao do
			// condutor ate ao local de partida
			if (linhaResposta.equals(OK)) {
				if (arr.length == 4) {
					System.out.println("Já se encontra no local de partida " + arr[1]);
					System.out.println("Foi iniciada a viagem para o destino " + arr[2]);
				} else {
					System.out.println("Foi iniciada a viagem para o destino " + arr[2]);
				}
			}

			// apos o serverconnection ter fetio o sleep do tempo de viagem
			// tem de receber o custo
			linhaResposta = in.readLine();
			if(linhaResposta.equals(OK)){
				System.out.println("Chegou ao destino");
			}
		} catch (IOException ex) {
			return "Viagem nao realizada";
		}
		return "Viagem realizada com sucesso";
	}

	@Override
	public void logout(String username) {
		try {
			out.write(LOGOUT);
			out.newLine();
			out.flush();
			this.email = null;
		} catch (IOException e) {
			System.out.println("ERRO fazer logout");
			e.printStackTrace();
		}
	}

	private static String menuInicio() {
		StringBuilder ap = new StringBuilder();
		ap.append("=========Menu=========\n");
		ap.append("0-Sair\n");
		ap.append("1-Registar utilizador\n");
		ap.append("2-Login Utilizador\n");
		return ap.toString();
	}

	private static String menuCondutor() {
		StringBuilder ap = new StringBuilder();
		ap.append("=========Menu Condutor=========\n");
		ap.append("1-Disponibilizar boleia\n");
		ap.append("Outro-Fazer logout\n");
		return ap.toString();
	}

	private static String menuCliente() {
		StringBuilder ap = new StringBuilder();
		ap.append("=========Menu Cliente=========\n");
		ap.append("1-Solicitar boleia\n");
		ap.append("Outro-Fazer logout\n");
		return ap.toString();
	}

	private static Local lerLocal() {
		System.out.print("X-");
		int x = lerint();
		System.out.print("Y-");
		int y = lerint();
		return new Local(x, y);
	}

	private static int lerint() {
		Integer ret = 0;
		String inp = input.nextLine();
		try {
			ret = Integer.parseInt(inp);
		} catch (Exception e) {
			ret = lerint();
		}
		return ret;
	}

	public static void main(String[] args) {
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("ERRO ao Ler porta");
			port = lerint();
		}
		ClienteMain c1 = new ClienteMain(args[0], 6969);
		int n;

		System.out.println(menuInicio());
		while ((n = lerint()) != 0) {
			switch (n) {
			case 1: {
				System.out.println("=========Registar=========");
				System.out.println("Email");
				String email = input.nextLine();
				System.out.println("Password");
				String pass = input.nextLine();
				c1.registaUtilizador(email, pass);
				break;
			}
			case 2: {
				System.out.println("=========LOGIN=========");
				System.out.println("Email");
				String email = input.nextLine();
				System.out.println("Password");
				String pass = input.nextLine();
				boolean ret = c1.autenticar(email, pass);
				if(ret){
					System.out.println("1-Condutor \noutro-Cliente");
					if ((n = lerint()) == 1) {
						c1.condutor = true;
					} else {
						c1.condutor = false;
					}
				}
				break;
			}
			default:
				System.out.println("Insira um numero do menu");
			}
			
			while(c1.email != null) {
				if (c1.condutor) {
					System.out.println(menuCondutor());
					if ((n = lerint()) == 1) {
						System.out.println("=========Disponibilizar Viagem=========");
						if (c1.getEmail() != null) {
							System.out.println("Local de Atual");
							Local atual = lerLocal();
							System.out.println("Matricula : __-__-__");
							String matricula = input.nextLine();
							System.out.println("Modelo :");
							String modelo = input.nextLine();
							System.out.println("Custo da viagem (unitário):");
							double custoUnitario = input.nextDouble();
							c1.disponivelViagem(c1.getEmail(), atual, matricula, modelo, custoUnitario);
						} else {
							System.out.println("Necessita de estar autenticado");
						}
					} else {
						c1.logout(null);
					}
				} else {
					// menu do cliente
					System.out.println(menuCliente());
					if ((n = lerint()) == 1) {
						System.out.println("=========Solicitar Viagem=========");
						if (c1.getEmail() != null) {
							System.out.println("Local de Partida");
							Local partida = lerLocal();
							System.out.println("Local de Destino");
							Local destino = lerLocal();
							c1.solicitarViagem(c1.getEmail(), partida, destino);
						} else {
							System.out.println("Necessita de estar autenticado");
						}
					} else {
						c1.logout(null);
					}
				}
			}
			System.out.println(menuInicio());
		}
	}
}
