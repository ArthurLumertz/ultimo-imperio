package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.engine.Resources;

public class GuiIntro extends GuiScreen {

	private TextWriter textWriter;
	private Music music;

	@Override
	public void init() {
		String[] dialogues = { "Brasil, 1889. O império se desestabiliza.",
				"O povo clama por mudança. Nas Forças Armadas, se espalham\nrumores sobre um possível golpe de Estado.",
				"Dom Pedro II, velho e cansado, resiste em abdicar.",
				"Marechal Deodoro da Fonseca, antes leal, agora dividido...",
				"E se a proclamação não fosse pacífica? E se o imperador lutasse até o fim?" };
		textWriter = new TextWriter(dialogues, 1, 80, this::onFinish);

		music = Resources.getMusic("intro");
		music.setLooping(true);
		music.play();
		
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void tick() {
		textWriter.tick();

		float progress = textWriter.getProgress();
		float volume = progress * 5f;
		if (progress > 0.9f) {
			volume = MathUtils.lerp(volume, 0f, progress);
		}
		volume = Math.min(volume, 1f);
		music.setVolume(volume);
	}

	@Override
	public void draw(Graphics g) {
		textWriter.draw(g, true);

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			textWriter.skip();
		}
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	private void onFinish() {
		Gdx.input.setCursorCatched(false);
		music.stop();

		theLastEmpire.setGuiScreen(null, true);
		theLastEmpire.load();
	}

}
