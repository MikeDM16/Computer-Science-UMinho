
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Leilao implements Comparable<Leilao> {

    private int idLeilao;
    private String vendedor;
    private String item;
    private Double valorBase;
    private Double maiorLicitacao;
    private String maiorLicitador;
    private boolean ativo;
    private Map<String, Double> licitacoes;
    private Map<String, Utilizador> licitadores;
    private Lock lock;

    public Leilao(int idLeilao, String vendedor, String item, Double valorBase) {
        this.idLeilao = idLeilao;
        this.vendedor = vendedor;
        this.item = item;
        this.valorBase = valorBase;
        this.maiorLicitacao = valorBase;
        this.maiorLicitador = new String();
        this.ativo = true;
        this.licitacoes = new TreeMap<>();
        this.licitadores = new TreeMap<>();
        this.lock = new ReentrantLock();
    }

    public Leilao(int idLeilao, String vendedor, String item, Double valorBase,
            Double maiorLicitacao, String maiorLicitador, Map<String, Double> licitacoes,
            Map<String, Utilizador> licitadores) {
        this.idLeilao = idLeilao;
        this.vendedor = vendedor;
        this.item = item;
        this.valorBase = valorBase;
        this.maiorLicitacao = maiorLicitacao;
        this.maiorLicitador = maiorLicitador;
        this.licitacoes = new TreeMap<>();
        for (Map.Entry<String, Double> e : licitacoes.entrySet()) {
            this.licitacoes.put(e.getKey(), e.getValue());
        }
        this.licitadores = new TreeMap<>();
        for (Map.Entry<String, Utilizador> e : licitadores.entrySet()) {
            this.licitadores.put(e.getKey(), e.getValue());
        }
        this.lock = new ReentrantLock();
    }

    public Double getMaiorLicitacao() {
        this.lock.lock();
        try {
            return this.maiorLicitacao;
        } finally {
            this.lock.unlock();
        }
    }

    public String getMaiorLicitador() {
        this.lock.lock();
        try {
            return this.maiorLicitador;
        } finally {
            this.lock.unlock();
        }
    }

    public String getVendedor() {
        return this.vendedor;
    }

    public Map<String, Double> getLicitacoes() {
        this.lock.lock();
        try {
            return licitacoes;
        } finally {
            this.lock.unlock();
        }
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public String getItem() {
        return item;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public boolean isAtivo() {
        this.lock.lock();
        try {
            return ativo;
        } finally {
            this.lock.unlock();
        }
    }

    public void registarLicitacao(Double valor, Utilizador u) {
        this.licitacoes.put(u.getUsername(), valor);
        if (this.licitadores.containsKey(u.getUsername()) == false) {
            this.licitadores.put(u.getUsername(), u);
        }
        if (this.maiorLicitacao < valor) {
            this.maiorLicitacao = valor;
            this.maiorLicitador = u.getUsername();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Número: " + this.getIdLeilao() + "   ");
        sb.append("Item: " + this.getItem() + "   ");
        sb.append("Valor base: " + this.getValorBase());
        return sb.toString();
    }

    public boolean isLicitador(String username) {
        this.lock.lock();
        boolean result;
        try {
            if ((this.licitacoes.containsKey(username)) == true) {
                result = true;
            } else {
                result = false;
            }
            return result;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int compareTo(Leilao o) {
        int compare = o.getIdLeilao() - this.idLeilao;
        return compare;
    }

    void fechar() {
        String message = new String();
        try {
            this.ativo = false;
            message = "f|" + " O leilao " + this.idLeilao + " do item "
                    + this.item + " foi encerrado pelo vendedor " + this.vendedor + ".";
            for (Map.Entry<String, Utilizador> e : this.licitadores.entrySet()) {
                if (e.getKey().equals(this.maiorLicitador)) {
                    e.getValue().getLog().add(message + " Parabéns! Venceu o leilão com uma "
                            + "licitação de " + this.maiorLicitacao);
                } else {
                    e.getValue().getLog().add(message + " O vencedor foi " + this.maiorLicitador
                            + " com uma licitação de " + this.maiorLicitacao);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    public Leilao clone() {
        return new Leilao(this.idLeilao, this.vendedor, this.item, this.valorBase,
                this.maiorLicitacao, this.maiorLicitador, this.licitacoes, this.licitadores);
    }
}
