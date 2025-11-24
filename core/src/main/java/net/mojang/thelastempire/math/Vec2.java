package net.mojang.thelastempire.math;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Vec2 {

	private static Pool<Vec2> pool = new Pool<Vec2>() {
		@Override
		protected Vec2 newObject() {
			return newPermanent(0f, 0f);
		}
	};

	public static Vec2 newPermanent(float x, float y) {
		return new Vec2(x, y);
	}

	public static Vec2 newTemp(float x, float y) {
		Vec2 vec = pool.obtain();
		return vec.set(x, y);
	}

	public static void clearPool() {
		pool.clear();
	}

	public static final Vec2 ZERO = new Vec2(0, 0);

	public float x;
	public float y;

	public Vec2() {
		set(ZERO);
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vec2 set(Vec2 other) {
		x = other.x;
		y = other.y;
		return this;
	}

	public Vec2 set(Vector2 other) {
		x = other.x;
		y = other.y;
		return this;
	}

	public Vec2 copy() {
		return new Vec2(x, y);
	}

	public static float distance(Vec2 v0, Vec2 v1) {
		float dx = v1.x - v0.x;
		float dy = v1.y - v0.y;
		return (float) Math.hypot(dx, dy);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float lengthSquared() {
		return x * x + y * y;
	}

	public void normalize() {
		float len = length();
		if (len > 0f) {
			x /= len;
			y /= len;
		}
	}

	@Override
	public String toString() {
		return "Vec2f(" + x + ", " + y + ")";
	}

	public boolean contains(Rectangle rectangle) {
		return rectangle.contains(x, y);
	}

}
