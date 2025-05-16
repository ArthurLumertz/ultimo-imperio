package net.mojang.thelastempire.gui.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.gui.GuiScreen;

public class SceneManager {

	private GuiScreen screen;
	private TheLastEmpire theLastEmpire;

	private boolean isFading = false;
	private int fadeTimer = 0;
	private float oldAlpha;
	private float alpha;
	private static final float FADE_DURATION = 20 * 4;

	public SceneManager(TheLastEmpire theLastEmpire) {
		this.theLastEmpire = theLastEmpire;
	}

	public void tick() {
		if (screen != null) {
			screen.tick();
		}

		if (isFading) {
			oldAlpha = alpha;

			fadeTimer++;
			if (fadeTimer < FADE_DURATION) {
				float progress = fadeTimer / FADE_DURATION;
				alpha = MathUtils.cos(progress * MathUtils.PI);
			} else {
				alpha = 0f;
				isFading = false;
			}
		}
	}

	public void draw(Graphics g) {
		if (screen != null) {
			screen.draw(g);
		}

		if (isFading) {
			float a = oldAlpha + (alpha - oldAlpha) * TheLastEmpire.a;
			g.setColor(0, 0, 0, a);
			g.drawRect(0, 0, g.getScreenWidth(), g.getScreenHeight());
		}
	}

	public void setScreen(GuiScreen screen, float width, float height, boolean fade) {
		if (this.screen != null) {
			this.screen.dispose();
		}
		if (screen != null) {
			screen.init(theLastEmpire, width, height);
		}

		if (fade) {
			if (screen == null || (screen != null && screen.shouldFadeIn())) {
				startFade();
			} 			
		}

		Gdx.graphics.setForegroundFPS((screen != null ? 60 : Gdx.graphics.getDisplayMode().refreshRate + 1));
		
		this.screen = screen;
	}

	public void startFade() {
		isFading = true;
		fadeTimer = 0;
		alpha = 1f;
		oldAlpha = 1f;
	}

	public GuiScreen getScreen() {
		return screen;
	}

	public boolean isPaused() {
		return screen != null && screen.pauseGame();
	}
	
	public boolean isFading() {
		return isFading;
	}

}
