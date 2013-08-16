/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.msg;

import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spout.api.Platform;
import org.spout.api.Spout;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.dynamicid.DynamicWrapperMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.VanillaProtocol;

public class ServerPluginMessage extends VanillaMainChannelMessage implements DynamicWrapperMessage {
	private final byte[] data;
	private final String type;

	public ServerPluginMessage(String type, byte[] data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("data", data)
				.append("type", type)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(61, 33)
				.append(data)
				.append(type)
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

		final ServerPluginMessage other = (ServerPluginMessage) obj;
		return new EqualsBuilder()
				.append(this.data, other.data)
				.append(this.type, other.type)
				.isEquals();
	}

	@Override
	public Message unwrap(Protocol activeProtocol) throws IOException {
		MessageCodec<?> codec = VanillaProtocol.getCodec(getType(), activeProtocol);
		if (codec != null) {
			return codec.decode(Spout.getPlatform() == Platform.CLIENT, Unpooled.wrappedBuffer(getData()));
		} else {
			return null;
		}
	}
}
