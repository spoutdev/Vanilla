package org.getspout.vanilla.block;

import org.getspout.api.material.BlockMaterial;
import org.getspout.api.render.BlockDesign;
import org.getspout.vanilla.item.GenericItemMaterial;

public class GenericBlockMaterial extends GenericItemMaterial implements BlockMaterial {

	private BlockDesign design;

	private GenericBlockMaterial(String name, int id, int data, boolean subtypes) {
		super(name, id, data, subtypes);
	}

	protected GenericBlockMaterial(String name, int id, int data) {
		this(name, id, data, true);
	}

	protected GenericBlockMaterial(String name, int id) {
		this(name, id, 0, false);
	}

	public float getFriction() {
		return 0;
	}

	public BlockMaterial setFriction(float friction) {
		return this;
	}

	public float getHardness() {
		return 0;
	}

	public BlockMaterial setHardness(float hardness) {
		return this;
	}

	public boolean isOpaque() {
		return false;
	}

	public BlockMaterial setOpaque(boolean opaque) {
		return this;
	}

	public int getLightLevel() {
		return 0;
	}

	public BlockMaterial setLightLevel(int level) {
		return this;
	}

	public BlockDesign getBlockDesign() {
		return design;
	}

	public BlockMaterial setBlockDesign(BlockDesign design) {
		this.design = design;
		return this;
	}
}
