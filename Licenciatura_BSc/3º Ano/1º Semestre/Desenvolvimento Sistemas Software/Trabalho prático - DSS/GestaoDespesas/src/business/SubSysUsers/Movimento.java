package business.SubSysUsers;

import java.util.Date;

public class Movimento implements Comparable<Movimento> {

    private String descricao;
    private Double valor;
    private Date data;
    private int id;
    private String refAssociada;

    public Movimento(String descricao, Double valor, Date data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.refAssociada = null;
    }

    public String getRefAssociada() {
        return refAssociada;
    }

    public Movimento(String descricao, Double valor, Date data, String refAssociada) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.refAssociada = refAssociada;
    }

    public Movimento(String descricao, Double valor, Date data, int id, String refAssociada) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.id = id;
        this.refAssociada = refAssociada;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public int compareTo(Movimento m1) {
        int compare = m1.getId();
        return compare - this.id;
    }
}
