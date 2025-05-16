package net.mojang.thelastempire.engine;

import com.badlogic.gdx.utils.Pool;

public class Color {

	private static final Pool<Color> pool = new Pool<Color>() {
		@Override
		protected Color newObject() {
			return new Color();
		}
	};
	
	public static Color newPermanent(float r, float g, float b, float a) {
		return new Color(r, g, b, a);
	}
	
	public static Color newTemp(float r, float g, float b, float a) {
		Color color = pool.obtain();
		color.set(r, g, b, a);
		return color;
	}
	
	public static void clearPool() {
		pool.clear();
	}

	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color() {}
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

}
