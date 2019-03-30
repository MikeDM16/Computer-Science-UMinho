//////////////////////////////////////////////////////////////////////////////////////////
//	Main.cpp
//	Project Template
//	Downloaded from: www.paulsprojects.net
//	Created:	1st October 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <time.h>
#include <stdlib.h>
#include "GL files/glee.h"	//header for opengl versions >1.1 and extensions
#include <GL/glu.h>
#include "Maths/Maths.h"
#include "Log/LOG.h"
#include "Timer/TIMER.h"
#include "Fps Counter/FPS_COUNTER.h"
#include "Window/WINDOW.h"
#include "Bitmap Font/BITMAP_FONT.h"
#include "Image/IMAGE.h"
#include "PAINTING_STYLE.h"
#include "InitPaintingStyles.h"
#include "PAINTING.h"
#include "Main.h"

//link to libraries
#pragma comment(lib, "opengl32.lib")
#pragma comment(lib, "glu32.lib")
#pragma comment(lib, "winmm.lib")

TIMER timer;
FPS_COUNTER fpsCounter;
BITMAP_FONT font;

COLOR backgroundColor(0.0f, 0.0f, 0.0f, 0.0f);

//The different paintings (one per source image)
const int numPaintings=3;
PAINTING paintings[numPaintings];

//Current painting
int currentPainting=0;

//Are we displaying the source image or a painting?
bool showSource=false;

//Painting styles
PAINTING_STYLE_DATA * paintingStyleData;
PAINTING_STYLE currentPaintingStyle=PAINTING_STYLE_IMPRESSIONIST;

//Texture ID for circle
GLuint circleTexture;

//Set up GL
bool GLInit()
{
	//Init window
	if(!WINDOW::Instance()->Init(	"Project Template", 640, 480, 8, 8, 8, 8, 24, 8,
									false, false, true))
		return false;
	
	//Check for OpenGL version/extensions
	if(!GLEE_ARB_texture_rectangle)
	{
		LOG::Instance()->OutputError("I require ARB_texture_rectangle support");
		return false;
	}

	//Init font
	if(!font.Init())
		return false;

	//set viewport
	int height=WINDOW::Instance()->height;
	if(height==0)
		height=1;

	glViewport(0, 0, WINDOW::Instance()->width, height);

	//Set up projection matrix
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(0, WINDOW::Instance()->width, 0, WINDOW::Instance()->height);

	//Load identity modelview
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	//Shading states
	glShadeModel(GL_SMOOTH);
	glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
	glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

	//Depth states
	glClearDepth(1.0f);
	glDepthFunc(GL_LEQUAL);
	glEnable(GL_DEPTH_TEST);

	glEnable(GL_CULL_FACE);

	return true;
}

//Set up variables
bool DemoInit()
{
	//Seed random number generator
	srand((unsigned int)time(NULL));

	//Load the circle texture
	IMAGE circleImage;
	circleImage.Load("circle.tga");
	glGenTextures(1, &circleTexture);
	glBindTexture(GL_TEXTURE_2D, circleTexture);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	glTexImage2D(	GL_TEXTURE_2D, 0, GL_INTENSITY8, circleImage.width, circleImage.height,
					0, GL_LUMINANCE, GL_UNSIGNED_BYTE, circleImage.data);

	//Initialise painting styles
	InitPaintingStyles();

	//Initialise paintings
	char * sourceFiles[numPaintings]={"zebras.tga", "church.tga", "lillies.tga"};

	for(int i=0; i<numPaintings; ++i)
	{
		//Display a simple progress meter
		glClear(GL_COLOR_BUFFER_BIT);
		glColor4fv(white);

		font.StartTextMode();
		font.Print(	WINDOW::Instance()->width/2-130, WINDOW::Instance()->height/2-20,
					"Painting picture %d of 3", i+1);
		font.EndTextMode();

		WINDOW::Instance()->SwapBuffers();

		//Initialise the painting
		if(!paintings[i].Init(sourceFiles[i]))
			return false;
	}
	
	//reset timer
	timer.Reset();

	return true;
}

//Perform per-frame updates
void UpdateFrame()
{
	//set currentTime and timePassed
	static double lastTime=timer.GetTime();
	double currentTime=timer.GetTime();
	double timePassed=currentTime-lastTime;
	lastTime=currentTime;

	//Update window
	WINDOW::Instance()->Update();

	//Toggle between painting styles
	if(WINDOW::Instance()->IsKeyPressed('1'))
		showSource=true;

	if(WINDOW::Instance()->IsKeyPressed('2'))
	{
		showSource=false;
		currentPaintingStyle=PAINTING_STYLE_IMPRESSIONIST;
	}

	if(WINDOW::Instance()->IsKeyPressed('3'))
	{
		showSource=false;
		currentPaintingStyle=PAINTING_STYLE_EXPRESSIONIST;
	}

	if(WINDOW::Instance()->IsKeyPressed('4'))
	{
		showSource=false;
		currentPaintingStyle=PAINTING_STYLE_POINTILLIST;
	}

	//Change between paintings
	if(WINDOW::Instance()->IsKeyPressed('Z'))
		currentPainting=0;

	if(WINDOW::Instance()->IsKeyPressed('C'))
		currentPainting=1;

	if(WINDOW::Instance()->IsKeyPressed('L'))
		currentPainting=2;	
	
	//Render frame
	RenderFrame(currentTime, timePassed);
}

//Render a frame
void RenderFrame(double currentTime, double timePassed)
{
	//Clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();										//reset modelview matrix
	glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

	//Draw the current painting
	if(showSource)
		paintings[currentPainting].BindSourceTexture();
	else
		paintings[currentPainting].BindPaintingTexture(currentPaintingStyle);
	
	glEnable(GL_TEXTURE_RECTANGLE_ARB);

	glBegin(GL_TRIANGLE_STRIP);
	{
		glTexCoord2i(0, 0);
		glVertex2i(0, 0);

		glTexCoord2i(paintings[currentPainting].width, 0);
		glVertex2i(WINDOW::Instance()->width, 0);

		glTexCoord2i(0, paintings[currentPainting].height);
		glVertex2i(0, WINDOW::Instance()->height);

		glTexCoord2i(paintings[currentPainting].width, paintings[currentPainting].height);
		glVertex2i(WINDOW::Instance()->width, WINDOW::Instance()->height);
	}
	glEnd();

	glDisable(GL_TEXTURE_RECTANGLE_ARB);

	
	//Print text
	font.StartTextMode();
	glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
	//font.Print(0, 28, "FPS: %.2f", fpsCounter.GetFps());
	font.EndTextMode();

	WINDOW::Instance()->SwapBuffers();

	//Save a screenshot
	if(WINDOW::Instance()->IsKeyPressed(VK_F1))
	{
		WINDOW::Instance()->SaveScreenshot();
		WINDOW::Instance()->SetKeyReleased(VK_F1);
	}

	//Check for an openGL error
	WINDOW::Instance()->CheckGLError();

	//quit if necessary
	if(WINDOW::Instance()->IsKeyPressed(VK_ESCAPE))
		PostQuitMessage(0);
}

//Shut down demo
void DemoShutdown()
{
	font.Shutdown();
	WINDOW::Instance()->Shutdown();
}

//WinMain
int WINAPI WinMain(	HINSTANCE	hInstance,			//Instance
					HINSTANCE	hPrevInstance,		//Previous Instance
					LPSTR		lpCmdLine,			//Command line params
					int			nShowCmd)			//Window show state
{
	//Save hInstance
	WINDOW::Instance()->hInstance=hInstance;

	//Init GL and variables
	if(!GLInit())
	{
		LOG::Instance()->OutputError("OpenGL Initiation Failed");
		return false;
	}
	else
		LOG::Instance()->OutputSuccess("OpenGL Initiation Successful");

	if(!DemoInit())
	{
		LOG::Instance()->OutputError("Demo Initiation Failed");
		return false;
	}
	else
		LOG::Instance()->OutputSuccess("Demo Initiation Successful");

	//Main Loop
	for(;;)
	{
		if(!(WINDOW::Instance()->HandleMessages()))	//quit if HandleMessages returns false
			break;

		UpdateFrame();
	}

	//Shutdown
	DemoShutdown();

	//Exit program
	LOG::Instance()->OutputSuccess("Exiting...");
	return 0;
}
