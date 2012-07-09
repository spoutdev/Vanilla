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
package org.spout.vanilla.material.block.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.Data;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.material.Burnable;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.solid.TNT;

public class Fire extends VanillaBlockMaterial implements DynamicMaterial {
	private static EffectRange dynamicRange = new CubicEffectRange(1);
	private static List<Vector3> spreadBlocks = new ArrayList<Vector3>();

	static {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x != 0 || y != 0 || z != 0) {
						spreadBlocks.add(new Vector3(x, y, z));
					}
				}
			}
		}		
	}

	public Fire(String name, int id) {
		super(name, id);
		this.setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent();
		this.clearDropMaterials();
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
		if (!this.canPlace(block, block.getData(), BlockFace.BOTTOM, false)) {
			this.onDestroy(block);
		}
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace attachedFace, boolean isClickedBlock) {
		if (super.canPlace(block, data, attachedFace, isClickedBlock)) {
			BlockMaterial mat = block.getMaterial();
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
		return dynamicRange;
	}

	/**
	 * Checks if a given block material can be burned by fire
	 * 
	 * @param material to check
	 * @return True if it can burn, False if not
	 */
	public static boolean isBurnable(BlockMaterial material) {
		return material instanceof Burnable && ((Burnable) material).getBurnPower() > 0;
	}

	/**
	 * Checks if the given fire block can actually spread out<br>
	 * It checks if the fire has a burnable block nearby
	 * 
	 * @param block of the fire
	 * @return True if it can spread, False if not
	 */
	public boolean hasBurningSource(Block block) {
		for (BlockFace face : BlockFaces.NESWBT) {
			if (isBurnable(block.translate(face).getMaterial())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Causes a single spread call to a random neighbor<br>
	 * May not actually create new fire.
	 * 
	 * @param block to spread from
	 */
	public void onSpread(Block block) {
		Vector3 offset = spreadBlocks.get((int) (Math.random() * spreadBlocks.size()));
		block = block.translate(offset);
		if (block.getMaterial() instanceof Liquid) {
			return;
		}
		if (this.canPlace(block, (short) 0, BlockFace.BOTTOM, false)) {
			this.onPlacement(block, (short) 0, BlockFace.BOTTOM, false);
		}
	}

	/**
	 * Gets if rain is falling nearby the block specified
	 * 
	 * @param block to check it nearby of
	 * @return True if it is raining, False if not
	 */
	public boolean hasRainNearby(Block block) {
		//TODO: Implement this!
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
		if (below.equals(VanillaMaterials.BEDROCK) && block.getWorld().getDataMap().get(Data.DIMENSION) == Dimension.THE_END) {
			return false;
		}
		return true;
	}

	public void tryCombust(Block block, Random random, int fireStrength, int chance) {
		BlockMaterial mat = block.getMaterial();
		if (mat instanceof Burnable && random.nextInt(((Burnable) mat).getCombustChance()) < chance) {
            // Destroy the old block
			if (mat instanceof TNT) {
            	((TNT) mat).onIgnite(block); // Ignite TNT
            } else {
            	block.setMaterial(VanillaMaterials.AIR); // prevent drops
            }

            // Put fire in it's place?
            if (random.nextInt(fireStrength + 10) < 5) { //TODO: && !world.isAboveGround(x, y, z)) {
                int newData = fireStrength + random.nextInt(5) / 4;
                if (newData > 15) {
                    newData = 15;
                }
                block.setMaterial(this, fireStrength);
            }
		}
	}
	
	private void scheduleNext(Block block) {
		//TODO: Get correct randomness...
		block.dynamicUpdate(block.getWorld().getAge() + 2000);
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		scheduleNext(b);
	}
	
	@Override
	public void onDynamicUpdate(Block b, Region r, long updateTime, long queuedTime, int data, Object hint) {
		boolean degrades = this.canDegrade(b);
		Random rand = new Random();

		// Fade out fire if raining
		if (degrades && this.hasRainNearby(b)) {
			this.onDestroy(b);
			return;
		}

		// Make fire strength increase over time
		short blockData = b.getData();
		if (blockData < 15) {
			b.setData(blockData + rand.nextInt(4) / 3);
		}

		if (degrades) {
			// Fires without source burn less long
			if (!hasBurningSource(b) && data > 3) {
				this.onDestroy(b);
				return;
			}

			// If fire is done with and the block below can not combust, destroy
			if (data == 15 && rand.nextInt(4) == 0 && !isBurnable(b.translate(BlockFace.BOTTOM).getMaterial())) {
				this.onDestroy(b);
				return;
			}
		}

		// Try to instantly combust surrounding blocks
		for (BlockFace face : BlockFaces.NESW) {
			tryCombust(b.translate(face), rand, data, 300);
		}
		tryCombust(b.translate(BlockFace.TOP), rand, data, 250);
		tryCombust(b.translate(BlockFace.BOTTOM), rand, data, 250);

		// Spreading logic
		this.onSpread(b);

		// Schedule for a next update
		this.scheduleNext(b);
	}
}
