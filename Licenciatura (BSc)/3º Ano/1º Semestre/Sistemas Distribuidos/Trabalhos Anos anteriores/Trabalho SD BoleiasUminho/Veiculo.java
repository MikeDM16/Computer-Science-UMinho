/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

/**
 *
 * @author Octavio
 */
public class Veiculo {
    
    private String modelo;
    private String matricula;

    public Veiculo(String modelo, String matricula) {
        this.modelo = modelo;
        this.matricula = matricula;
    }
    
    public String getModelo() {
        return modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
}
