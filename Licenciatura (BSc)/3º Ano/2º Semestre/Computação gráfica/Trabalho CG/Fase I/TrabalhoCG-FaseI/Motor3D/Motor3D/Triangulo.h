#include "Vertice.h"
#pragma once

class Triangulo{

	private:
		Vertice v1;
		Vertice v2;
		Vertice v3;
	public:
		/*Contrutor padrão */	
		Triangulo();
		/*Construtor da classe. Recebe como parametros os 3 vertices que vão
		dar origem ao triangulo*/
		Triangulo(Vertice v1, Vertice v2, Vertice v3);

		/*getters dos três vertices que compôe um triangulo*/
		Vertice Triangulo::getV1();
		Vertice Triangulo::getV2();
		Vertice Triangulo::getV3();
};