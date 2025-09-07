package net.mojang.thelastempire.level.entity.object.furniture;

import com.badlogic.gdx.utils.ObjectMap;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class ChairFurniture extends EntityFurniture {

	private static ObjectMap<String, RectangleI> textures = new ObjectMap<String, RectangleI>();

	static {
        textures.put("north", new RectangleI(112, 64, 16, 16));
        textures.put("south", new RectangleI(96, 64, 16, 16));
        textures.put("west", new RectangleI(96, 48, 16, 16));
        textures.put("east", new RectangleI(112, 48, 16, 16));
    }

	public ChairFurniture(Level level, float x, float y, String type) {
		super(level, x, y, 1f, 1f, textures.get(type));
	}

    @Override
    public int getDrawLayer() {
        return 2;
    }

	@Override
	public String getName() {
		return "Chair Furniture";
	}

}
