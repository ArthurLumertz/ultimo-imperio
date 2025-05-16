package net.mojang.thelastempire.level.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;

public class GrassTile extends Tile {

    protected GrassTile(int id, Material material) {
        super(id, material);
    }

    @Override
    public TextureRegion getDynamicTexture(Graphics g, int x, int y, Level level) {
        TextureRegion texture = getTexture();
        
        int data = level.getData(x, y);
        switch (data) {
            case 1:
                texture = g.getTile(7, 0);
                break;
            case 2:
                texture = g.getTile(8, 0);
                break;
            case 3:
                texture = g.getTile(9, 0);
                break;
        }
        
        return texture;
    }

}
