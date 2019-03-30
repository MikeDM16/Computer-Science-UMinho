
import Exceptions.ClientExistsException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GestaoContas {

    private Map<String, Utilizador> utilizadores;
    private Lock lock;

    public GestaoContas() {
        this.utilizadores = new TreeMap<>();
        this.lock = new ReentrantLock();
    }

    public void registar(String username, String password) throws ClientExistsException {
        this.lock.lock();
        if (this.utilizadores.containsKey(username)) {
            throw new ClientExistsException("Utilizador " + username + " já registado!");
        }
        Utilizador u = new Utilizador(username, password);
        this.utilizadores.put(username, u);
        this.lock.unlock();
    }

    public boolean validarLogin(String username, String password) {
        boolean result;
        this.lock.lock();
        try {
            if (this.utilizadores.containsKey(username) == false) {
                result = false;
            } else {
                result = this.utilizadores.get(username).getPassword().equals(password);
            }
            return result;
        } finally {
            this.lock.unlock();
        }
    }

    public Utilizador getUtilizador(String username) {
        return this.utilizadores.get(username);
    }
}
