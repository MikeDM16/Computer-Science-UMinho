
import Exceptions.SemPermissaoException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GestaoLeiloes {

    private int countLeilao;
    private Map<Integer, Leilao> leiloes;
    private Lock lock;
    private Condition escrever;
    private Condition ler;
    private int nEscritores, nQuerEscrever;
    private int nLeitores;

    public GestaoLeiloes() {
        this.countLeilao = 0;
        this.leiloes = new TreeMap<>();
        this.lock = new ReentrantLock();
        this.escrever = this.lock.newCondition();
        this.ler = this.lock.newCondition();
        this.nEscritores = 0;
        this.nLeitores = 0;
        this.nQuerEscrever = 0;
    }

    public int registarLeilao(String username, String item, Double valor) throws InterruptedException {
        escreverLock();
        int idNovo = ++countLeilao;
        Leilao l = new Leilao(idNovo, username, item, valor);
        this.leiloes.put(idNovo, l);
        escreverUnlock();
        return idNovo;
    }

    public int licitar(int id, Double valor, Utilizador u, Log log) throws InterruptedException {
        int result;
        Leilao l = null;
        lerLock();
        if (this.leiloes.containsKey(id) == false) {
            result = 1;
            lerUnlock();
        } else {
            l = this.leiloes.get(id);
            l.lock();
            lerUnlock();
            if (l.isAtivo() == false) {
                result = 2;
            } else if (l.getVendedor().equals(u.getUsername())) {
                result = 4;
            } else if (l.getMaiorLicitacao() >= valor) {
                result = 3;
            } else {
                l.registarLicitacao(valor, u);
                result = 0;
            }
            l.unlock();
        }
        return result;
    }

    public Collection<Leilao> getLeiloesAtivos() throws InterruptedException {
        Collection<Leilao> leiloes = new TreeSet<>();
        lerLock();
        for (Leilao l : this.leiloes.values()) {
            l.lock();
            if (l.isAtivo()) {
                leiloes.add(l.clone());
            }
            l.unlock();
        }
        lerUnlock();
        return leiloes;
    }

    public void fecharLeilao(int idLeilao, String username) throws SemPermissaoException, InterruptedException {
        lerLock();
        if (this.leiloes.containsKey(idLeilao) == false) {
            lerUnlock();
            throw new SemPermissaoException("O leilão " + idLeilao + " não existe");
        }
        Leilao l = this.leiloes.get(idLeilao);
        l.lock();
        lerUnlock();
        if (l.isAtivo() == false || l.getVendedor().equals(username) == false) {
            l.unlock();
            throw new SemPermissaoException("Sem permissão para fechar");
        }
        l.fechar();
        l.unlock();
    }

    public void lerLock() throws InterruptedException {
        this.lock.lock();
        while (nEscritores > 0 || nQuerEscrever > 0) {
            ler.await();
        }
        nLeitores++;
        ler.signalAll();
        this.lock.unlock();
    }

    public void lerUnlock() {
        this.lock.lock();
        nLeitores--;
        if (nLeitores == 0) {
            escrever.signalAll();
        }
        this.lock.unlock();
    }

    public void escreverLock() throws InterruptedException {
        this.lock.lock();
        nQuerEscrever++;
        while (nEscritores + nLeitores > 0) {
            escrever.await();
        }
        nQuerEscrever--;
        nEscritores++;
        this.lock.unlock();
    }

    public void escreverUnlock() {
        this.lock.lock();
        nEscritores--;
        ler.signalAll();
        escrever.signalAll();
        this.lock.unlock();
    }
}
