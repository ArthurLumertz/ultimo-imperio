package net.mojang.thelastempire.level.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.Vec2;

public class EntityBloodFX extends EntityFX {
	
	private static Animation<TextureRegion> defaultAnim;
	private static Animation<TextureRegion> splatAnim;

	private Animation<TextureRegion> currentAnim;
	
	static {
		TextureRegion[] f0 = {
				new TextureRegion(Graphics.instance.getParticle(0, 1)),
				new TextureRegion(Graphics.instance.getParticle(1, 1)),
				new TextureRegion(Graphics.instance.getParticle(2, 1))
		};
		defaultAnim = new Animation<TextureRegion>(0.3f, f0);
		
		TextureRegion[] f1 = {
				new TextureRegion(Graphics.instance.getParticle(16, 16, 16, 16)),
				new TextureRegion(Graphics.instance.getParticle(32, 16, 16, 16)),
		};
		splatAnim = new Animation<TextureRegion>(0.3f, f1);
	}
	
	public EntityBloodFX(Level level, float x, float y) {
		super(level, x, y, MathUtils.random(-0.05f, 0.05f), -0.2f, 0.6f, 0.6f, 1, 0f, null);
		currentAnim = defaultAnim;
		shouldRemove = false;
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public void remove() {
		super.remove();
		currentAnim = splatAnim;
	}

	@Override
	public void draw(Graphics g) {
		float rx = bbWidth;
		float ry = bbHeight;
		
		if (currentAnim == splatAnim) {
			rx = 1;
			ry = 1;
		}
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion texture = currentAnim.getKeyFrame(elapsedTime, currentAnim != splatAnim);

		Vec2 position = getDrawPosition();
		g.drawTexture(texture, position.x, position.y, rx, ry);
	}

}
