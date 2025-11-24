package net.mojang.thelastempire.level.entity.object.event;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.phys.AABB;

public class EntityEvent extends Entity {

	private String levelName;
	private float xSpawn;
	private float ySpawn;
	
	private AABB bb;

	public EntityEvent(Level level, float x, float y, String levelName, float xSpawn, float ySpawn) {
		super(level);
		setPos(x, y);
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		this.levelName = levelName;
		this.bb = AABB.newPermanent(x, y, x + 1, y + 1);
	}

	@Override
	public void tick() {
		Player player = level.getPlayer();
		if (player.boundingBox.intersects(bb)) {
			level.loadLevel(levelName, true, true);
			player.setPos(xSpawn, ySpawn);
		}
	}

}
