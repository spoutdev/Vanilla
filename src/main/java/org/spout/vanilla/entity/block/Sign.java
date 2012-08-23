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
package org.spout.vanilla.entity.block;

import java.util.HashSet;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.EventManager;
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.entity.VanillaBlockController;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.event.block.SignChangeEvent;
import org.spout.vanilla.event.block.SignUpdateEvent;
import org.spout.vanilla.material.VanillaMaterials;

public class Sign extends VanillaBlockController {
	private String[] text = new String[4];
	private HashSet<Player> dirty = new HashSet<Player>();
	private int range = 20;
	private EventManager eventManager = Spout.getEngine().getEventManager();

	public Sign() {
		super(VanillaControllerTypes.SIGN, VanillaMaterials.WALL_SIGN);
	}

	@Override
	public void onAttached() {
	}

	@Override
	public void onTick(float dt) {
		Block block = getBlock();
		HashSet<Player> nearby = new HashSet<Player>();
		nearby.addAll(block.getWorld().getNearbyPlayers(block.getPosition(), range));
		if (nearby.isEmpty()) {
			return;
		}

		if (dirty.isEmpty() || !dirty.containsAll(nearby)) {
			nearby.removeAll(dirty);
			dirty = nearby;
			update();
		}
	}

	/**
	 * Gets the block range within players can see changes to this Sign
	 * @return player view block range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Sets the block range within players can see changes to this Sign
	 * @param range to set to
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * Gets the current lines of text of this Sign
	 * @return an array of String lines shown on this Sign
	 */
	public String[] getText() {
		return text;
	}

	/**
	 * Sets the current lines of this Sign
	 * @param source which is setting the text lines
	 * @param text to set to
	 */
	public void setText(Source source, String[] text) throws IllegalArgumentException {
		if (text.length != 4) {
			throw new IllegalArgumentException("The amount of lines have to equal 4");
		}
		SignChangeEvent signChangeEvent = eventManager.callEvent(new SignChangeEvent(this, source, text));
		if (!signChangeEvent.isCancelled()) {
			this.text = text;
			update();
		}
	}

	/**
	 * Gets the line text at a certain line row
	 * @param line row index
	 * @return the text of that row
	 */
	public String getLine(int line) {
		return text[line];
	}

	/**
	 * Sets the line text at a certain line row
	 * @param source which sets the line
	 * @param line row index
	 * @param text to set the line to
	 */
	public void setLine(Source source, int line, String text) {
		String[] temp = this.text;
		temp[line] = text;
		SignChangeEvent signChangeEvent = eventManager.callEvent(new SignChangeEvent(this, source, temp));
		if (!signChangeEvent.isCancelled()) {
			this.text[line] = text;
			update();
		}
	}

	private void update() {
		for (Player player : dirty) {
			player.getNetworkSynchronizer().callProtocolEvent(new SignUpdateEvent(this, text));
		}
	}
}
