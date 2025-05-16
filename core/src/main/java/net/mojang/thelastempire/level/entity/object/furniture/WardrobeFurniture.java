package net.mojang.thelastempire.level.entity.object.furniture;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class WardrobeFurniture extends EntityFurniture {

	public WardrobeFurniture(Level level, float x, float y) {
		super(level, x, y, 25f/16f, 2, new RectangleI(64, 32, 25, 32));
	}
	
	@Override
	public String getName() {
		return "Wardrobe Furniture";
	}

}
