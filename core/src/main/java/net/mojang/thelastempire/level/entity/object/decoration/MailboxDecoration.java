package net.mojang.thelastempire.level.entity.object.decoration;

import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.math.RectangleI;

public class MailboxDecoration extends Decoration {
	
	public MailboxDecoration(Level level, float x, float y) {
		super(level, x, y, 1f, 2f, new RectangleI(16, 64, 16, 32));
	}

}
