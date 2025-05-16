package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
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

	protected void onButtonClick(int index) {
	}

	protected void drawButtons(Array<GuiButton> buttons) {
		Graphics g = Graphics.guiInstance;

		for (GuiButton button : buttons) {
			float x = button.x;
			float y = button.y;
			float w = button.width;
			float h = button.height;
			String str = button.title;

			int color = 0xAD9955;
			int outlineColor = 0xC2B37E;

			Rectangle buttonRect = Rectangle.tmp.set(x, y, w, h);
			if (mousePos.contains(buttonRect)) {
				color = 0xD1BB71;
				outlineColor = 0xFFF7DB;

				if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					onButtonClick(button.index);
					theLastEmpire.playSound("button_click", 1f, 1f);
				}
			}

			float rc = (color >> 16 & 0xFF) / 255f;
			float gc = (color >> 8 & 0xFF) / 255f;
			float bc = (color & 0xFF) / 255f;
			g.setColor(rc + 0.1f, gc + 0.1f, bc + 0.05f, 0.7f);
			g.drawRect(x, y, w, h);

			int ot = 2;
			g.setColor(outlineColor);
			g.drawRect(x, y, w, ot);
			g.drawRect(x, y + h, w, ot);
			g.drawRect(x, y, ot, h);
			g.drawRect(x + w - ot, y, ot, h);
			
			g.setColor(0xFFFFFF);

			float ts = g.getStringWidth(str);
			float sx = x + (w - ts) / 2;
			float sy = y + (h + g.getFontSize()) / 2 + 1;

			g.drawString(button.title, sx, sy, 0xFFF7D9);
		}
	}

	public boolean shouldFadeIn() {
		return true;
	}
	
}
