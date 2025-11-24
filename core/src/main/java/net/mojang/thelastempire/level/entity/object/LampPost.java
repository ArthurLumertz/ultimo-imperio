package net.mojang.thelastempire.level.entity.object;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.animal.FireflyEntity;

public class LampPost extends Entity {
	
	private TextureRegion texture;
	private int timer = 0;
	
	private Light light0;
	private Light light1;
	private int direction = 0;
	
	public LampPost(Level level, float x, float y, String direction) {
		super(level);
		setSize(1, 0.5f);
		setPos(x, y);
		Color color = new Color(1f, 0.5f, 0.2f, 1f);
		float outerRadius = 2f;
		light0 = new Light(x + 0.2f, y + 1.8f, 0.5f, 0.1f, outerRadius, color);
		light1 = new Light(x + 1.5f, y + 1.8f, 0.5f, 0.1f, outerRadius, color);
		Graphics.instance.setLight(light0);
		Graphics.instance.setLight(light1);
		
		if ("horizontal".equals(direction)) {
			texture = new TextureRegion(Graphics.instance.getFurniture(128, 0, 32, 48));
		} else if ("vertical".equals(direction)) {
			texture = new TextureRegion(Graphics.instance.getFurniture(96, 0, 32, 48));
		}
		this.direction = MathUtils.randomBoolean() ? -1 : 1;
	
		if (level.getGlobalLight() < 0.5f) {
			for (int i = 0; i < 2; i++) {
				float gap = (i + 1) * 1.5f;
				float rx = MathUtils.random(x - gap, x + gap);
				float ry = MathUtils.random(y - 0.5f, y + 0.5f) + 2f;
				FireflyEntity ff = new FireflyEntity(level, rx, ry);
				level.addEntity(ff);
			}
		}
	}
	
	@Override
	public void tick() {
		timer++;
		
		float scalar = 0f;
		float speed = 0.1f;
		float scale = 0.25f;
		
		if (direction == -1) {
			scalar = (float)MathUtils.sin(timer * speed * MathUtils.PI) * scale;
		} else {
			scalar = (float)MathUtils.cos(timer * speed * MathUtils.PI) * scale;
		}
		
		light0.outerRadius += scalar * scale;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawTexture(texture, x - 0.5f, y, 2, 3);
	}

}
