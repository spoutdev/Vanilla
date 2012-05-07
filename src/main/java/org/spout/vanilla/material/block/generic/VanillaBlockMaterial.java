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
package org.spout.vanilla.material.block.generic;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.VanillaMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.Attachable;
import org.spout.vanilla.material.block.redstone.RedstoneTorch;
import org.spout.vanilla.material.block.redstone.RedstoneWire;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class VanillaBlockMaterial extends BlockMaterial implements VanillaMaterial {
	private static BlockFace indirectSourcesWire[] = {BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};
	private float resistance;
	private Material dropMaterial;
	private int dropCount;

	public VanillaBlockMaterial(String name, int id) {
		super(name, id);
		dropMaterial = this;
		dropCount = 1;
		this.setCollision(CollisionStrategy.SOLID);
	}

	public VanillaBlockMaterial(String name, int id, int data, Material parent) {
		super(name, id, data, parent);
		this.setCollision(CollisionStrategy.SOLID);
	}

	@Override
	public void onDestroy(Block block) {
		this.onDestroyBlock(block);
		this.onDestroySpawnDrops(block);
	}

	@Override
	public VanillaBlockMaterial setFriction(float friction) {
		return (VanillaBlockMaterial) super.setFriction(friction);
	}

	@Override
	public VanillaBlockMaterial setHardness(float hardness) {
		return (VanillaBlockMaterial) super.setHardness(hardness);
	}

	@Override
	public VanillaBlockMaterial setLightLevel(byte level) {
		return (VanillaBlockMaterial) super.setLightLevel(level);
	}

	@Override
	public boolean isPlacementObstacle() {
		return true;
	}

	@Override
	public boolean hasPhysics() {
		return false;
	}

	@Override
	public boolean getNBTData() {
		return false;
	}

	@Override
	public int getDamage() {
		return 1;
	}

	/**
	 * Called when this block is destroyed to perform the actual block destruction
	 * @param block to destroy
	 */
	public void onDestroyBlock(Block block) {
		block.setMaterial(VanillaMaterials.AIR).update();
	}

	/**
	 * Called when this block is destroyed to perform the drops spawning
	 * @param block to spawn drops for
	 */
	public void onDestroySpawnDrops(Block block) {
		if (VanillaPlayerUtil.isCreative(block.getSource())) {
			return;
		}

		Material dropMat = getDrop();
		if (dropMat != null) {
			int count = this.getDropCount();
			for (int i = 0; i < count && dropMat.getId() != 0; ++i) {
				block.getWorld().createAndSpawnEntity(block.getPosition(), new Item(new ItemStack(dropMat, 1), block.getPosition().normalize().add(0, 5, 0)));
			}
		}
	}

	/**
	 * Represents power that comes into the block from a redstone wire or a torch that is below the block
	 * Indirect power from below powers redstone wire, but level indirect power just inverts adjacent redstone torches.
	 * @return the indirect redstone power.
	 */
	public short getIndirectRedstonePower(Block block) {
		short indirect = 0;
		for (BlockFace face : indirectSourcesWire) {
			Block tblock = block.translate(face);
			BlockMaterial material = tblock.getMaterial();
			if (material instanceof RedstoneWire) {
				indirect = (short) Math.max(indirect, tblock.getData());
			}
		}

		Block torchblock = block.translate(BlockFace.BOTTOM);
		BlockMaterial material = torchblock.getMaterial(); //Check for redstone torch below
		if (material instanceof RedstoneTorch) {
			RedstoneTorch torch = (RedstoneTorch) material;
			indirect = (short) Math.max(indirect, torch.getRedstonePower(torchblock, block));
		}
		return (short) Math.max(indirect, getDirectRedstonePower(block));
	}

	/**
	 * Represents power that comes from a repeater that points to this block.
	 * This power can be used by all neighbors that are redstone targets, even if they wouldn't attach.
	 * @return the direct redstone power.
	 */
	public short getDirectRedstonePower(Block block) {
		// TODO Waiting for repeaters
		return 0;
	}

	public VanillaBlockMaterial setResistance(Float newResistance) {
		resistance = newResistance;
		return this;
	}

	public float getResistance() {
		return resistance;
	}

	public Material getDrop() {
		return dropMaterial;
	}

	public int getDropCount() {
		return dropCount;
	}

	/**
	 * Gets whether this block material can support the attachable block material to the face given
	 * @param material to attach
	 * @param face     of this block to attach to
	 * @return
	 */
	public <T extends BlockMaterial & Attachable> boolean canSupport(T material, BlockFace face) {
		return true;
	}

	/**
	 * Gets whether this block material cancels block placement when clicked
	 */
	public boolean isPlacementSuppressed() {
		return false;
	}

	public VanillaBlockMaterial setLightLevel(int level) {
		return setLightLevel((byte) level);
	}

	public VanillaBlockMaterial setDrop(Material id) {
		dropMaterial = id;
		return this;
	}

	public VanillaBlockMaterial setDropCount(int count) {
		dropCount = count;
		return this;
	}
}
