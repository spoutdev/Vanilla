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
package org.spout.vanilla.material;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.entity.Entity;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockSnapshot;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.drops.flag.DropFlagSingle;
import org.spout.vanilla.data.drops.type.block.BlockDrops;
import org.spout.vanilla.data.effect.SoundEffect;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.util.MiningType;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.RedstonePowerMode;

public abstract class VanillaBlockMaterial extends BlockMaterial implements VanillaMaterial {
	public static short REDSTONE_POWER_MAX = 15;
	public static short REDSTONE_POWER_MIN = 0;
	private final int minecraftId;
	private float resistance;
	private int meleeDamage = 1;
	private int miningLevel;
	private MiningType miningType;
	private boolean liquidObstacle = true;
	private SoundEffect stepSound = SoundEffects.STEP_STONE;
	private final BlockDrops drops = new BlockDrops();

	public VanillaBlockMaterial(String name, int id) {
		this((short) 0, name, id);
	}

	public VanillaBlockMaterial(short dataMask, String name, int id) {
		super(dataMask, name);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setTransparent();
		this.setMiningLevel(0);
		this.setMiningType(MiningType.PICKAXE);
		this.getDrops().SILK_TOUCH.add(this);
		this.getDrops().DEFAULT.add(this);
	}

	public VanillaBlockMaterial(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, data, parent);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setTransparent();
		this.setMiningLevel(0);
		this.setMiningType(MiningType.PICKAXE);
		this.getDrops().SILK_TOUCH.add(this);
		this.getDrops().DEFAULT.add(this);
	}

	@Override
	public final int getMinecraftId() {
		return minecraftId;
	}

	@Override
	public short getMinecraftData(short data) {
		return (short) (data & 0xF);
	}

	/**
	 * Called when this block is destroyed because of an explosion
	 * @param block that got ignited
	 */
	public void onIgnite(Block block) {
		HashSet<DropFlagSingle> dropFlags = new HashSet<DropFlagSingle>();
		if (Math.random() > 0.3) {
			dropFlags.add(DropFlagSingle.NO_DROPS);
		}
		this.onDestroy(block, dropFlags);
	}

	@Override
	public final void onDestroy(Block block) {
		onDestroy(block, new HashSet<DropFlagSingle>());
	}

	public void onDestroy(Block block, Set<DropFlagSingle> dropFlags) {
		BlockChangeEvent event = new BlockChangeEvent(block, new BlockSnapshot(block, this, block.getData()), block.getSource());
		if (event.isCancelled()) {
			return;
		}
		this.onDestroyBlock(block);
		//TODO stack items together for more performance
		for (ItemStack item : this.getDrops(block, dropFlags)) {
			ItemUtil.dropItemNaturally(block.getPosition(), item);
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock) {
		return !block.getMaterial().isPlacementObstacle();
	}

	@Override
	public VanillaBlockMaterial setFriction(float friction) {
		return (VanillaBlockMaterial) super.setFriction(friction);
	}

	@Override
	public VanillaBlockMaterial setHardness(float hardness) {
		return (VanillaBlockMaterial) super.setHardness(hardness);
	}

	/**
	 * Sets the sound played when the block is being walked over or is being placed
	 * @param sound to play
	 * @return this Material
	 */
	public VanillaBlockMaterial setStepSound(SoundEffect sound) {
		this.stepSound = sound;
		return this;
	}

	/**
	 * Gets the sound played when the block is being walked over or is being placed
	 * @return step sound
	 */
	public SoundEffect getStepSound() {
		return this.stepSound;
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
	public boolean hasNBTData() {
		return false;
	}

	@Override
	public int getDamage() {
		return this.meleeDamage;
	}

	/**
	 * Temporary function to handle entities entering this Block<br>
	 * <b>This is a STUB! Needs to be moved to SpoutAPI!</b>
	 * 
	 * @param entity that entered or moved
	 * @param block of this material that got entered
	 */
	public void onEntityCollision(Entity entity, Block block) {
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
		block.setMaterial(VanillaMaterials.AIR);
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
	 * @param block to get it of
	 * @param powerMode to use to find the power
	 * @return the redstone power level
	 */
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		if (!this.isRedstoneConductor()) {
			return REDSTONE_POWER_MIN;
		}

		short power = 0;
		Block neigh;
		BlockMaterial mat;
		for (BlockFace face : BlockFaces.NESWBT) {
			neigh = block.translate(face);
			mat = neigh.getMaterial();
			if (mat instanceof RedstoneSource) {
				power = (short) Math.max(power, ((RedstoneSource) mat).getRedstonePowerTo(neigh, face.getOpposite(), powerMode));
			}
		}
		return power;
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
	 * @param block to get it of
	 * @param powerMode to use to find out the power levels
	 * @return True if the block receives power
	 */
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		if (this.isRedstoneConductor()) {
			Block neigh;
			BlockMaterial mat;
			for (BlockFace face : BlockFaces.NESWBT) {
				neigh = block.translate(face);
				mat = neigh.getMaterial();
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

	/**
	 * Gets whether this block material can support the attachable block material to the face given
	 * @param material to attach
	 * @param face of this block to attach to
	 * @return
	 */
	public boolean canSupport(BlockMaterial material, BlockFace face) {
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

	/**
	 * Gets all the drop flags for a Block
	 * 
	 * @param flags to fill
	 * @return the flags parameter
	 */
	public Set<DropFlagSingle> getDropFlags(Block block, Set<DropFlagSingle> flags) {
		return flags;
	}

	/**
	 * Gets all the drops for this block<br>
	 * To provide Block-specific rules, override getDropFlags.
	 * 
	 * @param block that needs drops
	 * @param flags to initially use
	 * @return list of ItemStack drops
	 */
	public final List<ItemStack> getDrops(Block block, Set<DropFlagSingle> flags) {
		flags = getDropFlags(block, flags);
		Random random = new Random(block.getWorld().getAge());
		return getDrops().getDrops(random, flags);
	}

	/**
	 * Gets if this material is a liquid obstacle
	 * @return True if it can stop liquids, False if this material gets destroyed
	 */
	public boolean isLiquidObstacle() {
		return this.liquidObstacle;
	}

	/**
	 * Sets if this material is a liquid obstacle
	 * @param state True to make it an obstacle, False to let liquids destroy this block
	 */
	public VanillaBlockMaterial setLiquidObstacle(boolean state) {
		this.liquidObstacle = state;
		return this;
	}

	/**
	 * Sets the mining level required for this block to spawn drops
	 * @param miningLevel to set to
	 * @return this material
	 */
	public VanillaBlockMaterial setMiningLevel(int miningLevel) {
		this.miningLevel = miningLevel;
		return this;
	}

	/**
	 * Gets the mining level required for this block to spawn drops
	 * @return mining level
	 */
	public int getMiningLevel() {
		return miningLevel;
	}

	/**
	 * Sets the mining type of this Block material<br>
	 * This type is used when checking for drops
	 * @param miningType to set to
	 * @return this material
	 */
	public VanillaBlockMaterial setMiningType(MiningType miningType) {
		this.miningType = miningType;
		return this;
	}

	/**
	 * Gets the mining type of this Block material<br>
	 * This type is used when checking for drops
	 * @return the mining type
	 */
	public MiningType getMiningType() {
		return miningType;
	}

	/**
	 * Gets the drops for this block material
	 * 
	 * @return the drops
	 */
	public BlockDrops getDrops() {
		return this.drops;
	}
}
