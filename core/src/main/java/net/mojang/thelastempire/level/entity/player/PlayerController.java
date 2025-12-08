package net.mojang.thelastempire.level.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.Mob;
import net.mojang.thelastempire.level.item.Item;
import net.mojang.thelastempire.level.particle.EntityDashFX;
import net.mojang.thelastempire.math.Vec2;

public class PlayerController extends InputAdapter {

	private static TheLastEmpire theLastEmpire = TheLastEmpire.getTheLastEmpire();
	
	private final Player player;
	private final Level level;

	private int attackTimer;
	private static final int ATTACK_INTERVAL = 10;
	private boolean isAttacking;

	private int ammo = 8;
	private boolean reloading = false;
	private int reloadingTimer = 0;

	private int dashCooldown = 0;
	private int dashTimer = 0;

	public PlayerController(Player player) {
		this.player = player;
		this.level = player.getLevel();
		Gdx.input.setInputProcessor(this);
	}

	public void tick() {
		if (dashCooldown > 0) {
			dashCooldown--;
		}

		if (player.isDashing) {
			dashTimer++;
			if (dashTimer > 10) {
				player.isDashing = false;
				dashTimer = 0;
			}
		}

		int attackInverval = ATTACK_INTERVAL;
		Item selectedItem = player.getInventory().getSelectedItem();
		if (selectedItem != null) {
			if ("Pistol".equals(selectedItem.getName())) {
				attackInverval = ATTACK_INTERVAL / 2;
			}
		} else {
			return;
		}

		if (reloading) {
			reloadingTimer++;
			if (reloadingTimer > 40) {
				ammo = 8;
				reloadingTimer = 0;
				reloading = false;
			}
		}

		if (!isAttacking) {
			attackTimer = 0;
			return;
		}

		attackTimer++;
		if (attackTimer <= attackInverval)
			return;
		attackTimer = 0;

		for (Mob mob : level.getVisibleMobs(false)) {
			if (selectedItem.usesHandCombat()) {
				if (Vec2.distance(player.getDrawPosition(), mob.getDrawPosition()) < 3f
						&& Rectangle.tmp.set(mob.x, mob.y, 1f, 2f).contains(theLastEmpire.mousePosition)) {
					attackMob(mob, selectedItem);
				}
			} else {
				if (ammo > 0) {
					selectedItem.onUse(player, level);
					ammo--;
				} else {
					if (!reloading) {
						reloading = true;
						theLastEmpire.playSound("reload", 1f, 1f);
					}
				}
			}
		}
	}

	public void draw(Graphics g) {
		isAttacking = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

		Item selectedItem = player.getInventory().getSelectedItem();
		if (selectedItem == null) {
			return;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && dashCooldown <= 0) {
			switch (player.getDirection()) {
			case "up":
				player.setPos(player.x, player.y + 1.5f);
				break;
			case "down":
				player.setPos(player.x, player.y - 1.5f);
				break;
			case "left":
				player.setPos(player.x - 1.5f, player.y);
				break;
			case "right":
				player.setPos(player.x + 1.5f, player.y);
				break;
			}

			for (int i = 0; i < 5; i++) {
				EntityDashFX dashFX = new EntityDashFX(level, player.x + MathUtils.random(0.2f, 0.8f),
						player.y + MathUtils.random(1f, 1.5f));
				level.addEntity(dashFX);
			}

			theLastEmpire.playSound("dodge", 1f, MathUtils.random(0.8f, 1.2f));
			dashCooldown = 15;
			player.isDashing = true;
			player.stopMoving();
		}
	}

	private void attackMob(Mob mob, Item item) {
		mob.onAttack(player, item.getAttackDamage());
		item.onUse(player, level);
	}

	@Override
	public boolean scrolled(float xx, float yy) {
		if (yy == 0)
			return false;

		player.getInventory().cycleSelected(yy < 0 ? -1 : 1);
		return true;
	}

	public int getAmmo() {
		return ammo;
	}

}
