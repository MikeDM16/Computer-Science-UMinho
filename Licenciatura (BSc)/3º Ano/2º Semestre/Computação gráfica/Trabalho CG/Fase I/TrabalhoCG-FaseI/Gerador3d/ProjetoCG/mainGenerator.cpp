#include <iostream>
#include <cstdlib>
#include <fstream>
#include <string.h>
#include "Modelo.h"
using namespace std;

void parseParametros(int argc, char* argv[]) {
	bool control = false;
	if ((strcmp(argv[1], "plane") == 0) && (argc == 4)) {
		double ladoSqr = std::atof(argv[2]);
		Modelo::plane(ladoSqr, argv[argc - 1]);
		control = true;
	}
	if ((strcmp(argv[1], "box") == 0) && ((argc == 7) || (argc == 6))) {
		double largura = std::atof(argv[2]);
		double compri = std::atof(argv[3]);
		double altura = std::atof(argv[4]);
		control = true;
		double nDiv = 1;
		if (argc == 7) {
			nDiv = std::atof(argv[5]);
		}
		Modelo::box(largura, compri, altura, nDiv, argv[argc - 1]);
	}
	if ((strcmp(argv[1], "cilindro") == 0) && (argc == 7)) {
		double raio = atof(argv[2]);
		double altura = atof(argv[3]);
		int fatias = atoi(argv[4]);
		int stacks = atoi(argv[5]);
		Modelo::cilindro(raio, altura, fatias, stacks, argv[argc - 1]);
		control = true;
	}
	if ((strcmp(argv[1], "sphere") == 0) && (argc == 6 )) {
		double raio = atof(argv[2]);
		int fatias = atoi(argv[3]);
		int stacks = atoi(argv[4]);
		Modelo::sphere(raio, fatias, stacks, argv[argc - 1]);
		control = true;
	}
	if ((strcmp(argv[1], "cone") == 0) && (argc == 7)) {
		double raio = atof(argv[2]);
		double altura = atof(argv[3]);
		int fatias = atoi(argv[4]);
		int stacks = atoi(argv[5]);
		control = true;
		Modelo::cone(raio, altura, fatias, stacks, argv[argc - 1]);
	}

	if(control ==false){
		cout << "Os parametros de entrada nao foram corretamente inseridos" << endl;
	}

} 

int main(int argc, char* argv[]) {
	cout << "Hello Folk!" << endl;

	/*Gerar um determinado tipo de sólido conforme os argumento passados*/
	parseParametros(argc, argv);

	return 0;
}