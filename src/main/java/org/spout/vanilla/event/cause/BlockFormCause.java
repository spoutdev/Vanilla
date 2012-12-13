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
package org.spout.vanilla.event.cause;

import org.spout.api.geo.cuboid.Block;

/**
 * Caused when a block is formed or spreads
 * todo needs to be implemented
 */
public class BlockFormCause extends BlockChangeCause {
	/**
	 * The different causes why a Block is formed in the world.
	 */
	public enum FormCause {
		/**
		 * Block spread randomly
		 */
		SPREAD_RANDOM,
		/**
		 * Block spread from other block
		 */
		SPREAD_FROM,
		/**
		 * Block formed due to world conditions (for example Snow)
		 */
		FORMING,
	}

	private final FormCause formCause;
	private final Block spreadFrom;

	/**
	 * Contains the formCause of the block which has formed in the world.
	 * @param block which was formed
	 * @param formCause which caused the block to be formed
	 */
	public BlockFormCause(Block block, FormCause formCause) {
		super(block);
		this.formCause = formCause;
		this.spreadFrom = null;
	}

	/**
	 * Contains the formCause of the block which has formed the world.
	 * @param block which was formed
	 * @param formCause which caused the block to be formed
	 * @param spreadFrom the block from where the cause spread
	 */
	public BlockFormCause(Block block, FormCause formCause, Block spreadFrom) {
		super(block);
		this.formCause = formCause;
		this.spreadFrom = spreadFrom;
	}

	/**
	 * Gets the {@link org.spout.vanilla.event.cause.BlockFormCause.FormCause} which removed the block
	 * @return the removeCause
	 */
	public FormCause getFormCause() {
		return formCause;
	}

	/**
	 * Block from which the spread occurred, only valid for SPREAD_FROM.
	 * Note: Can be NULL
	 * @return Block from which the spread occurred, NULL when not SPREAD_FROM
	 */
	public Block getSpreadFrom() {
		return spreadFrom;
	}
}
