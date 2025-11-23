package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class BarrelDecoration extends Decoration {
	
	public BarrelDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 2f, new RectangleI(48, 0, 16, 32));
	}

}
