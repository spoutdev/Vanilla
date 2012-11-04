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
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class TripWire extends GroundAttachable implements InitializableMaterial {
	public static final int MAX_DISTANCE = 42;
	public static final int MAX_DISTANCE_SQUARED = MAX_DISTANCE * MAX_DISTANCE;

	public TripWire(String name, int id) {
		super(name, id, (String)null);
		this.setHardness(0.0f).setResistance(0.0f).setTransparent();
	}

	@Override
	public void initialize() {
		getDrops().DEFAULT.clear().add(VanillaMaterials.STRING);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block wire) {
		super.onUpdate(oldMaterial, wire);
		if (!wire.isMaterial(this)) {
			return;
		}
		wire.setDataBits(0x1, validateHooks(findHook(wire, BlockFace.NORTH), findHook(wire, BlockFace.SOUTH)));
		wire.setDataBits(0x2, validateHooks(findHook(wire, BlockFace.EAST), findHook(wire, BlockFace.WEST)));
	}

	@Override
	public void onDestroy(Block block, Cause<?> cause) {
		this.trample(block);
		super.onDestroy(block, cause);
	}

	@Override
	public short getMinecraftData(short data) {
		return (data & 0x3) == 0 ? (short) 0x0 : (short) 0x4;
	}

	/**
	 * Performs a null-check and a distance check on the two hooks
	 * @param hook1
	 * @param hook2
	 * @return True if validated, False if not
	 */
	private boolean validateHooks(Block hook1, Block hook2) {
		return hook1 != null && hook2 != null && (hook1.getPosition().distanceSquared(hook2.getPosition()) <= MAX_DISTANCE_SQUARED);
	}

	/**
	 * Attempts to find a hook of this wire
	 * @param wire to search from
	 * @param direction to search to
	 * @return the block of the hook, or null if not find
	 */
	public Block findHook(Block wire, BlockFace direction) {
		for (int i = 0; i < MAX_DISTANCE; i++) {
			wire = wire.translate(direction);
			BlockMaterial material = wire.getMaterial();
			if (material.isMaterial(VanillaMaterials.TRIPWIRE_HOOK) && VanillaMaterials.TRIPWIRE_HOOK.getAttachedFace(wire) == direction) {
				return wire;
			} else if (!material.isMaterial(VanillaMaterials.TRIPWIRE)) {
				break;
			}
		}
		return null;
	}

	private void addHooks(List<Block> hooks, Block wire, BlockFace direction, int bits, boolean wasBitsSet) {
		if (!wasBitsSet) {
			return;
		}
		Block hook1 = findHook(wire, direction);
		Block hook2 = findHook(wire, direction.getOpposite());
		if (validateHooks(hook1, hook2)) {
			hooks.add(hook1);
			hooks.add(hook2);
		} else {
			wire.clearDataBits(bits);
		}
	}

	/**
	 * Gets all the Hooks connected to this Trip Wire
	 * @param wire block
	 * @param direction to search at
	 * @return A list of attached hooks
	 */
	public List<Block> getHooks(Block wire) {
		boolean south = wire.isDataBitSet(0x1);
		boolean west = wire.isDataBitSet(0x2);
		List<Block> hooks;
		if (south && west) {
			hooks = new ArrayList<Block>(4);
		} else if (south || west) {
			hooks = new ArrayList<Block>(2);
		} else {
			return Collections.emptyList(); // no hooks
		}
		addHooks(hooks, wire, BlockFace.NORTH, 0x1, south);
		addHooks(hooks, wire, BlockFace.EAST, 0x2, west);
		return hooks;
	}

	/**
	 * Tramples the wire, possibly causing hooks to get activated
	 * @param block to trample
	 */
	public void trample(Block block) {
		for (Block hook : getHooks(block)) {
			VanillaMaterials.TRIPWIRE_HOOK.setToggled(hook, true);
		}
	}

	@Override
	public void onEntityCollision(Entity entity, Block block) {
		trample(block);
	}
}
