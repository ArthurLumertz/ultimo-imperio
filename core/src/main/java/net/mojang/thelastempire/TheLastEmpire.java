package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

import net.mojang.thelastempire.engine.AudioManager;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.engine.TileSheet;
import net.mojang.thelastempire.gui.GuiMainMenu;
import net.mojang.thelastempire.gui.GuiScreen;
import net.mojang.thelastempire.gui.scene.SceneManager;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.LevelRenderer;

public class TheLastEmpire extends Game {

	private static TheLastEmpire tle = new TheLastEmpire();

	private Level level;
	private LevelRenderer levelRenderer;

	private volatile boolean hasLoaded = false;

	private SceneManager sceneManager;
	private AudioManager audioManager;

	private Pointer pointer;

	private boolean debug = false;

	public final Vector2 mousePosition = new Vector2();

	@Override
	public void create() {
		Resources.loadResources();

		pointer = new Pointer(new TileSheet("cursor"), this);

		sceneManager = new SceneManager(this);
		audioManager = new AudioManager();

		setGuiScreen(new GuiMainMenu(), false);
	}

	@Override
	public void tick() {
		sceneManager.tick();

		pointer.tick();

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
			mousePosition.set(g.unproject(Gdx.input.getX(), Gdx.input.getY()));
			levelRenderer.draw(g);
			g.end();
		}

		Graphics g = Graphics.guiInstance;
		g.begin();

		if (debug) {
			g.drawString("FPS: " + Gdx.graphics.getFramesPerSecond(), g.getFontSize(),
					g.getScreenHeight() - g.getFontSize(), 0xFFFFFF);
			g.drawString("X/Y: " + level.getPlayer().x + ", " + level.getPlayer().y, g.getFontSize(),
					g.getScreenHeight() - g.getFontSize() * 3f, 0xFFFFFF);
		}

		sceneManager.draw(g);
		pointer.draw(g);

		g.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			toggleFullscreen();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			level.reloadLevel();
		}

		pointer.setPointer(Pointer.Type.DEFAULT);
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

	public LevelRenderer getLevelRenderer() {
		return levelRenderer;
	}

	public void setPointer(int type) {
		pointer.setPointer(type);
	}

}
