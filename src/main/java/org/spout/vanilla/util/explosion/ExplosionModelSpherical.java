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
package org.spout.vanilla.util.explosion;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

public class ExplosionModelSpherical extends ExplosionModel implements Source {
	public ExplosionModelSpherical() {
		this.layers.add(new ExplosionLayer(this));
		this.root = this.layers.get(0).slots[0];
	}

	private List<ExplosionLayer> layers = new ArrayList<ExplosionLayer>();
	private ExplosionSlot root;

	public synchronized ExplosionLayer getLastLayer() {
		return this.layers.get(this.layers.size() - 1);
	}

	public synchronized ExplosionLayer getLayer(int index) {
		return this.layers.get(index);
	}

	public synchronized void execute(Point position, float size) {
		this.execute(position, size, this);
	}

	public synchronized void execute(Point position, float size, Source source) {
		this.execute(position, size, false, source);
	}

	public synchronized void execute(Point position, boolean fire, float size) {
		this.execute(position, size, fire, this);
	}

	public synchronized void execute(Point position, float size, boolean fire, Source source) {
		int xoff = position.getBlockX();
		int yoff = position.getBlockY();
		int zoff = position.getBlockZ();
		World world = position.getWorld();

		this.blocksToDestroy.clear();
		this.root.sourcedamage = size * (0.7F + (float) Math.random() * 0.6F);

		//recursively operate on all blocks
		float damageFactor;
		boolean hasDamage = true;
		ExplosionLayer layer;
		for (int i = 0; i < this.layers.size() && hasDamage; i++) {
			hasDamage = false;
			layer = this.layers.get(i);
			for (ExplosionSlot slot : layer.slots) {
				//prepare the block information
				if (!slot.block.isSet) {
					slot.block.prepare(world, xoff, yoff, zoff, this.random);
				}

				//subtract damage factor
				damageFactor = slot.sourcedamage - slot.block.damageFactor;
				slot.sourcedamage = 0f;
				if (damageFactor <= 0f) {
					continue;
				}

				//this block has been destroyed
				if (!slot.block.destroy) {
					slot.block.destroy = true;
					Block block = world.getBlock(slot.block.realx, slot.block.realy, slot.block.realz);
					blocksToDestroy.add(block);
				}

				//one block layer further...
				if ((damageFactor -= 0.225f) <= 0.0f) {
					continue;
				}

				//create a new layer if needed
				if (slot.next == null) {
					//create a new layer
					ExplosionLayer nextLayer = new ExplosionLayer(layer);
					//finalize the 'next' of the current layer to lead to the new layer
					for (ExplosionSlot slot2 : layer.slots) {
						slot2.finish();
					}
					this.layers.add(nextLayer);
				}

				//set source damage of next slots
				for (ExplosionSlot slot2 : slot.next) {
					if (damageFactor > slot2.sourcedamage) {
						slot2.sourcedamage = damageFactor;
					}
				}

				hasDamage = true;
			}
		}

		//perform the final block changes, entity damage and explosion messages
		super.execute(position, size, fire, source);
	}
}
