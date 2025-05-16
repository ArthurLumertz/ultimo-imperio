package net.mojang.thelastempire.level.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;
import net.mojang.thelastempire.level.phys.AABB;

public class FenceTile extends Tile {

	protected FenceTile(int id, Material material) {
		super(id, material);
	}

	@Override
	public void draw(Graphics g, int x, int y, Level level) {
		boolean north = level.getTile(x, y + 1) == id;
		boolean south = level.getTile(x, y - 1) == id;
		boolean west = level.getTile(x - 1, y) == id;
		boolean east = level.getTile(x + 1, y) == id;

		if (!north && !south && !west && !east) {
			return;
		}

		float w = 1f;
		float h = 1f;
		float xo = 0f;

		TextureRegion texture = g.getTile(112 + 6, 32, 16, 16);
		if (north && east) {
			texture = g.getTile(112, 32 + 16, 16, 16);
		} else if (north && west) {
			texture = g.getTile(112 + 16, 32 + 16, 16, 16);
		} else if (south && east) {
			texture = g.getTile(112, 32, 16, 16);
		} else if (south && west) {
			texture = g.getTile(112 + 16, 32, 16, 16);
		}

		if (north && south && !west && !east) {
			texture = g.getTile(112 + 32, 32, 16, 16);
			if (level.getTile(x + 1, y) == stone.id) {
				xo = (1f) - (5 / 16f);
			}
		}

		if (north && !south && !west && !east) {
			texture = g.getTile(112 + 32, 48, 16, 16);
		}

//		grass.draw(g, x, y, level);
		if (level.getTile(x, y+1)==Tile.dirt.id) {			
			g.drawTexture(Tile.dirt.getTexture(), x, y, 1, 1);
//			dirt.draw(g, x, y, level);
		} else {
			g.drawTexture(Tile.grass.getTexture(), x, y, 1, 1);
		}
		g.drawTexture(texture, x + xo, y, w, h);

	}

	@Override
	public AABB getAABB(int x, int y, Level level) {
		if (level.getTile(x + 1, y) == stone.id) {
			return AABB.newTemp(x + 1f - (5/16f), y, x + 1, y + 0.7f);
		} else if (level.getTile(x - 1, y) == stone.id) {
			return AABB.newTemp(x, y, x + (5/16f), y + 0.7f);
		}
		return AABB.newTemp(x, y, x + 1f, y + 0.7f);
	}

}
