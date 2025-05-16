package net.mojang.thelastempire.level.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.Vec2;

public class EntityBloodFX extends EntityFX {

	private static TextureRegion[] frames = { new TextureRegion(Graphics.instance.getParticle(0, 1)),
			new TextureRegion(Graphics.instance.getParticle(1, 1)),
			new TextureRegion(Graphics.instance.getParticle(2, 1)) };
	private static TextureRegion splat = new TextureRegion(Graphics.instance.getParticle(16, 16, 16, 16));

	public EntityBloodFX(Level level, float x, float y) {
		super(level, x, y, MathUtils.random(-0.05f, 0.05f), -0.2f, 0.6f, 0.6f, 1, 0.3f, frames);
		shouldRemove = false;
	}

	@Override
	public void draw(Graphics g) {

		Vec2 position = getDrawPosition();
		float rx = bbWidth;
		float ry = bbHeight;
		
		TextureRegion texture = animation.getKeyFrame(elapsedTime, true);
		if (shouldAnimate) {
			elapsedTime += Gdx.graphics.getDeltaTime();
		} else {
			texture = splat;
			rx = 1f;
			ry = 1f;
		}

		g.setColor(1f, 1f, 1f, 1f);
		g.drawTexture(texture, position.x, position.y, rx, ry);
		g.setColor(1f, 1f, 1f, 1f);
	}

}
