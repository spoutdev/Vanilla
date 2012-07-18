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
package org.spout.vanilla.material.block;

import org.spout.api.geo.cuboid.Block;

/**
 * A material that can grow over time. Growing could happen using multiple blocks. If that is the case, <br>
 * it does not matter what block you use when changing growth stages.
 */
public interface Growing {
	/**
	 * Gets the mimimum required light for this Growing material to grow
	 * @return minimum light
	 */
	public int getMinimumLightToGrow();

	/**
	 * Gets the amount of growth stages this Growing material has
	 * @return amount of growth stages
	 */
	public int getGrowthStageCount();

	/**
	 * Gets the current growth stage of this Growing material
	 * @param block of the Plant
	 * @return growth stage
	 */
	public int getGrowthStage(Block block);

	/**
	 * Sets the growth stage of this Growing material
	 * @param block of the plant
	 * @param stage to set to
	 */
	public void setGrowthStage(Block block, int stage);

	/**
	 * Gets if this Growing material is fully grown
	 * @param block of the Plant
	 * @return True if fully grown, False if not
	 */
	public boolean isFullyGrown(Block block);
}
