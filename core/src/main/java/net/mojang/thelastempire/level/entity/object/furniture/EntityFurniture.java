package net.mojang.thelastempire.level.entity.object.furniture;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.math.RectangleI;

public class EntityFurniture extends Entity {

	protected TextureRegion texture = null;

	public EntityFurniture(Level level, float x, float y, float width, float height, RectangleI texture) {
		super(level);
		setSize(width, height);
		setPos(x, y);
		if (texture != null) {
			this.texture = new TextureRegion(
					Graphics.instance.getFurniture(texture.x, texture.y, texture.width, texture.height));
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public void draw(Graphics g) {
		g.drawTexture(this.texture, x, y, bbWidth, bbHeight);
	}

	public int getDrawLayer() {
		return 0;
	}

	@Override
	public String getName() {
		return "Furniture";
	}

	public static EntityFurniture createFurniture(Level level, JsonValue data) {
		String name = data.name();
		float x = data.getFloat("xPos");
		float y = data.getFloat("yPos");
		String type = data.getString("type", null);

		switch (name) {
		case "Table":
			type = Objects.requireNonNullElse(type, "default");
			return new TableFurniture(level, x, y, type);
		case "Desk":
			type = Objects.requireNonNullElse(type, "medium");
			return new DeskFurniture(level, x, y, type);
		case "Bed":
			return new BedFurniture(level, x, y);
		case "Carpet":
			return new CarpetFurniture(level, x, y, type);
		case "Lamp":
			return new LampFurniture(level, x, y);
		case "Drawer":
			return new DrawerFurniture(level, x, y);
		case "Wardrobe":
			return new WardrobeFurniture(level, x, y);
		case "Bookshelf":
			return new BookshelfFurniture(level, x, y);
		case "Fireplace":
			return new FireplaceFurniture(level, x, y);
        case "Chair":
            type = Objects.requireNonNullElse(type, "south");
            return new ChairFurniture(level, x, y, type);
		default:
			throw new RuntimeException("Failed to find entity: " + name);
		}
	}

}
