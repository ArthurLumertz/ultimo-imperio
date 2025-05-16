package net.mojang.thelastempire.math;

import com.badlogic.gdx.math.Rectangle;

public class Vec2i {

    public static final Vec2i ZERO = new Vec2i(0, 0);

    public int x;
    public int y;

    public Vec2i() {
        set(ZERO);
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec2i other) {
        x = other.x;
        y = other.y;
    }

    public Vec2i copy() {
        return new Vec2i(x, y);
    }

    @Override
    public String toString() {
        return "Vec2i(" + x + ", " + y + ")";
    }

    public boolean contains(Rectangle rectangle) {
        return rectangle.contains(x, y);
    }

}
