package net.mojang.thelastempire.level.entity.object.furniture;

import com.badlogic.gdx.utils.ObjectMap;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class TableFurniture extends EntityFurniture {

	private static ObjectMap<String, RectangleI> textures = new ObjectMap<String, RectangleI>();

	static {
		textures.put("default", new RectangleI(32, 80, 34, 48));
		textures.put("glass", new RectangleI(80, 80, 34, 48));
    }
	
	public TableFurniture(Level level, float x, float y, String type) {
		super(level, x, y, 34f / 16f, 3f, textures.get(type));
	}

	@Override
	public String getName() {
		return "Table Furniture";
	}

}
