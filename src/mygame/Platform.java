package mygame;

import jgame.JGObject;

public class Platform extends JGObject {
	public Platform(double x, double y) {
		super("platform", true, x, y, Constants.PLATFORM_CID, "brickwall", 0, 0, 0, 0, -2);
		// I don't know what expiry is, but example code used -2
	}
	public void hit(JGObject obj) {
		if (obj.colid == Constants.FIREBALL_CID) {
			obj.remove();
		}
	}
}
