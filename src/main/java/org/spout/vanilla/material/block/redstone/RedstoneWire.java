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
package org.spout.vanilla.material.block.redstone;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.generic.Solid;
import org.spout.vanilla.material.block.generic.VanillaBlockMaterial;

public class RedstoneWire extends GroundAttachable implements RedstoneSource, RedstoneTarget {
	private final Vector3[] possibleIncoming = {new Vector3(1, 0, 0), new Vector3(0, 0, 1), new Vector3(-1, 0, 0), new Vector3(0, 0, -1), new Vector3(1, 1, 0), new Vector3(0, 1, 1), new Vector3(-1, 1, 0), new Vector3(0, 1, -1), new Vector3(0, 1, 0), //Redstone torch from above
	};
	@SuppressWarnings("unused")
	private final Vector3[] possibleOutgoing = {new Vector3(1, 0, 0), new Vector3(0, 0, 1), new Vector3(-1, 0, 0), new Vector3(0, 0, -1), new Vector3(1, 1, 0), new Vector3(0, 1, 1), new Vector3(-1, 1, 0), new Vector3(0, 1, -1), new Vector3(1, -1, 0), new Vector3(0, -1, 1), new Vector3(-1, -1, 0), new Vector3(0, -1, -1),};
	private final Vector3[] possibleOutgoingTorch = {new Vector3(2, 0, 0), new Vector3(0, 0, 2), new Vector3(-2, 0, 0), new Vector3(0, 0, -2), new Vector3(1, 1, 0), new Vector3(0, 1, 1), new Vector3(-1, 1, 0), new Vector3(0, 1, -1),};

	public RedstoneWire() {
		super("Redstone Wire", 55);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (!VanillaConfiguration.REDSTONE_PHYSICS.getBoolean()) {
			return;
		}

		System.out.println("Updating " + block.getX() + "/" + block.getY() + "/" + block.getZ());
		short maxPower = 0;
		Block below = block.translate(BlockFace.BOTTOM);
		if (below.getMaterial() instanceof VanillaBlockMaterial) {
			maxPower = ((VanillaBlockMaterial) below.getMaterial()).getIndirectRedstonePower(below); //Check for indirect power from below
		}
		Block incoming;
		for (Vector3 vec : possibleIncoming) {
			incoming = block.translate(vec);
			BlockMaterial incmat = incoming.getMaterial();
			short power = 0;
			if (incmat instanceof RedstoneSource) {
				RedstoneSource source = (RedstoneSource) incmat;
				power = source.getRedstonePower(incoming, block);
			} else if (incmat instanceof VanillaBlockMaterial) {
				VanillaBlockMaterial Vanilla = (VanillaBlockMaterial) incmat;
				power = Vanilla.getDirectRedstonePower(incoming);
			}
			maxPower = (short) Math.max(maxPower, power);
		}
		setPowerAndUpdate(block, maxPower);
	}

	/**
	 * Sets the wire at x,y,z to the given power and initiates an update process that will recalculate the wire.
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param power
	 */
	public void setPowerAndUpdate(Block block, short power) {
		short current = block.getData();
		if (current != power) {
			System.out.println("Old: " + current + " new: " + power);
			block.setMaterial(this);
			//Trace signal
			Block target;
			for (int j = 0; j < 3; j++) {
				int ty = block.getY();
				switch (j) {
					case 0:
						ty--;
						break;
					case 1: //ty = ty;
						break;
					case 2:
						ty++;
						break;
				}
				for (int i = 0; i < 4; i++) {
					int tx = block.getX();
					int tz = block.getZ();
					switch (i) {
						case 0:
							tx--;
							break;
						case 1:
							tx++;
							break;
						case 2:
							tz--;
							break;
						case 3:
							tz++;
							break;
					}
					target = block.getWorld().getBlock(tx, ty, tz);
					if (target.getMaterial() instanceof RedstoneWire) {
						if (providesPowerTo(block, target)) {
							onUpdate(target);
						}
					}
				}
			}

			//Update redstone torches.
			for (Vector3 torch : possibleOutgoingTorch) {
				target = block.translate(torch).update(false);
			}
		}
	}

	@Override
	public boolean providesPowerTo(Block source, Block target) {
		return attachesTo(source, target);
	}

	@Override
	public short getRedstonePower(Block source, Block target) {
		short power = 0;
		if (providesPowerTo(source, target)) {
			power = (short) Math.max(0, source.getData() - 1);
		}
		if (source.getX() == target.getX() && source.getY() == target.getY() && source.getZ() == target.getZ()) {
			power = source.getData();
		}
		return power;
	}

	public boolean attachesTo(Block source, Block target) {
		BlockMaterial targetmat = target.getMaterial();
		if (targetmat instanceof RedstoneTarget) {
			return ((RedstoneTarget) targetmat).providesAttachPoint(source, target);
		}
		return false;
	}

	@Override
	public boolean providesAttachPoint(Block source, Block target) {
		if (source.getY() == target.getY()) { //source and target are level
			if (source.getX() == target.getX()) {
				return source.getZ() - 1 == target.getZ() || source.getZ() + 1 == target.getZ();
			}
			if (source.getZ() == target.getZ()) {
				return source.getX() - 1 == target.getX() || source.getX() + 1 == target.getX();
			}
		} else {
			BlockMaterial targetmat = target.getMaterial();
			if (targetmat instanceof RedstoneWire) { //only send power down/up for other redstone wires
				if (source.getY() < target.getY()) {
					//This is the below block
					if (source.translate(BlockFace.TOP).getMaterial() instanceof Solid) { //Current does not walk through solids
						return false;
					}
				} else {
					//This is the upper block
					if (target.translate(BlockFace.TOP).getMaterial() instanceof Solid) { //Current does not walk through solids
						return false;
					}
				}
				if (source.getX() == target.getX()) {
					return source.getZ() - 1 == target.getZ() || source.getZ() + 1 == target.getZ();
				}
				if (source.getZ() == target.getZ()) {
					return source.getX() - 1 == target.getX() || source.getX() + 1 == target.getX();
				}
			} else {
				return false;
			}
		}
		return false;
	}
}
