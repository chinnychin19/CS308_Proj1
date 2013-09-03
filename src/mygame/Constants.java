package mygame;

import jgame.JGObject;

public class Constants {
	//Stage constants
	public static final int WIDTH_FACTOR = 3, HEIGHT_FACTOR = 2;
	public static final String STAGE_STRING = "00000000000"+
			"00000234560002200040300001230044040004030000123004400040300001230044"+
			"00000234560002200234560002200234560002200023462736522736473625200000";
	public static final int PF_VERTICAL_TILES_PART1 = 24, 
			PF_HORIZONTAL_TILES_PART1 = STAGE_STRING.length() * WIDTH_FACTOR;
	
	//Collision constants
	public static final int PLAYER_CID = 1, FIREBALL_CID = 2, DARKNESS_CID = 4,
			ITEM_CID = 8, PLATFORM_CID = 16, HEAVEN_CID = 32;
	
	//Spacing constants
	public static final double FIREBALL_HEADSTART = 100;
	public static final double TOP_MARGIN = 10, VISIBLE_DARKNESS_WIDTH = 10;
	public static final double PLAYER_START_X = JGObject.tilewidth*40, 
			PLAYER_START_Y = JGObject.tileheight * (PF_VERTICAL_TILES_PART1 - 4);
	public static final double HEAVEN_Y = JGObject.tileheight*5;
	
	//Speed/frequency constants
	public static final double JUMP_SPEED = 10.0, FALL_SPEED = -2, GRAVITY = 1.0;
	public static final double PLAYER_SPEED = 2.5;
	public static final double FIREBALL_MIN_XSPEED = .5, FIREBALL_MAX_XSPEED = 1.5,
			FIREBALL_MIN_YSPEED = 1, FIREBALL_MAX_YSPEED = 2;
	public static final int FIREBALL_PERIOD = 35;
	public static final double PLATFORM_SPEEDX = JGObject.tilewidth, PLATFORM_SPEEDY = JGObject.tileheight;

	//Dimension constants -- yes I know these are lowercase and I don't care
	public static final int nrtilesx = 32, nrtilesy = 24, tilex = 8, tiley = 8;

	//Color constants -- yes I know these are lowercase and I don't care
	public static final int bgcolorR = 80, bgcolorG = 250, bgcolorB = 91;
	
	//Item constants
	public static final int MIN_ITEM_STEPS = 10, MAX_ITEM_STEPS = 40;
	
	//Stage name constants
	public static final String STATE_TITLE = "Title", STATE_STARTGAME = "StartGame", 
			STATE_INGAME = "InGame", STATE_BUILDINFO = "BuildInfo", STATE_INBUILD = "InBuild",
			STATE_STAIRWAY = "Stairway";
}
