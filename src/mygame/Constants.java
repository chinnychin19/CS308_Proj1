package mygame;

public class Constants {
	public static final int PART1_HEIGHT = 24; //in tiles
	public static final int PLAYER_CID = 1, FIREBALL_CID = 2, DARKNESS_CID = 4,
			ITEM_CID = 8, PLATFORM_CID = 16;
	public static final int FALLING = 2, JUMPING = 3;
	
	public static final String STAGE_STRING = "00000000000"+
			"00000234560002200040300001230044040004030000123004400040300001230044"+
			"02345600022002345600022002345600022000234627365227364736252536325263";
	public static final int WIDTH_FACTOR = 3, HEIGHT_FACTOR = 2;
	public static final double FIREBALL_HEADSTART = 100;
	public static final double TOP_MARGIN = 10, VISIBLE_DARKNESS_WIDTH = 10;
	
	public static final double JUMP_SPEED = 10.0, FALL_SPEED = -2, GRAVITY = 1.0;
	public static final double PLAYER_SPEED = 2;

}
