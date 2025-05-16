package net.mojang.thelastempire.level.entity.object;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;

public class Tree extends EntityPassable {

	public Tree(Level level, int x, int y) {
		super(level, x, y, 3, 6);
		noPhysics = true;
	}

	@Override
	public void draw(Graphics g) {
		TextureRegion texture = g.getPlant(0, 0);

		g.setColor(1f, 1f, 1f, getAlpha());
		g.drawTexture(texture, x - 1, y, 3, 6);
		g.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public Rectangle getIntersectsRect() {
		return Rectangle.tmp.set(x - 1, y + 3, bbWidth, bbHeight - 3);
	}
	
	@Override
	public boolean shouldTick() {
		int offset = 5;
		Rectangle rectangle = Rectangle.tmp2.set(x - offset, y - offset, 3 + offset, 6 + offset);
		return Graphics.instance.inViewport(rectangle);
	}

	@Override
	public String getName() {
		return "Tree";
	}

}
