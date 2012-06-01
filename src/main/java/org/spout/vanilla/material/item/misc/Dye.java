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
package org.spout.vanilla.material.item.misc;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.material.block.solid.Wool.WoolColor;
import org.spout.vanilla.material.item.VanillaItemMaterial;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Dye extends VanillaItemMaterial {
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
		super(name, 351);
		this.color = DyeColor.BLACK;
	}

	private Dye(String name, DyeColor color, Dye parent) {
		super(name, 351, color.getData(), parent);
		this.color = color;
	}

	@Override
	public void onInteract(Entity entity, Entity other, Action action) {
		if (action == Action.RIGHT_CLICK) {
			if (!(other.getController() instanceof Sheep)) {
				return;
			}

			InventoryBase inv = VanillaPlayerUtil.getInventory(entity);
			if (inv != null) {
				ItemStack holding = inv.getCurrentItem();
				if (holding != null) {
					//get color from holding item
					((Sheep) other.getController()).setColor(WoolColor.getById((short) (0xF - holding.getData())));

					if (VanillaPlayerUtil.isSurvival(entity)) {
						inv.addCurrentItemAmount(-1);
					}
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
