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
package org.spout.vanilla.util.explosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spout.api.Source;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.VanillaMaterials;

public class ExplosionModelSpherical extends ExplosionModel implements Source {
	
	public ExplosionModelSpherical() {
		this.layers.add(new ExplosionLayer(this));
		this.root = this.layers.get(0).slotArray[0];
	}
	
	private List<ExplosionBlockSlot> blockList = new ArrayList<ExplosionBlockSlot>();
	private Map<Vector3, ExplosionBlockSlot> blocks = new HashMap<Vector3, ExplosionBlockSlot>();
	private List<ExplosionLayer> layers = new ArrayList<ExplosionLayer>();
	private ExplosionSlot root;
	
	public synchronized ExplosionBlockSlot getBlock(Vector3 position) {
		ExplosionBlockSlot block = this.blocks.get(position);
		if (block == null) {
			block = new ExplosionBlockSlot(position);
			this.blocks.put(position, block);
			this.blockList.add(block);
		}
		return block;
	}
	
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
		this.root.sourcedamage = (float) size * (0.7F + (float) Math.random() * 0.6F);

		//recursively operate on all blocks
		float damageFactor;
		boolean hasDamage = true;
		for (int i = 0; i < this.layers.size() && hasDamage; i++) {
			hasDamage = false;
			for (ExplosionSlot slot : this.layers.get(i).slotArray) {
				if (!slot.block.isSet) {
					//generate the info for this block
					slot.block.realx = (int) slot.block.pos.getX() + xoff;
					slot.block.realy = (int) slot.block.pos.getY() + yoff;
					slot.block.realz = (int) slot.block.pos.getZ() + zoff;
					slot.block.material = world.getBlockMaterial(slot.block.realx, slot.block.realy, slot.block.realz);
					if (slot.block.material != VanillaMaterials.AIR) {
						slot.block.damageFactor = (slot.block.material.getHardness() + 0.3F) * 0.3F;
						slot.block.damageFactor *= (2.0F + (float) Math.random()) / 3.0F;
					} else {
						slot.block.damageFactor = 0;
					}
					slot.block.isSet = true;
				}

				//subtract damage factor
				damageFactor = slot.sourcedamage - slot.block.damageFactor;
				slot.sourcedamage = 0;
				if (damageFactor <= 0) continue;	
				if (!slot.block.destroy) {
					slot.block.destroy = true;
					Block block = world.getBlock(slot.block.realx, slot.block.realy, slot.block.realz);
					blocksToDestroy.add(block);
				}

				//one block layer further...
				if ((damageFactor -= 0.225) <= 0.0F) continue;

				//force a new layer if needed
				if (slot.next == null) {
					//create a new layer
					ExplosionLayer nextLayer = new ExplosionLayer(this.getLastLayer());
					//finalize the 'next' of the current layer to lead to the new layer
					for (ExplosionSlot slot2 : this.getLastLayer().slots.values()) {
						slot2.end();
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
