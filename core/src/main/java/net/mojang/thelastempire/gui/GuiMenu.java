package net.mojang.thelastempire.gui;

public abstract class GuiMenu extends GuiScreen {

	@Override
	public boolean pauseGame() {
		return true;
	}
	
	@Override
	public boolean shouldFadeIn() {
		return false;
	}
	
}
