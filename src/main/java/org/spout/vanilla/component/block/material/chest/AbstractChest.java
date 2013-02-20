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
package org.spout.vanilla.component.block.material.chest;

import org.spout.api.entity.Player;

import org.spout.vanilla.component.block.ViewedBlockComponent;
import org.spout.vanilla.material.VanillaBlockMaterial;

/**
 * Base class for Chests.
 */
public abstract class AbstractChest extends ViewedBlockComponent {
	private boolean opened = false;

	/**
	 * Whether the chest is toggled open.
	 * @return true if chest is opened
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * Sets the open state of the chest.
	 * @param player who opened the chest
	 * @param opened state of chest
	 */
	public void setOpened(Player player, boolean opened) {
		this.opened = opened;
		VanillaBlockMaterial.playBlockAction(getBlock(), (byte) 1, opened ? (byte) 1 : (byte) 0);
	}

	@Override
	public boolean open(Player player) {
		setOpened(player, true);
		return true;
	}

	@Override
	public boolean close(Player player) {
		if (super.close(player)) {
			if (viewers.isEmpty()) {
				setOpened(player, false);
			}
			return true;
		}
		return false;
	}
}
