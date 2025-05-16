package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class SkullDecoration extends Decoration {
	
	public SkullDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 1f, new RectangleI(0, 0, 16, 16));
	}

}
