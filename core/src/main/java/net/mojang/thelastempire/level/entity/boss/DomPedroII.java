package net.mojang.thelastempire.level.entity.boss;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.gui.GuiBoss;
import net.mojang.thelastempire.gui.GuiDialogue;
import net.mojang.thelastempire.gui.GuiEnd;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.BulletEntity;
import net.mojang.thelastempire.level.entity.player.Player;

public class DomPedroII extends Boss {

	private int shootTimer;
	private int shootInterval;

	private static final int MAX_DEATHS = 5;
	private static int deaths = 0;
	
	private boolean dead = false;
	private boolean canAttack = false;
	
	private Animation<TextureRegion> deadAnimation;

	public DomPedroII(Level level, float x, float y, int health) {
		super(level, x, y, 10, "Dom Pedro II");
		updateDifficulty();
		
		TextureRegion[] frames = {
				new TextureRegion(Resources.getTexture("dompedro"), 80, 0, 16, 32)
		};
		deadAnimation = new Animation<TextureRegion>(5f, frames);

		createDialogues();
	}
	
	private void createDialogues() {
		Array<String> dialogues = new Array<>();
		dialogues.add("Ah, Deodoro, um dia você já foi um de\nmeus melhores amigos, e agora está\ncorrompido pela ideia de comandar um\npaís, sacrificando a nossa amizade\npara isso.");
		dialogues.add("A sala do trono nunca foi destinada ao\nsangue, mas hoje, a honra do Império\nexige que eu empunhe esta arma.");
		dialogues.add("Marechal, você irá pagar pela decisão\nque tomou.\nAgora, prepare-se.");
		TheLastEmpire.getTheLastEmpire().setGuiScreen(new GuiDialogue(dialogues, "dompedro", this::onDialogueFinish, level), false);
	}
	
	private void onDialogueFinish() {
		canAttack = true;
		TheLastEmpire.getTheLastEmpire().setGuiScreen(new GuiBoss(this), false);
	}

	private void updateDifficulty() {
		int eased = Math.min(deaths, MAX_DEATHS);
		this.speed = 0.11f - (eased * 0.015f);
		shootInterval = getShootInterval();
	}

	private int getShootInterval() {
		int eased = Math.min(deaths, MAX_DEATHS);
		return MathUtils.random(6 + eased * 4, 8 + eased * 5);
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		
		if (dead || !canAttack)
			return;

		Player player = level.getPlayer();
		if (player == null || player.removed)
			return;

		shootTimer++;
		if (shootTimer > shootInterval) {
			float inaccuracy = Math.min(deaths * 0.5f, 3f);
			float spreadX = MathUtils.random(-inaccuracy, inaccuracy);
			float spreadY = MathUtils.random(-inaccuracy, inaccuracy);
			BulletEntity bullet = new BulletEntity(level, x + 0.5f, y + 1f, player.x + spreadX + 0.5f,
					player.y + spreadY + 1f, Player.class);

			TheLastEmpire.getTheLastEmpire().playSound("shoot", 0.5f, MathUtils.random(0.8f, 1.2f));
			level.addEntity(bullet);

			shootInterval = getShootInterval();
			shootTimer = 0;
		}

		float dx = player.x - x;
		float dy = player.y - y;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		if (distance > 5f) {
			float nx = dx / distance;
			float ny = dy / distance;

			up = false;
			down = false;
			left = false;
			right = false;

			int tx = MathUtils.floor(x);
			int ty = MathUtils.floor(y);

			boolean blockedUp = level.isCollideable(tx, ty + 1);
			boolean blockedDown = level.isCollideable(tx, ty - 1);
			boolean blockedLeft = level.isCollideable(tx - 1, ty);
			boolean blockedRight = level.isCollideable(tx + 1, ty);

			boolean moveVertically = Math.abs(ny) > Math.abs(nx);

			if (moveVertically) {
				if (ny > 0.2f && !blockedUp) {
					up = true;
					direction = "up";
				} else if (ny < -0.2f && !blockedDown) {
					down = true;
					direction = "down";
				} else if (nx < -0.2f && !blockedLeft) {
					left = true;
					direction = "left";
				} else if (nx > 0.2f && !blockedRight) {
					right = true;
					direction = "right";
				}
			} else {
				if (nx < -0.2f && !blockedLeft) {
					left = true;
					direction = "left";
				} else if (nx > 0.2f && !blockedRight) {
					right = true;
					direction = "right";
				} else if (ny > 0.2f && !blockedUp) {
					up = true;
					direction = "up";
				} else if (ny < -0.2f && !blockedDown) {
					down = true;
					direction = "down";
				}
			}
		}

		super.tick();
	}

	@Override
	public void onAttack(Player player, int amount) {
		if (dead) {
			return;
		}
		super.onAttack(player, amount);
	}

	@Override
	public void remove() {
		currentAnimation = deadAnimation;
		setSize(1f, 2f);
		dead = true;
		
		level.deleteLevel();
		TheLastEmpire.getTheLastEmpire().unload();
		TheLastEmpire.getTheLastEmpire().setGuiScreen(new GuiEnd(), false);
		System.gc();
	}

	@Override
	public boolean shouldTick() {
		return true;
	}

	public static void increaseTries() {
		deaths++;
	}

}
