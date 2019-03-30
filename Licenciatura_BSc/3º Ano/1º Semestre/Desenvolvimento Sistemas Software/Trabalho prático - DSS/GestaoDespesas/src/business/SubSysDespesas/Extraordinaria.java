package business.SubSysDespesas;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Extraordinaria extends Despesa {

    private String designacao;
    private Map<String, Double> percents;
    private Map<String, Boolean> estados;
    private Map<String, Set<Prestacao>> prestacoes;

    public Extraordinaria(String ref, Date dataLimite, Double preco, String designacao, Collection<String> inquilinos,
            Map<String, Double> percents, Map<String, Set<Prestacao>> prestacoes) {
        super(ref, dataLimite, preco);
        this.designacao = designacao;
        this.percents = percents;
        this.estados = new TreeMap<>();
        for (String s : inquilinos) {
            this.estados.put(s, false);
        }
        this.prestacoes = prestacoes;
    }

    public Extraordinaria(String ref, Date dataLimite, Date dataEmissao, Double preco, Boolean estado, String designacao,
            Map<String, Double> percents, Map<String, Boolean> estados, Map<String, Set<Prestacao>> prestacoes) {
        super(ref, dataLimite, dataEmissao, preco, estado);
        this.designacao = designacao;
        this.percents = percents;
        this.estados = estados;
        this.prestacoes = prestacoes;
    }

    public String getDesignacao() {
        return designacao;
    }

    public Map<String, Double> getPercents() {
        return percents;
    }

    public Map<String, Boolean> getEstados() {
        return estados;
    }

    public Map<String, Set<Prestacao>> getPrestacoes() {
        return prestacoes;
    }

}
