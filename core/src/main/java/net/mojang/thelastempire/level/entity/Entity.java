package net.mojang.thelastempire.level.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.math.Vec2;

public class Entity {

	protected Level level;

	public float xo;
	public float yo;

	public float x;
	public float y;

	public float xd;
	public float yd;

	public AABB boundingBox;

	public float bbWidth = 1f;
	public float bbHeight = 0.8f;

	public float speed;
	public boolean noPhysics = false;

	public boolean removed = false;

	public Entity(Level level) {
		this.level = level;
		if (!noPhysics) {
			boundingBox = new AABB(x, y, x + bbWidth, y + bbHeight);
		}
	}

	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
		if (boundingBox != null) {
			boundingBox.set(x, y, x + bbWidth, y + bbHeight);
		}
	}

	protected void setSize(float bbWidth, float bbHeight) {
		this.bbWidth = bbWidth;
		this.bbHeight = bbHeight;
		setPos(x, y);
	}

	public void moveRelative(float xa, float ya, float speed) {
		float dist = xa * xa + ya * ya;
		if (dist < 0.1f) {
			return;
		}

		dist = speed / (float) Math.sqrt(dist);
		xa *= dist;
		ya *= dist;

		xd += xa;
		yd += ya;
	}

	public void move(float xa, float ya) {
		float xaOrg = xa;
		float yaOrg = ya;

		if (!noPhysics) {
			Array<AABB> aABBs = level.getCubes(boundingBox.expand(xa, ya));

			for (AABB aABB : aABBs) {
				xa = aABB.clipXCollide(boundingBox, xa);
				ya = aABB.clipYCollide(boundingBox, ya);
			}
		}
		
		boundingBox.move(xa, ya);

		if (xaOrg != xa)
			xd = 0f;
		if (yaOrg != ya)
			yd = 0f;

		x = boundingBox.x0;
		y = boundingBox.y0;
	}

	public void tick() {
	}

	public void draw(Graphics g) {
	}

	public String getName() {
		return "Desconhecido";
	}

	public boolean shouldTick() {
		Rectangle bb = Rectangle.tmp.set(x, y, bbWidth, bbHeight);
		return Graphics.instance.inViewport(bb);
	}

	public boolean shouldRender() {
		return shouldTick();
	}

	public Vec2 getDrawPosition() {
		float xx = xo + (x - xo) * TheLastEmpire.a;
		float yy = yo + (y - yo) * TheLastEmpire.a;
		return Vec2.newTemp(xx, yy);
	}

	public void remove() {
		removed = true;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o.getClass() != getClass())
			return false;
		Entity e = (Entity) o;
		return e.getName().equals(getName());
	}

}
