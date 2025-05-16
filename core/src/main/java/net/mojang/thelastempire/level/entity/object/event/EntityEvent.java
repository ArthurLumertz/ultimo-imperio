package net.mojang.thelastempire.level.entity.object.event;

import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.player.Player;

public class EntityEvent extends Entity {
	
	private String levelName;
	private float xSpawn;
	private float ySpawn;
	
	public EntityEvent(Level level, float x, float y, String levelName, float xSpawn, float ySpawn) {
		super(level);
		setPos(x, y);
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		this.levelName = levelName;
	}
	
	@Override
	public void tick() {
		Player player = level.getPlayer();
		Rectangle playerRect = Rectangle.tmp.set(player.x, player.y, player.bbWidth, player.bbHeight);
		Rectangle thisRect = Rectangle.tmp2.set(x, y, 1, 1);
		if (playerRect.overlaps(thisRect)) {
			level.loadLevel(levelName, true, true);
			level.getPlayer().setPos(xSpawn, ySpawn);
		}
	}
	
}
