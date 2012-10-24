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
package org.spout.vanilla.component.substance.material.chest;

import org.spout.api.Source;
import org.spout.api.entity.Player;

import org.spout.vanilla.component.substance.material.ViewedBlockComponent;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.util.VanillaBlockUtil;

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
	 * @param source {@link org.spout.api.Source} who opened the chest
	 * @param opened state of chest
	 */
	public void setOpened(Source source, boolean opened) {
		this.opened = opened;
		VanillaBlockMaterial.playBlockAction(getBlock(source), (byte) 1, opened ? (byte) 1 : (byte) 0);
	}

	@Override
	public void open(Player player) {
		setOpened(player, true);
	}

	@Override
	public void close(Player player) {
		super.close(player);
		if (viewers.isEmpty()) {
			setOpened(player, false);
		}
	}
}
