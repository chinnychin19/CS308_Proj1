package mygame;

import jgame.JGObject;

public class Platform extends JGObject {
	private boolean canFall;
	public Platform(double x, double y) {
		super("platform", true, x, y, Constants.PLATFORM_CID, "brickwall", 0, 0, 0, 0, -2);
		// I don't know what expiry is, but example code used -2
		canFall = false;
	}
	public Platform(double x, double y, boolean canFall) {
		this(x,y);
		this.canFall = canFall;
	}
	public boolean canFall() {
		return canFall;
	}
	public void hit(JGObject obj) {
		if (obj.colid == Constants.FIREBALL_CID) {
			obj.remove();
		}
	}
}
