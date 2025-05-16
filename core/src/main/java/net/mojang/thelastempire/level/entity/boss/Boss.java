package net.mojang.thelastempire.level.entity.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Mob;
import net.mojang.thelastempire.math.Vec2;

public class Boss extends Mob {

	private static Texture domPedro = Resources.getTexture("dompedro");
	private TextureRegion texture;

	private String name;

	private static final int UP_ANIMATION = 0;
	private static final int DOWN_ANIMATION = 1;
	private static final int LEFT_ANIMATION = 2;
	private static final int RIGHT_ANIMATION = 3;
	protected Animation<TextureRegion> currentAnimation;
	protected Animation<TextureRegion>[] allAnimations;

	private Light light;

	private float elapsedTime;
	
	public Boss(Level level, float x, float y, int health, String name) {
		super(level, health);
		setPos(x, y);
		this.name = name;

		this.baseHealth = health;
		this.health = health;

		if (level.getGlobalLight() < 0.4f) {
			light = new Light(x, y, 0.5f, 0.1f, 4f, new Color(1f, 0.9f, 0f, 1f));
			Graphics.instance.setLight(light);
		}
		
		texture = new TextureRegion(domPedro, 0, 0, 16, 14);

		createAnimations(domPedro);
	}

	@SuppressWarnings("unchecked")
	private void createAnimations(Texture texture) {
		TextureRegion[] upFrames = new TextureRegion[] { new TextureRegion(texture, 32, 32, 16, 32),
				new TextureRegion(texture, 48, 32, 16, 32) };
		TextureRegion[] downFrames = new TextureRegion[] { new TextureRegion(texture, 32, 0, 16, 32),
				new TextureRegion(texture, 48, 0, 16, 32) };
		TextureRegion[] leftFrames = new TextureRegion[] { new TextureRegion(texture, 32, 64, 16, 32),
				new TextureRegion(texture, 48, 64, 16, 32) };
		TextureRegion[] rightFrames = new TextureRegion[] { new TextureRegion(texture, 0, 64, 16, 32),
				new TextureRegion(texture, 16, 64, 16, 32) };

		allAnimations = (Animation<TextureRegion>[]) new Animation<?>[4];
		allAnimations[UP_ANIMATION] = new Animation<TextureRegion>(0.4f, upFrames);
		allAnimations[DOWN_ANIMATION] = new Animation<TextureRegion>(0.4f, downFrames);
		allAnimations[LEFT_ANIMATION] = new Animation<TextureRegion>(0.4f, leftFrames);
		allAnimations[RIGHT_ANIMATION] = new Animation<TextureRegion>(0.4f, rightFrames);
		currentAnimation = allAnimations[direction.equals("up") ? UP_ANIMATION
				: direction.equals("down") ? DOWN_ANIMATION
						: direction.equals("left") ? LEFT_ANIMATION : RIGHT_ANIMATION];
	}

	@Override
	public void tick() {
		baseTick(xd, yd);
		xd *= 0.71f;
		yd *= 0.71f;

		switch (direction) {
		case "up":
			currentAnimation = allAnimations[UP_ANIMATION];
			break;
		case "down":
			currentAnimation = allAnimations[DOWN_ANIMATION];
			break;
		case "left":
			currentAnimation = allAnimations[LEFT_ANIMATION];
			break;
		case "right":
			currentAnimation = allAnimations[RIGHT_ANIMATION];
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		Vec2 position = getDrawPosition();

		TextureRegion texture = currentAnimation.getKeyFrame(elapsedTime, true);
		if (up || down || left || right) {
			elapsedTime += Gdx.graphics.getDeltaTime();
		}
		if (light != null) {
			light.setPos(position.x + 0.5f, position.y + 1f);
		}

		g.drawTexture(texture, position.x, position.y, 1f, 2f);
	}

	@Override
	public String getName() {
		return name;
	}

	public static Boss createBoss(JsonValue bossData, Level level) {
		if (bossData == null) {
			return null;
		}

		String name = bossData.name();
		float xPos = bossData.getFloat("xPos");
		float yPos = bossData.getFloat("yPos");
		int health = bossData.getInt("health");

		switch (name) {
		case "Dom Pedro II":
			return new DomPedroII(level, xPos, yPos, health);
		}
		return null;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	public Level getLevel() {
		return level;
	}

}
