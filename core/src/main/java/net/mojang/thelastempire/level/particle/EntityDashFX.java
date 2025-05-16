package net.mojang.thelastempire.level.particle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.Vec2;

public class EntityDashFX extends EntityFX {

	private static TextureRegion[] frames = { new TextureRegion(Graphics.instance.getParticle(0, 16, 7, 10)),
			new TextureRegion(Graphics.instance.getParticle(7, 16, 9, 8)) };
	
	private Light light;

	public EntityDashFX(Level level, float x, float y) {
		super(level, x, y, MathUtils.random(-0.1f, 0.1f), -0.1f, 0.4f, 0.4f, 20, 0.3f, frames);
		light = new Light(x, y, 0.2f, 0.1f, 0.8f, new Color(0.2f, 0.2f, 1f, 1f));
		Graphics.instance.setLight(light);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		Vec2 position = getDrawPosition();
		light.setPos(position.x, position.y);
		float col = 1f - (timer / (float) lifespan);
		light.intensity = col / 3f;
	}
	
	@Override
	public void remove() {
		super.remove();
		Graphics.instance.removeLight(light);
	}

}
