package net.mojang.thelastempire.level.entity.object.furniture;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class CarpetFurniture extends EntityFurniture {

	private static RectangleI regularTexture = new RectangleI(128, 80, 48, 48);
	private static RectangleI largeTexture = new RectangleI(128, 128, 64, 64);

	public CarpetFurniture(Level level, float x, float y, String type) {
		super(level, x, y, "large".equals(type) ? 4 : 3, "large".equals(type) ? 4 : 3,
				"large".equals(type) ? largeTexture : regularTexture);
		noPhysics = true;
	}

	@Override
	public int getDrawLayer() {
		return -1;
	}

	@Override
	public String getName() {
		return "Carpet Furniture";
	}

}
