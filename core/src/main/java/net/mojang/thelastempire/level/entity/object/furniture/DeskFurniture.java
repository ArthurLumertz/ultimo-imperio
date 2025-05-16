package net.mojang.thelastempire.level.entity.object.furniture;

import com.badlogic.gdx.utils.ObjectMap;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class DeskFurniture extends EntityFurniture {

	private static ObjectMap<String, RectangleI> textures = new ObjectMap<String, RectangleI>();

	static {
		textures.put("small", new RectangleI(0, 32, 16, 16));
		textures.put("medium", new RectangleI(0, 48, 25, 16));
		textures.put("large", new RectangleI(0, 64, 34, 16));
		textures.put("east", new RectangleI(16, 32, 16, 16));
    }

	private static float getWidth(String size) {
		if ("large".equals(size)) {
			return 34f / 16f;
		} else if ("medium".equals(size)) {
			return 25f / 16f;
		}
		return 1f;
	}
	
	public DeskFurniture(Level level, float x, float y, String type) {
		super(level, x, y, getWidth(type), 1f, textures.get(type));
	}
	
	@Override
	public String getName() {
		return "Desk Furniture";
	}

}
