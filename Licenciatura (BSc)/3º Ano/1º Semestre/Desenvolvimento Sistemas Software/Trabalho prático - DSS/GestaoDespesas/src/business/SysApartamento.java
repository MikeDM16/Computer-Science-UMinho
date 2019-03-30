package business;

import Exceptions.CategoriaExistsException;
import Exceptions.DespesaExistsException;
import Exceptions.SemSaldoException;
import Exceptions.UserExistsException;
import business.SubSysDespesas.Categoria;
import business.SubSysDespesas.Despesa;
import business.SubSysDespesas.Prestacao;
import business.SubSysDespesas.SubSysDespesas;
import business.SubSysUsers.Administrador;
import business.SubSysUsers.Inquilino;
import business.SubSysUsers.Movimento;
import business.SubSysUsers.SubSysUsers;
import business.SubSysUsers.Utilizador;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SysApartamento {

    private String atualUser;
    private SubSysUsers subSysUsers;
    private SubSysDespesas subSysDespesas;

    public SysApartamento() {
        this.atualUser = new String();
        this.subSysUsers = new SubSysUsers();
        this.subSysDespesas = new SubSysDespesas();
    }
    
    public void setAtualUser(String username) {
        this.atualUser = username;
    }
    
    public Utilizador getAtualUser() {
        return getUser(atualUser);
    }

    public void registarInquilino(String username, String nome, char[] password) throws UserExistsException {
        this.subSysUsers.registarInquilino(username, nome, password);
    }

    public Boolean validarLogin(String username, char[] password) {
        return this.subSysUsers.validarLogin(username, password);
    }

    public Utilizador getUser(String username) {
        return this.subSysUsers.getUser(username);
    }

    public void registarAdmin(String username, String nome, char[] password) throws UserExistsException {
        this.subSysUsers.registarAdmin(username, nome, password);
    }

    public void editarAtualUser(String nome, char[] pass) {
        this.subSysUsers.editarUser(atualUser, nome, pass);
    }

    public Collection<Inquilino> getListaInquilinos() {
        return this.subSysUsers.getListaInquilinos();
    }

    public void removerInquilino(String username) {
        this.subSysUsers.removerInquilino(username);
    }

    public Double getSaldoAtualUser() {
        return this.subSysUsers.getSaldo(this.atualUser);
    }

    public void depositarValorAtualUser(Double valor) {
        this.subSysUsers.depositarValor(this.atualUser, valor);
    }

    public Collection<Movimento> getMovimentosAtualUser() {
        return this.subSysUsers.getMovimentos(this.atualUser);
    }

    public void registarDespesa(String ref, Date dataLimite, Double preco, String categoria) throws DespesaExistsException {
        this.subSysDespesas.registarDespesa(ref, dataLimite, preco, categoria);
    }

    public void registarDespesa(String ref, Date dataLimite, Double preco, String designacao, List<String> inquilinos,
            Map<String, Double> inPer, Map<String, Integer> inPrest) throws DespesaExistsException {
        this.subSysDespesas.registarDespesa(ref, dataLimite, preco, designacao, inquilinos, inPer, inPrest);
    }

    public Collection<Categoria> getListaCategorias() {
        return this.subSysDespesas.getListaCategorias();
    }

    public Map<String, Double> getPercentagensCategoria(String cat) {
        return this.subSysDespesas.getPercentagensCategoria(cat);
    }

    public void registarCategoria(String cat) throws CategoriaExistsException {
        this.subSysDespesas.registarCategoria(cat);
    }

    public Map<String, Double> getPercentagensCategorias(String inquilino) {
        return this.subSysDespesas.getPercentagensCategorias(inquilino);
    }

    public void associarCategoriaInquilino(String inquilino, String categoria, Double percentagem) {
        this.subSysDespesas.associarCategoriaInquilino(inquilino, categoria, percentagem);
    }

    public Collection<Despesa> getListaDespesas() {
        Utilizador c = getUser(atualUser);
        if (c instanceof Administrador) {
            return this.subSysDespesas.getListaDespesas();
        } else {
            return this.subSysDespesas.getListaDespesas(atualUser);
        }
    }

    public Despesa getDespesa(String ref) {
        return this.subSysDespesas.getDespesa(ref);
    }

    public Collection<Despesa> getListaDespesasPorPagar() {
        return this.subSysDespesas.getListaDespesasPorPagar(atualUser);
    }

    public Double getValorAPagarAtualUser(Despesa d) {
        return this.subSysDespesas.getValorAPagar(d, atualUser);
    }

    public void pagarDespesa(Despesa d) throws SemSaldoException {
        Double saldo = this.subSysUsers.getSaldo(atualUser);
        Double valor = this.subSysDespesas.getValorAPagar(d, atualUser);
        if (valor > saldo) {
            throw new SemSaldoException();
        }
        this.subSysUsers.descontarValor(atualUser, valor, d.getRef(), "Pagamento despesa");
        this.subSysDespesas.pagarDespesa(atualUser, d);
    }

    public Collection<Prestacao> getListaPrestacoesPorPagar() {
        return this.subSysDespesas.getListaPrestacoesPorPagar(atualUser);
    }

    public Despesa getDespesa(Prestacao p) {
        return this.subSysDespesas.getDespesa(p);
    }

    public void pagarPrestacao(Prestacao p) throws SemSaldoException {
        Double saldo = this.subSysUsers.getSaldo(atualUser);
        if (p.getValor() > saldo) {
            throw new SemSaldoException();
        }
        this.subSysUsers.descontarValor(atualUser, p.getValor(), Integer.toString(p.getId()), "Pagamento prestação");
        this.subSysDespesas.pagarPrestacao(p, atualUser);
    }

    public void desautenticar() {
        this.atualUser = null;
    }
}
