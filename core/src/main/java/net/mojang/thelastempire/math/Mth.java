package net.mojang.thelastempire.math;

import com.badlogic.gdx.math.MathUtils;

public class Mth {

	public static final float cosWave(float a, float min, float max) {
		float x = (MathUtils.cos(a) + 1) * 0.5f;
		return min + x * (max - min);
	}
	
}
