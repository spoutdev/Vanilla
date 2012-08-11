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

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;

import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.material.item.weapon.Sword;

public class CakeBlock extends Solid implements Mineable {
	public CakeBlock(String name, int id) {
		super(name, id);
		this.setHardness(0.5F).setResistance(0.8F).setTransparent();
		this.getDrops().clear();
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Sword ? (short) 2 : (short) 1;
	}

	@Override
	public void onInteractBy(Entity entity, Block block, PlayerInteractEvent.Action type, BlockFace clickedface) {
		if (type == PlayerInteractEvent.Action.RIGHT_CLICK) {
			if (block.getData() == CakeSize.ONE_PIECE.getData()) {
				// Cake has been fully eaten
				block.setMaterial(VanillaMaterials.AIR);
			} else {
				setSize(block, CakeSize.getByData((short) (getSize(block).getData() + 1)));
			}

			if (entity.getController() instanceof VanillaPlayerController) {
				VanillaPlayerController player = (VanillaPlayerController) entity.getController();
				player.getSurvivalComponent().setHunger((short) (player.getSurvivalComponent().getHunger() + 2));
			}
		}
	}

	/**
	 * Returns the {@link CakeSize} of this cake material.
	 * @param block Block with this cake material
	 * @return Size of this material
	 */
	public CakeSize getSize(Block block) {
		return CakeSize.getByData(block.getData());
	}

	/**
	 * Sets the {@link CakeSize} of this cake material.
	 * @param block Block with this cake material
	 * @param size Size to set
	 */
	public void setSize(Block block, CakeSize size) {
		block.setData(size.getData());
	}

	/**
	 * Represents the size of a cake material.
	 */
	public enum CakeSize implements DataSource {
		FULL(0x0),
		FIVE_PIECES(0x1),
		FOUR_PIECES(0x2),
		THREE_PIECES(0x3),
		TWO_PIECES(0x4),
		ONE_PIECE(0x5);
		private short data;

		private CakeSize(int data) {
			this.data = (short) data;
		}

		@Override
		public short getData() {
			return data;
		}

		public static CakeSize getByData(short data) {
			for (CakeSize size : CakeSize.values()) {
				if (size.data == data) {
					return size;
				}
			}

			return null;
		}
	}
}
