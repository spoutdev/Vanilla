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
package org.spout.vanilla.inventory.window.gui;

import org.spout.api.gui.render.RenderPart;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Rectangle;
import org.spout.api.math.Vector2;

import org.spout.vanilla.data.VanillaRenderMaterials;
import org.spout.vanilla.material.VanillaMaterial;

public class RenderItemStack extends RenderPart {
	private static final float SCALE = 0.75f;
	public static final float WIDTH = 0.0625f;
	public static final float HEIGHT = WIDTH;
	public static final Vector2 SOURCE_EXTENTS = new Vector2(WIDTH, HEIGHT);
	public static final Vector2 SPRITE_EXTENTS = new Vector2(HEIGHT * 2 * SCALE, HEIGHT * 2);
	private final ItemStack item;
	private Vector2 pos = Vector2.ZERO;

	public RenderItemStack(ItemStack item) {
		if (!(item.getMaterial() instanceof VanillaMaterial)) {
			throw new IllegalArgumentException("ItemStack must have a VanillaMaterial for a Material.");
		}
		this.item = item;
		setRenderMaterial(VanillaRenderMaterials.ITEMS_MATERIAL);
		setSource(new Rectangle(((VanillaMaterial) item.getMaterial()).getPosition(), SOURCE_EXTENTS));
	}

	public ItemStack getItem() {
		return item;
	}

	public Vector2 getPosition() {
		return pos;
	}

	public void setPosition(Vector2 pos) {
		setSprite(new Rectangle(pos, SPRITE_EXTENTS));
		this.pos = pos;
	}
}
