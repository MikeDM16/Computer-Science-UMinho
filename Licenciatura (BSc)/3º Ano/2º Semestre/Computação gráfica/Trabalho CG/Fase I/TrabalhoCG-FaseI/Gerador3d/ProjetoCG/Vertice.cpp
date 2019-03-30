#include <iostream>
#include "Vertice.h"
using namespace std;

Vertice::Vertice(){}

Vertice::Vertice(double x, double y, double z){
	this->x = x; this->y = y; this->z = z;
}

Vertice Vertice::esfericas(double raio, double alpha, double beta) {
	double x = raio * sin(alpha) * cos(beta);
	double y = raio * sin(beta);
	double z = raio * cos(alpha) * cos(beta);

	return Vertice(x, y, z);
}

Vertice Vertice::cilindricas(double raio, double alpha, double y) {
	double x = raio * sin(alpha);
	double z = raio * cos(alpha);
	return Vertice(x, y, z);
}
double Vertice::getX() {	return this->x;	}
double Vertice::getY() {	return this->y;	}
double Vertice::getZ() {	return this->z;	}


