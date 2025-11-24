package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.engine.Graphics;

public class GuiGameOver extends GuiMenu {

	private Array<Button> buttons = new Array<Button>();

	@Override
	public void init() {
		buttons.clear();
		buttons.add(new Button(0, (width - 48 * 8) / 2, 64 * 4f, 48 * 8, 44, "Tentar de novo"));
		buttons.add(new Button(1, (width - 48 * 8) / 2, 64 * 3f, 48 * 8, 44, "Sair do Jogo"));
	}

	@Override
	protected void onButtonClick(int index) {
		switch (index) {
		case 0:
			theLastEmpire.setGuiScreen(null, true);
			theLastEmpire.getLevel().reloadLevel();
			break;
		case 1:
			Gdx.app.exit();
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setFontSize(32f);
		g.drawCenteredString("Você morreu!", g.getScreenWidth(), g.getScreenHeight() - g.getFontSize() * 5f, 0xFFFFFF);
		g.setFontSize(16f);

		drawButtons(buttons);
	}

}
