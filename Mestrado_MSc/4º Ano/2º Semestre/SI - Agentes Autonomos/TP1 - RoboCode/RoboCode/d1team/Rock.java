/**
 * Copyright (c) 2001-2016 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package d1team;

import robocode.AdvancedRobot;
import java.awt.*;

/**
 * Based on SittingDuck - a sample robot by Mathew Nelson.
 * 
 * Sits inside a 'security margin' of 150+random*10
 */
public class Rock extends AdvancedRobot {

	public void run() {

		double gpsX, gpsY; 			// Current GPS position
		double newgpsX, newgpsY; 	// New GPS position
		double margin = 150; 		// 'Security margin' from the walls...
		double plusRandom; 			// ... plus a random increment
		double width, height;
		boolean wrong = false; 		// Robot outside 'secure margin'?

		// Paint robot white... obviously!
		setColors();

		// Get battlefield dimensions
		width = getBattleFieldWidth();
		height = getBattleFieldHeight();

		// Random distance to add to the 'security margin'...
		plusRandom = Math.random()*50;
		margin += plusRandom;

		// Get robot GPS position
		gpsX = getX();
		gpsY = getY();

		// Initialize robot new GPS position
		newgpsX = gpsX;
		newgpsY = gpsY;

		// If robot is inside left margin... or right margin
		if ( gpsX < margin ){
			wrong = true;
			newgpsX = margin;
		} else {
			if ( gpsX > (width - margin ) ) {
				wrong = true;
				newgpsX = width - margin;
			}
		}

		// If robot is inside bottom margin... or top margin
		if ( gpsY < margin ){
			wrong = true;
			newgpsY = margin;
		} else {
			if ( gpsY > ( height - margin ) ) {
				wrong = true;
				newgpsY = height - margin;
			}
		}

		// If robot is inside 'security margin' go to new position
		if ( wrong ) {
			gotoXY( newgpsX,newgpsY );
		}

	} // end run()


	// White robot, with the king to the chest!
	public void setColors() {
		setBodyColor(Color.white);
		setGunColor(Color.white);
		setRadarColor(Color.white);
		setBulletColor(Color.white);
		setScanColor(Color.white);
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
