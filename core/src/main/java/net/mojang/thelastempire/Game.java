package net.mojang.thelastempire;

import com.badlogic.gdx.Gdx;

import net.mojang.thelastempire.engine.Color;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.math.Vec2;

public abstract class Game extends com.badlogic.gdx.Game {

	private static final float TICKS_PER_SECOND = 1f / 20f;
	
    private float tickCount = 0;
    private float fpsCount = 0;
    protected int ticks;

    public static float a;

    public abstract void tick();

    @Override
    public void render() {
        tickCount += Gdx.graphics.getDeltaTime();
        while (tickCount > TICKS_PER_SECOND) {
            AABB.clearPool();
            Color.clearPool();
            Vec2.clearPool();

            tick();
            ticks++;
            tickCount -= TICKS_PER_SECOND;
        }

        fpsCount += Gdx.graphics.getDeltaTime();
        if (fpsCount > 1f) {
            System.out.println(ticks + " ticks, " + Gdx.graphics.getFramesPerSecond() + " fps");
            ticks = 0;
            fpsCount -= 1f;
        }

        a = tickCount / TICKS_PER_SECOND;
    }

}
