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
package org.spout.vanilla.material.block.misc;

import java.util.Random;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CuboidEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;
import org.spout.api.math.IntVector3;

import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class Fire extends VanillaBlockMaterial implements DynamicMaterial {
	private static final EffectRange SPREAD_RANGE = new CuboidEffectRange(-1, -1, -1, 1, 4, 1);

	public Fire(String name, int id) {
		super(name, id, VanillaMaterialModels.FIRE, null);
		this.setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent().setGhost(true);
		this.getDrops().clear();
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public byte getLightLevel(short data) {
		return 15;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		Cause<?> cause = toCause(block);
		if (!this.canCreate(block, block.getBlockData(), cause)) {
			this.onDestroy(block, cause);
		}
	}

	@Override
	public boolean canCreate(Block block, short data, Cause<?> cause) {
		if (super.canCreate(block, data, cause) && VanillaConfiguration.FIRE_PHYSICS.getBoolean()) {
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.BTNSWE) {
				mat = block.translate(face).getMaterial();
				if (mat instanceof VanillaBlockMaterial) {
					if (((VanillaBlockMaterial) mat).canSupport(this, face.getOpposite())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public EffectRange getDynamicRange() {
		return SPREAD_RANGE;
	}

	/**
	 * Checks if the given fire block has a burn source at a certain face<br> It checks if the fire has a {@link org.spout.vanilla.material.Burnable} block at the face
	 *
	 * @param block of the fire
	 * @param to the face the source is
	 * @return True if it has a source there, False if not
	 */
	public boolean hasBurningSource(Block block, BlockFace to) {
		BlockMaterial material = block.translate(to).getMaterial();
		return material instanceof Burnable && ((Burnable) material).getBurnPower() > 0;
	}

	/**
	 * Checks if the given fire block has a burn source<br> It checks if the fire has a {@link org.spout.vanilla.material.Burnable} block nearby
	 *
	 * @param block of the fire
	 * @return True if it has a source, False if not
	 */
	public boolean hasBurningSource(Block block) {
		for (BlockFace face : BlockFaces.NESWBT) {
			if (hasBurningSource(block, face)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the fire can degrade (disappear over time)
	 *
	 * @param block of the fire
	 * @return True if it can degrade, False if not
	 */
	public boolean canDegrade(Block block) {
		BlockMaterial below = block.translate(BlockFace.BOTTOM).getMaterial();
		if (below.equals(VanillaMaterials.NETHERRACK)) {
			return false;
		}
		return !(below.equals(VanillaMaterials.BEDROCK) && block.getWorld().getData().get(VanillaData.DIMENSION) == Dimension.THE_END);
	}

	@Override
	public void onFirstUpdate(Block block, long currentTime) {
		block.dynamicUpdate(currentTime + 1000 + (GenericMath.getRandom()).nextInt(2000), true);
	}

	@Override
	public void onDynamicUpdate(Block b, long updateTime, int data) {
		final Random rand = GenericMath.getRandom();

		// Make fire strength increase over time
		short blockData = b.getBlockData();
		if (blockData < 15) {
			blockData += rand.nextInt(4) / 3;
			b.setData(blockData);
		}

		if (this.canDegrade(b)) {
			// Fires without source burn less long, rain fades out fire
			if (VanillaBlockMaterial.hasRainNearby(b) || (!hasBurningSource(b) && blockData > 3)) {
				this.onDestroy(b, null);
				return;
			}

			// If fire is done with and the block below can not fuel fire, destroy
			if (blockData == 15 && rand.nextInt(4) == 0 && !hasBurningSource(b, BlockFace.BOTTOM)) {
				this.onDestroy(b, null);
				return;
			}
		}

		// Try to instantly combust surrounding blocks
		Block sBlock;
		for (BlockFace face : BlockFaces.NESWBT) {
			int chance = BlockFaces.TB.contains(face) ? 250 : 300;
			sBlock = b.translate(face);
			BlockMaterial mat = sBlock.getMaterial();
			if (mat instanceof Burnable && rand.nextInt(chance) < ((Burnable) mat).getCombustChance()) {
				// Destroy the old block
				if (mat instanceof TntBlock) {
					((TntBlock) mat).onIgnite(sBlock, toCause(b)); // Ignite TntBlock
				} else {
					sBlock.setMaterial(VanillaMaterials.AIR); // prevent drops
				}
				// Put fire in it's place?
				if (rand.nextInt(blockData + 10) < 5 && hasBurningSource(sBlock) && !VanillaBlockMaterial.isRaining(sBlock)) {
					sBlock.setMaterial(this, Math.min(15, blockData + rand.nextInt(5) / 4));
				}
			}
		}

		// Spreading component
		int chanceFactor, firePower, netChance;
		for (IntVector3 offset : SPREAD_RANGE) {

			// Don't spread to the middle or to non-air and other fire blocks
			if (offset.getX() == 0 && offset.getY() == 0 && offset.getZ() == 0) {
				continue;
			}
			sBlock = b.translate(offset);
			if (!sBlock.isMaterial(VanillaMaterials.AIR)) {
				continue;
			}

			// Get power level for this fire
			firePower = 0;
			for (BlockFace face : BlockFaces.NESWBT) {
				BlockMaterial mat = sBlock.translate(face).getMaterial();
				if (mat instanceof Burnable) {
					firePower = Math.max(firePower, ((Burnable) mat).getBurnPower());
				}
			}
			if (firePower == 0) {
				continue;
			}

			// Calculate and use the net chance of spreading
			// Higher blocks have a lower chance of spreading
			// Don't spread if it has rain falling nearby
			if (offset.getY() > 1) {
				chanceFactor = (offset.getY() * 100);
			} else {
				chanceFactor = 100;
			}
			netChance = (firePower + 40) / (blockData + 30);
			if (netChance <= 0 || rand.nextInt(chanceFactor) > netChance || VanillaBlockMaterial.hasRainNearby(sBlock)) {
				continue;
			}

			// Set new fire block with randomly increasing power (1/4 chance for +1 for fire power)
			sBlock.setMaterial(this, Math.min(15, blockData + rand.nextInt(5) / 4));
		}

		// Schedule for a next update
		b.dynamicUpdate(b.getWorld().getAge() + 2000, true);
	}
}
