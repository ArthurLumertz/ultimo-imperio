package net.mojang.thelastempire.level.entity.object;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.particle.EntitySmokeFX;

public class House extends EntityPassable {

	private String levelToGo;
	private boolean isPlayerNear;

	private TextureRegion[] textures;

	private Rectangle eventRect;
	private int frame = 0;
	private int timer = 0;

	public House(Level level, float x, float y, String levelToGo, int type) {
		super(level, x - (5f / 2f), y, 5, 4);
		this.levelToGo = levelToGo;

		Graphics g = Graphics.instance;

		textures = new TextureRegion[5];
		textures[0] = new TextureRegion(g.getHouse(0, 0, 96, 112));
		textures[1] = new TextureRegion(g.getHouse(0, 112, 96, 112));
		textures[2] = new TextureRegion(g.getHouse(0, 112 * 2, 96, 112));
		textures[3] = new TextureRegion(g.getHouse(0, 112 * 3, 96, 112));
		textures[4] = new TextureRegion(g.getHouse(0, 112 * 4, 96, 112));

		eventRect = new Rectangle(this.x + 2.4f, this.y + 0.8f, 1f, 1f);
		
	}

	@Override
	public void tick() {
		super.tick();

		Player player = level.getPlayer();

		float dx = player.x + player.bbWidth / 2 - (eventRect.x + eventRect.width / 2);
		float dy = player.y + player.bbHeight / 2 - (eventRect.y + eventRect.height / 2);
		float distSquared = dx * dx + dy * dy;
		isPlayerNear = distSquared < 8f;

		if (isPlayerNear) {
			if (frame < textures.length - 1)
				frame++;
		} else {
			if (frame > 0)
				frame--;
		}

		Rectangle playerRect = Rectangle.tmp.set(player.x, player.y, player.bbWidth, 2f);
		if (eventRect.overlaps(playerRect)) {
			level.loadLevel(levelToGo, true, true);
		}

		timer++;
		if (timer > 5) {
			EntitySmokeFX entityFX = new EntitySmokeFX(level, x + 4, y + 6);
			level.addEntity(entityFX);

			timer -= 5;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(1f, 1f, 1f, getAlpha());
		g.drawTexture(textures[frame], x, y, 6, 7);
		g.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public Rectangle getIntersectsRect() {
		return Rectangle.tmp2.set(x, y + 1f, bbWidth, bbHeight + 1.5f);
	}

	@Override
	public boolean shouldRender() {
		return Graphics.instance.inViewport(boundingBox.grow(4f, 4f));
	}

	@Override
	public String getName() {
		return "House";
	}

}
