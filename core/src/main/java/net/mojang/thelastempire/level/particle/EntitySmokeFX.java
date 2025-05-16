package net.mojang.thelastempire.level.particle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.Vec2;

public class EntitySmokeFX extends EntityFX {

	private static TextureRegion[] frames = { new TextureRegion(Graphics.instance.getParticle(0, 0)),
			new TextureRegion(Graphics.instance.getParticle(1, 0)),
			new TextureRegion(Graphics.instance.getParticle(2, 0)), };

	private int direction;

	public EntitySmokeFX(Level level, float x, float y) {
		super(level, x, y, 0, 0.1f, 0.6f, 0.6f, 60, 0.3f, frames);
		direction = MathUtils.randomBoolean() ? -1 : 1;
	}

	@Override
	public Vec2 getDrawPosition() {
		float xx = xo + (x - xo) * TheLastEmpire.a;
		float yy = yo + (y - yo) * TheLastEmpire.a;
		
		float speed = 5f;
		float scalar = 0.2f;

		if (direction < 0) {
			xx += (float) Math.sin(elapsedTime * speed) * scalar;
		} else {
			xx -= (float) Math.cos(elapsedTime * speed) * scalar;
		}

		return Vec2.newTemp(xx, yy);
	}

}
