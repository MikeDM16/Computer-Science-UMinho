/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

/**
 *
 * @author joaosilva
 */
public interface UMinhoBoleiasIface {
    public static final String REGISTAUTILIZADOR = "REGISTAUTILIZADOR";
    public static final String AUTENTICAR = "AUTENTICAR";
    public static final String SOLICITARVIAGEM = "SOLICITARVIAGEM";
    public static final String DISPONIVELVIAGEM ="DISPONIVELVIAGEM";
    public static final String LOGOUT ="LOGOUT";
    
    public static final String OK = "OK";
    public static final String KO = "KO";

    public boolean registaUtilizador(String user, String pass); //pode sre throws
    public boolean autenticar(String user, String pass);
    public String solicitarViagem(String user, Local partida, Local destino); //negativo demora x a estar disponivel, positivo em x tempo esta no local
    public String disponivelViagem(String user, Local actual, String matricula, String modelo,double custoUnitario);
    public void logout(String username);
    
    
}
