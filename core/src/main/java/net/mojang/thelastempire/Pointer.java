package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.TileSheet;
import net.mojang.thelastempire.math.Mth;

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
		if (currentPointer == null || Gdx.input.isCursorCatched()) {
			return;
		}

		Vector2 mouseCoords = g.unproject(Gdx.input.getX(), Gdx.input.getY());
		float a = oldTimer + (timer - oldTimer) * TheLastEmpire.a;
		float scalar = Mth.cosWave(a * 0.3f, 0.95f, 1.05f);
		float rs = 48f;
		float xs = rs * scalar;
		float ys = xs;
		float rx = mouseCoords.x + rs / 2f;
		float ry = mouseCoords.y - rs / 2f;
		g.setColor(1f, 1f, 1f, 1f);

		float xp = rx - xs / 2f;
		float yp = ry - ys / 2f;

		g.setColor(0f, 0f, 0f, 0.5f);
		g.drawTexture(currentPointer, xp+rs/16f, yp-rs/16f, xs, ys);
		g.setColor(1f, 1f, 1f, 1f);
		g.drawTexture(currentPointer, xp, yp, xs, ys);
	}

	public void setPointer(int type) {
		currentPointer = regions[type];
	}

	public static interface Type {
		int DEFAULT = 0;
		int ADD = 1;
		int MESSAGE = 2;
		int SKIP = 3;
		int UNAVAILABLE = 4;
	}

}
