package net.mojang.thelastempire.level.entity.object.furniture;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class BedFurniture extends EntityFurniture {

	public BedFurniture(Level level, float x, float y) {
		super(level, x, y, 3, 2, new RectangleI(48, 0, 48, 32));
	}
	
	@Override
	public String getName() {
		return "Bed Furniture";
	}

}
