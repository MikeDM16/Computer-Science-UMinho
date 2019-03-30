package business.SubSysUsers;

import java.util.Date;

public class Inquilino extends Utilizador {

    private Date dataEntrada;
    private Date dataSaida;
    private Conta conta;

    public Inquilino(String username, String nome, char[] password) {
        super(username, nome, password);
        this.dataEntrada = new Date();
        this.dataSaida = null;
        this.conta = new Conta();
    }

    public Inquilino(String username, String nome, char[] password, Date dataEntrada, Date dataSaida, Conta conta) {
        super(username, nome, password);
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.conta = conta;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public Conta getConta() {
        return conta;
    }

}
