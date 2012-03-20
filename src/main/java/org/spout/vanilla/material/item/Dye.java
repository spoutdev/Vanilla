/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.DataSource;
import org.spout.vanilla.entity.living.creature.passive.Sheep;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.generic.GenericItem;

public class Dye extends GenericItem {
	public static final Dye PARENT = new Dye("Ink Sac");
	public final Dye INK_SAC = new Dye("Ink Sac", DyeColor.BLACK, PARENT);
	public final Dye ROSE_RED = new Dye("Rose Red", DyeColor.RED, PARENT);
	public final Dye CACTUS_GREEN = new Dye("Cactus Green", DyeColor.GREEN, PARENT);
	public final Dye COCOA_BEANS = new Dye("Cocoa Beans", DyeColor.BROWN, PARENT);
	public final Dye LAPIS_LAZULI = new Dye("Lapis Lazuli", DyeColor.BLUE, PARENT);
	public final Dye PURPLE = new Dye("Purple Dye", DyeColor.PURPLE, PARENT);
	public final Dye CYAN = new Dye("Cyan Dye", DyeColor.CYAN, PARENT);
	public final Dye LIGHT_GRAY = new Dye("Light Gray Dye", DyeColor.LIGHT_GRAY, PARENT);
	public final Dye GRAY = new Dye("Gray Dye", DyeColor.GRAY, PARENT);
	public final Dye PINK = new Dye("Pink Dye", DyeColor.PINK, PARENT);
	public final Dye LIME = new Dye("Lime Dye", DyeColor.LIME, PARENT);
	public final Dye DANDELION_YELLOW = new Dye("Dandelion Yellow", DyeColor.YELLOW, PARENT);
	public final Dye LIGHT_BLUE = new Dye("Light Blue Dye", DyeColor.LIGHT_BLUE, PARENT);
	public final Dye MAGENTA = new Dye("Magenta Dye", DyeColor.MAGENTA, PARENT);
	public final Dye ORANGE = new Dye("Orange Dye", DyeColor.ORANGE, PARENT);
	public final Dye BONE_MEAL = new Dye("Bone Meal", DyeColor.WHITE, PARENT);

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

	public Dye(String name) {
		super(name, 351);
		this.color = DyeColor.BLACK;
	}

	private Dye(String name, DyeColor color, Dye parent) {
		super(name, 351, color.getData(), parent);
		this.color = color;
	}

	@Override
	public void onInteract(Entity entity, Entity other) {
		if (!(other.getController() instanceof Sheep)) {
			System.out.println("No sheep: " + other.getClass().getName() + " :(");
			return;
		}

		ItemStack holding = entity.getInventory().getCurrentItem();
		//get color from holding item
		other.setData("SheepColor", 0xF - holding.getData());
		System.out.println("Sheep go baaaa!");

		if (entity.getController() instanceof SurvivalPlayer) {
			if (holding.getAmount() > 1) {
				holding.setAmount(holding.getAmount() - 1);
				entity.getInventory().setItem(holding, entity.getInventory().getCurrentSlot());
			} else if (holding.getAmount() == 1) {
				entity.getInventory().setItem(null, entity.getInventory().getCurrentSlot());
			}
		}
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public short getData() {
		return this.color.getData();
	}
}
