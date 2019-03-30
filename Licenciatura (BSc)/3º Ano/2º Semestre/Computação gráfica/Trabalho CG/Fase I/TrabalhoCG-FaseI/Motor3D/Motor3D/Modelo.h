#include <vector>
#include "Triangulo.h"
#pragma once

class Modelo{
	
	private:
		std::vector<Triangulo> triangulos;

	public:
		/*Contrutor padrão */
		Modelo();
		/*Construtor da classe Modelo. Guarda o conjunto de triangulos que vão produzir
		o objeto tridimensional*/
		Modelo(std::vector<Triangulo> trianglinhos);

		/*Funçao get da varivel da classe triangulo*/
		std::vector<Triangulo> getTriangulos();

		/*Funçao responsavel por pegar num Modelo - esfera, caixa, etc - e escreve o seu conjunto 
		de vertice para um ficheiro com o nome especificado no 2º argumento*/
		static void escreveModelo(Modelo m, char * nameFile3d);

		/*Função que dada a medida do lado do quadrado, gera um plano em XZ,
		centrado na origem, feito a partir de um quadrado com 2 triangulos*/
		static void plane(double ladoSqr, char* nameFile3d);

		/*função que dada a largura, comprimento e altura de uma caixa, cria
		as várias faces da caixa usando 2 triangulos por face. */
		static void box(double largura, double compri, double altura, double nDiv, char* nameFile3d);
		
		/*Função que dado o raio, altura, numero de fatias e de stacks, gera os vários vertices que
		darão origem aos triangulos que compoem um cilindro*/
		static void cilindro(double raio, double altura, int fatias, int stacks, char* nameFile3d);

		/*Função que dado o raio, numero de fatias e de stacks, gera os vários vertices que
		darão origem aos triangulos que compoem uma esfera centrada na origem. 
		É usado o conceito de coordenadas esféricas, posteriormente convertidas para cartesianas.*/
		static void sphere(double raio, int fatias, int stacks, char * nameFile3d);
		
		/*Função que dado o raio, altura, numero de fatias e de stacks, gera os vários vertices que
		darão origem aos triangulos que compoem um cone
		É usado o conceito de coordenadas cilindricas, posteriormente convertidas para cartesianas.*/
		static void cone(double raio, double altura, int fatias, int stacks, char* nameFile3d);
};