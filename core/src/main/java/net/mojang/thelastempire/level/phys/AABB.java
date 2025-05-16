package net.mojang.thelastempire.level.phys;

import com.badlogic.gdx.utils.Pool;

public class AABB {

    public float x0;
    public float y0;
    public float x1;
    public float y1;

    private static final Pool<AABB> pool = new Pool<AABB>() {
        @Override
        protected AABB newObject() {
            return new AABB();
        }
    };

    public static AABB newPermanent(float x0, float y0, float x1, float y1) {
        return new AABB(x0, y0, x1, y1);
    }

    public static AABB newTemp(float x0, float y0, float x1, float y1) {
        AABB aABB = pool.obtain();
        aABB.set(x0, y0, x1, y1);
        return aABB;
    }

    public static void clearPool() {
        pool.clear();
    }

    public AABB() {
        this(0f, 0f, 0f, 0f);
    }

    public AABB(float x0, float y0, float x1, float y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public void set(float x0, float y0, float x1, float y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public void set(AABB aABB) {
        set(aABB.x0, aABB.y0, aABB.x1, aABB.y1);
    }

    public AABB expand(float xa, float ya) {
        float _x0 = x0;
        float _y0 = y0;
        float _x1 = x1;
        float _y1 = y1;
        if (xa < 0f)
            _x0 += xa;
        if (xa > 0f)
            _x1 += xa;
        if (ya < 0f)
            _y0 += ya;
        if (ya > 0f)
            _y1 += ya;
        return AABB.newTemp(_x0, _y0, _x1, _y1);
    }

    public AABB grow(float xa, float ya) {
        return AABB.newTemp(x0 - xa, y0 - ya, x1 + xa, y1 + ya);
    }

    public float clipXCollide(AABB c, float xa) {
        if (c.y1 <= y0 || c.y0 >= y1)
            return xa;

        if (xa > 0f && c.x1 <= x0) {
            float max = x0 - c.x1;
            if (max < xa)
                xa = max;
        }

        if (xa < 0f && c.x0 >= x1) {
            float max = x1 - c.x0;
            if (max > xa)
                xa = max;
        }

        return xa;
    }

    public float clipYCollide(AABB c, float ya) {
        if (c.x1 <= x0 || c.x0 >= x1)
            return ya;

        if (ya > 0f && c.y1 <= y0) {
            float max = y0 - c.y1;
            if (max < ya)
                ya = max;
        }

        if (ya < 0f && c.y0 >= y1) {
            float max = y1 - c.y0;
            if (max > ya)
                ya = max;
        }

        return ya;
    }

    public boolean intersects(AABB c) {
        if (x0 >= c.x1 || c.x0 >= x1)
            return false;
        if (y0 >= c.y1 || c.y0 >= y1)
            return false;
        return true;
    }

    public void move(float xa, float ya) {
        x0 += xa;
        y0 += ya;
        x1 += xa;
        y1 += ya;
    }

}
