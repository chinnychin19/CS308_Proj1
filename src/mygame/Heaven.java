package mygame;

import jgame.JGObject;

public class Heaven extends JGObject {
	public Heaven(double x, double y) {
		super("heaven",
				true,
				x,
				y,
				Constants.HEAVEN_CID, 
				"crossturn",
				0,
				0,
				-2 );
		// I don't know what expiry is, but example code used -2
	}
}
