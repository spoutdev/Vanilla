/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * vanilla is distributed in the hope that it will be useful,
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
package org.spout.vanilla.controller.object;

import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.action.MovingBlockAction;

/**
 * Represents a block that can move, such as sand or gravel.
 */
public class MovingBlock extends Substance {
	private final BlockMaterial block;

	public MovingBlock(BlockMaterial block) {
		this(VanillaControllerTypes.FALLING_BLOCK, block);

	}

	protected MovingBlock(VanillaControllerType type, BlockMaterial block) {
		super(type);
		this.block = block;
		registerAction(new MovingBlockAction());
	}

	/**
	 * Gets the block that is moving.
	 * @return moving block.
	 */
	public BlockMaterial getBlock() {
		return block;
	}
}
