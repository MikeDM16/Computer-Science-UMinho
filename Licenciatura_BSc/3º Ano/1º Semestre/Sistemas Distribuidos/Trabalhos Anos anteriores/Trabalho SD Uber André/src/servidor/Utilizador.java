package servidor;


import java.io.Serializable;
public class Utilizador implements Serializable, Comparable<Utilizador>{

    protected String name;
    protected String username;
    protected String password;
    protected String licensePlate;
    protected String model;


    public Utilizador(){}

    public Utilizador(String name, String username, String password) {
        this(name, username, password, null, null);
    }

    public Utilizador(String name, String username, String password, String licensePlate, String model) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.licensePlate = licensePlate;
        this.model = model;
    }

    public Utilizador(Utilizador u){
        this.name = u.getName();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.licensePlate = u.getLicensePlate();
        this.model = u.getModel();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public boolean condutor(){
        return licensePlate !=null && model !=null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilizador that = (Utilizador) o;
        return username.equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Utilizador{");
        sb.append("name='").append(name).append('\'');
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", licensePlate='").append(licensePlate).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Utilizador o) {
        return this.username.compareTo(o.getUsername());
    }

    public Utilizador clone(){
        return new Utilizador(this);
    }

}
