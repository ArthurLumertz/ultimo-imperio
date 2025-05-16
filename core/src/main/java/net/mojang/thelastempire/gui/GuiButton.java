package net.mojang.thelastempire.gui;

public class GuiButton {

	public int index;
    public float x;
    public float y;
    public float width;
    public float height;
    public String title;

    public GuiButton(int index, float x, float y, float width, float height, String title) {
        this.index = index;
    	this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

}
