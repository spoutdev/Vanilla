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

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;

/**
 * Caused when a block is ignited
 * todo needs be implemented
 */
public class BlockIgniteCause extends BlockChangeCause {
	/**
	 * The different causes why a Block was ignited.
	 */
	public enum IgniteCause {
		/**
		 * Block ignition caused by Lava
		 */
		LAVA,
		/**
		 * Block ignition caused by using the Lightener
		 */
		FLINT_AND_STEEL,
		/**
		 * Block ignition caused by dynamic spread of fire
		 */
		SPREAD,
		/**
		 * Block ignition caused by lightning
		 */
		LIGHTING,
		/**
		 * Block ignition caused by a fireball
		 */
		FIREBALL,
	}

	private final IgniteCause igniteCause;
	private final Entity entity;

	/**
	 * Contains the igniteCause and an entity of the block which was ignited.
	 * @param block which was ignited
	 * @param igniteCause which caused the block to ignite
	 * @param entity which ignited the block
	 */
	public BlockIgniteCause(Block block, IgniteCause igniteCause, Entity entity) {
		super(block);
		this.igniteCause = igniteCause;
		this.entity = entity;
	}

	/**
	 * Contains the igniteCause of the block which was ignited.
	 * @param block which was ignited
	 * @param igniteCause which caused the block to ignite
	 */
	public BlockIgniteCause(Block block, IgniteCause igniteCause) {
		super(block);
		this.igniteCause = igniteCause;
		this.entity = null;
	}

	/**
	 * Gets the {@link org.spout.vanilla.event.cause.BlockIgniteCause.IgniteCause} which ignited the block
	 * @return the igniteCause
	 */
	public IgniteCause getIgniteCause() {
		return igniteCause;
	}

	/**
	 * Gets the entity which ignited the block if applicable.
	 * NOTE: This can be NULL
	 * @return the entity which ignited the block, can be NULL
	 */
	public Entity getEntity() {
		return entity;
	}
}
