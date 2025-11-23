package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class FlowerDecoration extends Decoration {

	private static final RectangleI[] textures = {
			new RectangleI(16, 32, 16, 16),
			new RectangleI(32, 32, 16, 16),
			new RectangleI(48, 32, 16, 16)
	};

	public FlowerDecoration(Level level, float x, float y, int type) {
		super(level, x, y, 1f, 1f, textures[type]);
	}

}
