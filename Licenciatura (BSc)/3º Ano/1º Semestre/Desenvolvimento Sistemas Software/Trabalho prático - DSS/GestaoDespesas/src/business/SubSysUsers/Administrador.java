package business.SubSysUsers;

public class Administrador extends Utilizador {

    private Boolean estado;

    public Administrador(String username, String nome, char[] password) {
        super(username, nome, password);
        this.estado = true;
    }

    public Administrador(String username, String nome, char[] password, Boolean estado) {
        super(username, nome, password);
        this.estado = estado;
    }

    public Boolean getEstado() {
        return estado;
    }

}
