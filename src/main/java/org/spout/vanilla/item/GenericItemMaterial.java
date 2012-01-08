package org.spout.vanilla.item;

import org.getspout.api.material.ItemMaterial;

public class GenericItemMaterial implements ItemMaterial {

	private final short id;
	private final short data;
	private final boolean subtypes;
	private final String name;
	private String displayName;

	public GenericItemMaterial(String name, int id, int data, boolean subtypes) {
		this.name = name;
		this.displayName = name;
		this.id = (short) id;
		this.data = (short) data;
		this.subtypes = subtypes;
	}

	protected GenericItemMaterial(String name, int id, int data) {
		this(name, id, data, false);
	}

	public GenericItemMaterial(String name, int id) {
		this(name, id, 0, false);
	}

	public short getId() {
		return id;
	}

	public short getData() {
		return data;
	}

	public boolean hasSubtypes() {
		return subtypes;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
