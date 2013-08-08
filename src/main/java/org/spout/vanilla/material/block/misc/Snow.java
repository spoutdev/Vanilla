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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.GenericMath;

import org.spout.physics.collision.shape.BoxShape;
import org.spout.physics.collision.shape.CollisionShape;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.world.lighting.VanillaLighting;

public class Snow extends GroundAttachable implements DynamicMaterial, InitializableMaterial {
	private static final long POLL_TIME = 60000;
	private static final byte MIN_MELT_LIGHT = 11;
	public static final Snow[] SNOW = new Snow[8];

	static {
		SNOW[0] = new Snow("Snow", 78, VanillaMaterialModels.SNOW_1, null, true);
		SNOW[1] = new Snow("Snow_1", 1, SNOW[0], VanillaMaterialModels.SNOW_2, null, true);
		SNOW[2] = new Snow("Snow_2", 2, SNOW[0], VanillaMaterialModels.SNOW_3, null, true);
		SNOW[3] = new Snow("Snow_3", 3, SNOW[0], VanillaMaterialModels.SNOW_4, null, true);
		SNOW[4] = new Snow("Snow_4", 4, SNOW[0], VanillaMaterialModels.SNOW_5, null, true);
		SNOW[5] = new Snow("Snow_5", 5, SNOW[0], VanillaMaterialModels.SNOW_6, null, true);
		SNOW[6] = new Snow("Snow_6", 6, SNOW[0], VanillaMaterialModels.SNOW_7, null, true);
		SNOW[7] = new Snow("Snow_7", 7, SNOW[0], VanillaMaterialModels.SNOW_BLOCK, new BoxShape(1f, 1f, 1f), false);
	}

	//TODO: Box Shape
	public Snow(String name, int id, String model, CollisionShape shape, boolean isGhost) {
		super((short) 0x07, name, id, model, shape);
		this.setLiquidObstacle(false).setStepSound(SoundEffects.STEP_CLOTH).setHardness(0.1F).setResistance(0.2F).setTransparent().setOpacity(1).setGhost(isGhost);
		this.addMiningType(ToolType.SPADE).setMiningLevel(ToolLevel.WOOD);
	}

	private Snow(String name, int data, Snow parent, String model, CollisionShape shape, boolean isGhost) {
		super(name, SNOW[0].getMinecraftId(), data, parent, model, shape);
		this.setLiquidObstacle(false).setStepSound(SoundEffects.STEP_CLOTH).setHardness(0.1F).setResistance(0.2F).setTransparent().setOpacity(1).setGhost(isGhost);
		if (data == 7) {
			this.setOpaque();
		}
		this.addMiningType(ToolType.SPADE).setMiningLevel(ToolLevel.WOOD);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.SNOWBALL);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	public final boolean willMeltAt(Block block) {
		return VanillaLighting.getBlockLight(block) > MIN_MELT_LIGHT;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		//TODO : Delay before next check ?
		b.dynamicUpdate(60000 + currentTime, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (willMeltAt(block)) {
			short dataBlock = block.getBlockData();
			if (dataBlock > 0) {
				block.setData(dataBlock - 1);
			} else {
				block.setMaterial(VanillaMaterials.AIR);
			}
		} else { // not warm enough to melt the snow and last poll was a long time ago, might as well skip repeated polls
			long age = block.getWorld().getAge();
			if (age - updateTime > POLL_TIME) {
				block.dynamicUpdate(age + GenericMath.getRandom().nextInt((int) POLL_TIME), true);
				return;
			}
		}
		block.dynamicUpdate(updateTime + POLL_TIME, true);
		//TODO : Delay before next check ?
	}
}
