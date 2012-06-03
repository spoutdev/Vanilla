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
package org.spout.vanilla.protocol.event;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;

/**
 * @author zml2008
 */
public class BlockEffectProtocolEvent implements ProtocolEvent {
	private final int range;
	private final PlayEffectMessage.Messages effect;
	private final int data;
	private final Vector3 position;

	public BlockEffectProtocolEvent(Block block, PlayEffectMessage.Messages effect) {
		this(block, effect, 0);
	}

	public BlockEffectProtocolEvent(Block block, PlayEffectMessage.Messages effect, int data) {
		this(block, 16, effect, data);
	}

	public BlockEffectProtocolEvent(Block block, int range, PlayEffectMessage.Messages effect, int data) {
		this(block.getPosition(), range, effect, data);
	}

	public BlockEffectProtocolEvent(Vector3 position, PlayEffectMessage.Messages effect) {
		this(position, effect, 0);
	}

	public BlockEffectProtocolEvent(Vector3 position, PlayEffectMessage.Messages effect, int data) {
		this(position, 16, effect, data);
	}

	public BlockEffectProtocolEvent(Vector3 position, int range, PlayEffectMessage.Messages effect, int data) {
		this.range = range;
		this.effect = effect;
		this.data = data;
		this.position = position;
	}

	public PlayEffectMessage.Messages getEffect() {
		return effect;
	}

	public int getRange() {
		return range;
	}

	public int getData() {
		return data;
	}

	public Vector3 getPosition() {
		return position;
	}
}
