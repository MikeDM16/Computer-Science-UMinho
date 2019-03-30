#include <stdio.h>
#include <stdlib.h>
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>

float alfa = 0.0f, beta = 0.5f, radius = 100.0f;
float camX, camY, camZ;

int f1 = 1;
float f2 = 0.02;

float myR() {
	float r = ((float) rand()) /(float) RAND_MAX;
	float ret = r * 200 - 100;
	int ret2 = (int)ret;
	return ret2;
}

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

void drawTree() {
	glPushMatrix();
	glRotatef(-90, 1, 0, 0);
	glColor3f(0.7, 0.8, 0);
	glutSolidCone(0.8, 5, 20, 20);
	glTranslatef(0, 0, 2);
	glColor3f(0, 1, 0.5);
	glutSolidCone(2, 10, 20, 20);
	glPopMatrix();
}

void drawIndios() {
	int i;
	float step = 22.5;
	float xc, zc;
	glPushMatrix();
	glTranslatef(0, 3, 0);
	glColor3f(1, 0, 0);
	for (i = 0; i < 16; i++) {
		glPushMatrix();
		xc = 35 * (float)sin(0);
		zc = 35 * (float)cos(0);
		
		glRotatef((step*i), 0, 1, 0);
		glTranslatef(xc, 0, zc);

		glutSolidTeapot(1);
		glPopMatrix();
	}
	glPopMatrix();

}

void cemTrees() {
	int i;
	srand(27);
	for (i = 0; i < 200;) {

		int xr = myR();
		int zr = myR();
		if (((xr*xr) + (zr*zr)) > (50*50)) {
			glPushMatrix();
			glTranslatef(xr, 0, zr);
			drawTree();
			glPopMatrix();
			i++;
		}
		
	}

}

void drawCowboys() {
	int i;
	float step = 45;
	float xc, zc;
	glPushMatrix();
	glTranslatef(0, 3, 0);
	glColor3f(0, 0, 1);
	for (i = 0; i < 8; i++) {
		glPushMatrix();
		xc = 15 * (float)sin(0);
		zc = 15 * (float)cos(0);
		
		glRotatef((step*i), 0, 1, 0);
		glTranslatef(xc, 0, zc);
		glRotatef((-90), 0, 1, 0);
		
		
		glutSolidTeapot(1);
		glPopMatrix();
	}
	glPopMatrix();

}

void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(camX, camY, camZ,
		0.0, 0.0, 0.0,
		0.0f, 1.0f, 0.0f);

	cemTrees();
	glPushMatrix();
	glRotatef(90, 0, 0, 1);
	glColor3f(0.8f, 0.1f, 0.8);
	glutSolidTorus(1.1, 1.9, 20, 20);
	glPopMatrix();
	glPushMatrix();
	glRotatef(-f1, 0, 1, 0);
	drawCowboys();
	glPopMatrix();
	glPushMatrix();
	glRotatef(f1, 0, 1, 0);
	drawIndios();
	glPopMatrix();
	f1 += 1;

	glColor3f(0.2f, 0.8f, 0.2f);
	glBegin(GL_TRIANGLES);
		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);

		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);
		glVertex3f(100.0f, 0, 100.0f);
	glEnd();
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
	glutIdleFunc(renderScene);
	
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
