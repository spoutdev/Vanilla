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
package org.spout.vanilla.plugin.util.explosion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.effect.store.GeneralEffects;
import org.spout.vanilla.plugin.material.VanillaBlockMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.util.MathHelper;

public abstract class ExplosionModel {
	private List<ExplosionBlockSlot> blockList = new ArrayList<ExplosionBlockSlot>();
	private Map<Vector3, ExplosionBlockSlot> blocks = new HashMap<Vector3, ExplosionBlockSlot>();
	public List<Block> blocksToDestroy = new ArrayList<Block>(100);

	public synchronized ExplosionBlockSlot getBlock(Vector3 position) {
		ExplosionBlockSlot block = this.blocks.get(position);
		if (block == null) {
			block = new ExplosionBlockSlot(position);
			this.blocks.put(position, block);
			this.blockList.add(block);
		}
		return block;
	}

	public synchronized List<ExplosionBlockSlot> getBlocks() {
		return this.blockList;
	}

	/**
	 * Calculated with the following:
	 *
	 * o: Origin of explosion
	 * p: Location of entity for impact to be calculated for
	 * s: The damage radius
	 * di: distance from origin
	 * de: density of non-air blocks from the position to the origin
	 * i: impact of explosion
	 *
	 * 1. Calculate impact with <code>i = (1 - di / size) * de</code>
	 * 2. Return <code>(int) ((i * i + i) / 2 * 8 * size + 1)</code>
	 *
	 * Example:
	 * M
	 *
	 * @param o
	 * @param p
	 * @param s
	 * @return
	 */
	private int getDamage(Point o, Point p, double s) {
		double di = p.distance(o);
	 	double de = MathHelper.getBlockDensity(); // TODO: Implement
		double i = (1 - di / s) * de;
		return (int) ((i * i + i) / 2 * 8 * s + 1);
	}

	public synchronized void execute(Point position, float size, boolean fire, boolean damage, boolean ignoreWater, Cause<?> cause) {
		//reset all blocks for the next explosion
		for (ExplosionBlockSlot block : this.blockList) {
			block.isSet = false;
		}

		//TODO: Block Event?

		// See if it's touching water
		boolean breakBlocks = true;
		if (!ignoreWater) {
			Block block = position.getBlock();
			for (BlockFace face : BlockFaces.NESWBT) {
				if (block.translate(face).isMaterial(VanillaMaterials.WATER)) {
					breakBlocks = false;
					break;
				}
			}

		}

		// perform block changes
		if (breakBlocks) {
			BlockMaterial material;
			for (Block block : this.blocksToDestroy) {
				material = block.getMaterial();

				if (material == VanillaMaterials.AIR) {
					if (fire) {
						BlockMaterial below = block.translate(BlockFace.BOTTOM).getMaterial();
						if (below.isSolid() && GenericMath.getRandom().nextInt(3) == 0) {
							block.setMaterial(VanillaMaterials.FIRE);
						}
					}
				} else if (material != VanillaMaterials.FIRE) {
					//TODO: Item dropping yield?
					if (material instanceof VanillaBlockMaterial) {
						((VanillaBlockMaterial) material).onIgnite(block, cause);
					} else {
						material.destroy(block, cause);
					}
					block.setMaterial(VanillaMaterials.AIR);
				}
			}
		}

		// Damage entities within radius
		if (damage) {
			size *= 2;
			for (Entity entity : position.getWorld().getNearbyEntities(position, (int) size)) {
				// Check if entity can be damaged
				HealthComponent health = entity.get(HealthComponent.class);
				if (health == null) {
					continue;
				}
				health.damage(getDamage(position, entity.getTransform().getPosition(), size));
			}
		}

		//explosion packet (TODO: Limit the amount sent per tick? Don't want to lag-out clients!)
		GeneralEffects.EXPLOSION.playGlobal(position, size);
	}

	public synchronized void execute(Point pos, float size, boolean fire, boolean damage, Cause<?> cause) {
		execute(pos, size, fire, damage, false, cause);
	}

	public synchronized void execute(Point pos, float size, boolean fire, Cause<?> cause) {
		execute(pos, size, fire, true, cause);
	}

	public synchronized void execute(Point pos, float size, Cause<?> cause) {
		execute(pos, size, false, cause);
	}
}
