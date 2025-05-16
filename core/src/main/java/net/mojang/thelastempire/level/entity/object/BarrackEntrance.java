package net.mojang.thelastempire.level.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;

public class BarrackEntrance extends EntityPassable {

	private Texture texture = Resources.getTexture("barrackentrance");
	
	public BarrackEntrance(Level level, float x, float y, String levelToGo) {
		super(level, x - (16f / 2f), y, 16, 2);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(1f, 1f, 1f, getAlpha());
		g.drawTexture(texture, x, y, 16, 6);
		g.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public Rectangle getIntersectsRect() {
		return Rectangle.tmp2.set(x, y + 2f, bbWidth, bbHeight + 1.5f);
	}

	@Override
	public boolean shouldRender() {
		return Graphics.instance.inViewport(boundingBox.grow(4f, 4f));
	}

	@Override
	public String getName() {
		return "Barrack Entrance";
	}

}
