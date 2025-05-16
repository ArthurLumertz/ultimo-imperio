package net.mojang.thelastempire.level;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.level.tile.Tile;

public class Chunk {

	private Level level;

	private int x0;
	private int y0;
	private int x1;
	private int y1;
	private AABB boundingBox;

	public Chunk(Level level, int x0, int y0, int x1, int y1) {
		this.level = level;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.boundingBox = new AABB(x0, y0, x1, y1);
	}

	public void draw(Graphics g) {
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				int tileId = level.getTile(x, y);
				if (tileId > 0) {
					Tile.tiles[tileId].draw(g, x, y, level);
				}
			}
		}
	}

	public boolean shouldRender() {
		return Graphics.instance.inViewport(boundingBox);
	}

}
