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
package org.spout.vanilla.component.entity.misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import gnu.trove.map.hash.TByteObjectHashMap;

import org.spout.api.ClientOnly;
import org.spout.api.ServerOnly;
import org.spout.api.map.DefaultedKey;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.data.Metadata;

/**
 * Stores all the Metadata values as known by the Mojang client/server protocol. It is kept updated on demand. If no Mojang clients are using this server, or this client is not connected to a Mojang
 * server, it is not kept updated.<br><br>
 *
 * Please do not use this component to update or change metadata values of Entities. Use the Entity data map instead, since that is what this component generally uses to generate the metadata values.
 */
public class MetadataComponent extends VanillaEntityComponent {
	private final TByteObjectHashMap<Metadata<?>> meta = new TByteObjectHashMap<>();

	@Override
	public void onDetached() {
		// Clear all metadata - it is no longer valid at this point
		meta.clear();
	}

	/**
	 * Adds a new Metadata entry to be updated by and for the Entity. The entry directly links to an entry in the Entity datatable.
	 *
	 * @param index of the metadata parameter
	 * @param type of the metadata parameter
	 * @param dataKey for the Entity datatable to access the value
	 */
	public <T extends Serializable> void addMeta(int type, int index, final DefaultedKey<T> dataKey) {
		addMeta(new Metadata<T>(type, index) {
			@Override
			public T getValue() {
				return getOwner().getData().get(dataKey);
			}

			@Override
			public void setValue(T value) {
				getOwner().getData().put(dataKey, value);
			}
		});
	}

	/**
	 * Adds a new Metadata entry to be updated by and for the Entity. The entry directly links to an entry in the Entity datatable. The boolean state is turned into a 1 or 0 byte value.
	 *
	 * @param index of the metadata parameter
	 * @param dataKey for the Entity datatable to access the value
	 */
	public void addBoolMeta(int index, final DefaultedKey<Boolean> dataKey) {
		addBoolMeta(index, dataKey, false);
	}

	/**
	 * Adds a new Metadata entry to be updated by and for the Entity. The entry directly links to an entry in the Entity datatable. The boolean state is turned into a 1 or 0 byte value. If inverted is
	 * set to True, the 1 and 0 are turned around.
	 *
	 * @param index of the metadata parameter
	 * @param dataKey for the Entity datatable to access the value
	 * @param inverted - True to use 1 for 0 and 0 for 1, False for default
	 */
	public void addBoolMeta(int index, final DefaultedKey<Boolean> dataKey, boolean inverted) {
		if (inverted) {
			addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, index) {
				@Override
				public Byte getValue() {
					return getOwner().getData().get(dataKey) ? (byte) 0 : (byte) 1;
				}

				@Override
				public void setValue(Byte value) {
					getOwner().getData().put(dataKey, value.byteValue() != (byte) 1);
				}
			});
		} else {
			addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, index) {
				@Override
				public Byte getValue() {
					return getOwner().getData().get(dataKey) ? (byte) 1 : (byte) 0;
				}

				@Override
				public void setValue(Byte value) {
					getOwner().getData().put(dataKey, value.byteValue() == (byte) 1);
				}
			});
		}
	}

	/**
	 * Adds a new Metadata entry to be updated by and for the Entity. If an entry with the same index already exists, it is overwritten.
	 *
	 * @param metadata entry to add
	 */
	public void addMeta(Metadata<?> metadata) {
		meta.put((byte) (metadata.getIndex() & 255), metadata);
	}

	/**
	 * Removes a Metadata entry so it is no longer being updated
	 * 
	 * @param index to remove
	 */
	public void removeMeta(int index) {
		meta.remove((byte) (index & 255));
	}

	/**
	 * Tries to set a Metadata field to a new value using a Parameter.
	 *
	 * @param parameter to set to
	 */
	@ClientOnly
	public <T> void setParameter(Parameter<T> parameter) {
		Metadata<T> metadata = getMeta(parameter.getIndex());
		if (metadata == null) {
			VanillaPlugin.getInstance().getLogger().log(Level.WARNING, "Tried setting non-existent parameter: " + parameter);
		} else if (metadata.getType() != parameter.getType()) {
			VanillaPlugin.getInstance().getLogger().log(Level.WARNING, "Metadata and Parameter type mixmatch: " + parameter + " on " + metadata);
		} else {
			metadata.setParameter(parameter);
		}
	}

	/**
	 * Tries to set all Metadata fields to the new values using a List of Parameters.
	 *
	 * @param parameters to set to
	 */
	public void setParameters(List<Parameter<?>> parameters) {
		for (Parameter<?> param : parameters) {
			setParameter(param);
		}
	}

	/**
	 * Gets all Parameters used for spawning the Entity owner of this Metadata Component
	 *
	 * @return List of spawn Parameters
	 */
	@ServerOnly
	public List<Parameter<?>> getSpawnParameters() {
		List<Parameter<?>> parameters = new ArrayList<>(meta.size());
		for (Metadata<?> metadata : meta.valueCollection()) {
			parameters.add(metadata.getSpawnParameter());
		}
		return parameters;
	}

	/**
	 * Gets all Parameters used for updating the Entity owner of this Metadata Component
	 *
	 * @return List of update Parameters
	 */
	@ServerOnly
	public List<Parameter<?>> getUpdateParameters() {
		List<Parameter<?>> parameters = new ArrayList<>();
		for (Metadata<?> metadata : meta.valueCollection()) {
			Parameter<?> updateParam = metadata.getUpdateParameter();
			if (updateParam != null) {
				parameters.add(updateParam);
			}
		}
		return parameters;
	}

	@Override
	public boolean canTick() {
		return false;
	}

	@SuppressWarnings ("unchecked")
	private <T> Metadata<T> getMeta(int index) {
		return (Metadata<T>) meta.get((byte) (index & 255));
	}
}
