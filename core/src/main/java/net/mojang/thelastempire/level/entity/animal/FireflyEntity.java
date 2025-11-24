package net.mojang.thelastempire.level.entity.animal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Light;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.math.Vec2;

public class FireflyEntity extends Entity {

	private static Texture texture = Resources.getTexture("firefly");
	private Animation<TextureRegion> frames;
	private float timer = 0;
	private Light light;
	private boolean sine;
	private boolean shine = true;
	private int flickerTimer = 0;
	private static final float ANIMATION_DURATION = 0.3f;
	private float oldIntensity;
	private float intensity = 0.4f;
	private float piAmount;

	public FireflyEntity(Level level, float x, float y) {
		super(level);
		setPos(x, y);
		setSize(0.5f, 0.5f);

		TextureRegion[] regions = new TextureRegion[] {
				new TextureRegion(texture, 0, 0, 8, 8),
				new TextureRegion(texture, 8, 0, 8, 8)
		};
		if (MathUtils.randomBoolean()) {
			for (TextureRegion region : regions) {
				region.flip(true, false);
			}
		}
		frames = new Animation<TextureRegion>(ANIMATION_DURATION, regions);

		light = new Light(x + bbWidth / 2f, y + bbHeight / 2f, 0.1f, 0.1f, 0.8f, Color.newTemp(1f, 0.5f, 0f, 1f));
		Graphics.instance.setLight(light);
		noPhysics = true;
		sine = MathUtils.randomBoolean();
		piAmount = sine ? MathUtils.PI : MathUtils.PI2;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;

		oldIntensity = light.intensity;

		flickerTimer++;
		if (flickerTimer > 20 * ANIMATION_DURATION) {
			shine = !shine;
			if (!shine) {
				intensity = 0.05f;
			} else {
				intensity = 0.3f;
			}
			flickerTimer -= 20 * ANIMATION_DURATION;
		}

		float xx = sine ? MathUtils.sin(timer * 2f) * 0.05f : MathUtils.cos(timer * 2f) * 0.05f;
		float yy = sine ? MathUtils.cos(timer * 2f) * 0.05f : MathUtils.sin(timer * 2f * piAmount / 2f) * 0.05f;

		x += xx * 0.5f;
		y -= yy * 0.5f;

		light.setPos(x + bbWidth / 2f, y + bbHeight / 2f);
	}

	@Override
	public void draw(Graphics g) {
		timer += Gdx.graphics.getDeltaTime();
		TextureRegion texture = frames.getKeyFrame(timer, true);
		light.intensity = MathUtils.lerp(oldIntensity, intensity, TheLastEmpire.a / 2f);

		Vec2 position = getDrawPosition();
		g.setColor(1f, 1f, 1f, shine ? 1f : 0.3f);
		g.drawTexture(texture, position.x, position.y, bbWidth, bbHeight);
		g.setColor(0xFFFFFF);
	}
}
