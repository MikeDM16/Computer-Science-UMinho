/**
 * Copyright (c) 2001-2016 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package SA.d1team;

import robocode.AdvancedRobot;
import robocode.RobocodeFileOutputStream;
import java.awt.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;



/**
 * Based on SittingDuck - a sample robot by Mathew Nelson.
 * 
 * Sits inside a 'security margin' of 150+random*10
 */
public class RockQuad extends AdvancedRobot {

	public void run() {

		double gpsX, gpsY; 			// Current GPS position
		double newgpsX, newgpsY; 	// New GPS position
		double margin = 100; 		// 'Security margin' from the walls...
		double plusRandom; 			// ... plus a random increment
		double width, height;
		double centerX, centerY;
		boolean wrong = false; 		// Robot outside 'secure margin'?

		int robotCount;
		double corner;

		try {
			BufferedReader reader = null;
			try {
				// Read file "rockquad.txt" which contains 1 line with the round count
				reader = new BufferedReader(new FileReader(getDataFile("rockquad.txt")));

				// Try to get the counts
				robotCount = Integer.parseInt(reader.readLine());

			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (IOException e) {
			// Something went wrong reading the file, reset to 0.
			robotCount = 0;
		} catch (NumberFormatException e) {
			// Something went wrong converting to ints, reset to 0
			robotCount = 0;
		}

		// Increment the # of rounds
		robotCount++;

		PrintStream w = null;
		try {
			w = new PrintStream(new RobocodeFileOutputStream(getDataFile("rockquad.txt")));

			w.println(robotCount);

			// PrintStreams don't throw IOExceptions during prints, they simply set a flag.... so check it here.
			if (w.checkError()) {
				out.println("I could not write the count!");
			}
		} catch (IOException e) {
			out.println("IOException trying to write: ");
			e.printStackTrace(out);
		} finally {
			if (w != null) {
				w.close();
			}
		}
		out.println("I am ROCK number " + robotCount + "."); 

		corner = (int) Math.round( (((double)robotCount/3 - (int)robotCount/3)*3) );

		// Paint robot white... obviously!
		setColors( corner );

		// Get battlefield dimensions
		width = getBattleFieldWidth();
		height = getBattleFieldHeight();
		centerX = width/2;
		centerY = height/2;

		// Random distance to add to the 'security margin'...
		plusRandom = Math.random()*50;
		margin += plusRandom;

		// Get robot GPS position
		gpsX = getX();
		gpsY = getY();

		// Initialize robot new GPS position
		newgpsX = gpsX;
		newgpsY = gpsY;

		if ( corner == 0 ) {
			wrong = true;
			newgpsX = centerX - margin;
			newgpsY = centerY + margin;
		} else {
			if ( corner == 1 ) {
				wrong = true;
				newgpsX = centerX + margin;
				newgpsY = centerY + margin;
				} else {
					wrong = true;
					newgpsX = centerX + margin;
					newgpsY = centerY - margin;
					}			
			}


		// If robot is inside 'security margin' go to new position
		if ( wrong ) {
			gotoXY( newgpsX,newgpsY );
		}

	} // end run()


	// White robot, with the king to the chest!
	public void setColors( double corner ) {

		if ( corner == 0 ) {
			setBodyColor(Color.white);
			setGunColor(Color.white);
			setRadarColor(Color.white);
			setBulletColor(Color.white);
			setScanColor(Color.white);
		} else {
			if ( corner == 1 ) {
				setBodyColor(Color.black);
				setGunColor(Color.black);
				setRadarColor(Color.black);	
				setBulletColor(Color.white);
				setScanColor(Color.white);
				} else {
					setBodyColor(Color.white);
					setGunColor(Color.black);
					setRadarColor(Color.black);	
					setBulletColor(Color.white);
					setScanColor(Color.white);
				}
			}
	} // end setColors()


	// Go to GPS position (x,y)
	private void gotoXY(double x, double y) {
		double dx = x - getX();
		double dy = y - getY();
		double turnDegrees;

		// Determine how much to turn
		turnDegrees = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading()) % 360;
		turnRight(turnDegrees);
		ahead(Math.sqrt(dx*dx+dy*dy));
	} // end gotoXY()
	
} // end
