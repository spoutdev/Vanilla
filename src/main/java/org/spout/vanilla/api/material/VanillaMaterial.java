/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.api.material;

import org.spout.api.material.source.MaterialSource;
import org.spout.api.math.Vector2;
import org.spout.api.render.RenderMaterial;

public interface VanillaMaterial extends MaterialSource {
	/**
	 * Gets whether or not this material stores additional NBT tag data
	 */
	public boolean hasNBTData();

	/**
	 * Gets the amount of damage this material can deal as an item
	 */
	public int getDamage();

	/**
	 * Sets the amount of damage this material can deal as an item
	 */
	public VanillaMaterial setDamage(int damage);

	/**
	 * Gets the associated 'minecraft' or notchian id for this material
	 * @return minecraft id
	 */
	public int getMinecraftId();

	/**
	 * Gets the associated 'minecraft' or notchian data for this material
	 * @param data to convert
	 * @return minecraft data
	 */
	public short getMinecraftData(short data);

	public RenderMaterial getRenderMaterial();

	public Vector2 getPosition();
}
