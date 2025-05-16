package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;

public class Font {

	private Graphics g;

	private final int[] charWidths = new int[256];
	private final TextureRegion[] charRegions = new TextureRegion[256];

	private float size = 16f;

	public Font(Graphics g, String resourceName) {
		this.g = g;

		Pixmap pixmap;
		Texture fontTexture;
		try {
			fontTexture = Resources.getFont(resourceName);
			pixmap = new Pixmap(Gdx.files.internal(resourceName + ".gif"));
		} catch (GdxRuntimeException e) {
			throw new RuntimeException(e);
		}

		int w = pixmap.getWidth();
		int h = pixmap.getHeight();
		int[] rawPixels = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				rawPixels[i + j * w] = pixmap.getPixel(i, j);
			}
		}

		for (int i = 0; i < 256; ++i) {
			int xt = i % 16;
			int yt = i / 16;
			int x = 0;
			boolean emptyColumn = false;

			for (; x < 8 && !emptyColumn; ++x) {
				int xPixel = xt * 8 + x;
				emptyColumn = true;

				for (int y = 0; y < 8; ++y) {
					int yPixel = (yt * 8 + y) * w;
					int pixel = rawPixels[xPixel + yPixel] & 255;
					if (pixel > 128) {
						emptyColumn = false;
						break;
					}
				}
			}

			if (i == 32) {
				x = 4;
			}

			charWidths[i] = x;
			charRegions[i] = new TextureRegion(fontTexture, xt * 8, yt * 8, 8, 8);
		}
	}

	public void setSize(float size) {
		this.size = size;
	}

	public void drawShadow(String str, float x, float y, int color) {
		draw(str, x + size / 8, y - size / 8, color, true);
		draw(str, x, y, color, false);
	}

	public void draw(String str, float x, float y, int color) {
		draw(str, x, y, color, false);
	}

	public void draw(String str, float x, float y, int color, boolean darken) {
		if (darken) {
			color = (color & 0xFCFCFC) >> 2;
		}
		float a = ((color >>> 24) & 0xFF) != 0xFF ? 1f : (color >>> 24 & 0xFF) / 255f;
		float r = (color >> 16 & 0xFF) / 255f;
		float g = (color >> 8 & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;

		float xo = 0;
 
		for (int i = 0; i < str.length(); ++i) {
			float ty = y - size;
			char ch = str.charAt(i);
			if (ch >= 256) {
				ch = '?';
			} else if (ch == 'Ú') {
				ty += size / 8f;
			}

			TextureRegion region = charRegions[ch];
			this.g.setColor(r, g, b, a);
			this.g.drawTexture(region, x + xo, ty, size, size);
			xo += charWidths[ch] * (size / 8);
		}
		this.g.setColor(1f, 1f, 1f, 1f);
	}

	public int width(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == '&') {
				i++;
			} else {
				len += charWidths[ch];
			}
		}
		return (int) (len * (size / 8));
	}

	public float getSize() {
		return size;
	}

	public int getVisibleTextLength(String str, float maxWidth) {
		int len = 0;
		float width = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == '&') {
				i++;
				continue;
			}
			width += charWidths[ch] * (size / 8);
			if (width > maxWidth) {
				break;
			}
			len++;
		}
		return len;
	}

}
