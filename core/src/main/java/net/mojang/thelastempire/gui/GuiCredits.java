package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.TileSheet;
import net.mojang.thelastempire.level.tile.Tile;

public class GuiCredits extends GuiScreen {

	private String[] lines;

	private int yOldScroll;
	private int yScroll;

	private int scrollSpeed = 1;

	private TileSheet playerSheet;

	@Override
	public void init() {
		FileHandle creditsFile = Gdx.files.internal("credits.txt");
		lines = creditsFile.readString().split("\n");
		yScroll = -(48 * 10);
		yOldScroll = yScroll;
		playerSheet = new TileSheet("char");
        Gdx.input.setCursorCatched(false);
	}

	@Override
	public void tick() {
		yOldScroll = yScroll;
		if (yScroll < Graphics.guiInstance.getFontSize() * lines.length) {
			yScroll += scrollSpeed;
		} else {
			theLastEmpire.setGuiScreen(new GuiMainMenu(), true);
		}
	}

	private void drawTiledBackground(Graphics g, Tile tile, float yScroll) {
		if (tile == null) {
			return;
		}
		TextureRegion texture = tile.getTexture();

		g.setColor(1f, 1f, 1f, 0.3f);
		int rs = 48;
		int xx = (int) g.getScreenWidth() / rs;
		int yy = lines.length + 2;
		for (int x = 0; x < xx; x++) {
			for (int y = -yy / 2; y < yy; y++) {
				float rx = x * rs;
				float ry = (y * rs) + yScroll * 0.5f;
				g.drawTexture(texture, rx, ry, rs, rs);
			}
		}
		g.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public void draw(Graphics g) {
		float yy = yOldScroll + (yScroll - yOldScroll) * TheLastEmpire.a;

		drawTiledBackground(g, Tile.stone, yy);

		g.setFontSize(20f);

		for (int i = 0; i < lines.length; i++) {
			int index = lines.length - 1 - i;
			String str = lines[index];

			float size = 20f;
			float spacing = 2f;

			if (index == 0) {
				size = 48f;
				spacing = 1.5f;
			} else if (index == 2 || index == 5 || index == 10 || index == 14) {
				size = 32f;
				spacing = 2f;
				yy += size * 0.5f;
			} else {
				size = 20f;
			}

			g.setFontSize(size);
			g.drawCenteredString(str, width, yy, 0xFFFFFF);
			yy += size * spacing;
		}

		float rs = 96f;
		g.drawTexture(playerSheet.getRegion(0, 0, 16, 16), (g.getScreenWidth() - rs) / 2, yy, rs, rs);

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			scrollSpeed = 5;
		} else {
			scrollSpeed = 1;
		}
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

}
