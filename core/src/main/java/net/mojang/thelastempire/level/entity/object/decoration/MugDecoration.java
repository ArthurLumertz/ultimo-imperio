package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.particle.EntityVaporFX;
import net.mojang.thelastempire.math.RectangleI;

public class MugDecoration extends Decoration {

    private int timer;

	public MugDecoration(Level level, float x, float y, int type) {
		super(level, x, y, 1f, 1f, new RectangleI(0, 32, 16, 16));
	}

    @Override
    public void tick() {
        super.tick();

        int i = 10;

        timer++;
        if (timer > i) {
            EntityVaporFX entityFX = new EntityVaporFX(level, x + 0.3f, y + 0.15f);
            level.addEntity(entityFX);
            timer -= i;
        }
    }

}
