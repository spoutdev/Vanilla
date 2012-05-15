/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.material.block.RedstoneSource;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class VanillaBlockMaterial extends BlockMaterial implements VanillaMaterial {
	public static final short REDSTONE_POWER_MAX = 15;
	public static final short REDSTONE_POWER_MIN = 0;
	private float resistance;
	private Material dropMaterial;
	private int dropCount;
	private int meleeDamage;

	public VanillaBlockMaterial(String name, int id) {
		super(name, id);
	}

	public VanillaBlockMaterial(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, id, data, parent);
	}

	@Override
	public void loadProperties() {
		this.setDrop(this).setDropCount(1).setDamage(1);
		this.setCollision(CollisionStrategy.SOLID);
		if (this.hasSubMaterials()) {
			for (Material material : this.getSubMaterials()) {
				if (material instanceof VanillaMaterial) {
					((VanillaMaterial) material).loadProperties();
				}
			}
		}
	}

	@Override
	public void onDestroy(Block block) {
		this.onDestroyBlock(block);
		this.onDestroySpawnDrops(block);
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
		return !block.getSubMaterial().isPlacementObstacle();
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
		return this.meleeDamage;
	}

	@Override
	public VanillaBlockMaterial setDamage(int damage) {
		this.meleeDamage = damage;
		return this;
	}

	/**
	 * Gets the instrument note blocks play with this block material below
	 * @return the instrument
	 */
	public Instrument getInstrument() {
		return Instrument.PIANO;
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
	 * Gets whether this block material acts as a solid redstone conductor
	 * @return True if it is a conductor
	 */
	public boolean isRedstoneConductor() {
		return false;
	}

	/**
	 * Gets the power level of this block<br>
	 * @param block to get it of
	 * @return the redstone power level
	 */
	public short getRedstonePower(Block block) {
		return this.getRedstonePower(block, RedstonePowerMode.ALL);
	}

	/**
	 * Gets the power level of a single block
	 * @param block     to get it of
	 * @param powerMode to use to find the power
	 * @return the redstone power level
	 */
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		if (this.isRedstoneConductor()) {
			short power = 0;
			Block neigh;
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESWBT) {
				neigh = block.translate(face);
				mat = neigh.getSubMaterial();
				if (mat instanceof RedstoneSource) {
					power = (short) Math.max(power, ((RedstoneSource) mat).getRedstonePowerTo(neigh, face.getOpposite(), powerMode));
				}
			}
			return power;
		} else {
			return REDSTONE_POWER_MIN;
		}
	}

	/**
	 * Gets if this block is being powered or not
	 * @param block to get it of
	 * @return True if the block receives power
	 */
	public boolean hasRedstonePower(Block block) {
		return this.hasRedstonePower(block, RedstonePowerMode.ALL);
	}

	/**
	 * Gets if this block is being powered or not
	 * @param block     to get it of
	 * @param powerMode to use to find out the power levels
	 * @return True if the block receives power
	 */
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		if (this.isRedstoneConductor()) {
			Block neigh;
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESWBT) {
				neigh = block.translate(face);
				mat = neigh.getSubMaterial();
				if (mat instanceof RedstoneSource) {
					if (((RedstoneSource) mat).hasRedstonePowerTo(neigh, face.getOpposite(), powerMode)) {
						return true;
					}
				}
			}
		}
		return false;
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
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	/**
	 * Gets if this block material can burn and be destroyed as a result of fire
	 */
	public boolean canBurn() {
		return false;
	}

	/**
	 * Gets the move reaction of block material for the block specified
	 * @param block that is being moved
	 * @return the move reaction
	 */
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.BREAK;
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
