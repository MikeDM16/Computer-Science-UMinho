#pragma once

class Vertice{

	private:
		double x,y,z;

	public:
		/*Construtor padrão */
		Vertice::Vertice();
		/*Contrutor de um vertice, caracterizados pelas suas tres 
		coordenadas espaciais x, y e z */
		Vertice::Vertice(double x, double y, double z);

		/*Função que dado as coordenadas de um vertice em coordenadas cilindricas
		as converte para um referencial cartesiano. Função necessária para calcular os vertices de
		um cone*/
		static Vertice cilindricas(double raio, double alpha, double y);

		/*Função que dado as coordenadas de um vertice em coordenadas esféricas
		as converte para um referencial cartesiano. Função necessária para a criaçao dos
		vertices de um esfera*/
		static Vertice Vertice::esfericas(double raio, double alpha, double beta);

		/*Metodos get das variaveis da classe  Vertice*/
		double Vertice::getX();
		double Vertice::getY();
		double Vertice::getZ();

};