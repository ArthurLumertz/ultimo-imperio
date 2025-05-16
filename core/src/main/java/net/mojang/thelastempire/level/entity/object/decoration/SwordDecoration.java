package net.mojang.thelastempire.level.entity.object.decoration;

import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class SwordDecoration extends Decoration {
	
	private Light light;
	private int timer;
	
	public SwordDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 2f, new RectangleI(32, 0, 16, 32));
		
		light = new Light(x + 0.5f, y + 0.7f, 0.7f, 0.1f, 2f, new Color(0.2f, 0.2f, 1f, 1f));
		Graphics.instance.setLight(light);
	}
	
	@Override
	public void tick() {
		timer++;
		
		float scalar = 0f;
		float speed = 0.1f;
		float scale = 0.3f;
		scalar = (float)MathUtils.cos(timer * speed * MathUtils.PI) * scale;
		light.outerRadius += scalar * scale;
	}

}
