package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.TileSheet;

public class Pointer {

	private final TextureRegion[] regions;

	private TextureRegion currentPointer;

	private int timer = 0;
	private int oldTimer = 0;

	public Pointer(TileSheet tileSheet, TheLastEmpire theLastEmpire) {
		Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);

		int xx = tileSheet.getWidth() / 16;
		int yy = tileSheet.getHeight() / 16;

		regions = new TextureRegion[xx * yy];
		for (int x = 0; x < xx; x++) {
			for (int y = 0; y < yy; y++) {
				int rx = x * 16;
				int ry = y * 16;
				regions[x + y * xx] = new TextureRegion(tileSheet.getRegion(rx, ry, 16, 16));
			}
		}

		currentPointer = regions[0];
	}

	public void tick() {
		oldTimer = timer;
		timer++;
	}

	public void draw(Graphics g) {
		if (currentPointer == null) {
			return;
		}

		Vector2 mouseCoords = g.unproject(Gdx.input.getX(), Gdx.input.getY());
		float a = oldTimer + (timer - oldTimer) * TheLastEmpire.a;
		float scalar = 0.9f + ((1f + MathUtils.cos(a * 0.2f)) / 2f) * 0.2f;
		float rs = 48f;
		float xs = rs * scalar;
		float ys = xs;
		float rx = mouseCoords.x + rs / 2f;
		float ry = mouseCoords.y - rs / 2f;
		g.drawTexture(currentPointer, rx - xs / 2f, ry - ys / 2f, xs, ys);
	}

	public void setPointer(int type) {
		currentPointer = regions[type];
	}

	public static interface Type {
		int DEFAULT = 0;
		int TEXT_SELECT = 1;
	}

}
