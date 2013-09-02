package mygame;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import jgame.*;
import jgame.platform.*;

public class StairwayToHeaven extends StdGame {
	private Player player;
	private Darkness darkness;
	private int frames;
	private double origX, origY;
	
	public static void main(String[]args) {
		new StairwayToHeaven(parseSizeArgs(args,0));
	}
	public StairwayToHeaven() {
		initEngineApplet();
	}
	public StairwayToHeaven(JGPoint size) {
		initEngine(size.x,size.y);
	}
	
	public void initCanvas() {
		int nrtilesx = 32;
		int nrtilesy = 24;
		int tilex = 8;
		int tiley = 8;
		
		int bcolorR = 80;
		int bcolorG = 250;
		int bcolorB = 91;
				
		setCanvasSettings(nrtilesx, nrtilesy, tilex, tiley, 
				null, 
				new JGColor(bcolorR, bcolorG, bcolorB), 
				null);
	}
	public void initGame() {
		defineMedia("mygame.tbl");
		if (isMidlet()) {
			setFrameRate(20,1);
			setGameSpeed(2.0);
		} else {
			setFrameRate(45,1);
		}
		setPFSize(Constants.STAGE_STRING.length()*Constants.WIDTH_FACTOR, Constants.PART1_HEIGHT);
		frames = 0;
//		startgame_ingame=true;
		setGameState("Title");
	}
	
	public void initNewLife() {
		removeObjects(null,0);
		origX = Platform.tilewidth*40;
		origY = pfHeight()-32;
		player = new Player(origX, origY, Constants.PLAYER_SPEED);
		darkness = new Darkness(origX-viewWidth(), Constants.TOP_MARGIN, 
				viewWidth()/2, viewHeight());
		makeStage();
	}
	private void makeStage() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (char c: Constants.STAGE_STRING.toCharArray()) {
			list.add(Integer.parseInt(""+c));
		}
		for (int i = 0; i < list.size(); i++) {
			int level = list.get(i)*Constants.HEIGHT_FACTOR; //multiply to make it taller
			for (int j = 0; j < Constants.WIDTH_FACTOR; j++) { //make stage wider
				new Platform((i * Constants.WIDTH_FACTOR + j) * Platform.tilewidth, 
						origY - level*Platform.tileheight);
			}
		}
	}
	
	public void startGameOver() {
		removeObjects(null,0); 
	}
	
	public void doFrameInGame() {
		setViewOffset((int)player.x, (int)player.y, true); //pans screen with player
		frames++;
		moveObjects();
		
		checkCollision(Constants.FIREBALL_CID, Constants.PLAYER_CID);
		checkCollision(Constants.FIREBALL_CID, Constants.PLATFORM_CID);
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		
		if (darkness.x+darkness.width > player.x) {
			lifeLost(); //caught by darkness
		} else if (player.y > viewHeight()) {
			lifeLost(); //plummeted to hell
		}
		
		if (frames % 35 == 0)//(checkTime(0, 800, 12))
			new Fireball();
	}
	JGFont scoring_font = new JGFont("Arial",0,8);
	public class Fireball extends JGObject {
		public Fireball() {
			super("fireball",
					true,
					player.x + Constants.FIREBALL_HEADSTART,
					0,
					Constants.FIREBALL_CID, 
					"ball",
					random(.5,1.5),
					random(1,2),
					-2 );
		}
	}
	public class Player extends JGObject {
		private int state;
		public Player(double x,double y,double speed) {
			super("player",
					true,
					x,
					y,
					Constants.PLAYER_CID,
					"human1",
					0,
					0,
					speed,
					speed,-1);
			ydir = -1;
			state = Constants.FALLING;
		}
		public void move() {
			xdir = 0; //prevents ice skating
			if (getKey(key_left)  && x > xspeed) xdir=-1;
			if (getKey(key_right) && x < pfWidth() - 3*xspeed) xdir=1;
			if (state != Constants.JUMPING && getKey(key_up)) {
				startJump();
				clearKey(key_up);
			}
			
			if (state == Constants.JUMPING) {
				yspeed -= Constants.GRAVITY;
			} else if (state == Constants.FALLING){
				yspeed = Constants.FALL_SPEED; //If on a platform, the platform prevents falling
			}

		}
		public void startJump() {
			state = Constants.JUMPING;
			yspeed = Constants.JUMP_SPEED;
		}
		public void hit(JGObject obj) {
			if (obj.colid == Constants.FIREBALL_CID) lifeLost();
			else if (obj.colid == Constants.PLATFORM_CID) {
				if (isAbove(obj)){
					state = Constants.FALLING;
					y = obj.y - tileheight;
				}
			}
		}
		private boolean isAbove(JGObject obj) {
			return y+tileheight > obj.y;
		}
	}
	
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
	
	public class Darkness extends JGObject {
		private double width, height;
		public Darkness(double x, double y, double w, double h) {
			super("darkness", true, x, y, Constants.DARKNESS_CID, null, 0, 0, 0, 0, -2);
			// I don't know what expiry is, but example code used -2
			width = w;
			height = h;
			xdir = 1;
			xspeed = 1;
		}
		
		public void move() {
			if (x < player.x - 2*width + Constants.VISIBLE_DARKNESS_WIDTH) {
				x = player.x - 2*width + Constants.VISIBLE_DARKNESS_WIDTH;
			}
		}
		
		public void paint() {
			setColor(JGColor.black);
			drawRect(x,y,width,height,true,false);
		}
	}
}
