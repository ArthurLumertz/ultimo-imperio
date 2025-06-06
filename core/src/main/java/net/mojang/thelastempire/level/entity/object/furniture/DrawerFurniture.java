package net.mojang.thelastempire.level.entity.object.furniture;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class DrawerFurniture extends EntityFurniture {

	public DrawerFurniture(Level level, float x, float y) {
		super(level, x, y, 1, 2, new RectangleI(48, 32, 16, 32));
	}
	
	@Override
	public String getName() {
		return "Drawer Furniture";
	}

}
