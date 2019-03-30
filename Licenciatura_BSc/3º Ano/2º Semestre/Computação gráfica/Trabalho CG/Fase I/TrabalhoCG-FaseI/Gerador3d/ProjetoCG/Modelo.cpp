#include <iostream>
#include <vector>
#include <cstdlib>
#include <fstream>
#include <string>
#include "Modelo.h"
#include "Vertice.h"
using namespace std;

#define _USE_MATH_DEFINES 
#include <math.h>

/*Construtor da classe*/
Modelo::Modelo(){}
/*Construtor da classe com parametros*/
Modelo::Modelo(std::vector<Triangulo> trianglinhos){
	this->triangulos = trianglinhos;
}

std::vector<Triangulo> Modelo:: getTriangulos(){
	return this->triangulos;
}

void Modelo::escreveModelo(Modelo m, char* nameFile3d) {
	std::ofstream outfile3d(nameFile3d);
	std::vector<Triangulo> tris = m.getTriangulos();

	for (auto iter : tris) {
		Vertice v1 = iter.getV1();
		Vertice v2 = iter.getV2();
		Vertice v3 = iter.getV3();
		outfile3d << v1.getX() << " " << v1.getY() << " " << v1.getZ() << ", "
			<< v2.getX() << " " << v2.getY() << " " << v2.getZ() << ", "
			<< v3.getX() << " " << v3.getY() << " " << v3.getZ() << endl;
	}
	outfile3d.close();

	cout << "Escrita concluida" << endl;
}


void Modelo::cone(double raio, double altura, int fatias, int stacks, char* nameFile3d) {
	double angFatias = 2 * M_PI / fatias;
	double paramY = altura / stacks;

	std::vector<Triangulo> tris;
	double iterY, yinf, ysup, angulo;
	yinf = - altura / 2;

	double rinf, rsup, r = 0;
	for (iterY = 0; iterY < stacks; iterY++) {
		// iterar o eixo dos YY para as stacks 
		ysup = yinf + paramY;
		for (angulo = 0; angulo < 2 * M_PI; angulo += angFatias) {
			// iteras as várias fatias
			rinf = raio - raio*iterY / stacks;
			rsup = raio - (raio*(iterY+1)) / stacks;
			Vertice v1 = Vertice::cilindricas(rinf, angulo, yinf);
			Vertice v2 = Vertice::cilindricas(rinf, angulo + angFatias, yinf);
			Vertice v3 = Vertice::cilindricas(rsup, angulo, ysup);
			Vertice v4 = Vertice::cilindricas(rsup, angulo + angFatias, ysup);

			tris.insert(tris.end(), { Triangulo(v2,v4,v3), Triangulo(v2,v3,v1) });
			if (yinf == -altura / 2) {
				//Condição para criar os triangulos da base do cone
				Vertice o = Vertice(0, -altura / 2, 0); // Vertice do centro do circulo da base do cone
				tris.insert(tris.end(), { Triangulo(v1,o,v2)});
			}
		}
		yinf = ysup;
	}

	Modelo cone = Modelo(tris);
	escreveModelo(cone, nameFile3d);
	cout << "O modelo do cone foi criado com sucesso no ficheiro "
		<< nameFile3d << "." << endl;
}

void Modelo::sphere(double raio, int fatias, int stacks, char* nameFile3d) {
	double angStacks = M_PI / stacks;
	double angSlices = 2 * M_PI / fatias;
	std::vector<Triangulo> tris;
	double alpha, beta;
	for (beta = M_PI; beta > -M_PI; beta -= angStacks) {
		//Iterar o eixo dos Y, de cima para baixo, conforme o numero de stacks
		for (alpha = 0; alpha < 2 * M_PI; alpha+=angSlices) {
			//Iterar cada nivel stack, rodando de 0 a 2*pi para criar os triangulos das fatias 
			Vertice v1 = Vertice::esfericas(raio, alpha, beta);
			Vertice v2 = Vertice::esfericas(raio, alpha + angSlices, beta);
			Vertice v3 = Vertice::esfericas(raio, alpha + angSlices, beta + angStacks);
			Vertice v4 = Vertice::esfericas(raio, alpha, beta + angStacks);
			tris.insert(tris.end(), { Triangulo(v2,v3,v4), Triangulo(v2,v4,v1)});
		}
	}

	Modelo esfera = Modelo(tris);
	escreveModelo(esfera, nameFile3d);
	cout << "O modelo da esfera foi criado com sucesso no ficheiro "
		<< nameFile3d << "." << endl;
}

void Modelo::cilindro(double raio, double altura, int fatias, int stacks, char* nameFile3d) {
	double angFatias = 2 * M_PI / fatias; 
	double paramY = altura / stacks;
	double yinf, ysup, ang = 0, x1,x2,z1,z2;
	int iterY;
	std::vector<Triangulo> tris;

	double y = altura / 2;

	yinf = -altura / 2;
	for (iterY = 0; iterY < stacks; iterY++) {
		//iterar o eixo dos Y, conforme o numero de stacks
		ysup = yinf + paramY;
		for (ang = 0; ang < 2 * M_PI; ang += angFatias) { 
			//rodar desde 0 a 2*pi cada nivel stack para compor os triangulos
			x1 = raio*cos(ang);
			x2 = raio*cos(ang + angFatias);
			z1 = raio*sin(ang);
			z2 = raio* sin(ang + angFatias);
			//Considerando cada stack como um anel, v1 e v2 vão estar na circunferencia inferior
			Vertice v1 = Vertice(x1, yinf, z1);
			Vertice v2 = Vertice(x2, yinf, z2);
			//Considerando cada stack como um anel, v3 e v4 vão estar na circunferencia superior
			Vertice v3 = Vertice(x1, ysup, z1);
			Vertice v4 = Vertice(x2, ysup, z2);
			tris.insert(tris.end(), { Triangulo(v1,v4, v2), Triangulo(v1,v3,v4)});
			if (yinf == -y) {
				//Condição para gerar a face inferior do cilindro
				Vertice b1 = Vertice(0, yinf, 0); //Vertice do centro do circulo da base do cilindro
				Vertice b2 = Vertice(x1, yinf, z1);
				Vertice b3 = Vertice(x2, yinf, z2);
				tris.insert(tris.end(), { Triangulo(b3,b1,b2) }); // Face inferior
			}
			if (iterY == stacks -1 ) {
				//Condição para gerar a face superior do cilindro
				Vertice v1 = Vertice(0, ysup, 0); //Vertice do centro do circulo do teto do cilindro
				Vertice v2 = Vertice(x1, ysup, z1);
				Vertice v3 = Vertice(x2, ysup, z2);
				tris.insert(tris.end(), {Triangulo(v2,v1,v3) });// Face superior
			}
		}
		yinf = ysup;
	}

	Modelo cilindro = Modelo(tris);
	escreveModelo(cilindro, nameFile3d);
	cout << "O modelo do cilindro foi criado com sucesso no ficheiro "
		<< nameFile3d << "." << endl;
}

void Modelo::box(double largura, double compri, double altura, double nDiv, char* nameFile3d) {

	double x = largura / 2, y = altura / 2, z = compri / 2;
	double paramY = (altura / nDiv);
	double paramX = largura / nDiv;
	double paramZ = compri / nDiv;

	std::vector<Triangulo> tris;
	int iterX, iterY, iterZ;
	double xinf, xsup, yinf, ysup, zinf, zsup;
	
	/*ciclo no eixo do y para criar as stacks*/
	yinf = -y;
	for (iterY = 0; iterY != nDiv; iterY++) {
		ysup = yinf + paramY;
		/*ciclo no eixo do x para criar as faces frontal e traseira*/
		xinf = -x;
		for (iterX = 0; iterX != nDiv; iterX++) {
			xsup = xinf + paramX;
			/*Vertices da base inferior*/
			Vertice b1 = Vertice(xinf, yinf, z);
			Vertice b2 = Vertice(xsup, yinf, z);
			Vertice b3 = Vertice(xsup, yinf, -z);
			Vertice b4 = Vertice(xinf, yinf, -z);
			/*Vertice da base superior*/
			Vertice s1 = Vertice(xinf, ysup, z);
			Vertice s2 = Vertice(xsup, ysup, z);
			Vertice s3 = Vertice(xsup, ysup, -z);
			Vertice s4 = Vertice(xinf, ysup, -z);

			tris.insert(tris.end(),
			{ Triangulo(b2,s1,b1), Triangulo(b2,s2,s1), // Face frontal
			  Triangulo(b4,s4,s3), Triangulo(b4,s3,b3) }); //face traseira 
			xinf = xsup;
			}

		/*ciclo no eixo do Z para criar as faces esquerda e direita*/
		zinf = -z;
		for (iterZ = 0; iterZ != nDiv; iterZ++) {
			zsup = zinf + paramZ;
			/*Vertices da base inferior*/
			Vertice b1 = Vertice(-x, yinf, zsup);
			Vertice b2 = Vertice(x, yinf, zsup);
			Vertice b3 = Vertice(x, yinf, zinf);
			Vertice b4 = Vertice(-x, yinf, zinf);
			/*Vertice da base superior*/
			Vertice s1 = Vertice(-x, ysup, zsup);
			Vertice s2 = Vertice(x, ysup, zsup);
			Vertice s3 = Vertice(x, ysup, zinf);
			Vertice s4 = Vertice(-x, ysup, zinf);

			tris.insert(tris.end(),
			{ Triangulo(b1,s1,s4), Triangulo(b1,s4,b4), // Face esquerda
			  Triangulo(b3,s3,s2), Triangulo(s2,b2,b3) }); //face direita 
			zinf = zsup;
		}

		yinf = ysup; // subir um camada na stack 
		}

	/*Ciclo nos eixos X e Z para criar as faces superior e inferior do cubo*/
	xinf = -x;
	for (iterX = 0; iterX != nDiv; iterX++) {
		xsup = xinf + paramX;
		zinf = -z;
		for (iterZ = 0; iterZ != nDiv; iterZ++) {
			zsup = zinf + paramZ;
			/*Vertices da base inferior*/
			Vertice b1 = Vertice(xinf, -y, zsup);
			Vertice b2 = Vertice(xsup, -y, zsup);
			Vertice b3 = Vertice(xsup, -y, zinf);
			Vertice b4 = Vertice(xinf, -y, zinf);
			/*Vertice da base superior*/
			Vertice s1 = Vertice(xinf, y, zsup);
			Vertice s2 = Vertice(xsup, y, zsup);
			Vertice s3 = Vertice(xsup, y, zinf);
			Vertice s4 = Vertice(xinf, y, zinf);

			tris.insert(tris.end(),
			{ Triangulo(s2,s3,s4), Triangulo(s2,s4,s1), // Face superior
				Triangulo(b1,b4,b3), Triangulo(b1,b3,b2) }); //face inferior 
			zinf = zsup;
		}
		xinf = xsup;
	}

	Modelo caixa = Modelo(tris);
	escreveModelo(caixa, nameFile3d);
	cout << "O modelo da caixa foi criado com sucesso no ficheiro "
		<< nameFile3d << "." << endl;
}

void Modelo::plane(double ladoSqr, char* nameFile3d){

	double x = ladoSqr / 2;

	Vertice v1 = Vertice(x, 0, x);
	Vertice v2 = Vertice(x, 0,-x);
	Vertice v3 = Vertice(-x,0,-x);
	Vertice v4 = Vertice(-x, 0,x);

	vector<Triangulo> tris = { Triangulo(v1, v2, v3), Triangulo(v1, v3, v4) };

	Modelo plano = Modelo( tris );

	escreveModelo(plano, nameFile3d);

	cout << "O modelo do plano foi criado com sucesso no ficheiro "
		<< nameFile3d << "." << endl;
}


