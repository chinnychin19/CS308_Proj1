package CodeSubmission;

import jgame.JGObject;
import mygame.Constants;
import mygame.Platform;

public class NewCollisionsCode {
	/* 
	 * The following two functions would appear in StairwayToHeaven.java
	 * Now, the coder doesn't need to keep track of which CID to place first in the parameters because it calls both.
	 */
	public void checkInGameCollisions() {
		checkBothCollisions(Constants.FIREBALL_CID, Constants.PLAYER_CID);
		checkBothCollisions(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		checkBothCollisions(Constants.ITEM_CID, Constants.PLAYER_CID);
		checkBothCollisions(Constants.DARKNESS_CID, Constants.PLAYER_CID);
		checkBothCollisions(Constants.FIREBALL_CID, Constants.PLATFORM_CID);
		checkBothCollisions(Constants.ITEM_CID, Constants.DARKNESS_CID);
	}
	private void checkBothCollisions(int cid1, int cid2) {
		checkCollision(cid1, cid2);
		checkCollision(cid2, cid1);
	}

	/*
	 * The following classes only include the rewrite of the hit function.
	 * I prefix each class with an underscore to not have repeat class names in the same project.
	 * In addition, I write the code in a way that assumes that Player is keeping track of its own lives
	 * instead of StairwayToHeaven keeping track of everything.
	 */
	public class _Player extends JGObject {
		public void hit(JGObject obj) {
			if (obj.colid == Constants.FIREBALL_CID) {
				loseLife();
			} else if (obj.colid == Constants.PLATFORM_CID) {
				isJumping = false;
				y = obj.y - tileheight; //touching a tile will push the user on top of it
				//_Platform hit() handles the falling of the platform
			} else if (obj.colid == Constants.ITEM_CID) {
				//Item's hit() handles removing the Item
				game.items++;
			} else if (obj.colid == Constants.HEAVEN_CID) {
				game.winGame();
			} else if (obj.colid == Constants.DARKNESS_CID) {
				game.goToHell();
			}
		}
	}
	
	public class _Platform extends JGObject {
		public void hit(JGObject obj) {
			if (obj.colid == Constants.PLAYER_CID) {
				if (canFall) { //Platform is a temporary platform from Stage 2 (post-build stage)
					ydir = 1;
					yspeed = Constants.PLATFORM_FALL_SPEED;
				}
			}
		}
	}
	
	public class Item extends JGObject {
		public void hit(JGObject obj) {
			if (obj.colid == Constants.PLAYER_CID) {
				remove();
			} else if (obj.colid == Constants.DARKNESS_CID) {
				remove();
			}
		}
	}
	
	public class Darkness extends JGObject {
		public void hit(JGObject obj) {
			//do nothing
			//originally it would tell Items to remove themselves
		}
	}
	
	public class Platform extends JGObject {
		public void hit(JGObject obj) {
			//do nothing
			//originally it would tell Fireballs to remove themselves
		}
	}
	
	public class Fireball extends JGObject {
		public void hit(JGObject obj) {
			if (obj.colid == Constants.PLATFORM_CID) {
				remove();
			}
		}
	}
}
