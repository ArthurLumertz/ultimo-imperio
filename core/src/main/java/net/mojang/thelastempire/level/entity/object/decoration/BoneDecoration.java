package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class BoneDecoration extends EntityDecoration {
	
	public BoneDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 1f, new RectangleI(16, 0, 16, 16));
	}

}
