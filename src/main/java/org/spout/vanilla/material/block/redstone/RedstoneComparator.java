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
package org.spout.vanilla.material.block.redstone;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Container;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.util.InventoryIterator;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.block.BlockSnapshot;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.data.RedstonePowerMode;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.util.PlayerUtil;
import org.spout.vanilla.util.RedstoneUtil;

public class RedstoneComparator extends GroundAttachable implements Directional, RedstoneSource, RedstoneTarget, DynamicMaterial, InitializableMaterial {
	private static final Material[] inputs;
	private static final int DIRECTION_MASK = 0x3;
	private static final int TICK_DELAY = 500; // This block pulses every half second when powered.
	private static final EffectRange[] PHYSIC_RANGES;
	private final boolean powered;

	static {
		PHYSIC_RANGES = new EffectRange[4];
		for (int i = 0; i < PHYSIC_RANGES.length; i++) {
			PHYSIC_RANGES[i] = new ListEffectRange(BlockFaces.ESWN.get(i));
		}
		inputs = new Material[] {VanillaMaterials.HOPPER, VanillaMaterials.CHEST, VanillaMaterials.TRAPPED_CHEST_BLOCK, VanillaMaterials.DISPENSER, VanillaMaterials.BREWING_STAND_BLOCK, VanillaMaterials.FURNACE, VanillaMaterials.FURNACE_BURNING};
	}

	public RedstoneComparator(String name, int id, boolean powered) {
		//TODO: Box Shape
		super(name, id, null, null);
		this.powered = powered;
		this.setHardness(0.0F).setResistance(0.0F).setOpacity(0).setOcclusion((short) 0, BlockFace.BOTTOM);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().add(VanillaMaterials.REDSTONE_COMPARATOR);
	}

	public boolean isPowered() {
		return this.powered;
	}

	@Override
	public byte getLightLevel(short data) {
		return powered ? (byte) 9 : (byte) 0;
	}

	public void setPowered(Block block, boolean powered) {
		block.setMaterial(powered ? VanillaMaterials.REDSTONE_COMPARATOR_ON : VanillaMaterials.REDSTONE_COMPARATOR_OFF, block.getBlockData());
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3f clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause));
		Block input = block.translate(this.getFacing(block).getOpposite());
		if (input.getMaterial().isMaterial(inputs)) {
			Inventory inventory = input.getType(Container.class).getInventory();
			boolean viewed = false;
			for (InventoryViewer viewer : inventory.getViewers()) {
				if (viewer instanceof ComparableViewer) {
					viewed = true;
					((ComparableViewer) viewer).addViewer(block);
					break;
				}
			}
			if (!viewed) {
				inventory.addViewer(new ComparableViewer(block, inventory));
			}
		}
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		Block input = block.translate(this.getFacing(block).getOpposite());
		if (input.getMaterial().isMaterial(inputs)) {
			Inventory inventory = input.getType(Container.class).getInventory();
			ComparableViewer cViewer = null;
			for (InventoryViewer viewer : inventory.getViewers()) {
				if (viewer instanceof ComparableViewer) {
					cViewer = (ComparableViewer) viewer;
				}
			}
			if (cViewer != null) {
				cViewer.removeViewer(block);
			}
		}
		return super.onDestroy(block, cause);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteractBy(entity, block, type, clickedFace);
		if (type == Action.RIGHT_CLICK) {
			// Flip inputs A & B. (if B is greater than A output B - A)
			// Or if inputs are already flipped, revert back to original.
		}
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		boolean receiving = this.isReceivingPower(block);
		if (this.isPowered() != receiving) {
			block.dynamicUpdate(block.getWorld().getAge() + TICK_DELAY, receiving ? 1 : 0, false);
		}
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.ESWN.get(block.getDataField(DIRECTION_MASK));
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setDataField(DIRECTION_MASK, BlockFaces.ESWN.indexOf(facing, 0));
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return 0;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		BlockFace face = this.getFacing(block).getOpposite();
		return RedstoneUtil.isEmittingPower(block.translate(face), getFacing(block));
	}

	@Override
	public void onFirstUpdate(Block block, long currentTime) {
		block.dynamicUpdate(TICK_DELAY + currentTime, false);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		boolean receiving = this.isReceivingPower(block);
		if ((data & 1) == 1) {
			// Was receiving and should power up
			if (!this.isPowered()) {
				this.setPowered(block, true);
			}
			if (!receiving) {
				block.dynamicUpdate(updateTime + TICK_DELAY, false);
			}
		} else if (receiving != this.isPowered()) {
			// Was not receiving and should update state
			this.setPowered(block, receiving);
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public EffectRange getPhysicsRange(short data) {
		return PHYSIC_RANGES[data & 0x3];
	}

	@Override
	public short getDirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasDirectRedstonePower(block, direction, powerMode) ? (short) 1 : 0; //TODO: Redstone power is conditional on input blocks, and comparator setting
	}

	@Override
	public boolean hasDirectRedstonePower(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.isPowered() && this.getFacing(block) == direction;
	}

	@Override
	public short getRedstonePowerStrength(BlockSnapshot state) {
		return this.isPowered() ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;  //TODO:
	}

	private short getInputAStrength(Block block) {
		Block input = block.translate(this.getFacing(block).getOpposite());
		if (input.getMaterial().isMaterial(inputs)) {
			Inventory inv = input.getType(Container.class).getInventory();
			ComparableViewer view = null;
			for (InventoryViewer viewer : inv.getViewers()) {
				if (viewer instanceof ComparableViewer) {
					view = (ComparableViewer) viewer;
				}
			}
			if (view != null) {
				return view.getRedstonePower();
			} else {
				return 0;
			}
		}
		return 0;
	}

	private short getInputBStrength(Block block) {
		BlockFace dir = this.getFacing(block);
		BlockFace opp = dir.getOpposite();
		short power = 0;
		for (BlockFace face : BlockFaces.ESWN) {
			if (face == dir || face == opp) {
				continue;
			}
			Block toCheck = block.translate(face);
			VanillaBlockMaterial material = (VanillaBlockMaterial) toCheck.getMaterial();
			short matPower = material.getRedstonePower(toCheck);
			short indPower = material.getIndirectRedstonePower(toCheck, face.getOpposite(), RedstonePowerMode.ALLEXCEPTWIRE);
			if (matPower > power) {
				power = matPower;
			}
			if (indPower > power) {
				power = indPower;
			}
		}
		return power;
	}

	public class ComparableViewer implements InventoryViewer {
		private final Set<Block> viewingBlocks = new HashSet<Block>(1);
		private final Inventory viewed;
		private final double MAX_ITEM_VALUE;
		private int currentValue = 0;
		private short redstonePower = 0;

		public ComparableViewer(Block block, Inventory inventory) {
			viewed = inventory;
			viewingBlocks.add(block);

			MAX_ITEM_VALUE = inventory.size() * 64.0;

			InventoryIterator iter = inventory.iterator();
			while (iter.hasNext()) {
				ItemStack next = iter.next();
				if (next != null && !next.isEmpty()) {
					currentValue += getItemStackValue(next);
				}
			}
			updateRedstoneValue();
			inventory.addViewer(this);
		}

		@Override
		public void onSlotSet(Inventory inventory, int slot, ItemStack item, ItemStack previous) {
			currentValue -= getItemStackValue(previous);
			currentValue += getItemStackValue(item);
			updateRedstoneValue();
		}

		private int getItemStackValue(ItemStack item) {
			if (item == null || item.isEmpty()) {
				return 0;
			}
			return (item.getAmount() * 64) / item.getMaxStackSize();
		}

		private void updateRedstoneValue() {
			redstonePower = (short) (1 + (currentValue / MAX_ITEM_VALUE) * (VanillaBlockMaterial.REDSTONE_POWER_MAX - 1));
		}

		public short getRedstonePower() {
			return redstonePower;
		}

		public boolean isViewing(Block block) {
			return viewingBlocks.contains(block);
		}

		public void addViewer(Block block) {
			viewingBlocks.add(block);
		}

		public void removeViewer(Block block) {
			viewingBlocks.remove(block);
			if (viewingBlocks.isEmpty()) {
				viewed.removeViewer(this);
			}
		}
	}
}
