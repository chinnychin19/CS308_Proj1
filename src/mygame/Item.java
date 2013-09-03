package mygame;

import jgame.JGObject;

public class Item extends JGObject {
	public Item(double x, double y) {
		super("item",
				true,
				x,
				y,
				Constants.ITEM_CID, 
				"block",
				0,
				0,
				-2 );
	}
}
