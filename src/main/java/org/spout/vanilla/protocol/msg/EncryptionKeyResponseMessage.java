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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.ChannelProcessor;
import org.spout.api.protocol.ProcessorHandler;
import org.spout.api.protocol.ProcessorSetupMessage;
import org.spout.api.util.SpoutToStringStyle;

public class EncryptionKeyResponseMessage implements ProcessorSetupMessage {
	private final byte[] secret, verifyToken;
	private final boolean locking; //TODO: Locking is not used in the codec...should locking be removed?
	private ChannelProcessor processor;
	private ProcessorHandler handler;

	public EncryptionKeyResponseMessage(boolean locking, byte[] secret, byte[] verifyToken) {
		this.locking = locking;
		this.secret = secret;
		this.verifyToken = verifyToken;
	}

	@Override
	public ChannelProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ChannelProcessor processor) {
		this.processor = processor;
	}

	@Override
	public void setProcessorHandler(ProcessorHandler handler) {
		this.handler = handler;
	}

	public ProcessorHandler getProcessorHandler() {
		return handler;
	}

	public byte[] getSecretArray() {
		return secret;
	}

	public byte[] getVerifyTokenArray() {
		return verifyToken;
	}

	@Override
	public boolean isChannelLocking() {
		return locking;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EncryptionKeyResponseMessage other = (EncryptionKeyResponseMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.isChannelLocking(), other.isChannelLocking())
				.append(this.getSecretArray(), other.getSecretArray())
				.append(this.getVerifyTokenArray(), other.getVerifyTokenArray())
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("locking", this.isChannelLocking())
				.append("secret", this.getSecretArray())
				.append("verifyToken", this.getVerifyTokenArray())
				.toString();
	}
}
