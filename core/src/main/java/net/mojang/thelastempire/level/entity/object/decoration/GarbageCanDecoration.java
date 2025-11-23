package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class GarbageCanDecoration extends Decoration {
	
	public GarbageCanDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 2f, new RectangleI(0, 64, 16, 32));
	}

}
