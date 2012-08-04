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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockSnapshot;

import org.spout.vanilla.data.effect.SoundEffect;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.material.enchantment.Enchantments;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.Instrument;
import org.spout.vanilla.util.ItemUtil;
import org.spout.vanilla.util.MiningType;
import org.spout.vanilla.util.MoveReaction;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.VanillaPlayerUtil;

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
	private Map<Material, int[]> dropMaterials = new HashMap<Material, int[]>();

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
		this.setDropMaterial(this);
	}

	public VanillaBlockMaterial(String name, int id, int data, VanillaBlockMaterial parent) {
		super(name, data, parent);
		this.minecraftId = id;
		this.setCollision(CollisionStrategy.NOCOLLIDE);
		this.setTransparent();
		this.setMiningLevel(0);
		this.setMiningType(MiningType.PICKAXE);
		this.setDropMaterial(this);
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
		this.onDestroy(block, 0.3);
	}

	@Override
	public final void onDestroy(Block block) {
		onDestroy(block, 1.0);
	}

	public void onDestroy(Block block, double dropChance) {
		//Grab the drops based on material classes' rules.
		List<ItemStack> drops = Collections.<ItemStack>emptyList();
		if (Math.random() < dropChance && !VanillaPlayerUtil.isCreative(block.getSource())) {
			drops = getDrops(block);
		}
		BlockChangeEvent event = new BlockChangeEvent(block, new BlockSnapshot(block, this, getData()), block.getSource());
		if (event.isCancelled()) {
			return;
		}
		this.onDestroyBlock(block);
		if (!drops.isEmpty()) {
			this.onDestroySpawnDrops(block, drops);
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace against, boolean isClickedBlock) {
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
	 * Called when this block is destroyed to perform the drops spawning
	 * @param block to spawn drops for
	 */
	public void onDestroySpawnDrops(Block block, List<ItemStack> drops) {
		if (drops == null || drops.isEmpty() || drops.size() <= 0) {
			return;
		}

		//TODO stack items together for more performance
		for (ItemStack item : drops) {
			if (item == null) {
				continue;
			}
			ItemUtil.dropItemNaturally(block.getPosition(), item);
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
		if (canDrop(block, holding)) {
			if (holding != null && EnchantmentUtil.hasEnchantment(holding, Enchantments.SILK_TOUCH)) {
				return Arrays.asList(new ItemStack(this, 1));
			} else if (!this.dropMaterials.isEmpty()) {
				ArrayList<ItemStack> drops = new ArrayList<ItemStack>(this.dropMaterials.size());
				for (Material m : dropMaterials.keySet()) {
					int[] amount = dropMaterials.get(m);
					drops.add(new ItemStack(m, amount[(int) (Math.random() * amount.length)]));
				}
				return drops;
			}
		}
		return Collections.<ItemStack>emptyList();
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
	 * Gets whether this Block Material can spawn drops when dug by the held item specified
	 * @param block to be broken
	 * @param holding item of the diggers
	 * @return True if it spawns items, False if not
	 */
	public boolean canDrop(Block block, ItemStack holding) {
		if (this.miningLevel == 0) {
			return true;
		}
		Material heldMaterial = holding.getMaterial();
		if (heldMaterial instanceof Tool) {
			Tool heldTool = (Tool) heldMaterial;
			if (this.miningType.isInstance(heldTool) && heldTool.getMiningLevel() >= this.miningLevel) {
				return true;
			}
		}
		return false;
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

	public VanillaBlockMaterial removeDropMaterial(Material dropMaterial) {
		dropMaterials.remove(dropMaterial);
		return this;
	}

	public VanillaBlockMaterial clearDropMaterials() {
		dropMaterials.clear();
		return this;
	}

	public VanillaBlockMaterial addDropMaterial(Material dropMaterial) {
		return addDropMaterial(dropMaterial, 1);
	}

	public VanillaBlockMaterial addDropMaterial(Material dropMaterial, int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("min must be less than max");
		}
		int j = 0;
		int[] amount = new int[(max - min) + 1];
		for (int i = min; i <= max; i++) {
			amount[j++] = i;
		}
		return addDropMaterial(dropMaterial, amount);
	}

	public VanillaBlockMaterial addDropMaterial(Material dropMaterial, int... amount) {
		dropMaterials.put(dropMaterial, amount);
		return this;
	}

	public VanillaBlockMaterial setDropMaterial(Material dropMaterial) {
		return setDropMaterial(dropMaterial, 1);
	}

	public VanillaBlockMaterial setDropMaterial(Material dropMaterial, int min, int max) {
		dropMaterials.clear();
		return addDropMaterial(dropMaterial, min, max);
	}

	public VanillaBlockMaterial setDropMaterial(Material dropMaterial, int... amount) {
		dropMaterials.clear();
		return addDropMaterial(dropMaterial, amount);
	}

	public Set<Material> getDropMaterialsSet() {
		return dropMaterials.keySet();
	}

	public Map<Material, int[]> getDropMaterialsMap() {
		return dropMaterials;
	}
}
