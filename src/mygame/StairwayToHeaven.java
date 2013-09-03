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
	
	public void startGameOver() {
		print("startGameOver");
		removeObjects(null,0); 
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
		setGameState(Constants.STATE_TITLE);
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
	
	public int getRandomItemSteps() {
		return (int)random(Constants.MIN_ITEM_STEPS, Constants.MAX_ITEM_STEPS);
	}
	public void makeStage() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (char c: Constants.STAGE_STRING.toCharArray()) {
			list.add(Integer.parseInt(""+c));
		}
		int nextItemSteps = getRandomItemSteps();
		for (int i = 0; i < list.size(); i++) {
			//subtract 1 from level to force the lowest to be beneath the player
			int level = -1 + list.get(i)*Constants.HEIGHT_FACTOR;
			
			double platformY = Constants.PLAYER_START_Y - level*Platform.tileheight;			
			for (int j = 0; j < Constants.WIDTH_FACTOR; j++) { //make stage wider
				double platformX = (i * Constants.WIDTH_FACTOR + j) * Platform.tilewidth;
				new Platform(platformX, platformY);
				if (nextItemSteps == 0) {
					nextItemSteps = getRandomItemSteps();
					new Item(platformX, platformY - JGObject.tileheight);					
				} else {
					nextItemSteps --;
				}
			}
		}
	}


	//Start of Title stage methods
	public void paintFrameTitle() {
		double firstTextHeight = 40;
		double interval = 25;
		drawString("\"BAT OUT OF HELL\"", viewWidth()/2, firstTextHeight, 0);
		drawString("Hell is chasing you!", viewWidth()/2, firstTextHeight + interval, 0);
		drawString("Avoid fireballs and pick up items!", viewWidth()/2, firstTextHeight + 2*interval, 0);
		drawString("(Press spacebar to begin)", viewWidth()/2, firstTextHeight + 4*interval, 0);
	}
	public void doFrameTitle() {
		if (getKey(' ')) {
			clearKey(' ');
			// Set both StartGame and InGame states simultaneously.
			setGameState(Constants.STATE_STARTGAME);
			addGameState(Constants.STATE_INGAME);
			// set a timer to remove the StartGame state after a few seconds,
			new JGTimer(70, true, Constants.STATE_STARTGAME) {
				public void alarm() {
					removeGameState(Constants.STATE_STARTGAME);
				}
			};
		}
	}
	//End of Title stage methods

	//Start of StartGame stage methods
	public void paintFrameStartGame() {
		drawString("GO!",viewWidth()/2,90,0);
	}
	//End of StartGame stage methods

	//Start of InGame stage methods
	public void startInGame() {
		print("startInGame()");
	}
			
	public void doFrameInGame() {
		if (getKey('C')) { //Cheat code: SHIFT+C
			clearKey('C');
			//TODO: cheat code
			items = 10;
			beginBuildStage();
		}
		
		setViewOffset((int)player.x, (int)player.y, true); //pans screen with player
		frames++;
		moveObjects();
		
		checkCollision(Constants.FIREBALL_CID, Constants.PLAYER_CID);
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		checkCollision(Constants.ITEM_CID, Constants.PLAYER_CID);
		checkCollision(Constants.FIREBALL_CID, Constants.PLATFORM_CID);
		checkCollision(Constants.ITEM_CID, Constants.DARKNESS_CID);
		checkCollision(Constants.PLAYER_CID, Constants.DARKNESS_CID);
		
		if (player.x > this.pfWidth() - JGObject.tilewidth ) {
			beginBuildStage();
		}  else if (player.y > viewHeight()) {
			lifeLost(); //plummeted to hell
		}
		
		if (frames % Constants.FIREBALL_PERIOD == 0)
			new Fireball(this);
	}
	//End of InGame stage methods
	
	public void beginBuildStage() {
		setGameState(Constants.STATE_BUILDINFO);
	}

	//Start of BuildInfo stage methods
	public void startBuildInfo() {
		print("startBuildInfo()");
		lives = 1;
		removeObjects(null,0);
		setPFSize(viewWidth(), viewHeight());
		setViewOffset(0, 0, false);
	}
	public void paintFrameBuildInfo() {
		String congrats = "CONGRATULATIONS! (you are not done)";
		String dir1 = "Use arrow keys to position items.";
		String dir2 = "Place items with spacebar.";
		String dir3 = "Items will sink when you touch them.";
		String dir4 = "Reach heaven to win! (One life)";
		String dir5 = "Press spacebar to continue";
		double dirYStart = 70;
		double interval = 20;
		drawString(congrats,viewWidth()/2,dirYStart-interval,0);
		drawString(dir1,viewWidth()/2,dirYStart,0);
		drawString(dir2,viewWidth()/2,dirYStart+interval,0);
		drawString(dir3,viewWidth()/2,dirYStart+2*interval,0);
		drawString(dir4,viewWidth()/2,dirYStart+3*interval,0);
		drawString(dir5,viewWidth()/2,dirYStart+4*interval,0);
	}
	public void doFrameBuildInfo() {
		if (getKey(' ')) {
			clearKey(' ');
			setGameState(Constants.STATE_INBUILD);
		}
	}
	//End of BuildInfo stage methods

	//Start of InBuild stage methods
	private Platform tempBlock1, tempBlock2;
	public void startInBuild() {
		print("startInBuild()");
		removeObjects(null,0);
		player = new Player(this, 0, Constants.PLAYER_START_Y, Constants.PLAYER_SPEED);
		new Platform(player.x, player.y+JGObject.tileheight); //Platform under player
		new Platform(viewWidth()-JGObject.tilewidth, 
				Constants.HEAVEN_Y+JGObject.tileheight); //Platforms under heaven
		new Platform(viewWidth()-2*JGObject.tilewidth, 
				Constants.HEAVEN_Y+JGObject.tileheight); //Platforms under heaven
		new Heaven(viewWidth()-2*JGObject.tilewidth, Constants.HEAVEN_Y);
		tempBlock1 = new Platform(viewWidth()/2, viewHeight()/2);
		tempBlock2 = new Platform(viewWidth()/2+JGObject.tilewidth, viewHeight()/2);
	}
			
	public void doFrameInBuild() {
		if (items == 0) {
			tempBlock1.remove();
			tempBlock2.remove();
			setGameState(Constants.STATE_STAIRWAY);
		}
		if (getKey(KeyEnter)) {
			clearKey(KeyEnter);
			items--;
			tempBlock1 = new Platform(tempBlock1.x, tempBlock1.y);
			tempBlock2 = new Platform(tempBlock2.x, tempBlock2.y);
		}
		if (getKey(KeyUp)) {
			clearKey(KeyUp);
			tempBlock1.y -= Constants.PLATFORM_SPEEDY;
			tempBlock2.y -= Constants.PLATFORM_SPEEDY;
		}
		if (getKey(KeyDown)) {
			clearKey(KeyDown);
			tempBlock1.y += Constants.PLATFORM_SPEEDY;
			tempBlock2.y += Constants.PLATFORM_SPEEDY;
		}
		if (getKey(KeyLeft)) {
			clearKey(KeyLeft);
			tempBlock1.x -= Constants.PLATFORM_SPEEDX;
			tempBlock2.x -= Constants.PLATFORM_SPEEDX;
		}
		if (getKey(KeyRight)) {
			clearKey(KeyRight);
			tempBlock1.x += Constants.PLATFORM_SPEEDX;
			tempBlock2.x += Constants.PLATFORM_SPEEDX;
		}
	}
	//End of InBuild stage methods
	
	//Start of Stairway stage methods
	public void doFrameStairway() {
		moveObjects();
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
	}
	//End of Stairway stage methods	
}
