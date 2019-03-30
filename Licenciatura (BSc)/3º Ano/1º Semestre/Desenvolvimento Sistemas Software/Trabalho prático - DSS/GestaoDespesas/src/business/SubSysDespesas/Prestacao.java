package business.SubSysDespesas;

public class Prestacao implements Comparable<Prestacao> {

    private int id;
    private Double valor;
    private int nrSeq;
    private Boolean estado;

    public Prestacao(Double valor, int nrSeq) {
        this.valor = valor;
        this.nrSeq = nrSeq;
        this.estado = false;
    }

    public Prestacao(Double valor, int nrSeq, Boolean estado, int id) {
        this.valor = valor;
        this.nrSeq = nrSeq;
        this.estado = estado;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getValor() {
        return valor;
    }

    public int getNrSeq() {
        return nrSeq;
    }

    public Boolean getEstado() {
        return estado;
    }

    @Override
    public int compareTo(Prestacao o1) {
        int compare = o1.getNrSeq();
        return this.nrSeq - compare;
    }

}
