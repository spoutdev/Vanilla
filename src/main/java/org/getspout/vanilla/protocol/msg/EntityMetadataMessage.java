/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.protocol.msg;

import java.util.List;

import org.getspout.api.protocol.Message;
import org.getspout.api.util.Parameter;

public final class EntityMetadataMessage extends Message {
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
		StringBuilder build = new StringBuilder("EntityInteractionMessage{id=");
		build.append(id).append(",metadata=[");

		for (Parameter<?> param : parameters) {
			build.append(param.toString()).append(",");
		}

		build.append("]}");
		return build.toString();
	}
}