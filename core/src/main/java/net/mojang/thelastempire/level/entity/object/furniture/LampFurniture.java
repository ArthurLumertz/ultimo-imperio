package net.mojang.thelastempire.level.entity.object.furniture;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class LampFurniture extends EntityFurniture {

	public LampFurniture(Level level, float x, float y, float intensity) {
		super(level, x - 0.05f, y - 0.05f, 1f, 1f, new RectangleI(0, 80, 16, 17));

		Light light = new Light(x + 0.5f, y + 0.5f, intensity, 0.3f, 2.5f,
				new Color(1f, 0.5f, 0.15f, 1f));
		Graphics.instance.setLight(light);
	}

	@Override
	public int getDrawLayer() {
		return 1;
	}

	@Override
	public String getName() {
		return "Lamp Furniture";
	}
}
