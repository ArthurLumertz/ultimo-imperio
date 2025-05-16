package net.mojang.thelastempire.level.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;

import net.mojang.thelastempire.engine.Resources;
import net.mojang.thelastempire.level.Level;
import net.mojang.thelastempire.level.entity.player.Player;

public class Item {

	public static final Item[] items = new Item[8];
	public static final Item fist = new Item(0, "Fist").setTexture(0, 0).setAttackDamage(1);
	public static final Item gun = new GunItem(1, "Pistol").setHandCombat(false).setTexture(1, 0).setAttackDamage(8);
	public static final Item longSword = new Item(2, "Long Sword").setTexture(2, 0).setAttackDamage(4);
	
	private byte id;
	private TextureRegion texture;
	private String name;
	private int damage;
	private boolean handCombat = true;
	
	public Item(int id, String name) {
		items[id] = this;
		this.id = (byte) id;
		this.name = name;
	}
	
	public void onUse(Player player, Level level) {}
	
	private Item setAttackDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public int getAttackDamage() {
		return damage;
	}
	
	protected Item setHandCombat(boolean handCombat) {
		this.handCombat = handCombat;
		return this;
	}
	
	public boolean usesHandCombat() {
		return handCombat;
	}
	
	private Item setTexture(int x, int y) {
		texture = new TextureRegion(Resources.getTexture("icons"), x * 16, y * 16, 16, 16);
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	public byte getId() {
		return id;
	}
	
	public static Item createItem(JsonValue itemData) {
		if (itemData == null) {
			return null;
		}
		
		String name = itemData.toString();
		for (int i = 0; i < items.length; i++) {
			if (name.equals(items[i].getName())) {
				return items[i];
			}
		}
		return null;
	}
	
}
