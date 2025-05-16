package net.mojang.thelastempire.level.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;
import net.mojang.thelastempire.level.phys.AABB;

public class BorderTile extends Tile {

	protected BorderTile(int id, Material material) {
		super(id, material);
	}

	@Override
	public void draw(Graphics g, int x, int y, Level level) {
		int northId = level.getTile(x, y + 1);
		int southId = level.getTile(x, y - 1);
		int westId = level.getTile(x - 1, y);
		int eastId = level.getTile(x + 1, y);
		
		boolean north = northId == Tile.woodFloor.id || northId == Tile.stoneFloor.id;
		boolean south = southId == Tile.woodFloor.id || southId == Tile.stoneFloor.id;
		boolean west = westId == Tile.woodFloor.id || westId == Tile.stoneFloor.id;
		boolean east = eastId == Tile.woodFloor.id || eastId == Tile.stoneFloor.id;

		if (!north && !south && !west && !east) {
			return;
		}

		float w = 1f;
		float h = 1f;

		TextureRegion texture = null;
		if (north) {
			texture = g.getTile(14, 7);
		} else if (south) {
			texture = g.getTile(14 * 16, 5 * 16, 16, 23);
			h = 23f / 16f;
		} else if (west) {
			texture = g.getTile(13, 8);
		} else if (east) {
			texture = g.getTile(15, 8);
		}
		
		if (!north && south && !west && east) {
			texture = g.getTile(13, 7);
		} else if (!north && south && west && !east) {
			texture = g.getTile(15, 7);
		} else if (north && !south && west && !east) {
			texture = g.getTile(13, 7);
		} else if (north && !south && !west && east) {
			texture = g.getTile(15, 7);
		}

		g.drawTexture(texture, x, y, w, h);
	}
	
	@Override
	public AABB getAABB(int x, int y, Level level) {
		return AABB.newTemp(x, y, x + 0.9f, y + 1f);
	}

}
