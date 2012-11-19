/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.item;

import org.spout.api.material.Material;
import org.spout.api.math.Rectangle;
import org.spout.api.render.RenderMaterial;

import org.spout.vanilla.data.RenderMaterials;
import org.spout.vanilla.material.VanillaMaterial;

public class VanillaItemMaterial extends Material implements VanillaMaterial {
	private final int minecraftId;
	private final Rectangle source;
	private int meleeDamage = 1;

	public VanillaItemMaterial(String name, int id, Rectangle source) {
		super(name);
		this.minecraftId = id;
		this.source = source;
	}

	public VanillaItemMaterial(short dataMask, String name, int id, Rectangle source) {
		super(dataMask, name);
		this.minecraftId = id;
		this.source = source;
	}

	public VanillaItemMaterial(String name, int id, int data, Material parent, Rectangle source) {
		super(name, data, parent);
		this.minecraftId = id;
		this.source = source;
	}

	@Override
	public final int getMinecraftId() {
		return minecraftId;
	}

	@Override
	public short getMinecraftData(short data) {
		return data;
	}

	@Override
	public RenderMaterial getRenderMaterial() {
		return RenderMaterials.ITEMS_MATERIAL;
	}

	@Override
	public Rectangle getSource() {
		return source;
	}

	public boolean hasNBTData() {
		return false;
	}

	@Override
	public int getDamage() {
		return this.meleeDamage;
	}

	@Override
	public VanillaItemMaterial setDamage(int damage) {
		this.meleeDamage = damage;
		return this;
	}
}
