#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <cmath>
#include <GL/glut.h>
#endif
#define _USE_MATH_DEFINES
#include <math.h>

#include <iostream>
#include <cstdlib>
#include <fstream>
#include <string>
#include <vector>
#include "Triangulo.h"
#include "Vertice.h"
#include "tinystr.h"
#include "tinyxml.h"
#include <direct.h>
using namespace std;

// Variaveis globais

/*	Diretoria para o ficheiro xml
	Deve ser mudada para cada pc em que se testar
*/
char* dirToXML = "C:\\Users/migue/OneDrive/Trabalho CG/Fase I/TrabalhoCG-FaseI/Motor3D/";
std::vector<Vertice> vertices;

// angle of rotation for the camera direction
float angleX = 0.0f;
float angleY = 0.0f;
float angleZ = 0.0f;
float beta = 0.0f;

// Straight line distance between the camera and look at point
float rCam = 30;
float xxx = 0.0f;
float zzz = 0.0f;
float yyy = 0.0f;

int comodesenha = 1;
float alpha = 0.0f;

void imprimeMenu() {
	cout << "\n		Sugestao de interacao com o modelo gerado:\n" <<
		"1. Alteracao do ponto visao da camara\n"
		"   Tecla Up    - Deslocar para cima                Tecla Down - Deslocar para Baixo\n"
		"   Tecla Right - Deslocar para esquerda            Tecla Left - Deslocar para direita\n"
		"2. Opcoes de visualizacao do modelo\n"
		"   Tecla 1 - Desenho so com linhas                 Tecla 2    - Desenho a cheio\n"
		"   Tecla 3 - Desenho so com os pontos dos vertices\n"
		"3. Movimentacao do modelo\n"
		"   Tecla w - Mover no sentido positivo do eixo Z\n"
		"   Tecla s - Mover no sentido negativo do eixo Z\n"
		"   Tecla a - Mover no sentido positivo do eixo X\n"
		"   Tecla d - Mover no sentido negativo do eixo X\n"
		"4. Rotacao do modelo\n"
		"   Tecla y - Rodar sentido anti-horario eixo X     Tecla h - Rodar sentido horario eixo X\n"
		"   Tecla u - Rodar sentido anti-horario eixo Y     Tecla j - Rodar sentido horario eixo Y\n"
		"   Tecla i - Rodar sentido anti-horario eixo Z     Tecla k - Rodar sentido horario eixo Z\n"
		"5. Teclas Gerais\n"
		"   Tecla + - Aumentar o zoom da camara             Tecla - - Diminuir o zoom da camara\n"
		"   Tecla o - restaurar todas as definicoes de visualizacao iniciais\n"
		<< endl;
}

void linhaTok(string linha) {
	double x, y, z;
	int i;
	char* verticesStr[3];

	/*ler do ficheiro retorna var do tipo string
	strtokanizer usa char* --> converter string p/ char*
	*/
	char *aux = new char[linha.length() + 1];
	strcpy(aux, linha.c_str());

	/*partir a linha pelo caracter ',' ou \n*/
	char* t = strtok(aux, ",\n");
	for (i = 0; t != nullptr; i++) {
		verticesStr[i] = t;
		t = strtok(nullptr, ",\n");;
	}
	/*partir cada vertice, ainda em string, pelo caracter ' ' ou \n*/
	for (i = 0; i != 3; i++) {
		x = atof( strtok(verticesStr[i], " \n") );
		y = atof(strtok(nullptr, " \n"));
		z = atof(strtok(nullptr, " \n"));
		vertices.push_back(Vertice(x, y, z));
	}
}

void lerFicheiro(char* dirFile) {

	cout << "Sera usado o ficheiro na diretoria:\n " << dirFile << endl;
	ifstream inputfile(dirFile);
	if (!inputfile.is_open()) {
		cout << "Nao foi encontrado nenhum ficheiro na diretoria " << dirFile << endl;
	}
	else {
		string linha;
		for (int i = 1; !inputfile.eof(); i++) {
			getline(inputfile, linha);
			/*linha.size()==0 --> linha = null*/
			if (linha.size() != 0) { linhaTok(linha); }
		}
	}
	inputfile.close();
}

void parseXML(char* fileXml) {
	char dirAux[120];
	strcpy(dirAux, dirToXML);
	strcat(dirAux, fileXml);

	TiXmlDocument doc(dirAux);
	if (doc.LoadFile())
	{
		// Elemento scene
		TiXmlElement* scene = doc.FirstChildElement("scene");
		// Elemento diretoria e modelo contidos em scene
		TiXmlElement* diretoria = scene->FirstChildElement("diretoria");
		TiXmlElement* modelo = scene->FirstChildElement("modelo");

		//Elemento diretoria tem o atributo dir = caminho até aos ficheiros
		char* dir = (char*)diretoria->Attribute("dir");

		//Enquanto houver ficheiros para ler
		while (modelo) {
			//Elemento modelo tem o atributo file com o nome do ficheiro .3d
			const char* nameFile3d = modelo->Attribute("file");
			//Juntar á diretoria dos ficheiros o nome do ficheiro
			char dirFile[120];
			strcpy(dirFile, dir);
			strcat(dirFile, nameFile3d);
			//chamar a funçao que le o ficheiro e o converte para vertice de um modelo
			lerFicheiro(dirFile);

			//passar para o proximo irmao de modelo, caso exista
			modelo = modelo->NextSiblingElement("modelo");
		}
		imprimeMenu();
	}
	else
	{
		cout << "Nao foi possivel abrir o ficheiro na diretoria " 
			 << dirAux << endl;
	}
}

void changeSize(int w, int h) {

	// Prevent a divide by zero, when window is too short
	// (you cant make a window with zero width).
	if (h == 0)
		h = 1;

	// compute window's aspect ratio 
	float ratio = w * 1.0 / h;

	// Set the projection matrix as current
	glMatrixMode(GL_PROJECTION);
	// Load Identity Matrix
	glLoadIdentity();

	// Set the viewport to be the entire window
	glViewport(0, 0, w, h);

	// Set perspective
	gluPerspective(45.0f, ratio, 1.0f, 1000.0f);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}

float size = 1;

void renderScene(void) {
	double x, y, z;

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	float cX = rCam * -sinf((alpha)*(M_PI / 180)) * cosf((beta)*(M_PI / 180));
	float cY = rCam * -sinf((beta)*(M_PI / 180));
	float cZ = -rCam * cosf((alpha)*(M_PI / 180)) * cosf((beta)*(M_PI / 180));

	// Set the camera position and lookat point
	gluLookAt(cX, cY, cZ,   // Camera position
		0.0, 0.0, 0.0,    // Look at point
		0.0, 1.0, 0.0);   // Up vector;
	// Put the geometric transformations here
	if (comodesenha == 1) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	if (comodesenha == 2) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	if (comodesenha == 3) glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
	glRotatef(angleY, 0, 1, 0);
	glRotatef(angleX, 1, 0, 0);
	glRotatef(angleZ, 0, 0, 1);
	glTranslatef(xxx, yyy, zzz);
	
	// put drawing instructions here
	glBegin(GL_TRIANGLES);
	glColor3f(0.0f, 1.0f, 0.0f);
	
	for (auto vertice : vertices) {
		x = vertice.getX();
		y = vertice.getY();
		z = vertice.getZ();
		glColor3f(x, y, z);
		glVertex3f(x, y, z);
	}

	glEnd();
	// End of frame
	glutSwapBuffers();
}

// write function to process keyboard events
void arrows(int key_code, int xx, int yy) {
	//fracção em que vão aumentar os angulos
	float fraction = 0.9f;
	switch (key_code) {
	case GLUT_KEY_LEFT:
		alpha += fraction;
		break;
	case GLUT_KEY_RIGHT:
		alpha -= fraction;
		break;
	case GLUT_KEY_DOWN:
		if (abs((beta + fraction)*(M_PI / 180))<1.5)
			beta += fraction;
		break;
	case GLUT_KEY_UP:
		if (abs((beta - fraction)*(M_PI / 180))<1.5)
			beta -= fraction;
		break;
	}
	glutPostRedisplay();
};
/*Função que atribui a função de zoom ás teclas + e -, aumentando e diminuindo 
o zoom da camara, sem exceder a distancia de zoom maxima*/
void normalkeyboard(unsigned char tecla, int x, int y) {
	switch (tecla) {
	case '+': if (rCam > 1.0f) rCam -= 1.0f; break;
	case '-':  rCam += 1.0f; break;
	case 'w':  zzz -= 1.0f; break;
	case 'a':  xxx -= 1.0f; break;
	case 's':  zzz += 1.0f; break;
	case 'd':  xxx += 1.0f; break;
	case 't':  yyy -= 1.0f; break;
	case 'g':  yyy += 1.0f; break;
	case '1':  comodesenha = 1; break;
	case '2':  comodesenha = 2; break;
	case '3':  comodesenha = 3; break;
	case 'y':  angleX += 0.5f; break;
	case 'h':  angleX -= 0.5f; break;
	case 'u':  angleY += 0.5f; break;
	case 'j':  angleY -= 0.5f; break;
	case 'i':  angleZ += 0.5f; break;
	case 'k':  angleZ -= 0.5f; break;
	case 'o':  rCam = 30.0f;
		angleX = 0;
		angleY = 0;
		angleZ = 0;
		alpha = 0;
		beta = 0;
		xxx = 0;
		yyy = 0;
		zzz = 0;
		break;

	}
	glutPostRedisplay();
}


int main(int argc, char **argv) {
	parseXML(argv[1]);
	
	// put GLUT init here
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(1000, 800);
	glutCreateWindow("Trabalho Computação Gráfica - Fase 1");


	// put callback registration here
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);

	// put here the registration of the keyboard callbacks
	glutSpecialFunc(arrows);
	glutKeyboardFunc(normalkeyboard);

	// OpenGL settings 
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	//cor cinzenta;
	glClearColor(0.65, 0.65, 0.65, 1.0f);

	// enter GLUT's main loop
	glutMainLoop();
	
	return 1;
}

