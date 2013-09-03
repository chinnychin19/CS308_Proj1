package mygame;

import jgame.JGObject;

public class Player extends JGObject {
	private StairwayToHeaven game;
	private boolean isJumping;
	public Player(StairwayToHeaven game, double x, double y, double speed) {
		super("player",
				true,
				x,
				y,
				Constants.PLAYER_CID,
				"bat1",
				0,
				0,
				speed,
				speed,
				-1);
		ydir = -1;
		isJumping = false;
		this.game = game;
	}
	public void move() {
		xdir = 0; //prevents ice skating
		if (game.getKey(game.key_left)  && x > xspeed) xdir=-1;
		if (game.getKey(game.key_right)) xdir=1;
		if (!isJumping && game.getKey(game.key_up)) {
			startJump();
			game.clearKey(game.key_up);
		}
		
		if (isJumping) {
			yspeed -= Constants.GRAVITY;
		} else {
			yspeed = Constants.FALL_SPEED; //If on a platform, the platform prevents falling
		}

	}
	public void startJump() {
		isJumping = true;
		yspeed = Constants.JUMP_SPEED;
	}
	public void hit(JGObject obj) {
		if (obj.colid == Constants.FIREBALL_CID) {
			game.goToHell();
		} else if (obj.colid == Constants.PLATFORM_CID) {
			isJumping = false;
			y = obj.y - tileheight; //touching a tile will push the user on top of it
			if (((Platform)obj).canFall()) {
				obj.ydir = 1;
				obj.yspeed = Constants.PLATFORM_FALL_SPEED;
			}
		} else if (obj.colid == Constants.ITEM_CID) {
			obj.remove();
			game.items++;
		} else if (obj.colid == Constants.HEAVEN_CID) {
			game.winGame();
		} else if (obj.colid == Constants.DARKNESS_CID) {
			game.goToHell();
		}
	}
}
