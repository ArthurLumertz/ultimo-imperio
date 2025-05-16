package net.mojang.thelastempire.level.entity.object.furniture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.Level;

public class FireplaceFurniture extends EntityFurniture {

	private Animation<TextureRegion> animation;
	private int timer = 0;
	private float animationTimer = 0;

	private Light light;
	private int direction = 0;

	public FireplaceFurniture(Level level, float x, float y) {
		super(level, x, y, 3, 4, null);
		
		direction = MathUtils.randomBoolean() ? -1 : 1;
		light = new Light(x + 1.5f, y + 0.5f, 1f, 0.1f, 3f, new Color(1f, 0.4f, 0.2f, 1f));
		Graphics.instance.setLight(light);
		
		animation = new Animation<TextureRegion>(0.3f, new TextureRegion[] {
				new TextureRegion(Graphics.instance.getFurniture(0, 128, 48, 64)),
				new TextureRegion(Graphics.instance.getFurniture(48, 128, 48, 64))
		});
		
	}

	@Override
	public void tick() {
		timer++;

		float scalar = 0f;
		float speed = 0.1f;
		float scale = 0.2f;

		if (direction == -1) {
			scalar = (float) MathUtils.sin(timer * speed * MathUtils.PI) * scale;
		} else {
			scalar = (float) MathUtils.cos(timer * speed * MathUtils.PI) * scale;
		}

		light.outerRadius += scalar * scale;
	}

	@Override
	public void draw(Graphics g) {
		animationTimer += Gdx.graphics.getDeltaTime();
		TextureRegion texture = animation.getKeyFrame(animationTimer, true);
		g.drawTexture(texture, x, y, bbWidth, bbHeight);
	}

}
