/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.block;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.player.Player;
import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;

import java.util.HashSet;
import java.util.Set;

public class SignController extends VanillaBlockController {
	private String[] text = new String[4];
	private Set<Player> dirty = new HashSet<Player>();
	private int range = 20;

	public SignController() {
		super(VanillaControllerTypes.SIGN, VanillaMaterials.SIGN.getPlacedMaterial());
	}

	@Override
	public void onAttached() {

	}

	@Override
	public void onTick(float dt) {
		Block block = getBlock();
		Set<Player> nearby = block.getRegion().getNearbyPlayers(block.getPosition(), range);
		if (nearby == null || nearby.isEmpty()) {
			return;
		}

		if (!dirty.containsAll(nearby)) {
			nearby.removeAll(dirty);
			dirty = nearby;
			update();
		}
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public String getLine(int line) {
		return text[line + 1];
	}

	public void setLine(String text, int line) {
		this.text[line + 1] = text;
	}

	private void update() {
		Block block = getBlock();
		VanillaNetworkSynchronizer.sendPacket((Player[]) dirty.toArray(), new UpdateSignMessage(block.getX(), block.getY(), block.getZ(), text));
	}
}

