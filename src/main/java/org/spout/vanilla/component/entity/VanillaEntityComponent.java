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
package org.spout.vanilla.component.entity;

import java.util.HashMap;

import org.spout.api.component.entity.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.VanillaNetworkProtocol;
import org.spout.vanilla.protocol.entity.VanillaEntityProtocol;

public class VanillaEntityComponent extends EntityComponent {

	@Override
	public void onAttached() {
		HashMap<Class<? extends VanillaEntityComponent>, Integer> map = getOwner().getData().get(VanillaData.ATTACHED_COUNT);
		Integer count = map.get(getClass());
		if (count == null) {
			count = 0;
		}
		count++;
		map.put(getClass(), count);
		getOwner().getData().put(VanillaData.ATTACHED_COUNT, map);
		getOwner().setSavable(true);

		// Players initialized in initializeSession
		if (!(getOwner() instanceof Player)) {
			getOwner().add(VanillaNetworkComponent.class);
		}
	}

	protected void setEntityProtocol(VanillaEntityProtocol p) {
		if (!(getOwner().getNetwork() instanceof VanillaNetworkProtocol)) {
			return;
		}
		((VanillaNetworkProtocol) getOwner().getNetwork()).setEntityProtocol(p);
	}

	/**
	 * A counter of how many times this component has been attached to an entity <p> Values > 1 indicate how many times this component has been saved to disk, and reloaded <p> Values == 1 indicate a new
	 * component that has never been saved and loaded.
	 *
	 * @return attached count
	 */
	public final int getAttachedCount() {
		return (Integer) getOwner().getData().get(VanillaData.ATTACHED_COUNT).get(getClass());
	}
}
