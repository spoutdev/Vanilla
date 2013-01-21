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
/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
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
package org.spout.vanilla.plugin.protocol.msg.world.block;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jboss.netty.buffer.ChannelBuffer;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.Protocol;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.api.protocol.msg.VanillaMainChannelMessage;
import org.spout.vanilla.plugin.protocol.codec.world.block.CommandBlockCodec;
import org.spout.vanilla.plugin.protocol.msg.ServerPluginMessage;

public class CommandBlockMessage extends VanillaMainChannelMessage {
	private int x, y, z;
	private String command;
	
	public CommandBlockMessage(int x, int y, int z, String command) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.command = command;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public String getCommandInput() {
		return command;
	}
	
	public Message wrap(boolean upstream, Protocol activeProtocol) {
		ChannelBuffer buffer = ((CommandBlockCodec) activeProtocol.getCodecLookupService().find(this.getClass())).encode(this);
		byte[] data = new byte[buffer.readableBytes()];
		System.arraycopy(buffer.array(), 0, data, 0, data.length);
		return new ServerPluginMessage(CommandBlockCodec.channelName, data);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("command", command)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(61, 33)
				.append(x)
				.append(y)
				.append(z)
				.append(command)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		final CommandBlockMessage other = (CommandBlockMessage) obj;
		return new EqualsBuilder()
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.command, other.command)
				.isEquals();
	}
}
