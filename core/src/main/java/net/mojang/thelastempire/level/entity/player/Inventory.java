package net.mojang.thelastempire.level.entity.player;

import net.mojang.thelastempire.level.item.Item;

public class Inventory {

	private Item[] items;
	private byte selected = 0;
	
	public Inventory(int size) {
		items = new Item[size];
	}

	public void addItem(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}

	public Item getItem(int idx) {
		if (idx < 0 || idx >= items.length) {
			return null;
		}
		return items[idx];
	}

	public int getCapacity() {
		return items.length;
	}

	public Item[] getItems() {
		return items;
	}

	public void addAll(Item[] items) {
		int length = Math.min(this.items.length, items.length);
		for (int i = 0; i < length; i++) {
			this.items[i] = items[i];
		}
		for (int i = length; i < this.items.length; i++) {
			this.items[i] = null;
		}
	}
	
	public void setSelected(int selected) {
		this.selected = (byte)selected;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public Item getSelectedItem() {
		return getItem(selected);
	}

	public void cycleSelected(int i) {
		selected += i;
		if (selected < 0) {
			selected = (byte) (items.length - 1);
		} else if (selected >= items.length - 1) {
			selected = 0;
		}
	}
	
	public boolean isSelected(int idx) {
		return selected == (byte) idx;
	}
	
}
