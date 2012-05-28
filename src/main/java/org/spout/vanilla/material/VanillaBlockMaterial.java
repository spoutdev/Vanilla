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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockSnapshot;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.object.moving.Item;
import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.MiningType;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.VanillaPlayerUtil;

public abstract class VanillaBlockMaterial extends BlockMaterial implements VanillaMaterial {
	public static short REDSTONE_POWER_MAX = 15;
	public static short REDSTONE_POWER_MIN = 0;
	private final int minecraftId;
	private float resistance;
	private int meleeDamage;
	private int miningLevel;
	private MiningType miningType;
	private Material dropMaterial;

	public VanillaBlockMaterial(String name, int id) {
		super(name);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setOccludes(false);
		this.setMiningLevel(0);
		this.setMiningType(MiningType.PICKAXE);
		this.setDropMaterial(this);
	}

	public VanillaBlockMaterial(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, data, parent);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE).setOccludes(false);
	}

	@Override
	public final int getMinecraftId() {
		return minecraftId;
	}

	/**
	 * Called when this block is destroyed because of an explosion
	 * @param block that got ignited
	 */
	public void onIgnite(Block block) {
		//TODO Remove percentage of drops from getDrops
		this.onDestroy(block);
	}

	@Override
	public void onDestroy(Block block) {
		//Grab the drops based on material classes' rules.
		List<ItemStack> drops = null;
		if (!VanillaPlayerUtil.isCreative(block.getSource())) {
			drops = getDrops(block);
		}
		BlockChangeEvent event = new BlockChangeEvent(block, new BlockSnapshot(block, this, getData()), block.getSource());
		if (event.isCancelled()) {
			return;
		}
		this.onDestroyBlock(block);
		if (!VanillaPlayerUtil.isCreative(block.getSource())) {
			this.onDestroySpawnDrops(block, drops);
		}
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
	public void onDestroySpawnDrops(Block block, List<ItemStack> drops) {
		if (drops == null || drops.isEmpty() || drops.size() <= 0) {
			return;
		}

		//TODO stack items together for more performance
		for (ItemStack item : drops) {
			block.getPosition().getWorld().createAndSpawnEntity(block.getPosition(), new Item(item, new Vector3(0, 5, 0)));
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

	/**
	 * Gets the drops that should be dropped. Override this method to provide rules
	 * for what should be dropped and when.
	 * @param block
	 * @return a list of drops
	 */
	public List<ItemStack> getDrops(Block block) {
		return getDrops(block, VanillaPlayerUtil.getCurrentItem(block.getSource()));
	}

	/**
	 * Gets the drops that should be dropped. Override this method to provide rules
	 * for what should be dropped and when.
	 * @param block
	 * @param holding item that is held by the player
	 * @return a list of drops
	 */
	public List<ItemStack> getDrops(Block block, ItemStack holding) {
		if (!canDrop(block, holding)) {
			return Collections.<ItemStack>emptyList();
		}
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (holding != null && EnchantmentUtil.hasEnchantment(holding, Enchantments.SILK_TOUCH)) {
			drops.add(new ItemStack(this, 1));
		} else {
			drops.add(new ItemStack(dropMaterial, 1));
		}
		return drops;
	}

	public boolean canDrop(Block block, ItemStack holding) {
		if (this.miningLevel == 0) {
			return true;
		}
		Material heldMaterial = holding.getSubMaterial();
		if (heldMaterial instanceof Tool) {
			Tool heldTool = (Tool) heldMaterial;
			if (this.miningType.isInstance(heldTool) && heldTool.getMiningLevel() >= this.miningLevel) {
				return true;
			}
		}
		return false;
	}

	public VanillaBlockMaterial setMiningLevel(int miningLevel) {
		this.miningLevel = miningLevel;
		return this;
	}

	public int getMiningLevel() {
		return miningLevel;
	}

	public VanillaBlockMaterial setMiningType(MiningType miningType) {
		this.miningType = miningType;
		return this;
	}

	public MiningType getMiningType() {
		return miningType;
	}

	public VanillaBlockMaterial setDropMaterial(Material dropMaterial) {
		this.dropMaterial = dropMaterial;
		return this;
	}

	public Material getDropMaterial() {
		return dropMaterial;
	}
}
