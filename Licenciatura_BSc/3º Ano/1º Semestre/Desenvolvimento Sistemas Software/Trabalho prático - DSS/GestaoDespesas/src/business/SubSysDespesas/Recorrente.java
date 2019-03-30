package business.SubSysDespesas;

import java.util.Date;

public class Recorrente extends Despesa {

    private Categoria categoria;

    public Recorrente(String ref, Date dataLimite, Double preco, Categoria categoria) {
        super(ref, dataLimite, preco);
        this.categoria = categoria;
    }

    public Recorrente(String ref, Date dataLimite, Date dataEmissao, Double preco, Boolean estado, Categoria categoria) {
        super(ref, dataLimite, dataEmissao, preco, estado);
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
