#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <math.h>

float posX = 0.0f, posY = 0.0f, posZ = 5.0f;
float angle = 0.0f;
float lx = 0.0f, lz = -1.0f;

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


void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(posX, 1.0, posZ,
		posX + lx, 1.0f, posZ + lz,
		0.0f, 1.0f, 0.0f);

	// put the geometric transformations here
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glRotatef(angle, 0.0f, 1.0f, 0.0f);

	// put drawing instructions here
	glBegin(GL_TRIANGLES);
	// base
	glColor3f(1, 0, 0); //red
	glVertex3f(1, 0, 1);
	glVertex3f(1, 0, 1);
	glVertex3f(1, 0, 1);
	//base
	glColor3f(0, 1, 0); //green
	glVertex3f(1, 0, 1);
	glVertex3f(1, 0, -1);
	glVertex3f(-1, 0, -1);
	
	//face1
	glColor3f(0, 0, 1); //blue
	glVertex3f(-1, 0, 1);
	glVertex3f(-1, 0, -1);
	glVertex3f(0, 3, 0);
	
	//face2
	glColor3f(1, 1, 0); //yellow
	glVertex3f(posX -1, 0, -1);
	glVertex3f(posX + 1, 0, -1);
	glVertex3f(posX + 0, 3, 0);

	//face3
	glColor3f(1, 0, 1); //purple
	glVertex3f(1, 0, -1);
	glVertex3f(1, 0, 1);
	glVertex3f(0, 3, 0);

	//face4
	glColor3f(1, 0.5, 1); //orange
	glVertex3f(1, 0, 1);
	glVertex3f(-1, 0, 1);
	glVertex3f(0, 3, 0);
	
	glEnd();

	angle += 0.2f;

	// End of frame
	glutSwapBuffers();
}


// write function to process keyboard events
void keyboardFunc(int key, int x, int y) {
	float fraction = 0.1f;
	switch (key) {
	case GLUT_KEY_UP:	posX += lx * fraction;
						posX += lx * fraction;
	case GLUT_KEY_DOWN:	posX -= lx * fraction;
						posX -= lx * fraction;
	case GLUT_KEY_LEFT:	posZ += 2;
	case GLUT_KEY_RIGHT: posZ -= 2;
	}
	glutPostRedisplay();
}


int main(int argc, char **argv) {

	// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(800, 800);
	glutCreateWindow("CG@DI-UM");

	// Required callback registry 
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);

	// put here the registration of the keyboard callbacks
	glutSpecialFunc(keyboardFunc);

	//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);

	// enter GLUT's main cycle
	glutMainLoop();

	return 1;
}