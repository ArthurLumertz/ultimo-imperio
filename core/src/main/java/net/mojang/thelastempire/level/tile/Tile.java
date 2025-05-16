package net.mojang.thelastempire.level.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mojang.thelastempire.engine.Graphics;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.Material;
import net.mojang.thelastempire.level.phys.AABB;

public class Tile {

	public static final Tile[] tiles = new Tile[16];
	public static final Tile grass = new GrassTile(1, Material.earth).setSoundType(SoundType.EARTH).setName("Grass")
			.setIcon(6, 0);
	public static final Tile stone = new StoneTile(2, Material.rock).setSoundType(SoundType.ROCK).setName("Stone")
			.setIcon(2, 14);
	public static final Tile dirt = new DirtTile(3, Material.earth).setSoundType(SoundType.EARTH).setName("Dirt")
			.setIcon(6, 1);
	public static final Tile tree = new TreeTile(4, Material.woodSolid).setSoundType(SoundType.WOOD).setName("Tree");
	public static final Tile woodFloor = new WoodTile(5, Material.wood).setSoundType(SoundType.WOOD).setName("Wood")
			.setIcon(7, 1);
	public static final Tile border = new BorderTile(6, Material.rockSolid).setName("Border");
	public static final Tile woodFence = new FenceTile(7, Material.woodSolid).setName("Wooden Fence");
	public static final Tile stoneFloor = new Tile(8, Material.rock).setName("Stone Tile").setIcon(8, 1);

	protected byte id;
	private String name;
	private TextureRegion texture;
	private Material material;
	private SoundType soundType;

	protected Tile(int id, Material material) {
		tiles[id] = this;
		this.id = (byte) id;
		this.material = material;
	}

	protected Tile setSoundType(SoundType soundType) {
		this.soundType = soundType;
		return this;
	}

	public SoundType getSoundType() {
		return soundType;
	}

	protected Tile setName(String name) {
		this.name = name;
		return this;
	}

	protected Tile setIcon(int tx, int ty) {
		this.texture = new TextureRegion(Graphics.instance.getTile(tx, ty));
		return this;
	}

	public void onGenerate(Level level, int x, int y) {
	}

	public void draw(Graphics g, int x, int y, Level level) {
		TextureRegion texture = getDynamicTexture(g, x, y, level);
		g.drawTexture(texture, x, y, 1f, 1f);
	}

	protected boolean isNearbyTile(int tileId, int x, int y, Level level) {
		return level.getTile(x - 1, y) == tileId || level.getTile(x + 1, y) == tileId
				|| level.getTile(x, y - 1) == tileId || level.getTile(x, y + 1) == tileId;
	}

	public byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public TextureRegion getDynamicTexture(Graphics g, int x, int y, Level level) {
		return getTexture();
	}

	public AABB getAABB(int x, int y, Level level) {
		return AABB.newTemp(x, y, x + 1f, y + 1f);
	}

	public Material getMaterial() {
		return material;
	}

	protected TextureRegion getConnectedTextures(Level level, int x, int y, int xs, int ys, int tileId,
			TextureRegion texture, Graphics g) {
		boolean north = level.getTile(x, y + 1) == tileId;
		boolean south = level.getTile(x, y - 1) == tileId;
		boolean west = level.getTile(x - 1, y) == tileId;
		boolean east = level.getTile(x + 1, y) == tileId;

		if (north && west && south && east) {
			texture = g.getTile(xs, ys);
		} else if (north && south && west) {
			texture = g.getTile(xs + 1, ys);
		} else if (north && south && !east && !west) {
			texture = g.getTile(xs + 2, ys);
		} else if (north && south && east) {
			texture = g.getTile(xs + 3, ys);
		} else if (north && east && west) {
			texture = g.getTile(xs, ys + 1);
		} else if (!north && !south && east && west) {
			texture = g.getTile(xs, ys + 2);
		} else if (south && east && west) {
			texture = g.getTile(xs, ys + 3);
		} else if (north && west) {
			texture = g.getTile(xs + 1, ys + 1);
		} else if (north && !east && !west) {
			texture = g.getTile(xs + 2, ys + 1);
		} else if (north && east) {
			texture = g.getTile(xs + 2, ys + 1);
		} else if (west && !south) {
			texture = g.getTile(xs + 1, ys + 2);
		} else if (west && south) {
			texture = g.getTile(xs + 1, ys + 3);
		} else if (east && !south) {
			texture = g.getTile(xs + 3, ys + 2);
		} else if (east && south) {
			texture = g.getTile(xs + 3, ys + 3);
		} else if (south) {
			texture = g.getTile(xs + 2, ys + 3);
		}

		return texture;
	}

	public static enum SoundType {
		WOOD("step_wood"), ROCK("step_stone"), EARTH("step_earth");

		private String sound;

		SoundType(String sound) {
			this.sound = sound;
		}

		public String getStepSound() {
			return sound;
		}
	}

}
