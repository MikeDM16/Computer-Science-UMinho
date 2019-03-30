package business.SubSysUsers;

import java.util.Collection;

public class Conta {

    private int nrConta;
    private Double saldo;
    private Collection<Movimento> movimentos;

    public Conta() {
        this.saldo = 0.0;
    }

    public Conta(int nrConta, Double saldo, Collection<Movimento> movimentos) {
        this.nrConta = nrConta;
        this.saldo = saldo;
        this.movimentos = movimentos;
    }

    public int getNrConta() {
        return nrConta;
    }

    public Double getSaldo() {
        return saldo;
    }

}
