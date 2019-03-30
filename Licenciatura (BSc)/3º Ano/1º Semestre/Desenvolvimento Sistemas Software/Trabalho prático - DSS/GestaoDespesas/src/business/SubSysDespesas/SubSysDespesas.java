package business.SubSysDespesas;

import Exceptions.CategoriaExistsException;
import Exceptions.DespesaExistsException;
import data.CategoriaDAO;
import data.DespesaDAO;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SubSysDespesas {

    DespesaDAO despesas;
    CategoriaDAO categorias;

    public SubSysDespesas() {
        this.despesas = new DespesaDAO();
        this.categorias = new CategoriaDAO();
    }

    public void registarDespesa(String ref, Date dataLimite, Double preco, String categoria) throws DespesaExistsException {
        if (this.despesas.containsKey(ref)) {
            throw new DespesaExistsException();
        }
        Categoria c = this.categorias.get(categoria);
        Despesa d = null;
        if (c != null) {
            d = new Recorrente(ref, dataLimite, preco, c);
        }
        this.despesas.put(ref, d);
    }

    public void registarDespesa(String ref, Date dataLimite, Double preco, String designacao, Collection<String> inquilinos,
            Map<String, Double> percentagens, Map<String, Integer> prestacoes) throws DespesaExistsException {
        if (this.despesas.containsKey(ref)) {
            throw new DespesaExistsException();
        }
        Map<String, Set<Prestacao>> prestacoesInq = new TreeMap<>();
        for (Map.Entry<String, Integer> e : prestacoes.entrySet()) {
            Set<Prestacao> prests = new TreeSet<>();
            Double valorCadaPres = (preco * (percentagens.get(e.getKey()) / 100)) / e.getValue();
            for (int i = 0; i < e.getValue(); i++) {
                Prestacao p = new Prestacao(valorCadaPres, i + 1);
                prests.add(p);
            }
            prestacoesInq.put(e.getKey(), prests);
        }
        Despesa d = new Extraordinaria(ref, dataLimite, preco, designacao, inquilinos, percentagens, prestacoesInq);
        this.despesas.put(ref, d);
    }

    public Collection<Categoria> getListaCategorias() {
        return this.categorias.values();
    }

    public Map<String, Double> getPercentagensCategoria(String cat) {
        return this.categorias.get(cat).getPercentagens();
    }

    public void registarCategoria(String cat) throws CategoriaExistsException {
        if (this.categorias.containsKey(cat)) {
            throw new CategoriaExistsException();
        }
        Categoria c = new Categoria(cat);
        this.categorias.put(cat, c);
    }

    public Map<String, Double> getPercentagensCategorias(String inquilino) {
        return this.categorias.getPercentagensCategorias(inquilino);
    }

    public void associarCategoriaInquilino(String inquilino, String categoria, Double percentagem) {
        this.categorias.associarCategoriaInquilino(inquilino, categoria, percentagem);
    }

    public Collection<Despesa> getListaDespesas() {
        return this.despesas.values();
    }

    public Collection<Despesa> getListaDespesas(String user) {
        return this.despesas.getDespesas(user);
    }

    public Despesa getDespesa(String ref) {
        return this.despesas.get(ref);
    }

    public Collection<Despesa> getListaDespesasPorPagar(String user) {
        Collection<Despesa> c = this.despesas.getDespesas(user).stream().filter(d
                -> despesas.inquilinoPagou(user, d) == false).collect(Collectors.toCollection(TreeSet::new));
        Collection<Despesa> cs = new TreeSet<>();
        for (Despesa d : c) {
            if (d instanceof Extraordinaria) {
                if (((Extraordinaria) d).getPrestacoes().get(user).size() == 1) {
                    cs.add(d);
                }
            } else {
                cs.add(d);
            }
        }
        return cs;
    }

    public Double getValorAPagar(Despesa d, String inquilino) {
        Double percentagem = 0.0;
        Double valorTotal = d.getPreco();
        if (d instanceof Extraordinaria) {
            percentagem = ((Extraordinaria) d).getPercents().get(inquilino);
        } else {
            percentagem = ((Recorrente) d).getCategoria().getPercentagens().get(inquilino);
        }
        return (valorTotal * percentagem / 100);
    }

    public void pagarDespesa(String inquilino, Despesa d) {
        if (d instanceof Recorrente) {
            if (tudoPago(d)) {
                this.despesas.pagarDespesa(d);
            }
        } else {
            Extraordinaria e = (Extraordinaria) d;
            if (e.getPrestacoes().get(inquilino).size() == 1) {
                for (Prestacao p : e.getPrestacoes().get(inquilino)) {
                    this.despesas.pagarPrestacao(p, inquilino);
                }
            }
            this.despesas.pagarDespesa(inquilino, (Extraordinaria) d);
            if (tudoPago(d)) {
                this.despesas.pagarDespesa(d);

            }
        }
    }

    public Boolean tudoPago(Despesa d) {
        Collection<String> inquilinos = null;
        if (d instanceof Recorrente) {
            inquilinos = ((Recorrente) d).getCategoria().getPercentagens().keySet();
        } else {
            inquilinos = ((Extraordinaria) d).getPercents().keySet();
        }
        for (String s : inquilinos) {
            if (this.despesas.inquilinoPagou(s, d) == false) {
                return false;
            }
        }
        return true;
    }

    public Collection<Prestacao> getListaPrestacoesPorPagar(String user) {
        return this.despesas.getPrestacao(user).stream().filter(p -> p.getEstado() == false).collect(Collectors.toCollection(TreeSet::new));
    }

    public Despesa getDespesa(Prestacao p) {
        return this.despesas.getDespesaPrestacao(p);
    }

    public int prestQueFaltam(Prestacao p, String user) {
        Double total = getValorAPagar(getDespesa(p), user);
        Double valorPres = p.getValor();
        return (int) ((total / valorPres) - p.getNrSeq());
    }

    public void pagarPrestacao(Prestacao p, String user) {
        this.despesas.pagarPrestacao(p, user);
        if (getValorAPagar(getDespesa(p), user) == (p.getValor() * p.getNrSeq())) {
            this.despesas.pagarDespesa(getDespesa(p));
        }
    }
}
