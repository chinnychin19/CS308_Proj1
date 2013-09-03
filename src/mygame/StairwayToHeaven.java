package mygame;
import java.util.ArrayList;

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
		print("initCanvas");
		setCanvasSettings(Constants.nrtilesx, Constants.nrtilesy,
				Constants.tilex, Constants.tiley, 
				null, 
				new JGColor(Constants.bgcolorR, Constants.bgcolorG, Constants.bgcolorB), 
				null);
	}

	public void initGame() {
		defineMedia("mygame.tbl");
		setFrameRate(45,1);
		setGameState("Title");
	}


	/** Called when the Title state is entered. */
	public void startTitle() {
	}

	public void paintFrameTitle() {
		double firstTextHeight = 40;
		double interval = 25;
		drawString("\"BAT OUT OF HELL\"", viewWidth()/2, firstTextHeight, 0);
		drawString("Hell is chasing you!", viewWidth()/2, firstTextHeight + interval, 0);
		drawString("Run away and pick up building blocks!", viewWidth()/2, firstTextHeight + 2*interval, 0);
		drawString("These will help you later... ;)", viewWidth()/2, firstTextHeight + 3*interval, 0);
		drawString("(Press spacebar to begin)", viewWidth()/2, firstTextHeight + 4*interval, 0);
	}

	public void doFrameTitle() {
		if (getKey(' ')) {
			clearKey(' ');
			// Set both StartGame and InGame states simultaneously.
			setGameState("StartGame");
			addGameState("InGame");
			// set a timer to remove the StartGame state after a few seconds,
			new JGTimer(70, true, "StartGame") {
				public void alarm() {
					removeGameState("StartGame");
				}
			};
		}
	}

	/** The StartGame state is just for displaying a start message. */
	public void paintFrameStartGame() {
		drawString("GO!",viewWidth()/2,90,0);
	}

	/** Called once when game goes into the InGame state. */
	public void startInGame() {
		print("startInGame()");
	}
		
	public void print(Object o) {
		System.out.println(o.toString());
	}
	
	public void initNewLife() {
		print("initNewLife()");
		removeObjects(null,0);
		setPFSize(Constants.PF_HORIZONTAL_TILES_PART1, Constants.PF_VERTICAL_TILES_PART1);
		player = new Player(this, Constants.PLAYER_START_X, Constants.PLAYER_START_Y, Constants.PLAYER_SPEED);
		darkness = new Darkness(this, Constants.PLAYER_START_X-viewWidth(), Constants.TOP_MARGIN, 
				viewWidth()/2, viewHeight());
		frames = 0;
		makeStage();
	}
	
	public void makeStage() {
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
		print("startGameOver");
		removeObjects(null,0); 
	}
	
	public void doFrameInGame() {
		if (getKey('Z')) {
			clearKey('Z');
			//TODO: cheat code
		}
		
		setViewOffset((int)player.x, (int)player.y, true); //pans screen with player
		frames++;
		moveObjects();
		
		checkCollision(Constants.FIREBALL_CID, Constants.PLAYER_CID);
		checkCollision(Constants.FIREBALL_CID, Constants.PLATFORM_CID);
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		
		if (player.x > this.pfWidth() - JGObject.tilewidth ) {
			setGameState("Stairs");
		} else if (darkness.x+darkness.getWidth() > player.x) {
			lifeLost(); //caught by darkness
		} else if (player.y > viewHeight()) {
			lifeLost(); //plummeted to hell
		}
		
		if (frames % Constants.FIREBALL_PERIOD == 0)
			new Fireball(this);
	}
}
