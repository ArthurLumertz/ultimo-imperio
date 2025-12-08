package net.mojang.thelastempire.level.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.math.Vec2;

public class EntityFX extends Entity {

	protected Animation<TextureRegion> animation;
	protected float elapsedTime;
	protected int lifespan;
	protected int timer = 0;
	protected boolean shouldRemove = true;
	protected boolean shouldAnimate = true;

	public EntityFX(Level level, float x, float y, float xd, float yd, float w, float h, int lifespan, float fd,
			TextureRegion[] frames) {
		super(level);
		this.xd = xd;
		this.yd = yd;
		xo = x;
		yo = y;
		if (frames != null) {
			animation = new Animation<TextureRegion>(fd, frames);
		}
		noPhysics = true;
		speed = 0.2f;
		boundingBox = AABB.newPermanent(x, y, x + bbWidth, y + bbHeight);
		setSize(w, h);
		setPos(x, y);
		this.lifespan = lifespan;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;

		if (timer > lifespan) {
			shouldAnimate = false;
			return;
		}

		x += xd;
		y += yd;

		timer++;
		if (timer > lifespan) {
			remove();
		}
	}

	@Override
	public void remove() {
		if (shouldRemove) {
			super.remove();
		}
	}

	@Override
	public void draw(Graphics g) {
		Vec2 position = getDrawPosition();

		TextureRegion texture = animation.getKeyFrame(elapsedTime, true);
		if (shouldAnimate) {
			elapsedTime += Gdx.graphics.getDeltaTime();
		}

		float col = 1f - (timer / (float) lifespan);
		g.setColor(1f, 1f, 1f, col);
		g.drawTexture(texture, position.x, position.y, bbWidth, bbHeight);
		g.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public boolean shouldTick() {
		return true;
	}

	@Override
	public boolean shouldRender() {
		Rectangle bb = Rectangle.tmp.set(x, y, bbWidth, bbHeight);
		return Graphics.instance.inViewport(bb);
	}

	@Override
	public String getName() {
		return "EntityFX";
	}

}
