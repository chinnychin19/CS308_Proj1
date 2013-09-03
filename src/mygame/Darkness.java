package mygame;

import jgame.JGColor;
import jgame.JGObject;

public class Darkness extends JGObject {
	private double width, height;
	private StairwayToHeaven game;
	private int grayLevel, grayIncrement;
	public Darkness(StairwayToHeaven game, double x, double y, double w, double h) {
		super("darkness", true, x, y, Constants.DARKNESS_CID, null, 0, 0, 0, 0, -2);
		// I don't know what expiry is, but example code used -2
		width = w;
		height = h;
		xdir = 1;
		xspeed = 1;
		grayLevel = 0;
		grayIncrement = 1;
		this.game = game;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void move() {
		//this is the left edge of the screen + VISIBLE_DARKNESS_WIDTH
		double darknessHandicapX = game.getPlayer().x - game.viewWidth()/2 + Constants.VISIBLE_DARKNESS_WIDTH;
		if (x + width < darknessHandicapX) {
			x = darknessHandicapX - width;
		}
	}
	
	public void paint() {
		if (grayLevel == 30) grayIncrement = -1;
		else if (grayLevel == 0) grayIncrement = 1;
		grayLevel+= grayIncrement;
		game.setColor(new JGColor(grayLevel, grayLevel, grayLevel));
		game.drawRect(x,y,width,height,true,false);
	}
}