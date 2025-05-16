package net.mojang.thelastempire.math;

public class RectangleI {

	public static final RectangleI tmp = new RectangleI();

	public int x;
	public int y;
	public int width;
	public int height;

	public RectangleI() {
	}

	public RectangleI(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public RectangleI set(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RectangleI that = (RectangleI) o;
		return x == that.x && y == that.y && width == that.width && height == that.height;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + width;
		result = 31 * result + height;
		return result;
	}

}
