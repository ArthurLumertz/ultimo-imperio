package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.AudioManager;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.gui.GuiMainMenu;
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

	private String postCard;
	private float timer = 0;
	private float oldAlpha;
	private float alpha;
	private float yo;

	private boolean debug = false;

	public void setPostCard(String postCard) {
		this.postCard = postCard;
		this.timer = 0;
		this.alpha = 1;
		this.oldAlpha = alpha;
		this.yo = 32 * 3;
	}

	@Override
	public void create() {
		Resources.loadResources();
		backdropTexture = new TextureRegion(Resources.getTexture("dialogue"), 0, 64, 48, 32);

		sceneManager = new SceneManager(this);
		audioManager = new AudioManager();

		setGuiScreen(null, false);
		load();
	}

	@Override
	public void tick() {
		sceneManager.tick();

		if (!isInGame()) {
			return;
		}

		levelRenderer.tick();

		if (postCard == null) {
			return;
		}
		if (yo > 0) {
			yo -= 2;
			return;
		}

		timer += 0.04f;
		oldAlpha = alpha;

		if (timer > MathUtils.PI / 3) {
			alpha = MathUtils.cos(timer);
			alpha = Math.max(0f, Math.min(alpha, 1f));
		}

		if (timer > MathUtils.PI) {
			postCard = null;
			timer = 0;
		}
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

		if (postCard != null) {
			float a = MathUtils.lerp(oldAlpha, alpha, TheLastEmpire.a);
			g.setColor(1f, 1f, 1f, a);
			g.drawTexture(backdropTexture, (g.getScreenWidth() - 48 * 4) / 2, g.getScreenHeight() - 32 * 3 + yo, 48 * 4,
					32 * 3);

			int ir = (int) (1f * 255.0f);
			int ig = (int) (1f * 255.0f);
			int ib = (int) (1f * 255.0f);
			int ia = (int) (a * 255.0f);
			int color = (ia << 24) | (ir << 16) | (ig << 8) | ib;

			g.drawString(postCard, (g.getScreenWidth() - g.getStringWidth(postCard)) / 2f,
					g.getScreenHeight() - g.getFontSize() * 2.5f + yo, color);
			g.setColor(1f, 1f, 1f, 1f);
		}

		sceneManager.draw(g);

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

	public void load() {
		hasLoaded = true;

		level = new Level(this);
		level.loadLevel("palace", false, false);
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
