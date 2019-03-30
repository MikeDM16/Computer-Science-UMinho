package business.SubSysUsers;

public class Utilizador {

    private String username;
    private String nome;
    private char[] password;

    public Utilizador(String username, String nome, char[] password) {
        this.username = username;
        this.nome = nome;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public char[] getPassword() {
        char[] pass = new char[this.password.length];
        System.arraycopy(this.password, 0, pass, 0, this.password.length);
        return password;
    }

}
