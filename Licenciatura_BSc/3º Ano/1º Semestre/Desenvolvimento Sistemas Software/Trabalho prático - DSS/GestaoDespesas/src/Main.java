
import business.SysApartamento;
import presentation.Login;

public class Main {

    public static void main(String args[]) {
        SysApartamento sysApart = new SysApartamento();
        Login login = new Login(sysApart);
        login.setVisible(true);
    }
}
