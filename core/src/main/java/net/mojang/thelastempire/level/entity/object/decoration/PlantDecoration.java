package net.mojang.thelastempire.level.entity.object.decoration;

import com.badlogic.gdx.utils.ObjectMap;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class PlantDecoration extends EntityDecoration {

	private static final ObjectMap<String, RectangleI> textures = new ObjectMap<String, RectangleI>(){{
			put("early", new RectangleI(0, 48, 16, 16));
			put("mid", new RectangleI(16, 48, 16, 16));
			put("full", new RectangleI(32, 48, 16, 16));
			put("ripe", new RectangleI(48, 48, 16, 16));
	}};

	public PlantDecoration(Level level, float x, float y, String stage) {
		super(level, x, y, 1f, 1f, textures.get(stage));
	}

}
