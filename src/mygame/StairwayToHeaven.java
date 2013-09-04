package mygame;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import jgame.*;
import jgame.platform.*;

public class StairwayToHeaven extends StdGame {
	private Player player;
	private boolean reachedHeaven, fireBallsFalling;
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
		setFrameRate(45,1);
		setupFonts();
		setGameState(Constants.STATE_TITLE);
		reachedHeaven = false;
		playAudio("music", "themesong", true);
		lives = Constants.USER_LIVES;
	}
	
	public void setupFonts() {
		status_font = new JGFont("Chiller", 0, 14);
		title_font = new JGFont("Chiller", 0, 14);
		status_color = JGColor.red;
		title_color = JGColor.red;
	}
	
	public void print(Object o) {
		System.out.println(o.toString());
	}
	
	public void initNewLife() {
		removeObjects(null,0);
		items = 0;
		fireBallsFalling = true;
		setPFSize(Constants.PF_HORIZONTAL_TILES_PART1, Constants.PF_VERTICAL_TILES_PART1);
		player = new Player(this, Constants.PLAYER_START_X, Constants.PLAYER_START_Y, Constants.PLAYER_SPEED);
		new Darkness(this, Constants.PLAYER_START_X-viewWidth(), Constants.TOP_MARGIN, 
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
	
	public void goToHell() {
		lifeLost();
	}
	
	//Start of Title stage methods
	public void paintFrameTitle() {
		double firstTextHeight = 40;
		double interval = 25;
		drawString("\"STAIRWAY TO HEAVEN\"", viewWidth()/2, firstTextHeight, 0);
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
			new JGTimer(Constants.STARTGAMEDELAY, true, Constants.STATE_STARTGAME) {
				public void alarm() {
					removeGameState(Constants.STATE_STARTGAME);
				}
			};
		}
	}
	//End of Title stage methods

	//Start of StartGame stage methods
	public void paintFrameStartGame() {
		drawString("GO!",viewWidth()/2,viewHeight()/2,0);
	}
	//End of StartGame stage methods

	//Start of InGame stage methods
	public void checkCheats() {
		if (getKey('B')) { //advance to build stage with 20 items
			clearKey('B');
			items = Constants.CHEAT_ITEMS;
			beginBuilding();
		}
		if (getKey('D')) { //destroy the darkness 
			clearKey('D');
			removeObjects(null, Constants.DARKNESS_CID);
		}
		if (getKey('F')) { //turn off fireballs
			fireBallsFalling = false;
		}
	}
	
	public void checkInGameCollisions() {
		checkCollision(Constants.FIREBALL_CID, Constants.PLAYER_CID);
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		checkCollision(Constants.ITEM_CID, Constants.PLAYER_CID);
		checkCollision(Constants.DARKNESS_CID, Constants.PLAYER_CID);
		checkCollision(Constants.FIREBALL_CID, Constants.PLATFORM_CID);
		checkCollision(Constants.ITEM_CID, Constants.DARKNESS_CID);
	}
	
	public void doFrameInGame() {		
		frames++;
		setViewOffset((int)player.x, (int)player.y, true); //pans screen with player
		moveObjects();
		checkCheats();
		checkInGameCollisions();
		
		if (player.x > this.pfWidth() - JGObject.tilewidth ) {
			beginBuilding();
		}  else if (player.y > viewHeight()) {
			goToHell();
		}
		
		if (fireBallsFalling && frames % Constants.FIREBALL_PERIOD == 0)
			new Fireball(this);
	}
	//End of InGame stage methods
	
	public void beginBuilding() {
		setGameState(Constants.STATE_BUILDINFO);
	}

	//Start of BuildInfo stage methods
	public void startBuildInfo() {
		lives = 1;
		removeObjects(null,0);
		setPFSize(viewWidth(), viewHeight());
		setViewOffset(0, 0, false);
	}
	public void paintFrameBuildInfo() {
		String congrats = "CONGRATULATIONS! (you are not done)";
		String dir1 = "Use arrow keys to position items.";
		String dir2 = "Place items with spacebar.";
		String dir3 = "Items will fall when you touch them.";
		String dir4 = "Reach heaven to win! (One life)";
		String dir5 = "Press spacebar to continue...";
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
	private Platform tempBlock1, tempBlock2; //blocks that user will place on playing field
	public void startInBuild() {
		removeObjects(null,0);
		player = new Player(this, 0, Constants.PLAYER_START_Y, Constants.PLAYER_SPEED);
		new Platform(player.x, player.y+JGObject.tileheight); //Platform under player
		new Platform(viewWidth()-JGObject.tilewidth, 
				Constants.HEAVEN_Y+JGObject.tileheight); //Platforms under heaven
		new Platform(viewWidth()-2*JGObject.tilewidth, 
				Constants.HEAVEN_Y+JGObject.tileheight); //Platforms under heaven
		new Heaven(viewWidth()-2*JGObject.tilewidth, Constants.HEAVEN_Y);
		tempBlock1 = new Platform(viewWidth()/2, viewHeight()/2, true);
		tempBlock2 = new Platform(viewWidth()/2+JGObject.tilewidth, viewHeight()/2, true);
	}
			
	public void doFrameInBuild() {
		if (items == 0) {
			tempBlock1.remove();
			tempBlock2.remove();
			setGameState(Constants.STATE_STAIRWAY);
		}
		if (getKey(' ')) {
			clearKey(' ');
			items--;
			tempBlock1 = new Platform(tempBlock1.x, tempBlock1.y, true);
			tempBlock2 = new Platform(tempBlock2.x, tempBlock2.y, true);
		}
		moveTempBlocks();
		checkCollision(Constants.HEAVEN_CID, Constants.PLAYER_CID);
	}
	public void moveTempBlocks() {
		double dx = 0, dy = 0;
		if (getKey(KeyUp)) {
			clearKey(KeyUp);
			dy = -1*Constants.PLATFORM_SPEEDY;
		}
		if (getKey(KeyDown)) {
			clearKey(KeyDown);
			dy = Constants.PLATFORM_SPEEDY;
		}
		if (getKey(KeyLeft)) {
			clearKey(KeyLeft);
			dx = -1*Constants.PLATFORM_SPEEDY;
		}
		if (getKey(KeyRight)) {
			clearKey(KeyRight);
			dx = Constants.PLATFORM_SPEEDY;
		}
		tempBlock1.x+=dx;
		tempBlock1.y+=dy;
		tempBlock2.x+=dx;
		tempBlock2.y+=dy;
	}
	//End of InBuild stage methods
	
	//Start of Stairway stage methods
	public void doFrameStairway() {
		moveObjects();
		checkCollision(Constants.PLATFORM_CID, Constants.PLAYER_CID);
		checkCollision(Constants.HEAVEN_CID, Constants.PLAYER_CID);
		if (player.y > viewHeight()) {
			goToHell();
		}
		if (player.x > viewWidth() - JGObject.tilewidth) {
			player.x = viewWidth() - JGObject.tilewidth;
		} else if (player.x < 0) {
			player.x = 0;
		}
	}
	public void paintFrameStairway() {
		//Present at bottom of screen
		drawString("Jump to heaven!", viewWidth()/2, Constants.PLAYER_START_Y+JGObject.tileheight*2,0);
	}
	//End of Stairway stage methods
	
	public void winGame() {
		reachedHeaven = true;
		setGameState(Constants.STATE_GAMEOVER);
	}
	
	//Start of GameOver stage methods
	public void startGameOver() {
		removeObjects(null,0);
	}
	public void paintFrameGameOver() {
		String message = reachedHeaven ? "YOU WIN!!! :)" : "YOU LOSE... :(";
		drawString(message, viewWidth()/2, viewHeight()/2, 0);
		drawString("(spacebar for title menu)", viewWidth()/2, viewHeight()/2+40, 0);
		
	}
	public void doFrameGameOver() {
		if (getKey(KeyEsc)) {
			System.exit(0);
		}
	}
	//End of GameOver stage methods
}
