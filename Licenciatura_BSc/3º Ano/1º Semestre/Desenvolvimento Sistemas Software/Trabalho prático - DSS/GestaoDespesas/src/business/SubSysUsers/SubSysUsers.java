package business.SubSysUsers;

import Exceptions.*;
import data.AdministradorDAO;
import data.InquilinoDAO;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class SubSysUsers {

    private InquilinoDAO inquilinos;
    private AdministradorDAO administrador;

    public SubSysUsers() {
        this.inquilinos = new InquilinoDAO();
        this.administrador = new AdministradorDAO();
    }

    public void registarInquilino(String username, String nome, char[] password) throws UserExistsException {
        if (this.inquilinos.containsKey(username) || this.administrador.containsKey(username)) {
            throw new UserExistsException();
        }
        Inquilino i = new Inquilino(username, nome, password);
        this.inquilinos.put(username, i);
    }

    public Boolean validarLogin(String username, char[] password) {
        char[] pass = null;
        if (this.administrador.containsKey(username)) {
            pass = this.administrador.getPassword(username);
            if (Arrays.equals(password, pass) == false) {
                return false;
            }
            return true;
        }
        if (this.inquilinos.containsKey(username)) {
            pass = this.inquilinos.getPassword(username);
            if (Arrays.equals(password, pass) == false) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Utilizador getUser(String username) {
        Utilizador u = null;
        if (this.administrador.containsKey(username)) {
            u = this.administrador.get(username);
        } else if (this.inquilinos.containsKey(username)) {
            u = this.inquilinos.get(username);
        }
        return u;
    }

    public void registarAdmin(String username, String nome, char[] password) throws UserExistsException {
        if (this.inquilinos.containsKey(username) || this.administrador.containsKey(username)) {
            throw new UserExistsException();
        }
        String ativo = this.administrador.getAdminAtivo();
        if (ativo != null) {
            this.administrador.remove(ativo);
        }
        Administrador a = new Administrador(username, nome, password);
        this.administrador.put(username, a);
    }

    public void editarUser(String username, String nome, char[] pass) {
        if (this.administrador.containsKey(username)) {
            this.administrador.update(username, nome, pass);
        } else if (this.inquilinos.containsKey(username)) {
            this.inquilinos.update(username, nome, pass);
        }
    }

    public Collection<Inquilino> getListaInquilinos() {
        return this.inquilinos.values();
    }

    public void removerInquilino(String username) {
        this.inquilinos.remove(username);
    }

    public Double getSaldo(String username) {
        return this.inquilinos.get(username).getConta().getSaldo();
    }

    public void depositarValor(String username, Double valor) {
        this.inquilinos.depositar(username, valor);
        Movimento m = new Movimento("Depósito", valor, new Date());
        this.inquilinos.registarMovimento(username, m);
    }

    public Collection<Movimento> getMovimentos(String username) {
        return this.inquilinos.getMovimentos(username);
    }

    public void descontarValor(String user, Double valor, String referencia, String message) {
        this.inquilinos.descontar(user, valor);
        Movimento m = new Movimento(message, valor, new Date(), referencia);
        this.inquilinos.registarMovimento(user, m);
    }
}
