package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.tile.Tile;
import net.mojang.thelastempire.math.Vec2i;

public class GuiScreen {

	protected TheLastEmpire theLastEmpire;
	protected float width;
	protected float height;

	protected Vec2i mousePos = Vec2i.ZERO;

	public void init(TheLastEmpire theLastEmpire, float width, float height) {
		this.theLastEmpire = theLastEmpire;
		this.width = width;
		this.height = height;
		init();
	}

	public void init() {
	}

	public void tick() {
		Vector2 tmp = Graphics.guiInstance.unproject(Gdx.input.getX(), Gdx.input.getY());
		mousePos.set((int) tmp.x, (int) tmp.y);
	}

	public void draw(Graphics g) {
	}

	public void dispose() {
	}

	public boolean pauseGame() {
		return false;
	}

	protected void drawTiledBackground(int tint, Tile tile, Graphics g) {
		int rs = 48;
		int xx = (int) g.getScreenWidth() / rs;
		int yy = (int) g.getScreenHeight() / rs + 1;

		g.setColor(tint);
		for (int x = 0; x < xx; x++) {
			for (int y = 0; y < yy; y++) {
				float xp = x * rs;
				float yp = y * rs;
				g.drawTexture(tile.getTexture(), xp, yp, rs, rs);
			}
		}
		g.setColor(1f, 1f, 1f, 1f);
	}

	protected void onButtonClick(int index) {
	}

	protected void drawButtons(Array<Button> buttons) {
		Graphics g = Graphics.guiInstance;

		for (Button button : buttons) {
			float x = button.x;
			float y = button.y;
			float w = button.width;
			float h = button.height;
			String str = button.title;

			Rectangle buttonRect = Rectangle.tmp.set(x, y, w, h);
			if (mousePos.contains(buttonRect)) {
				theLastEmpire.setPointer(button.type);
				if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					onButtonClick(button.index);
					theLastEmpire.playSound("button_click", 1f, 1f);
				}

				float xx = x;
				float ww = w;
				if (button.centerText) {
					xx = x + w / 4;
					ww = w / 2;
				}
				g.setColor(0xFFF7D9);
				g.drawRect(xx, y, ww, 2);
			}

			float sx = x + 15;
			float sy = y + (h + g.getFontSize()) / 2 + 1;

			if (button.centerText) {
				float ts = g.getStringWidth(str);
				sx = x + (w - ts) / 2;
			}

			g.drawString(button.title, sx, sy, 0xFFF7D9);
		}
	}

	public boolean shouldFadeIn() {
		return true;
	}

}
