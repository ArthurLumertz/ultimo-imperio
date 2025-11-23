package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.mojang.thelastempire.engine.AudioManager;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.gui.GuiCredits;
import net.mojang.thelastempire.gui.GuiScreen;
import net.mojang.thelastempire.gui.scene.SceneManager;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.LevelRenderer;

public class TheLastEmpire extends Game {

	private static TextureRegion backdropTexture;

	private static TheLastEmpire tle = new TheLastEmpire();

	private Level level;
	private LevelRenderer levelRenderer;

	private volatile boolean hasLoaded = false;

	private SceneManager sceneManager;
	private AudioManager audioManager;

	private int timer = 0;
	private int oldTimer = 0;

	private boolean debug = false;

	@Override
	public void create() {
		Resources.loadResources();
		backdropTexture = new TextureRegion(Resources.getTexture("dialogue"), 0, 64, 48, 32);

		Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);

		sceneManager = new SceneManager(this);
		audioManager = new AudioManager();

		String[] mapsInOrder = { "palace", "palacecutscene", "level0" };

		setGuiScreen(null, false);
		load(mapsInOrder[0]);
	}

	@Override
	public void tick() {
		sceneManager.tick();

		oldTimer = timer;
		timer++;

		if (!isInGame()) {
			return;
		}

		levelRenderer.tick();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);

		if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
			debug = !debug;
		}

		if (hasLoaded) {
			Graphics g = Graphics.instance;
			g.begin();
			levelRenderer.draw(g);
			g.end();
		}

		Graphics g = Graphics.guiInstance;
		g.begin();

		if (debug) {
			g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond(), g.getFontSize(),
					g.getScreenHeight() - g.getFontSize(), 0xFFFFFF);
		}

		sceneManager.draw(g);

		Vector2 mouseCoords = g.unproject(Gdx.input.getX(), Gdx.input.getY());
		float a = oldTimer + (timer - oldTimer) * TheLastEmpire.a;
		float scalar = 0.9f + ((1f + MathUtils.cos(a * 0.2f)) / 2f) * 0.2f;
		float rs = 48f;
		float xs = rs * scalar;
		float ys = xs;
		float rx = mouseCoords.x + rs / 2f;
		float ry = mouseCoords.y - rs / 2f;
		g.drawTexture(Resources.getTexture("cursor"), rx - xs / 2f, ry - ys / 2f, xs, ys);

		g.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			toggleFullscreen();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			level.reloadLevel();
		}

		super.render();
	}

	private void toggleFullscreen() {
		if (Gdx.graphics.isFullscreen()) {
			Gdx.graphics.setWindowedMode(960, 540);
		} else {
			DisplayMode displayMode = Gdx.graphics.getDisplayMode();
			Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
			Gdx.graphics.setFullscreenMode(displayMode);
		}
	}

	@Override
	public void dispose() {
		Graphics.instance.dispose();
		Graphics.guiInstance.dispose();
	}

	public void setGuiScreen(GuiScreen guiScreen, boolean fade) {
		float w = Graphics.guiInstance.getScreenWidth();
		float h = Graphics.guiInstance.getScreenHeight();
		sceneManager.setScreen(guiScreen, w, h, fade);
	}

	public boolean isInGame() {
		return hasLoaded || !sceneManager.isPaused();
	}

	public void load(String levelName) {
		hasLoaded = true;

		level = new Level(this);
		level.loadLevel(levelName, false, false);
		levelRenderer = new LevelRenderer(level);
		level.addLevelRenderer(levelRenderer);
	}

	public void unload() {
		hasLoaded = false;
	}

	public static TheLastEmpire getTheLastEmpire() {
		return tle;
	}

	public void playSound(String sound, float volume, float pitch) {
		audioManager.playSound(sound, volume, pitch);
	}

	public void playMusic(String music, boolean looping) {
		audioManager.playMusic(music, looping);
	}

	public void stopMusic(String music) {
		audioManager.stopMusic(music);
	}

	public SceneManager getSceneManager() {
		return sceneManager;
	}

	public Level getLevel() {
		return level;
	}

}
