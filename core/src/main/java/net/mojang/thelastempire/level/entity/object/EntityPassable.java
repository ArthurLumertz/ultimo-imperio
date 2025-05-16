package net.mojang.thelastempire.level.entity.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.player.Player;

public class EntityPassable extends Entity {

	protected float oldAlpha;
	protected float alpha;

    private final Rectangle playerRect = new Rectangle();
    private final Rectangle objectRect = new Rectangle();
	
	public EntityPassable(Level level, float x, float y, float bbWidth, float bbHeight) {
		super(level);
		setPos(x, y);
		setSize(bbWidth, bbHeight);
	}
	
	@Override
	public void tick() {
		Player player = level.getPlayer();

        playerRect.set(player.x, player.y, player.bbWidth, 2f);
        objectRect.set(getIntersectsRect());
		
		oldAlpha = alpha;
		if (playerRect.overlaps(objectRect)) {
			alpha = MathUtils.lerp(alpha, 0.5f, 0.2f);
		} else {
			alpha = MathUtils.lerp(alpha, 1f, 0.2f);
		}
	}
	
	public float getAlpha() {
		float a = MathUtils.lerp(oldAlpha, alpha, TheLastEmpire.a);
		return a;
	}
	
	public Rectangle getIntersectsRect() {
		return Rectangle.tmp.set(x, y, bbWidth, bbHeight);
	}

}
