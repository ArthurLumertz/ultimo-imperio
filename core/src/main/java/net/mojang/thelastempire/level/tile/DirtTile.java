package net.mojang.thelastempire.level.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;

public class DirtTile extends Tile {

	protected DirtTile(int id, Material material) {
		super(id, material);
	}
	
	@Override
    public TextureRegion getDynamicTexture(Graphics g, int x, int y, Level level) {
        TextureRegion texture = getTexture();
        texture = getConnectedTextures(level, x, y, 0, 4, Tile.grass.id, texture, g);
        texture = getConnectedTextures(level, x, y, 0, 8, Tile.stone.id, texture, g);
        return texture;
    }

}
