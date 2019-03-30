
/**
 * Escreva a descrição da classe radom aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */

import java.util.Random;

public class Random2 {

	public static void main(String[] args) {

		//instância um objeto da classe Random usando o construtor básico
		Random gerador = new Random();
	    
		//imprime sequência de 10 números inteiros aleatórios entre 0 e 25
	    for (int i = 0; i < 10; i++) {
	    	System.out.println(gerador.nextInt(26));
		 }
	}
}
