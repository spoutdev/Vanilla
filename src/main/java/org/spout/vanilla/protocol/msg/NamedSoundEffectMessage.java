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
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class NamedSoundEffectMessage extends Message {

	private final String soundName;
	private final int effectPositionX, effectPositionY, effectPositionZ;
	private final float volume;
	private final byte pitch;
	public NamedSoundEffectMessage(String soundName, int effectPositionX, int effectPositionY, int effectPositionZ, float volume, byte pitch) {
		this.soundName = soundName;
		this.effectPositionX = effectPositionX;
		this.effectPositionY = effectPositionY;
		this.effectPositionZ = effectPositionZ;
		this.volume = volume;
		this.pitch = pitch;
	}

	public String getSoundName() {
		return soundName;
	}

	public int getEffectPositionX() {
		return effectPositionX;
	}

	public int getEffectPositionY() {
		return effectPositionY;
	}

	public int getEffectPositionZ() {
		return effectPositionZ;
	}

	public float getVolume() {
		return volume;
	}

	public byte getPitch() {
		return pitch;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("soundName", soundName)
				.append("effectPositionX", effectPositionX)
				.append("effectPositionY", effectPositionY)
				.append("effectPositionZ", effectPositionZ)
				.append("volume", volume)
				.append("pitch", pitch)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NamedSoundEffectMessage other = (NamedSoundEffectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.soundName, other.soundName)
				.append(this.effectPositionX, other.effectPositionX)
				.append(this.effectPositionY, other.effectPositionY)
				.append(this.effectPositionZ, other.effectPositionZ)
				.append(this.volume, other.volume)
				.append(this.pitch, other.pitch)
				.isEquals();
	}

}
