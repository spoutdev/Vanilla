/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.material.item.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.Placeable;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;
import org.spout.api.math.Vector3;

import org.spout.api.inventory.Slot;

import org.spout.vanilla.plugin.component.living.passive.Sheep;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.solid.Wool.WoolColor;
import org.spout.vanilla.plugin.material.item.VanillaItemMaterial;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class Dye extends VanillaItemMaterial implements Placeable {
	public static final Dye INK_SAC = new Dye("Ink Sac");
	public static final Dye ROSE_RED = new Dye("Rose Red", DyeColor.RED, INK_SAC);
	public static final Dye CACTUS_GREEN = new Dye("Cactus Green", DyeColor.GREEN, INK_SAC);
	public static final Dye COCOA_BEANS = new Dye("Cocoa Beans", DyeColor.BROWN, INK_SAC);
	public static final Dye LAPIS_LAZULI = new Dye("Lapis Lazuli", DyeColor.BLUE, INK_SAC);
	public static final Dye PURPLE = new Dye("Purple Dye", DyeColor.PURPLE, INK_SAC);
	public static final Dye CYAN = new Dye("Cyan Dye", DyeColor.CYAN, INK_SAC);
	public static final Dye LIGHT_GRAY = new Dye("Light Gray Dye", DyeColor.LIGHT_GRAY, INK_SAC);
	public static final Dye GRAY = new Dye("Gray Dye", DyeColor.GRAY, INK_SAC);
	public static final Dye PINK = new Dye("Pink Dye", DyeColor.PINK, INK_SAC);
	public static final Dye LIME = new Dye("Lime Dye", DyeColor.LIME, INK_SAC);
	public static final Dye DANDELION_YELLOW = new Dye("Dandelion Yellow", DyeColor.YELLOW, INK_SAC);
	public static final Dye LIGHT_BLUE = new Dye("Light Blue Dye", DyeColor.LIGHT_BLUE, INK_SAC);
	public static final Dye MAGENTA = new Dye("Magenta Dye", DyeColor.MAGENTA, INK_SAC);
	public static final Dye ORANGE = new Dye("Orange Dye", DyeColor.ORANGE, INK_SAC);
	public static final Dye BONE_MEAL = new Dye("Bone Meal", DyeColor.WHITE, INK_SAC);
	private final DyeColor color;

	public static enum DyeColor implements DataSource {
		BLACK(0),
		RED(1),
		GREEN(2),
		BROWN(3),
		BLUE(4),
		PURPLE(5),
		CYAN(6),
		LIGHT_GRAY(7),
		GRAY(8),
		PINK(9),
		LIME(10),
		YELLOW(11),
		LIGHT_BLUE(12),
		MAGENTA(13),
		ORANGE(14),
		WHITE(15);
		private final short data;

		private DyeColor(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return this.data;
		}
	}

	private Dye(String name) {
		super((short) 0x0F, name, 351, null);
		this.color = DyeColor.BLACK;
	}

	private Dye(String name, DyeColor color, Dye parent) {
		super(name, 351, color.getData(), parent, null);
		this.color = color;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		return this == COCOA_BEANS && VanillaMaterials.COCOA_PLANT.canPlace(block, data, against, clickedPos, isClickedBlock, cause);
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		if (this == COCOA_BEANS) {
			VanillaMaterials.COCOA_PLANT.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		}
	}

	@Override
	public void onInteract(Entity entity, Entity other, Action action) {
		if (action == Action.RIGHT_CLICK) {
			Sheep sheep = other.get(Sheep.class);
			if (sheep == null) {
				return;
			}

			Slot inv = PlayerUtil.getHeldSlot(entity);
			if (inv != null && inv.get() != null) {
				// get color from holding item
				sheep.setColor(WoolColor.getById((short) (0xF - inv.get().getData())));
				if (!PlayerUtil.isCostSuppressed(entity)) {
					inv.addAmount(-1);
				}
			}
		}
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public Dye getParentMaterial() {
		return (Dye) super.getParentMaterial();
	}
}
