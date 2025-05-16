package net.mojang.thelastempire.level.tile;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;
import net.mojang.thelastempire.level.entity.object.Tree;

public class TreeTile extends Tile {

	protected TreeTile(int id, Material material) {
		super(id, material);
	}

	@Override
	public void draw(Graphics g, int x, int y, Level level) {
		if (isNearbyTile(grass.id, x, y, level)) {
			grass.draw(g, x, y, level);
		} else {
			dirt.draw(g, x, y, level);
		}
	}

	@Override
	public void onGenerate(Level level, int x, int y) {
		Tree tree = new Tree(level, x, y);
		level.addEntity(tree);
	}

}
