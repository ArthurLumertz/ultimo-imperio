package net.mojang.thelastempire.level.entity.object.decoration;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.math.RectangleI;

public class EntityDecoration extends Entity {

	private static Texture tilesheet = Resources.getTexture("decoration");

	private TextureRegion texture;

	public EntityDecoration(Level level, float x, float y, float w, float h, RectangleI rect) {
		super(level);
		setPos(x, y);
		setSize(w, h);
		this.texture = new TextureRegion(tilesheet, rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void draw(Graphics g) {
		g.drawTexture(texture, x, y, bbWidth, bbHeight);
	}

	public static EntityDecoration createDecoration(JsonValue decorationValue, Level level) {
		String name = decorationValue.name();
		float xPos = decorationValue.getFloat("xPos");
		float yPos = decorationValue.getFloat("yPos");
        int type = decorationValue.getInt("type", 0);
        String stage = decorationValue.getString("stage", "full");

		switch (name) {
		case "Skull":
			return new SkullDecoration(level, xPos, yPos);
		case "Bone":
			return new BoneDecoration(level, xPos, yPos);
		case "Sword":
			return new SwordDecoration(level, xPos, yPos);
        case "Book":
            return new BookDecoration(level, xPos, yPos);
        case "Mug":
            return new MugDecoration(level, xPos, yPos, type);
		case "Barrel":
			return new BarrelDecoration(level, xPos, yPos);
		case "Plant":
			return new PlantDecoration(level, xPos, yPos, stage);
		case "Flower":
			return new FlowerDecoration(level, xPos, yPos, type);
		case "Garbage Can":
			return new GarbageCanDecoration(level, xPos, yPos);
		case "Mailbox":
			return new MailboxDecoration(level, xPos, yPos);
		}
		throw new IllegalStateException("Failed to create decoration");
	}

}
