package net.mojang.thelastempire.level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.object.furniture.EntityFurniture;
import net.mojang.thelastempire.level.particle.EntityFX;

public class LevelRenderer {

	private static final int CHUNK_SIZE = 16;

	private Level level;
	private int xChunks;
	private int yChunks;
	private Chunk[] chunks;
	private boolean dirty = true;
	private boolean shouldWork = true;

	private ObjectSet<Entity> tickableEntities = new ObjectSet<Entity>(64);
	private ObjectSet<Entity> renderableEntities = new ObjectSet<Entity>(64);
	private Array<Entity> sortedRenderableEntities = new Array<>(64);
	
	public LevelRenderer(Level level) {
		this.level = level;
	}

	private void rebuild() {
		tickableEntities.clear();
		renderableEntities.clear();
		sortedRenderableEntities.clear();

		xChunks = Math.max(1, level.getWidth() / CHUNK_SIZE);
		yChunks = Math.max(1, level.getHeight() / CHUNK_SIZE);
		chunks = new Chunk[xChunks * yChunks];
		for (int x = 0; x < xChunks; x++) {
			for (int y = 0; y < yChunks; y++) {
				int x0 = x * CHUNK_SIZE;
				int y0 = y * CHUNK_SIZE;
				int x1 = Math.min((x + 1) * CHUNK_SIZE, level.getWidth());
				int y1 = Math.min((y + 1) * CHUNK_SIZE, level.getHeight());
				chunks[x + y * xChunks] = new Chunk(level, x0, y0, x1, y1);
			}
		}
	}
	
	public void unload() {
		shouldWork = false;
		tickableEntities.clear();
		renderableEntities.clear();
		sortedRenderableEntities.clear();
	}

	public void tick() {
		if (!shouldWork) return;

		Array<Entity> rawEntities = level.getEntities();

		for (int i = 0; i < rawEntities.size; i++) {
			Entity entity = rawEntities.get(i);
			if (entity != null && entity.removed) {
				tickableEntities.remove(entity);
				renderableEntities.remove(entity);
				rawEntities.removeIndex(i);
			}
		}

		tickableEntities.clear();
		renderableEntities.clear();

		rawEntities.forEach(entity -> {
			if (entity != null && !entity.removed) {
				if (!tickableEntities.contains(entity) && entity.shouldTick()) {
					tickableEntities.add(entity);
				}
				if (!renderableEntities.contains(entity) && entity.shouldRender()) {
					renderableEntities.add(entity);
				}
			}
		});

		sortedRenderableEntities.clear();
		renderableEntities.forEach(sortedRenderableEntities::add);

		sortedRenderableEntities.sort((e1, e2) -> {
			boolean e1IsFurniture = e1 instanceof EntityFurniture;
			boolean e2IsFurniture = e2 instanceof EntityFurniture;

			if (e1IsFurniture != e2IsFurniture) {
				return e1IsFurniture ? -1 : 1;
			}

			if (e1IsFurniture && e2IsFurniture) {
				int layer1 = ((EntityFurniture) e1).getDrawLayer();
				int layer2 = ((EntityFurniture) e2).getDrawLayer();
				return Integer.compare(layer1, layer2);
			}

			boolean e1IsFX = e1 instanceof EntityFX;
			boolean e2IsFX = e2 instanceof EntityFX;

			if (e1IsFX != e2IsFX) {
				return e1IsFX ? -1 : 1;
			}

			return Double.compare(e2.y, e1.y);
		});

		for (Entity entity : tickableEntities) {
			if (entity != null && !entity.removed) {
				entity.tick();
			}
		}
		
		level.setVisibleMobs(renderableEntities);
	}

	public void draw(Graphics g) {
		if (!shouldWork) return;
		if (dirty) {
			rebuild();
			dirty = false;
		}

		for (Chunk chunk : chunks) {
			if (chunk.shouldRender()) {
				chunk.draw(g);
			}
		}

		for (Entity entity : sortedRenderableEntities) {
			entity.draw(g);
		}

		float color = level.getGlobalLight();
		g.setAmbientLight(color, color, color);
	}

	public void setDirty() {
		dirty = true;
	}
	
}
