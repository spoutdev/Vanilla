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
package org.spout.vanilla.material.block.solid;

import java.util.Random;

import org.spout.api.Platform;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.SpreadingSolid;
import org.spout.vanilla.material.block.misc.Snow;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.render.VanillaEffects;
import org.spout.vanilla.util.PlayerUtil;

public class Grass extends SpreadingSolid implements DynamicMaterial, InitializableMaterial {
	public Grass(String name, int id) {
		super(name, id, VanillaMaterialModels.GRASS);
		this.setHardness(0.6F).setResistance(0.8F).setStepSound(SoundEffects.STEP_GRASS);
		this.addMiningType(ToolType.SPADE);
		if (VanillaPlugin.getInstance().getEngine().getPlatform() == Platform.CLIENT) {
			if (!getModel().getRenderMaterial().getBufferEffects().contains(VanillaEffects.BIOME_GRASS_COLOR)) {
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.BIOME_GRASS_COLOR);
			}
		}
	}

	@Override
	public void initialize() {
		this.setReplacedMaterial(VanillaMaterials.DIRT);
		getDrops().DEFAULT.clear();
		getDrops().DEFAULT.add(VanillaMaterials.DIRT);
		getDrops().SILK_TOUCH.add(VanillaMaterials.GRASS);
		getDrops().EXPLOSION.clear().add(VanillaMaterials.DIRT);
	}

	@Override
	public int getMinimumLightToSpread() {
		return 9;
	}

	@Override
	public boolean canDecayAt(Block block) {
		block = block.translate(BlockFace.TOP);
		BlockMaterial mat = block.getMaterial();
		if ((!(mat instanceof Snow)) || (!(mat.isMaterial(VanillaMaterials.SLAB)))) {
			return (block.getMaterial().getOpacity() > 0 && block.getLight() < 4);
		}
		return false;
	}

	@Override
	public long getSpreadingTime(Block b) {
		return 60000L + GenericMath.getRandom().nextInt(60000) * 3;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		Slot inv = PlayerUtil.getHeldSlot(entity);

		if (inv != null && inv.get() != null && inv.get().isMaterial(Dye.BONE_MEAL) && type.equals(Action.RIGHT_CLICK)) {
			// This flag is used to determine if we should remove a Bone Meal if at least one block
			// gets a Tall Grass attached to the top of it, then we should remove a bone meal.
			boolean shouldConsume = false;

			final Random random = GenericMath.getRandom();
			// Minecraft does grass growing by Bone Meal as follows. Keep in mind the radius is 8.
			// - Tall Grass is placed 9/10 times.
			// - If Tall Grass fails, place Dandelion 2/3 times (within the 1/10 window Tall Grass failed on)
			// - If Dandelion fails, place Rose within the 1/3 times window that Dandelion failed (which is within the 1/10 window Tall Grass failed on).
			for (int dx = -4; dx < 4; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					for (int dz = -4; dz < 4; dz++) {
						//Fertilization only occurs 1/3 times.
						if (random.nextInt(3) != 2) {
							continue;
						}
						// Grass/flowers have lower chance to go to a lower/higher height than the center block is at.
						// It incurs another 1/3 times. Only do this when iterating over -1 or 1 on the dy, otherwise its on the same
						// plane and we don't care.
						if (dy != 0) {
							if (random.nextInt(3) != 2) {
								continue;
							}
						}

						final Block around = block.translate(dx, dy, dz);

						// Only spread to Grass blocks
						if (!around.getMaterial().equals(VanillaMaterials.GRASS)) {
							continue;
						}

						final Block aboveAround = around.translate(BlockFace.TOP);

						// Make sure the block above the translated one is Air.
						if (!aboveAround.getMaterial().equals(VanillaMaterials.AIR)) {
							continue;
						}

						if (random.nextInt(10) != 0) {
							if (VanillaMaterials.TALL_GRASS.canAttachTo(around, BlockFace.TOP)) {
								aboveAround.setMaterial(VanillaMaterials.TALL_GRASS);
								shouldConsume = true;
							}
						} else if (random.nextInt(3) != 0) {
							if (VanillaMaterials.DANDELION.canAttachTo(around, BlockFace.TOP)) {
								aboveAround.setMaterial(VanillaMaterials.DANDELION);
								shouldConsume = true;
							}
						} else {
							if (VanillaMaterials.ROSE.canAttachTo(around, BlockFace.TOP)) {
								aboveAround.setMaterial(VanillaMaterials.ROSE);
								shouldConsume = true;
							}
						}
					}
				}
			}

			if (!PlayerUtil.isCostSuppressed(entity) && shouldConsume) {
				inv.addAmount(-1);
			}
		}
	}
}
