package net.mojang.thelastempire.gui;

public class Button {

	public int index;
    public float x;
    public float y;
    public float width;
    public float height;
    public String title;
    public boolean centerText;
    public int type;

    public Button(int index, float x, float y, float width, float height, String title) {
        this.index = index;
    	this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }
    
    public Button(int index, float x, float y, float width, float height, String title, boolean centerText) {
        this.index = index;
    	this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.centerText = centerText;
    }
    
    public Button(int index, int type, float x, float y, float width, float height, String title, boolean centerText) {
        this.index = index;
        this.type = type;
    	this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.centerText = centerText;
    }

}
