package net.mojang.thelastempire.gui;

import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;

public class TextWriter {

	private String[] texts;

	private int textIndex = 0;
	private int charIndex = 0;

	private int delay = 0;
	private int timer = -10;

	private boolean paused = false;
	private int pauseTimer = 0;
	private int pauseDuration;

	private boolean finished = false;

	private Runnable onFinish;

	public TextWriter(String[] texts, int delay, int pauseDuration, Runnable onFinish) {
		this.texts = texts;
		this.delay = delay;
		this.pauseDuration = pauseDuration;
		this.onFinish = onFinish;
	}

	public void tick() {
		if (finished)
			return;

		timer++;
		if (timer >= delay && !paused) {
			if (charIndex > getCurrentText().length() - 1) {
				paused = true;
				return;
			}

			playSound();
			charIndex++;
			timer = 0;
		}

		if (paused) {
			pauseTimer++;
			if (pauseTimer > pauseDuration) {
				if (textIndex < texts.length - 1) {
					textIndex++;
					charIndex = 0;
				} else {
					if (onFinish != null) {
						onFinish.run();
					}
					finished = true;
				}
				pauseTimer = 0;
				paused = false;
			}
		}
	}

	public void skip() {
		if (finished)
			return;

		if (!paused) {
			charIndex = getCurrentText().length();
			paused = true;
			timer = 0;
		} else {
			pauseTimer = pauseDuration + 1;
		}
	}

	public void draw(Graphics g, float centerX, float centerY, int color) {
		if (finished)
			return;

		String str = getCurrentText().substring(0, Math.min(charIndex, getCurrentText().length()));

		g.drawCenteredString(str, centerX, centerY - g.getFontSize(), color);
	}

	public void draw(Graphics g, boolean centered) {
		if (finished)
			return;

		float centerY = (g.getScreenHeight() - g.getFontSize()) / 2f;

		String[] rawStr = getCurrentText().substring(0, Math.min(charIndex, getCurrentText().length())).split("\n");
		
		float yy = centerY;
		
		for (int y = 0; y < rawStr.length; y++) {
			String str = rawStr[y];
			g.drawCenteredString(str, g.getScreenWidth(), yy, 0xFFFFFF);
			yy -= g.getFontSize() * 2f;
		}
	}

	public boolean isFinished() {
		return finished;
	}

	private String getCurrentText() {
		return texts[textIndex];
	}

	private void playSound() {
		TheLastEmpire.getTheLastEmpire().playSound("type", 0.2f, MathUtils.random(0.8f, 1.2f));
	}

	public float getProgress() {
		if (texts.length == 0) {
			return 1f;
		}

		float totalChars = 0f;
		for (String text : texts) {
			totalChars += text.length();
		}

		float writtenChars = 0f;
		for (int i = 0; i < textIndex; i++) {
			writtenChars += texts[i].length();
		}
		writtenChars += charIndex;

		return Math.min(writtenChars / totalChars, 1f);
	}

}
