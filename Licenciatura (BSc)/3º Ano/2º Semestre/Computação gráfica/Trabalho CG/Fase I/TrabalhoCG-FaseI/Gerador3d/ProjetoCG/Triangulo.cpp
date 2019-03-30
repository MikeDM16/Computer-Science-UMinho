#include "Triangulo.h"
#include "Vertice.h"

Triangulo::Triangulo(){}
Triangulo::Triangulo(Vertice v1, Vertice v2, Vertice v3){
	this->v1 = v1;
	this->v2 = v2;
	this->v3 = v3;
}

Vertice Triangulo::getV1() { return this->v1; }
Vertice Triangulo::getV2() { return this->v2; }
Vertice Triangulo::getV3() { return this->v3; }