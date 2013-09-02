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
	
	public static void main(String[]args) {
		new StairwayToHeaven(parseSizeArgs(args,0));
	}
	public StairwayToHeaven() {
		initEngineApplet();
	}
	public StairwayToHeaven(JGPoint size) {
		initEngine(size.x,size.y);
	}
	public Player getPlayer() {
		return player;
	}
	
	public void initCanvas() {						
		setCanvasSettings(Constants.nrtilesx, Constants.nrtilesy,
				Constants.tilex, Constants.tiley, 
				null, 
				new JGColor(Constants.bgcolorR, Constants.bgcolorG, Constants.bgcolorB), 
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
		setPFSize(Constants.PF_HORIZONTAL_TILES_PART1, Constants.PF_VERTICAL_TILES_PART1);
		frames = 0;
		setGameState("Scrolling");
	}
	
	public void startScrolling() {
		removeObjects(null,0);
		print("started scrolling mode");
	}
	
	public void print(Object o) {
		System.out.println(o.toString());
	}
	
	public void initNewLife() {
		removeObjects(null,0);
		player = new Player(this, Constants.PLAYER_START_X, Constants.PLAYER_START_Y, Constants.PLAYER_SPEED);
		darkness = new Darkness(this, Constants.PLAYER_START_X-viewWidth(), Constants.TOP_MARGIN, 
				viewWidth()/2, viewHeight());
		makeStage();
	}
	private void makeStage() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (char c: Constants.STAGE_STRING.toCharArray()) {
			list.add(Integer.parseInt(""+c));
		}
		for (int i = 0; i < list.size(); i++) {
			//subtract 1 from level to force the lowest to be beneath the player
			int level = -1 + list.get(i)*Constants.HEIGHT_FACTOR;
			
			for (int j = 0; j < Constants.WIDTH_FACTOR; j++) { //make stage wider
				new Platform((i * Constants.WIDTH_FACTOR + j) * Platform.tilewidth, 
						Constants.PLAYER_START_Y - level*Platform.tileheight);
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
		
		if (player.x > this.pfWidth() - JGObject.tilewidth ) {
			System.exit(0); //TODO: set new stage with text first
		} else if (darkness.x+darkness.getWidth() > player.x) {
			lifeLost(); //caught by darkness
		} else if (player.y > viewHeight()) {
			lifeLost(); //plummeted to hell
		}
		
		if (frames % Constants.FIREBALL_PERIOD == 0)
			new Fireball(this);
	}
}
