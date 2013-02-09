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

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.api.material.Burnable;
import org.spout.vanilla.api.material.InitializableMaterial;
import org.spout.vanilla.api.material.block.Plant;

import org.spout.vanilla.api.data.drops.SwitchDrops;
import org.spout.vanilla.api.data.drops.flag.ToolTypeFlags;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.attachable.GroundAttachable;
import org.spout.vanilla.plugin.render.VanillaEffects;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public final class TallGrass extends GroundAttachable implements Burnable, Plant, InitializableMaterial {
	public static final TallGrass DEAD_GRASS = new TallGrass("Dead Grass", 31, VanillaMaterialModels.DEADGRASS);
	public static final TallGrass TALL_GRASS = new TallGrass("Tall Grass", 1, DEAD_GRASS, VanillaMaterialModels.TALLGRASS);
	public static final TallGrass FERN = new TallGrass("Fern", 2, DEAD_GRASS, VanillaMaterialModels.FERN);

	private TallGrass(String name, int id, String model) {
		super((short) 0x0003, name, id, model);
	}

	private TallGrass(String name, int data, TallGrass parent, String model) {
		super(name, parent.getMinecraftId(), data, parent, model);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT && data == 1) {
			if (!getModel().getRenderMaterial().getBufferEffects().contains(VanillaEffects.BIOME_GRASS_COLOR)) {
				getModel().getRenderMaterial().addBufferEffect(VanillaEffects.BIOME_GRASS_COLOR);
			}
			if (!getMeshEffects().contains(VanillaEffects.TALL_GRASS_OFFSET)) {
				addMeshEffect(VanillaEffects.TALL_GRASS_OFFSET);
			}
		}
	}

	@Override
	public void initialize() {
		setLiquidObstacle(false).setHardness(0.0F).setResistance(0.0F).setTransparent();
		SwitchDrops drops = getDrops().DEFAULT.clear().addSwitch(ToolTypeFlags.SHEARS);
		drops.TRUE.add(this);
		drops.FALSE.add(VanillaMaterials.SEEDS).setChance(0.15);
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return mat.equals(VanillaMaterials.FIRE);
	}

	@Override
	public int getBurnPower() {
		return (this == DEAD_GRASS) ? 0 : 60;
	}

	@Override
	public int getCombustChance() {
		return (this == DEAD_GRASS) ? 0 : 100;
	}

	@Override
	public TallGrass getParentMaterial() {
		return (TallGrass) super.getParentMaterial();
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (face == BlockFace.TOP) {
			if (this == DEAD_GRASS) {
				return block.isMaterial(VanillaMaterials.SAND);
			} else {
				return block.isMaterial(VanillaMaterials.GRASS, VanillaMaterials.DIRT);
			}
		}
		return false;
	}
}
