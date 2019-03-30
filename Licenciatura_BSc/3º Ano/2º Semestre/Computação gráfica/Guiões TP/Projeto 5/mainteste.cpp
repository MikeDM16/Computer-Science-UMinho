#include <stdio.h>
#include <stdlib.h>
#include <ctime>
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>

#define RAND_MAX 100
float alfa = 0.0f, beta = 0.5f, radius = 100.0f;
float camX, camY, camZ;
float angulo;
float angGirar = 0;

void spherical2Cartesian() {

	camX = radius * cos(beta) * sin(alfa);
	camY = radius * sin(beta);
	camZ = radius * cos(beta) * cos(alfa);
}


void changeSize(int w, int h) {

	// Prevent a divide by zero, when window is too short
	// (you cant make a window with zero width).
	if(h == 0)
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
	gluPerspective(45.0f ,ratio, 1.0f ,1000.0f);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}

int getRandom(int lim1, int lim2) {
	float xaux = rand() / RAND_MAX;
	while (xaux > lim1 || xaux < lim2) {
		xaux = rand() / RAND_MAX;
	}
	return xaux;
}

void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(camX, camY, camZ,
		0.0, 0.0, 0.0,
		0.0f, 1.0f, 0.0f);

	// desenhar o plano 100x100
	glColor3f(0.2f, 0.8f, 0.2f);
	glBegin(GL_TRIANGLES);
		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);

		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);
		glVertex3f(100.0f, 0, 100.0f);
	glEnd();

	//Desenhar o grande torus!!!!
	glPushMatrix();
	glColor3f(155, 0, 140);
	glutSolidTorus(1, 3, 45, 50);
	glPopMatrix();

	//Desenhar primeiros teapots
	float alpha = 2 * M_PI / 8;
	angulo = 0;
	float xaux, zaux;
	int raio = 15;
	for (angulo = 0; angulo < 2 * M_PI; angulo += alpha) {
		xaux = raio * sin(angulo);
		zaux = raio * cos(angulo);
		
		glPushMatrix();
		glTranslatef(xaux, 1, zaux);
		glColor3f(0, 0, 1);
		glRotatef(90, 0, 1, 0);
		glutSolidTeapot(1);
		glPopMatrix();
	}
	//Desenhar segundos teapots
	raio = 35;
	alpha = 2 * M_PI / 20;
	for (angulo = 0; angulo < 2 * M_PI; angulo += alpha) {
		xaux = raio * sin(angulo);
		zaux = raio * cos(angulo);

		glPushMatrix();
		glTranslatef(xaux, 1, zaux);
		glColor3f(1, 0.1, 0.1);
		glutSolidTeapot(1);
		glPopMatrix();
	}
	srand(34);
	//Desenhar as arvores de forma random
	int trees = 0;
	float r;
	while (trees != 100) {
		r = rand() / RAND_MAX;
		//xaux = r * 200 - 100;
		//zaux = r * 200 - 100;
		xaux = r * 200 - 100;
		r = rand() / RAND_MAX;
		zaux = r * 200 - 100;
		//Fazer o tronco
		glPushMatrix();
		glTranslatef(xaux, 0, zaux);
		glRotatef(180, 0, 1, 1);
		glColor3f(0.65, 0.16, 0.2);
		glutSolidCone(1, 3, 50, 50);
		glPopMatrix();
		//Fazer a copa
		glPushMatrix();
		glTranslatef(xaux, 3, zaux);
		glRotatef(180, 0, 1, 1);
		glColor3f(0.4, 1, 0);
		glutSolidCone(4, 10, 45, 50);
		glPopMatrix();

		trees++;
	}

	// End of frame
	glutSwapBuffers();
}


void processKeys(unsigned char c, int xx, int yy) {

// put code to process regular keys in here

}


void processSpecialKeys(int key, int xx, int yy) {

	switch (key) {

	case GLUT_KEY_RIGHT:
		alfa -= 0.1; break;

	case GLUT_KEY_LEFT:
		alfa += 0.1; break;

	case GLUT_KEY_UP:
		beta += 0.1f;
		if (beta > 1.5f)
			beta = 1.5f;
		break;

	case GLUT_KEY_DOWN:
		beta -= 0.1f;
		if (beta < -1.5f)
			beta = -1.5f;
		break;

	case GLUT_KEY_PAGE_DOWN: radius -= 1.0f;
		if (radius < 1.0f)
			radius = 1.0f;
		break;

	case GLUT_KEY_PAGE_UP: radius += 1.0f; break;
	}
	spherical2Cartesian();
	glutPostRedisplay();

}


void printInfo() {

	printf("Vendor: %s\n", glGetString(GL_VENDOR));
	printf("Renderer: %s\n", glGetString(GL_RENDERER));
	printf("Version: %s\n", glGetString(GL_VERSION));

	printf("\nUse Arrows to move the camera up/down and left/right\n");
	printf("Home and End control the distance from the camera to the origin");
}


int main(int argc, char **argv) {

// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(800,800);
	glutCreateWindow("CG@DI-UM");
		
// Required callback registry 
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);
	
// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);

	spherical2Cartesian();

	printInfo();

// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}
