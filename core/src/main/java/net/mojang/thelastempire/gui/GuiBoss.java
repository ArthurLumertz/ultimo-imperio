package net.mojang.thelastempire.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.entity.boss.Boss;
import net.mojang.thelastempire.level.entity.player.Inventory;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.entity.player.PlayerController;
import net.mojang.thelastempire.level.item.Item;

public class GuiBoss extends GuiScreen {

	private static TextureRegion bulletTexture = new TextureRegion(Resources.getTexture("icons"), 0, 32, 4, 6);
	private static TextureRegion heartTexture = new TextureRegion(Resources.getTexture("gui"), 0, 0, 8, 8); 	
	
	private Boss boss;
	private float oldProgress;
	private float progress;
	private float poldProgress;
	private float pprogress;

	public GuiBoss(Boss boss) {
		this.boss = boss;
	}

	@Override
	public void tick() {
		oldProgress = progress;
		progress = (float) boss.getHealth() / boss.getBaseHealth();

		poldProgress = pprogress;
		pprogress = (float) boss.getLevel().getPlayer().getHealth() / boss.getLevel().getPlayer().getBaseHealth();
	}

	@Override
	public void draw(Graphics g) {
		drawBossHealthBar(g);
		drawPlayerHealthBar(g);
		drawItemTips(g);

		PlayerController controller = boss.getLevel().getPlayer().getController();
		Item item = boss.getLevel().getPlayer().getInventory().getSelectedItem();
		if (item != null && "Pistol".equals(item.getName())) {
			g.drawTexture(bulletTexture, (g.getScreenWidth() - 48) / 2f - 16, 100 - 20, 12, 24);
			g.drawCenteredString(controller.getAmmo() + "/8", g.getScreenWidth(), 100, 0xFFFFFF);
		}

	}

	private void drawItemTips(Graphics g) {
		float ww = 48;
		float x = (g.getScreenWidth() - ww * 3) / 2f;
		float y = 16;
		float w = 32;
		float h = 32;

		float www = ww * 3;
		float xx = x - 8;
		float yy = y - 8;
		g.setColor(0.2f, 0.2f, 0.2f, 0.5f);
		g.drawRect(xx, yy, www, 48);
		g.setColor(1f, 1f, 1f, 1f);

		float t = 3;

		Player player = boss.getLevel().getPlayer();
		Inventory inventory = player.getInventory();

		for (int i = 0; i < inventory.getCapacity(); i++) {
			Item item = inventory.getItem(i);

			if (inventory.isSelected(i)) {
				g.setColor(0.5f, 0.5f, 0.5f, 0.5f);
				g.drawRect(xx + (ww * i), yy, ww, ww);
				g.setColor(1f, 1f, 1f, 1f);
			}

			if (item != null) {
				g.drawTexture(item.getTexture(), x, y, w, h);
				x += ww;
			}
		}

		g.drawRect(xx, yy, www, t);
		g.drawRect(xx, yy + ww, www, t);
		g.drawRect(xx, yy, t, ww);
		g.drawRect(xx + www - t, yy, t, ww);
	}

	private void drawBossHealthBar(Graphics g) {
		float scale = 3f;
		float w = 64f * scale;
		float h = 12f * scale;
		float x = (g.getScreenWidth() - w) / 2f;
		float y = (g.getScreenHeight() - h) - 16;
		float t = 2f;
		float p = MathUtils.lerp(oldProgress, progress, TheLastEmpire.a);

		g.setColor(0.4f, 0.3f, 0.8f, 0.5f);
		g.drawRect(x, y, w * p, h);
		g.setColor(1f, 1f, 1f, 1f);
		g.drawRect(x, y + h, w, t);
		g.drawRect(x, y, w, t);
		g.drawRect(x, y, t, h);
		g.drawRect(x + w - t, y, t, h);

		g.drawTexture(boss.getTexture(), x - 16 * 3f, y, 16 * 2.5f, 14 * 2.5f);

		String str = (int) (p * 100f) + "%";
		g.drawCenteredString(str, g.getScreenWidth(), y + g.getFontSize() * 1.6f, 0xFFFFFF);
	}

	private void drawPlayerHealthBar(Graphics g) {
		float scale = 3f;
		float w = 12f * scale;
		float h = 48f * scale;
		float x = g.getScreenWidth() - w * 1.5f;
		float y = w * 2f;
		float t = 2f;
		float p = MathUtils.lerp(poldProgress, pprogress, TheLastEmpire.a);
		
		g.setColor(1f, 0f, 0f, 0.5f);
		g.drawRect(x, y, w, h * p);
		g.setColor(1f, 1f, 1f, 1f);
		g.drawRect(x, y + h, w, t);
		g.drawRect(x, y, w, t);
		g.drawRect(x, y, t, h);
		g.drawRect(x + w - t, y, t, h);
		
		g.drawTexture(heartTexture, x + 4, w * 0.5f, 32, 32);
	}

}
