
public class Utilizador {

    private String username;
    private String password;
    private Log log;

    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.log = new Log();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Log getLog() {
        return log;
    }
}
