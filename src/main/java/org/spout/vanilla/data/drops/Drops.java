/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.data.drops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.data.drops.type.FixedDrop;
import org.spout.vanilla.data.drops.type.RandomDrop;
import org.spout.vanilla.data.drops.type.RandomRangeDrop;

/**
 * Contains multiple drops
 */
public class Drops extends Drop {
	private final List<Drop> drops = new ArrayList<Drop>();

	@Override
	public List<ItemStack> getDrops(Random random, Set<Flag> flags, List<ItemStack> drops) {
		if (this.canDrop(random, flags) && !this.drops.isEmpty()) {
			for (Drop drop : this.drops) {
				drops = drop.getDrops(random, flags, drops);
			}
		}
		return drops;
	}

	@Override
	public boolean containsDrop(Material material) {
		for (Drop drop : drops) {
			if (drop.containsDrop(material)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the given Drop Material from these drops
	 *
	 * @param dropMaterial to remove
	 * @return this
	 */
	public Drops remove(Material dropMaterial) {
		Iterator<Drop> iter = this.drops.iterator();
		while (iter.hasNext()) {
			Drop next = iter.next();
			if (next instanceof Drops) {
				((Drops) next).remove(dropMaterial);
			} else if (next.containsDrop(dropMaterial)) {
				iter.remove();
			}
		}
		return this;
	}

	/**
	 * Adds a drop to these drops
	 *
	 * @param drop to add
	 * @return the drop parameter
	 */
	public <T extends Drop> T add(T drop) {
		this.drops.add(drop);
		return drop;
	}

	/**
	 * Gets whether these Drops contains other drops or not
	 *
	 * @return True if empty, False if not
	 */
	public boolean isEmpty() {
		return this.drops.isEmpty();
	}

	/**
	 * Gets the amount of drops
	 *
	 * @return Drop count
	 */
	public int getDropCount() {
		return this.drops.size();
	}

	/**
	 * Gets a single set drop from these Drops
	 *
	 * @param index to get at
	 * @return drop
	 */
	public Drop getDrop(int index) {
		return this.drops.get(index);
	}

	/**
	 * Removes all the Drops set
	 *
	 * @return these Drops
	 */
	public Drops clear() {
		this.drops.clear();
		return this;
	}

	/**
	 * Gets the all the Drops contained
	 *
	 * @return unsafe List of drops
	 */
	public List<Drop> getAll() {
		return this.drops;
	}

	@Override
	public Drops setChance(double chance) {
		super.setChance(chance);
		return this;
	}

	@Override
	public Drops addFlags(Flag... dropFlags) {
		super.addFlags(dropFlags);
		return this;
	}

	public SwitchDrops addSwitch(Flag... dropFlags) {
		return this.add(new SwitchDrops()).addFlags(dropFlags);
	}

	public Drops forFlags(Flag... flags) {
		return add(new Drops().addFlags(flags));
	}

	public SelectedDrops addSelect(Flag... flags) {
		return add(new SelectedDrops().addFlags(flags));
	}

	public RandomDrop add(Material dropMaterial, int... amount) {
		return add(new RandomDrop(dropMaterial, amount));
	}

	public FixedDrop add(Material dropMaterial) {
		return add(new FixedDrop(dropMaterial, 1));
	}

	public RandomRangeDrop addRange(Material dropMaterial, int max) {
		return add(new RandomRangeDrop(dropMaterial, 0, max));
	}

	public RandomRangeDrop addRange(Material dropMaterial, int min, int max) {
		return add(new RandomRangeDrop(dropMaterial, min, max));
	}
}
