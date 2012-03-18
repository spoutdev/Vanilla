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
import org.spout.api.material.SubMaterial;
import org.spout.vanilla.entity.living.creature.passive.Sheep;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.material.generic.GenericItem;

public class Dye extends GenericItem implements SubMaterial {
	public final Dye INK_SAC;
	public final Dye ROSE_RED;
	public final Dye CACTUS_GREEN;
	public final Dye COCOA_BEANS;
	public final Dye LAPIS_LAZULI;
	public final Dye PURPLE;
	public final Dye CYAN;
	public final Dye LIGHT_GRAY;
	public final Dye GRAY;
	public final Dye PINK;
	public final Dye LIME;
	public final Dye DANDELION_YELLOW;
	public final Dye LIGHT_BLUE;
	public final Dye MAGENTA;
	public final  Dye ORANGE;
	public final Dye BONE_MEAL;
	
	private final Dye parent;
	private final Color color;
	
	public static enum Color implements DataSource {
		BLACK(0), RED(1), GREEN(2), BROWN(3), BLUE(4), PURPLE(5), CYAN(6),
		LIGHT_GRAY(7), GRAY(8), PINK(9), LIME(10), YELLOW(11), LIGHT_BLUE(12),
		MAGENTA(13), ORANGE(14), WHITE(15);
		
		private final short data;
		private Color(int data) {
			this.data = (short) data;
		}
		
		@Override
		public short getData() {
			return this.data;
		}
	}

	private Dye(String name, Color color, Dye parent) {
		super(name, 351);
		this.parent = parent;
		this.color = color;
		this.INK_SAC = parent.INK_SAC;
		this.ROSE_RED = parent.ROSE_RED;
		this.CACTUS_GREEN = parent.CACTUS_GREEN;
		this.COCOA_BEANS = parent.COCOA_BEANS;
		this.LAPIS_LAZULI = parent.LAPIS_LAZULI;
		this.PURPLE = parent.PURPLE;
		this.CYAN = parent.CYAN;
		this.LIGHT_GRAY = parent.LIGHT_GRAY;
		this.GRAY = parent.GRAY;
		this.PINK = parent.PINK;
		this.LIME = parent.LIME;
		this.DANDELION_YELLOW = parent.DANDELION_YELLOW;
		this.LIGHT_BLUE = parent.LIGHT_BLUE;
		this.MAGENTA = parent.MAGENTA;
		this.ORANGE = parent.ORANGE;
		this.BONE_MEAL = parent.BONE_MEAL;
		this.register();
		this.parent.registerSubMaterial(this);
	}

	public Dye(String name) {
		super(name, 351);
		this.parent = this;
		this.color = Color.BLACK;
		
		this.register();
		
		this.INK_SAC = new Dye("Ink Sac", Color.BLACK, this);
		this.ROSE_RED = new Dye("Rose Red", Color.RED, this);
		this.CACTUS_GREEN = new Dye("Cactus Green", Color.GREEN, this);
		this.COCOA_BEANS = new Dye("Cocoa Beans", Color.BROWN, this);
		this.LAPIS_LAZULI = new Dye("Lapis Lazuli", Color.BLUE, this);
		this.PURPLE = new Dye("Purple Dye", Color.PURPLE, this);
		this.CYAN = new Dye("Cyan Dye", Color.CYAN, this);
		this.LIGHT_GRAY = new Dye("Light Gray Dye", Color.LIGHT_GRAY, this);
		this.GRAY = new Dye("Gray Dye", Color.GRAY, this);
		this.PINK = new Dye("Pink Dye", Color.PINK, this);
		this.LIME = new Dye("Lime Dye", Color.LIME, this);
		this.DANDELION_YELLOW = new Dye("Dandelion Yellow", Color.YELLOW, this);
		this.LIGHT_BLUE = new Dye("Light Blue Dye", Color.LIGHT_BLUE, this);
		this.MAGENTA = new Dye("Magenta Dye", Color.MAGENTA, this);
		this.ORANGE = new Dye("Orange Dye", Color.ORANGE, this);
		this.BONE_MEAL = new Dye("Bone Meal", Color.WHITE, this);
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

	public Color getColor() {
		return this.color;
	}
	
	@Override
	public short getData() {
		return this.color.getData();
	}

	@Override
	public Dye getParentMaterial() {
		return this.parent;
	}
}
