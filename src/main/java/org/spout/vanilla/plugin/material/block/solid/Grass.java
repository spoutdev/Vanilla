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
package org.spout.vanilla.plugin.material.block.solid;

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.MathHelper;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.plugin.data.effect.store.SoundEffects;
import org.spout.vanilla.plugin.data.tool.ToolType;
import org.spout.vanilla.plugin.material.InitializableMaterial;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.SpreadingSolid;
import org.spout.vanilla.plugin.render.VanillaEffects;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;

public class Grass extends SpreadingSolid implements DynamicMaterial, InitializableMaterial {
	public Grass(String name, int id) {
		super(name, id, VanillaMaterialModels.GRASS);
		this.setHardness(0.6F).setResistance(0.8F).setStepSound(SoundEffects.STEP_GRASS);
		this.addMiningType(ToolType.SPADE);
		if (Spout.getEngine().getPlatform() == Platform.CLIENT) {
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
	}

	@Override
	public int getMinimumLightToSpread() {
		return 9;
	}

	@Override
	public boolean canDecayAt(Block block) {
		block = block.translate(BlockFace.TOP);
		return block.getMaterial().getOpacity() > 0 || block.getLight() < 4;
	}

	@Override
	public long getSpreadingTime(Block b) {
		return 1000L + MathHelper.getRandom().nextInt(60000) * 3;
	}
}