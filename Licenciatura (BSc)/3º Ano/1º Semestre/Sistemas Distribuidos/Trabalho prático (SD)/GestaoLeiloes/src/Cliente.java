
import java.io.*;
import java.util.*;
import java.net.*;

class ClientWritter implements Runnable {

    private Socket cs;
    private PrintWriter out;
    private String usernameLog;
    private Log log;

    public ClientWritter(Socket cs, Log log) throws IOException {
        this.cs = cs;
        this.log = log;
        this.out = new PrintWriter(cs.getOutputStream());
    }

    public void run() {
        try {
            menuHUB(MENU.MAIN);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void menuHUB(MENU menuDestino) throws IOException, InterruptedException {
        MENU menu = menuDestino;
        while (menu != MENU.QUIT) {
            switch (menu) {
                case MAIN:
                    menu = mainMenu();
                    break;
                case LOGIN:
                    menu = loginMenu();
                    break;
                case REGISTAR:
                    menu = registerMenu();
                    break;
                case PAGINA:
                    menu = paginaMENU();
                    break;
                case INICIAR:
                    menu = iniciarMENU();
                    break;
                case LICITAR:
                    menu = licitarMENU();
                    break;
                case LISTAR:
                    menu = listarMENU();
                    break;
                case FECHAR:
                    menu = fecharMENU();
                    break;
                case QUIT:
                    break;
            }
        }
        out.println("q");
        out.flush();
    }

    private int readChoice() {
        boolean escolhaOk = false;
        int escolha = 0;
        System.out.print(" Escolha uma opção: ");
        while (!escolhaOk) {
            try {
                Scanner sc = new Scanner(System.in);
                escolha = sc.nextInt();
                escolhaOk = true;
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida. Escolha outra: ");
            }
        }
        return escolha;
    }

    private void limpaTerminal() {
        for (int i = 0; i < 35; i++) {
            System.out.println("");
        }
    }

    private void cabecalho() {
        System.out.println(" --------------------------------------------------------------");
        System.out.println("|                                                              |");
        System.out.println("|                      SISTEMA LEILÔES                         |");
        System.out.println("|                                                              |");
        System.out.println(" --------------------------------------------------------------");
    }

    private MENU mainMenu() {
        int escolha = 0;
        limpaTerminal();
        cabecalho();
        System.out.println("(1) - Iniciar sessão");
        System.out.println("(2) - Fazer registo");
        System.out.println("(0) - Sair");
        escolha = readChoice();
        switch (escolha) {
            case 1:
                return MENU.LOGIN;
            case 2:
                return MENU.REGISTAR;
            case 0:
                return MENU.QUIT;
            default:
                return MENU.MAIN;
        }
    }

    private MENU loginMenu() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int choice = 0;
        String username, password, serverAnswer;
        boolean loginOk = false;
        Scanner sc;
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTAO LEILÕES > LOGIN");
        System.out.println("---------------------------------------------------------------");
        while (!loginOk) {
            sc = new Scanner(System.in);
            System.out.print("    Username: ");
            username = sc.nextLine();
            System.out.print("    Password: ");
            password = sc.nextLine();
            out.println("l" + "|" + username + "|" + password);
            out.flush();
            serverAnswer = log.get();
            if (serverAnswer.equalsIgnoreCase("OK")) {
                loginOk = true;
                usernameLog = username;
            } else {
                System.out.println("O username não existe ou a passowrd está errada!");
                System.out.println("---------------------------------------------------------------");
                System.out.println("1 - Tentar Novo         ");
                System.out.println("0 - Main menu | 9 - Quit");
                System.out.println("---------------------------------------------------------------");
                choice = readChoice();
                switch (choice) {
                    case 0:
                        return MENU.MAIN;
                    case 1:
                        return MENU.LOGIN;
                    case 9:
                        return MENU.QUIT;
                    default:
                        return MENU.LOGIN;
                }
            }
        }
        return MENU.PAGINA;
    }

    private MENU registerMenu() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int escolha = 0;
        String username = null, password = null, serverAnswer;
        boolean registoOK = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTÃO LEILÕES > REGISTAR");
        System.out.println("---------------------------------------------------------------");
        while (!registoOK) {
            System.out.print("    Username: ");
            username = sc.nextLine();
            System.out.print("    Password: ");
            password = sc.nextLine();
            out.println("r" + "|" + username + "|" + password);
            out.flush();
            serverAnswer = log.get();
            if (serverAnswer.equalsIgnoreCase("OK")) {
                System.out.println("Registado com sucesso!");
                registoOK = true;
            } else {
                System.out.println("Erro no registo.");
            }
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("0 - Menu anterior | 9 - Sair");
        System.out.println("---------------------------------------------------------------");
        escolha = readChoice();
        switch (escolha) {
            case 0:
                return MENU.MAIN;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.REGISTAR;
        }
    }

    private MENU paginaMENU() throws IOException {
        limpaTerminal();
        cabecalho();
        System.out.println("\t-- Olá " + usernameLog + " --");
        System.out.println("---------------------------------------------------------------");
        System.out.println("1 - Inicializar leilão  ");
        System.out.println("2 - Listar leilões      ");
        System.out.println("3 - Licitar item        ");
        System.out.println("4 - Finalizar leilão    ");
        System.out.println("0 - Main Menu | 9 - Quit");
        System.out.println("---------------------------------------------------------------");
        int choice = readChoice();
        switch (choice) {
            case 0:
                return MENU.MAIN;
            case 1:
                return MENU.INICIAR;
            case 2:
                return MENU.LISTAR;
            case 3:
                return MENU.LICITAR;
            case 4:
                return MENU.FECHAR;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.PAGINA;
        }
    }

    private MENU iniciarMENU() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int escolha = 0;
        String serverAnswer, item;
        Double valorBase;
        Scanner sc = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTÃO LEILÕES > INICIAR LEILÃO");
        System.out.println("---------------------------------------------------------------");
        System.out.print("    Descrição do item: ");
        item = sc.nextLine();
        System.out.print("    Valor base: ");
        valorBase = sc.nextDouble();
        out.println("i" + "|" + item + "|" + valorBase);
        out.flush();
        serverAnswer = log.get();
        System.out.println("Registado com sucesso! Id de registo: " + serverAnswer);
        System.out.println("---------------------------------------------------------------");
        System.out.println("0 - Menu anterior | 9 - Sair");
        System.out.println("---------------------------------------------------------------");
        escolha = readChoice();
        switch (escolha) {
            case 0:
                return MENU.PAGINA;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.PAGINA;
        }
    }

    private MENU licitarMENU() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int escolha = 0, idLeilao;
        String serverAnswer;
        Double valor;
        boolean ok = false;
        Scanner sc = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTÃO LEILÕES > LICITAR ITEM");
        System.out.println("---------------------------------------------------------------");
        while (!ok) {
            System.out.print("    Número do leilão: ");
            idLeilao = sc.nextInt();
            System.out.print("    Valor: ");
            valor = sc.nextDouble();
            out.println("li" + "|" + idLeilao + "|" + valor);
            out.flush();
            serverAnswer = log.get();
            if (serverAnswer.equalsIgnoreCase("OK")) {
                System.out.println("Licitação efetuada!");
                ok = true;
            } else if (serverAnswer.equalsIgnoreCase("ERRO1")) {
                System.out.println("Leilão não existe. Insira outro.");
            } else if (serverAnswer.equalsIgnoreCase("ERRO2")) {
                System.out.println("Leilão terminado.");
                ok = true;
            } else if (serverAnswer.equalsIgnoreCase("ERRO3")) {
                System.out.println("Leilão encontrado, mas valor não é "
                        + "superior à licitação atual.");
                ok = true;
            } else if (serverAnswer.equalsIgnoreCase("ERRO4")) {
                System.out.println("Leilão encontrado, mas não pode"
                        + "licitar um leilão do qual é vendedor.");
                ok = true;
            }
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("0 - Menu anterior | 9 - Sair");
        System.out.println("---------------------------------------------------------------");
        escolha = readChoice();
        switch (escolha) {
            case 0:
                return MENU.PAGINA;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.PAGINA;
        }
    }

    private MENU listarMENU() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int escolha = 0, count;
        String serverAnswer;
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTÃO LEILÕES > LISTAR LEILÕES");
        System.out.println("---------------------------------------------------------------");
        out.println("lis");
        out.flush();
        count = Integer.parseInt(log.get());
        if (count == 0) {
            System.out.println("Não há leilões a listar.");
        }
        while (count > 0) {
            serverAnswer = log.get();
            System.out.println(serverAnswer);
            count--;
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("0 - Menu anterior | 9 - Sair");
        System.out.println("---------------------------------------------------------------");
        escolha = readChoice();
        switch (escolha) {
            case 0:
                return MENU.PAGINA;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.PAGINA;
        }
    }

    private MENU fecharMENU() throws IOException, InterruptedException {
        limpaTerminal();
        cabecalho();
        int escolha = 0, idLeilao;
        boolean ok = false;
        String serverAnswer;
        Scanner sc = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------");
        System.out.println("GESTÃO LEILÕES > FECHAR LEILÃO");
        System.out.println("---------------------------------------------------------------");
        while (!ok) {
            System.out.print("    Número do leilão: ");
            idLeilao = sc.nextInt();
            out.println("f" + "|" + idLeilao);
            out.flush();
            serverAnswer = log.get();
            if (serverAnswer.equalsIgnoreCase("ERRO")) {
                System.out.println("Não existe nenhum leilão ativo com o número "
                        + "indicado ou não tem permissão!");
                ok = true;
            } else {
                System.out.println("Leilão fechado!");
                ok = true;
            }
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("0 - Menu anterior | 9 - Sair");
        System.out.println("---------------------------------------------------------------");
        escolha = readChoice();
        switch (escolha) {
            case 0:
                return MENU.PAGINA;
            case 9:
                return MENU.QUIT;
            default:
                return MENU.PAGINA;
        }
    }

    enum MENU {
        MAIN, LOGIN, REGISTAR, PAGINA, INICIAR, LISTAR, LICITAR, FECHAR, QUIT, SAIR
    }
}

class ClientReader implements Runnable {

    private Socket cs;
    private Log log;

    public ClientReader(Socket cs, Log log) throws IOException {
        this.cs = cs;
        this.log = log;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String s;
            String[] data;
            while ((s = in.readLine()) != null) {
                data = s.split("[|]");
                if (data[0].equals("f")) {
                    System.out.print("\n" + data[1] + "\n");
                } else if (data[0].equals("q")) {
                    log.add("OK");
                    break;
                } else {
                    log.add(data[0]);
                }
            }
            in.close();
            cs.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Cliente {

    public static void main(String[] args) {
        try {
            Log log = new Log();
            Socket cs = new Socket("127.0.0.1", 9999);
            Thread tWrite = new Thread(new ClientWritter(cs, log));
            Thread tRead = new Thread(new ClientReader(cs, log));
            tWrite.start();
            tRead.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
