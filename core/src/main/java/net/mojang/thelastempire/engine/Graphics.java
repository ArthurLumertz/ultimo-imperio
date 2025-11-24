package net.mojang.thelastempire.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.mojang.thelastempire.gui.Font;
import net.mojang.thelastempire.level.phys.AABB;

public class Graphics {

	public static Graphics instance = new Graphics(false);
	public static Graphics guiInstance = new Graphics(true);

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;

	private ShaderProgram shader;

	private TileSheet tileSheet;
	private TileSheet playerSheet;
	private TileSheet houseSheet;
	private TileSheet furnitureSheet;
	private TileSheet plantSheet;
	private TileSheet particleSheet;

	private Vector2 tmpVec2 = new Vector2();
	private Vector3 tmpVec3 = new Vector3();

	private Font font;

	private boolean gui;

	private Array<Light> lights = new Array<Light>();
	private Array<Light> queryLights = new Array<Light>();

	public Graphics(boolean gui) {
		this.gui = gui;

		float scalar = gui ? 1f : 16f * 3f;
		float w = Gdx.graphics.getWidth() / scalar;
		float h = Gdx.graphics.getHeight() / scalar;

		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		viewport = new FitViewport(w, h, camera);
		font = new Font(this, "bold");
		tileSheet = new TileSheet("tiles");

		String shaderName = gui ? "gui" : "default";
		shader = new ShaderProgram(Gdx.files.internal("shaders/" + shaderName + ".vert"),
				Gdx.files.internal("shaders/" + shaderName + ".frag"));
		if (!shader.isCompiled()) {
			throw new RuntimeException(shader.getLog());
		}
		batch.setShader(shader);

		plantSheet = new TileSheet("plants");
		
		if (!gui) {
			font.setSize(16f / 48f);
		} else {
			return;
		}

		playerSheet = new TileSheet("char");
		houseSheet = new TileSheet("houses");
		furnitureSheet = new TileSheet("furniture");
		particleSheet = new TileSheet("particles");
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public ShaderProgram getShader() {
		return shader;
	}

	public void begin() {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gui);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if (!gui) {			
			applyVignette();
			applyLights();
		}
	}
	
	private void applyVignette() {
		shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	private void applyLights() {
		queryLights.clear();
		for (Light light : lights) {
			if (light.shouldRender()) {
				queryLights.add(light);
			}
		}

		shader.setUniformi("u_numLights", queryLights.size);

		float scaleY = Gdx.graphics.getHeight() / camera.viewportHeight;

		for (int i = 0; i < queryLights.size; i++) {
			Light light = queryLights.get(i);

			tmpVec3.set(light.x, light.y, 0f);
			camera.project(tmpVec3, 0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());

			float screenRadius = light.innerRadius * scaleY;
			float screenORadius = light.outerRadius * scaleY;

			shader.setUniformf("u_pointLights[" + i + "].position", tmpVec3.x, tmpVec3.y);
			shader.setUniformf("u_pointLights[" + i + "].color", light.color.r, light.color.g, light.color.b,
					light.color.a);
			shader.setUniformf("u_pointLights[" + i + "].intensity", light.intensity);
			shader.setUniformf("u_pointLights[" + i + "].innerRadius", screenRadius);
			shader.setUniformf("u_pointLights[" + i + "].outerRadius", screenORadius);
		}
	}

	public void end() {
		batch.end();
	}

	public void dispose() {
		batch.dispose();

		tileSheet.cleanup();

		if (gui)
			return;
		playerSheet.cleanup();
		houseSheet.cleanup();
		furnitureSheet.cleanup();
		particleSheet.cleanup();
		plantSheet.cleanup();
	}

	public void drawTexture(Texture texture, float x, float y, float width, float height) {
		batch.draw(texture, x, y, width, height);
	}

	public void drawTexture(TextureRegion texture, float x, float y, float width, float height) {
		batch.draw(texture, x, y, width, height);
	}

	public void drawTexture(TextureRegion texture, float x, float y, float width, float height, float rotation) {
//		batch.draw(texture, x, y, width, height);
		float ox = width / 2f;
		float oy = height / 2f;
		batch.draw(texture, x, y, ox, oy, width, height, 1f, 1f, rotation);
	}

	public void bindTileSheet() {
		tileSheet.bind();
	}

	public void drawRect(float x, float y, float width, float height) {
		TextureRegion texture = getTile(6, 2);
		drawTexture(texture, x, y, width, height);
	}

	public void setColor(float r, float g, float b, float a) {
		batch.setColor(r, g, b, a);
	}

	public void setColor(int col) {
		float a = ((col >>> 24) & 0xFF) != 0xFF ? 1f : (col >>> 24 & 0xFF) / 255f;

		float r = (col >> 16 & 0xFF) / 255f;
		float g = (col >> 8 & 0xFF) / 255f;
		float b = (col & 0xFF) / 255f;
		setColor(r, g, b, a);
	}

	public TextureRegion getTile(int tx, int ty) {
		return tileSheet.getRegion(tx * 16, ty * 16, 16, 16);
	}

	public TextureRegion getTile(int tx, int ty, int tw, int th) {
		return tileSheet.getRegion(tx, ty, tw, th);
	}

	public TextureRegion getPlayer(int px, int py) {
		return playerSheet.getRegion(px * 16, px * 32, 16, 32);
	}

	public TextureRegion getHouse(int hx, int hy, int hw, int hh) {
		return houseSheet.getRegion(hx, hy, hw, hh);
	}

	public TextureRegion getFurniture(int fx, int fy, int fw, int fh) {
		return furnitureSheet.getRegion(fx, fy, fw, fh);
	}

	public TextureRegion getPlant(int px, int py) {
		return plantSheet.getRegion(px * 48, py * 96, 48, 96);
	}

	public TextureRegion getParticle(int px, int py) {
		return particleSheet.getRegion(px * 8, py * 8, 8, 8);
	}

	public TextureRegion getParticle(int px, int py, int pw, int ph) {
		return particleSheet.getRegion(px, py, pw, ph);
	}
	
	public void setOffset(float x, float y, float a) {
		tmpVec3.set(x, y, 0f);
		camera.position.set(tmpVec3);
	}

	public void limitOffset(float x0, float y0, float x1, float y1) {
		camera.position.x = Math.max(x0, Math.min(camera.position.x, x1));
		camera.position.y = Math.max(y0, Math.min(camera.position.y, y1));
	}

	public boolean inViewport(AABB aABB) {
		float halfWidth = getScreenWidth() / 2f;
		float halfHeight = getScreenHeight() / 2f;

		float viewportMinX = camera.position.x - halfWidth;
		float viewportMinY = camera.position.y - halfHeight;
		float viewportMaxX = camera.position.x + halfWidth;
		float viewportMaxY = camera.position.y + halfHeight;

		return aABB.x1 > viewportMinX && aABB.x0 < viewportMaxX && aABB.y1 > viewportMinY && aABB.y0 < viewportMaxY;
	}

	public boolean inViewport(Rectangle r) {
		AABB aABB = AABB.newTemp(r.x, r.y, r.x + r.width, r.y + r.height);
		return inViewport(aABB);
	}

	public float getScreenWidth() {
		return viewport.getWorldWidth();
	}

	public float getScreenHeight() {
		return viewport.getWorldHeight();
	}

	public void drawString(String str, float x, float y, int col) {
		font.drawShadow(str, x, y, col);
	}

	public int getStringWidth(String str) {
		return font.width(str);
	}

	public void drawCenteredString(String str, float width, float y, int col) {
		float x = (width - font.width(str)) / 2f;
		drawString(str, x, y, col);
	}

	public void setFontSize(float size) {
		font.setSize(size);
	}

	public float getFontSize() {
		return font.getSize();
	}

	public Vector2 unproject(float screenX, float screenY) {
		tmpVec2.set(screenX, screenY);
		return viewport.unproject(tmpVec2);
	}

	public void setAmbientLight(float r, float g, float b) {
		shader.setUniformf("u_ambientLight", r, g, b);
	}

	public void setLight(Light light) {
		lights.add(light);
	}

	public void removeLight(Light light) {
		lights.removeValue(light, true);
	}
	
	public void clearLights() {
		lights.clear();
	}

	public Vector2 getVector(float x, float y) {
		return tmpVec2.set(x, y);
	}

}
