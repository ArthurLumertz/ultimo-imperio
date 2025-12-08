package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import net.mojang.thelastempire.engine.Graphics;

public class GuiEnd extends GuiMenu {

	private TextWriter textWriter;

	@Override
	public void init() {
		String[] texts = { "*Som de tambores ao fundo*", "Narrador: 15 de novembro de 1889.",
				"Narrador: Marechal Deodoro da Fonseca ergue a nova bandeira da República.",
				"*Multidão aplaude e grita*", "Multidão: Viva a República! Viva o Brasil!",
				"Narrador: Nasce uma nova era. O Império chega ao fim." };

		textWriter = new TextWriter(texts, 1, 80, this::onFinish);
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void tick() {
		textWriter.tick();
	}

	@Override
	public void draw(Graphics g) {
		textWriter.draw(g, true);

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			textWriter.skip();
		}
	}

	private void onFinish() {
        theLastEmpire.setGuiScreen(new GuiCredits(), true);
	}

}
