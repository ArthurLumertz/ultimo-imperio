package net.mojang.thelastempire.level.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.Vec2;

public class BulletEntity extends Entity {

	private static TextureRegion texture = new TextureRegion(Resources.getTexture("icons"), 0, 32, 4, 6);

	private float distance;
	private float rotation;

	private Class<?> clazz;

	public BulletEntity(Level level, float x, float y, float tx, float ty, Class<?> clazz) {
		super(level);
		setPos(x, y);

		float dx = tx - x;
		float dy = ty - y;
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		if (length > 0f) {
			dx /= length;
			dy /= length;
		}

		xd = dx;
		yd = dy;

		rotation = MathUtils.atan2(dx, dy) * MathUtils.radDeg;
		noPhysics = true;
		bbWidth = 4f / 16f;
		bbHeight = 6f / 16f;
		this.clazz = clazz;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;

		move(xd, yd);
		moveRelative(0f, 0f, speed);

		float dx = x - xo;
		float dy = y - yo;
		distance += (float) Math.sqrt(dx * dx + dy * dy);
		if (distance > 16f) {
			remove();
		}

		for (Mob mob : level.getVisibleMobs(true)) {
			if (!mob.getClass().equals(clazz)) {
				continue;
			}

			Rectangle mobRectangle = Rectangle.tmp.set(mob.x, mob.y, 1f, 2f);
			Rectangle thisRectangle = Rectangle.tmp2.set(x, y, bbWidth, bbHeight);
			if (thisRectangle.overlaps(mobRectangle)) {
				mob.onAttack(level.getPlayer(), 1);
				
				float s = 0.15f;
				mob.applyForce(xd * s, yd * s);
				
				remove();
				break;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		Vec2 position = getDrawPosition();
		g.drawTexture(texture, position.x, position.y, bbWidth, bbHeight, -rotation);
	}
	
	@Override
	public boolean shouldTick() {
		return true;
	}

}
