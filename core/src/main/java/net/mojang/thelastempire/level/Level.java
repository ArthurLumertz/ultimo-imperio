package net.mojang.thelastempire.level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import net.mojang.thelastempire.TheLastEmpire;
import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.entity.Entity;
import net.mojang.thelastempire.level.entity.Mob;
import net.mojang.thelastempire.level.entity.player.Player;
import net.mojang.thelastempire.level.phys.AABB;
import net.mojang.thelastempire.level.tile.Tile;

public class Level {

	private final TheLastEmpire tle;

	private int width;
	private int height;
	private byte[] blocks;
	private byte[] data;

	private Array<AABB> aABBs = new Array<>();
	private Array<Entity> entities = new Array<>();

	private Player player;

	private LevelParser levelParser = new LevelParser(this);
	private LevelRenderer levelRenderer;

	private ObjectSet<Mob> visibleMobs = new ObjectSet<>();
	private ObjectSet<Mob> visibleMobsWithPlayer = new ObjectSet<>();

	private float globalLight;

	public Level(TheLastEmpire tle) {
		this.tle = tle;
	}

	public void addLevelRenderer(LevelRenderer levelRenderer) {
		this.levelRenderer = levelRenderer;
	}

	public void deleteLevel() {
		levelRenderer.unload();
		levelParser.stopMusic();
	}

	protected void createLevel(int width, int height) {
		this.width = width;
		this.height = height;
		blocks = new byte[width * height];
		data = new byte[width * height];
	}

	public void loadLevel(String levelName, boolean fade, boolean sound, String postCard) {
		aABBs.clear();
		entities.clear();
		player = null;
		tle.setGuiScreen(null, fade);

		levelParser.loadLevel(levelName);
		if (levelRenderer != null) {
			levelRenderer.setDirty();
		}

		if (sound)
			tle.playSound("open_door", 0.6f, 1f);
		if (fade)
			tle.getSceneManager().startFade();

		System.gc();
	}

	public void loadLevel(String levelName, boolean fade, boolean sound) {
		loadLevel(levelName, fade, sound, null);
	}

	public void reloadLevel() {
		loadLevel(levelParser.getCurrentLevel(), false, false, "");
	}

	public void addEntity(Entity entity) {
		if ("Player".equals(entity.getName())) {
			player = (Player) entity;
		}
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		entities.removeValue(entity, true);
	}

	public int getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return 0;
		}
		return blocks[x + y * width];
	}

	public void setTile(int x, int y, int id) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return;
		}
		if (id > 0) {
			Tile.tiles[id].onGenerate(this, x, y);
		}
		blocks[x + y * width] = (byte) id;
	}

	public boolean isTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		}
		return blocks[x + y * width] > 0;
	}

	public void setData(int x, int y, int value) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return;
		}
		data[x + y * width] = (byte) (value & 0xFF);
	}

	public int getData(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return 0;
		}
		return data[x + y * width];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Array<AABB> getCubes(AABB aABB) {
		aABBs.clear();

		int x0 = (int) aABB.x0;
		int x1 = (int) (aABB.x1 + 1);
		int y0 = (int) aABB.y0;
		int y1 = (int) (aABB.y1 + 1);

		x0 = Math.max(x0, 0);
		y0 = Math.max(y0, 0);
		x1 = Math.min(x1, width);
		y1 = Math.min(y1, height);

		for (Entity entity : entities) {
			if (entity != null) {
				if (entity.boundingBox != null && aABB.intersects(entity.boundingBox)) {
					if (Graphics.instance.inViewport(entity.boundingBox)) {
						if (!entity.noPhysics)
							aABBs.add(entity.boundingBox);
					}
				}
			}
		}

		if (aABB.intersects(player.boundingBox))
			aABBs.add(player.boundingBox);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				int tileId = getTile(x, y);
				if (tileId > 0) {
					Tile tile = Tile.tiles[tileId];
					if (tile.getMaterial().isSolid()) {
						aABBs.add(tile.getAABB(x, y, this));
					}
				}
			}
		}

		aABBs.add(AABB.newTemp(0f, -1f, 0f, height));
		aABBs.add(AABB.newTemp(0f, -1f, width, -1f + 1f));
		aABBs.add(AABB.newTemp(width, 0f, width, height));
		aABBs.add(AABB.newTemp(0f, height - 1.14f, width, height));

		return aABBs;
	}

	public Array<Entity> getEntities() {
		return entities;
	}

	public Entity getEntity(int i) {
		return entities.get(i);
	}

	public Player getPlayer() {
		return player;
	}

	public void setGlobalLight(float globalLight) {
		this.globalLight = globalLight;
	}

	public float getGlobalLight() {
		return globalLight;
	}

	public void setVisibleMobs(ObjectSet<Entity> entities) {
		visibleMobs.clear();
		visibleMobsWithPlayer.clear();

		for (Entity entity : entities.iterator()) {
			if (entity != null && entity instanceof Mob) {
				if (!(entity instanceof Player)) {
					visibleMobs.add((Mob) entity);
				}
				visibleMobsWithPlayer.add((Mob) entity);
			}
		}

	}

	public ObjectSet<Mob> getVisibleMobs(boolean withPlayer) {
		if (withPlayer) {
			return visibleMobsWithPlayer;
		}
		return visibleMobs;
	}

	public boolean isCollideable(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		}
		Tile tile = Tile.tiles[blocks[x + y * width]];
		return tile != null && tile.getMaterial().isSolid();
	}

}
