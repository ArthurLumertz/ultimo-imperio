package net.mojang.thelastempire.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileSheet {

	private Texture texture;
	private TextureRegion region;

	public TileSheet(String fileName) {
		texture = Resources.getTexture(fileName);
		region = new TextureRegion(texture);
	}

	public TextureRegion getRegion(int x, int y, int width, int height) {
		region.setRegion(x, y, width, height);
		return region;
	}

	public void cleanup() {
		texture.dispose();
	}

	public void bind() {
		texture.bind();
	}

}
