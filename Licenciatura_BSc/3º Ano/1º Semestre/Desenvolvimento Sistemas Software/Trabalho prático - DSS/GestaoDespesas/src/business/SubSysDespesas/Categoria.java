package business.SubSysDespesas;

import java.util.Map;
import java.util.TreeMap;

public class Categoria {

    private String designacao;
    private Map<String, Double> percentagens;

    public Categoria(String designacao) {
        this.designacao = designacao;
        this.percentagens = new TreeMap<>();
    }

    public Categoria(String designacao, Map<String, Double> percentagens) {
        this.designacao = designacao;
        this.percentagens = percentagens;
    }

    public String getDesignacao() {
        return designacao;
    }

    public Map<String, Double> getPercentagens() {
        return percentagens;
    }

}
