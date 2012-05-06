/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import java.util.List;

import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

public final class EntityMetadataMessage extends Message {
	//Entity flags
	public static final Parameter<Byte> META_INFLAMED = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x01);
	public static final Parameter<Byte> META_CROUCHED = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x02);
	public static final Parameter<Byte> META_MOBRIDER = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x04);
	public static final Parameter<Byte> META_SPRINTING = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x08);
	public static final Parameter<Byte> META_RIGHTCLICKACTION = new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0x10);

	/**
	 * 	Drowning counter for entities. This should be sent when an entity spawns and be decremented
	 * 	per tick. If the value hits -19, send a 0x26 and reset this to 0
	 */
	public static final Parameter<Short> META_FULLDROWNINGCOUNTER = new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 300);
	public static final Parameter<Short> META_DROWNINGCOUNTEDDEPLETED = new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) -19);
	public static final Parameter<Short> META_STARTDROWNING = new Parameter<Short>(Parameter.TYPE_SHORT, 1, (short) 0);

	/**
	 * Potion color for effect. The value is composed of RRGGBB (in that order).
	 */
	public static final Parameter<Integer> META_NOPOTIONEFFECT = new Parameter<Integer>(Parameter.TYPE_INT, 8, (int) 000000);

	/**
	 * Animal growth and control. -23999 is the value for babies, 6000 is the value for adults. When set
	 * to 6000, decrease over time (to determine when to breed again). Value of 0 means the adult can breed.
	 */
	public static final Parameter<Integer> META_BABYANIMALSTAGE = new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) -23999);
	public static final Parameter<Integer> META_PARENTANIMALSTAGE = new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) 6000);
	public static final Parameter<Integer> META_BREEDANIMALSTAGE = new Parameter<Integer>(Parameter.TYPE_INT, 12, (int) 0);

	private final int id;
	private final List<Parameter<?>> parameters;

	public EntityMetadataMessage(int id, List<Parameter<?>> parameters) {
		this.id = id;
		this.parameters = parameters;
	}

	public int getId() {
		return id;
	}

	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("EntityMetadataMessage{id=");
		build.append(id).append(",metadata=[");

		for (Parameter<?> param : parameters) {
			build.append(param.toString()).append(",");
		}

		build.append("]}");
		return build.toString();
	}
}
