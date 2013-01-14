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
package org.spout.vanilla.plugin.material.block.plant;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.plugin.inventory.Slot;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Plant;
import org.spout.vanilla.plugin.material.block.Spreading;
import org.spout.vanilla.plugin.material.block.attachable.GroundAttachable;
import org.spout.vanilla.plugin.material.item.misc.Dye;
import org.spout.vanilla.plugin.util.PlayerUtil;
import org.spout.vanilla.plugin.world.generator.normal.object.largeplant.HugeMushroomObject;
import org.spout.vanilla.plugin.world.generator.normal.object.largeplant.HugeMushroomObject.HugeMushroomType;
import org.spout.vanilla.plugin.world.generator.object.LargePlantObject;

public class Mushroom extends GroundAttachable implements Spreading, Plant, DynamicMaterial {
	private static final EffectRange MUSHROOM_RANGE = new CuboidEffectRange(-4, -1, -4, 4, 1, 4);
	private static final int MAX_PER_GROUP = 5;

	public Mushroom(String name, int id, String model) {
		super(name, id, model);
		this.setLiquidObstacle(false);
		this.setHardness(0.0F).setResistance(0.0F).setTransparent();
	}

	@Override
	public void onInteractBy(Entity entity, Block block, PlayerInteractEvent.Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type != PlayerInteractEvent.Action.RIGHT_CLICK) {
			return;
		}
		Slot inv = PlayerUtil.getHeldSlot(entity);
		if (inv != null && inv.get() != null && inv.get().isMaterial(Dye.BONE_MEAL)) {
			if (!PlayerUtil.isCostSuppressed(entity)) {
				inv.addAmount(-1);
			}
			final BlockMaterial mushroomType = block.getMaterial();
			final LargePlantObject mushroom;
			if (mushroomType == VanillaMaterials.RED_MUSHROOM) {
				mushroom = new HugeMushroomObject(HugeMushroomType.RED);
			} else {
				mushroom = new HugeMushroomObject(HugeMushroomType.BROWN);
			}
			final World world = block.getWorld();
			final int x = block.getX();
			final int y = block.getY();
			final int z = block.getZ();
			if (mushroom.canPlaceObject(world, x, y, z)) {
				mushroom.placeObject(world, x, y, z);
			}
		}
	}

	@Override
	public int getMinimumLightToSpread() {
		return 0;
	}

	@Override
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative) {
		if (super.isValidPosition(block, attachedFace, seekAlternative)) {
			final Block under = block.translate(BlockFace.BOTTOM);
			return under.isMaterial(VanillaMaterials.MYCELIUM, VanillaMaterials.FLOWER_POT_BLOCK) || block.getLight() <= 12 && under.getMaterial().isOpaque();
		}
		return false;
	}

	@Override
	public EffectRange getDynamicRange() {
		return MUSHROOM_RANGE;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		//TODO : delay before update
		b.dynamicUpdate(currentTime + getGrowthTime(b), true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		Random rand = new Random(block.getWorld().getAge());
		if (rand.nextInt(25) == 0) {
			// can we spread?
			int max = MAX_PER_GROUP;
			for (IntVector3 coord : MUSHROOM_RANGE) {
				if (block.translate(coord).isMaterial(this) && --max <= 0) {
					return;
				}
			}
			Cause<?> cause = toCause(block);
			// spread from the source (4 times)
			Block newShroom = null;
			for (int i = 0; i < 4; i++) {
				newShroom = block.translate(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
				if (newShroom.isMaterial(VanillaMaterials.AIR) && this.canCreate(newShroom, (short) 0, cause)) {
					block = newShroom;
				}
			}
			// try to place at last
			if (block.isMaterial(VanillaMaterials.AIR) && this.canCreate(block, (short) 0, cause)) {
				this.onCreate(block, (short) 0, cause);
			}
		}

		//TODO : delay before update
		block.dynamicUpdate(updateTime + getGrowthTime(block), true);
	}

	private long getGrowthTime(Block block) {
		return 120000L + new Random(block.getWorld().getAge()).nextInt(120000);
	}
}
