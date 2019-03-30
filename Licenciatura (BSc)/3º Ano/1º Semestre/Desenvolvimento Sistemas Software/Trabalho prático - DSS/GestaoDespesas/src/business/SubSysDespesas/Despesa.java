package business.SubSysDespesas;

import java.util.Date;

public class Despesa implements Comparable<Despesa> {

    private String ref;
    private Date dataLimite;
    private Date dataEmissao;
    private Double preco;
    private Boolean estado;

    public Despesa(String ref, Date dataLimite, Double preco) {
        this.ref = ref;
        this.dataLimite = dataLimite;
        this.dataEmissao = new Date();
        this.preco = preco;
        this.estado = false;
    }

    public Despesa(String ref, Date dataLimite, Date dataEmissao, Double preco, Boolean estado) {
        this.ref = ref;
        this.dataLimite = dataLimite;
        this.dataEmissao = dataEmissao;
        this.preco = preco;
        this.estado = estado;
    }

    public String getRef() {
        return ref;
    }

    public Date getDataLimite() {
        return dataLimite;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public Double getPreco() {
        return preco;
    }

    public Boolean getEstado() {
        return estado;
    }

    @Override
    public int compareTo(Despesa o1) {
        return o1.getRef().compareTo(this.ref);
    }

}
