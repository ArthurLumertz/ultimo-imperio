package net.mojang.thelastempire.level.entity;

import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.particle.EntityBloodFX;
import net.mojang.thelastempire.level.tile.Tile;
import net.mojang.thelastempire.level.tile.Tile.SoundType;

public class Mob extends Entity {

	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;
	public String direction = "down";

	protected float walkDist;
	protected boolean makeStepSound = true;
	protected int nextStep = 1;

	protected int health;
	protected int baseHealth;

	public Mob(Level level, int health) {
		super(level);
		this.baseHealth = health;
		this.health = health;
	}
	
	protected void baseTick(float xa, float ya) {
		xo = x;
		yo = y;

		boolean moving = up || down || left || right;
		if (moving) {
			if (up) {
				ya++;
				direction = "up";
			}
			if (down) {
				ya--;
				direction = "down";
			}
			if (left) {
				xa--;
				direction = "left";
			}
			if (right) {
				xa++;
				direction = "right";
			}
		}

		move(xd, yd);
		moveRelative(xa, ya, speed);
	}
	
	@Override
	public void move(float xa, float ya) {
		super.move(xa, ya);

		if (makeStepSound) {
			walkDist += (float) Math.sqrt(xa * xa + ya * ya) * 0.7f;

			int tx = MathUtils.floor(x);
			int ty = MathUtils.floor(y);
			Tile tile = Tile.tiles[level.getTile(tx, ty)];

			if (walkDist > nextStep) {
				nextStep++;
				if (tile != null) {
					SoundType soundType = tile.getSoundType();
					if (soundType != null) {
						TheLastEmpire tle = TheLastEmpire.getTheLastEmpire();
						float pitch = MathUtils.random(0.9f, 1.1f);
						tle.playSound(soundType.getStepSound(), 1.2f, pitch);
					}

				}
			}
		}
	}

	public void onAttack(Player player, int amount) {
		for (int i = 0; i < 4; i++) {
			EntityBloodFX bloodFX = new EntityBloodFX(level, x, y);
			level.addEntity(bloodFX);
		}
		damage(amount);
	}

	public void damage(int amount) {
		if (health > 0) {
			health--;
		} else {
			remove();	
		}
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public int getHealth() {
		return health;
	}
	
	public String getDirection() {
		return direction;
	}
	
}
