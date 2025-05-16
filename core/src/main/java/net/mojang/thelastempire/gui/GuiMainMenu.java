package net.mojang.thelastempire.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.engine.Graphics;

public class GuiMainMenu extends GuiMenu {

	private Array<GuiButton> buttons = new Array<GuiButton>();

	private Texture texture;

	@Override
	public void init() {
		texture = new Texture("screens/main_menu_backdrop.png");

		buttons.add(new GuiButton(0, (width - 48 * 8) / 2, 64 * 4f, 48 * 8, 44, "Começar"));
		buttons.add(new GuiButton(1, (width - 48 * 8) / 2, 64 * 3.1f, 48 * 8, 44, "Sair do Jogo"));
	}

	@Override
	protected void onButtonClick(int index) {
		switch (index) {
		case 0:
			theLastEmpire.setGuiScreen(new GuiIntro(), true);
			break;
		case 1:
			Gdx.app.exit();
			break;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(0x777777);
		g.drawTexture(texture, 0, 0, g.getScreenWidth(), g.getScreenHeight());
		g.setColor(1f, 1f, 1f, 1f);

		g.setFontSize(32f);
		g.drawCenteredString("O Último Império", g.getScreenWidth(), g.getScreenHeight() - g.getFontSize() * 4f,
				0xFFFFFF);
		g.setFontSize(16f);

		g.drawCenteredString("Feito por", g.getScreenWidth(), 70, 0xBBBBBB);
		g.drawCenteredString("Arthur Lumertz, Mateus Braga, Matheus Sartori, Martin Ascoli, Felipe Argenti",
				g.getScreenWidth(), 40, 0xBBBBBB);

		drawButtons(buttons);
	}

}
