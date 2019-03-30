 
import Exceptions.ClientExistsException;
import Exceptions.SemPermissaoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

class ServerReader implements Runnable {

    private Socket cs;
    private GestaoContas gc;
    private GestaoLeiloes gl;
    private Log log;
    private String username;

    public ServerReader(Socket cs, GestaoContas gc, GestaoLeiloes gl, Log log) throws IOException {
        this.cs = cs;
        this.gc = gc;
        this.gl = gl;
        this.log = log;
        this.username = null;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                try {
                    responder(line);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            in.close();
            cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responder(String line) throws InterruptedException {
        String[] data = line.split("[|]");
        String username, password, item, vendedor, maiorLicitador, mensagem;
        Double valor;
        int id, erro;
        boolean teste;
        Collection<Leilao> leiloesAtivos;
        switch (data[0]) {
            case "l":
                username = data[1];
                password = data[2];
                teste = gc.validarLogin(username, password);
                if (teste == true) {
                    log.add("OK");
                    this.username = username;
                    this.log.setQueue(this.gc.getUtilizador(username).getLog().getQueue());
                    System.out.println(">>>> " + this.username + " iniciou sessão");
                } else {
                    log.add("ERRO");
                }
                break;
            case "r":
                username = data[1];
                password = data[2];
                try {
                    gc.registar(username, password);
                    System.out.println(">>>> Novo registo: " + username);
                    log.add("OK");
                } catch (ClientExistsException e) {
                    log.add("ERRO");
                }
                break;
            case "i":
                item = data[1];
                valor = Double.valueOf(data[2]);
                id = this.gl.registarLeilao(this.username, item, valor);
                log.add(Integer.toString(id));
                break;
            case "li":
                id = Integer.parseInt(data[1]);
                valor = Double.valueOf(data[2]);
                erro = this.gl.licitar(id, valor, this.gc.getUtilizador(this.username), log);
                if (erro == 0) {
                    log.add("OK");
                } else if (erro == 1) {
                    log.add("ERRO1");
                } else if (erro == 2) {
                    log.add("ERRO2");
                } else if (erro == 3) {
                    log.add("ERRO3");
                } else {
                    log.add("ERRO4");
                }
                break;
            case "lis":
                leiloesAtivos = this.gl.getLeiloesAtivos();
                log.add(Integer.toString(leiloesAtivos.size()));
                for (Leilao l : leiloesAtivos) {
                    vendedor = l.getVendedor();
                    maiorLicitador = l.getMaiorLicitador();
                    if (vendedor.equals(this.username)) {
                        mensagem = "* " + l.toString();
                    } else if (maiorLicitador.equals(this.username)) {
                        mensagem = "+ " + l.toString();
                    } else if (l.isLicitador(this.username)) {
                        mensagem = "- " + l.toString();
                    } else {
                        mensagem = "  " + l.toString();
                    }
                    log.add(mensagem);
                }
                break;
            case "f":
                try {
                    this.gl.fecharLeilao(Integer.parseInt(data[1]), this.username);
                    log.add("OK");
                } catch (SemPermissaoException e) {
                    log.add("ERRO");
                }
                break;
            case "q":
                System.out.println(">>>> " + this.username + " saiu");
                log.add("q|");
                this.log.setQueue(new ArrayBlockingQueue<>(1000));
                break;
        }
    }
}

class ServerWriter implements Runnable {

    private Socket cs;
    private Log log;

    public ServerWriter(Socket cs, Log log) {
        this.cs = cs;
        this.log = log;

    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(cs.getOutputStream());
            String s;
            while ((s = log.get()) != null) {
                out.println(s);
                out.flush();
            }
            out.close();
            cs.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Servidor {

    public static void main(String[] args) {
        try {
            GestaoContas gc = new GestaoContas();
            GestaoLeiloes gl = new GestaoLeiloes();
            ServerSocket ss = new ServerSocket(9999);

            gc.registar("rui", "rui");
            gc.registar("diogo", "diogo");
            gc.registar("miguel", "miguel");
            gc.registar("esmeralda", "esmeralda");
            gl.registarLeilao("rui", "iPhone 5S", 200.0);
            gl.registarLeilao("diogo", "MacBook Pro", 1000.0);

            Socket cs = null;
            System.out.println(">> Porta 9999 aberta!");
            while ((cs = ss.accept()) != null) {
                System.out.println(">> Nova conexão: " + cs.getInetAddress().getHostAddress());
                Log l = new Log();
                Thread tRead = new Thread(new ServerReader(cs, gc, gl, l));
                Thread tWrite = new Thread(new ServerWriter(cs, l));
                tRead.start();
                tWrite.start();
            }
            ss.close();
        } catch (IOException | ClientExistsException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
