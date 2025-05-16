package net.mojang.thelastempire.engine;

import com.badlogic.gdx.math.Rectangle;

public class Light {

	public float x;
	public float y;
	public float innerRadius;
	public float outerRadius;
	public float intensity;
	public Color color;

	public Light(float x, float y, float intensity, float innerRadius, float outerRadius, Color color) {
		this.x = x;
		this.y = y;
		this.intensity = intensity;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.color = color;
	}

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean shouldRender() {
    	Rectangle rectangle = Rectangle.tmp.set(
		    x - outerRadius, 
		    y - outerRadius, 
		    outerRadius * 2, 
		    outerRadius * 2
		);
    	return Graphics.instance.inViewport(rectangle);
    }

}
