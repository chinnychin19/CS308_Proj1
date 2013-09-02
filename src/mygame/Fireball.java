package mygame;

import jgame.JGObject;

public class Fireball extends JGObject {
	public Fireball(StairwayToHeaven game) {
		super("fireball",
				true,
				game.getPlayer().x + Constants.FIREBALL_HEADSTART,
				0,
				Constants.FIREBALL_CID, 
				"ball",
				game.random(Constants.FIREBALL_MIN_XSPEED, Constants.FIREBALL_MAX_XSPEED),
				game.random(Constants.FIREBALL_MIN_YSPEED, Constants.FIREBALL_MAX_YSPEED),
				-2 );
	}
}
