package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.Pointer;
import net.mojang.thelastempire.engine.FontSize;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.TileSheet;
import net.mojang.thelastempire.level.tile.Tile;

public class GuiMainMenu extends GuiMenu {

	private Array<Button> buttons = new Array<>();
	private TileSheet playerSheet;

	private int timer;
	private int frame;

	@Override
	public void init() {
		buttons.clear();

		int x = 72;
		int y = 16 * 11;
		int w = 128;

		buttons.add(new Button(0, Pointer.Type.SKIP, x, y + 60 * 2, w, 44, "Começar", false));
		buttons.add(new Button(1, Pointer.Type.MESSAGE, x, y + 60, w, 44, "Creditos", false));
		buttons.add(new Button(2, Pointer.Type.UNAVAILABLE, x, y, w, 44, "Sair", false));

		playerSheet = new TileSheet("char");
	}

	@Override
	protected void onButtonClick(int index) {
		switch (index) {
		case 0:
			theLastEmpire.setGuiScreen(new GuiIntro(), true);
			break;
		case 1:
			theLastEmpire.setGuiScreen(new GuiCredits(), true);
			break;
		case 2:
			Gdx.app.exit();
			break;
		}
	}

	@Override
	public void tick() {
		super.tick();
		init();

		timer++;
		if (timer > 20 * 0.8f) {
			frame++;
			if (frame > 1) {
				frame = 0;
			}
			timer = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		drawTiledBackground(0x555555, Tile.stone, g);

		g.setFontSize(FontSize.XXLARGE);
		float fs = g.getFontSize();
		float x = fs * 1.5f;
		float y = g.getScreenHeight() - fs * 2f;

		g.drawString("O Último Império", x, y, 0xFFFFFF);
		g.setFontSize(FontSize.DEFAULT);

		g.drawCenteredString("Feito por", g.getScreenWidth(), 70, 0xBBBBBB);
		g.drawCenteredString("Arthur Lumertz, Mateus Braga, Matheus Sartori, Martin Ascoli, Felipe Argenti",
				g.getScreenWidth(), 40, 0xBBBBBB);

		drawButtons(buttons);

		//

		float th = 320f;
		float tw = th * 0.5f;
		float tx = g.getScreenWidth() - tw * 2f;
		float ty = (g.getScreenHeight() - th) * 0.7f;

		TextureRegion t = g.getPlant(0, 0);
		g.drawTexture(t, tx, ty, tw, th);

		t = playerSheet.getRegion(frame * 16, 0, 16, 32);
		float h = 96f;
		float w = h * 0.5f;
		x = tx + w * 0.9f;
		y = ty - y * 0.05f;
		g.drawTexture(t, x, y, w, h);
	}

}
