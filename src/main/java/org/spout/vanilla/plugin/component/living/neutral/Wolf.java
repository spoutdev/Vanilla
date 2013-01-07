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
package org.spout.vanilla.plugin.component.living.neutral;

import org.spout.vanilla.api.component.Neutral;
import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.protocol.entity.creature.WolfEntityProtocol;

/**
 * A component that identifies the entity as a Wolf.
 */
public class Wolf extends Living implements Neutral {
	private boolean redEyes = false;

	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new WolfEntityProtocol());
	}

	public boolean isTamed() {
		return getOwner().getData().get(VanillaData.TAMED);
	}

	public void setTamed(boolean tamed) {
		getOwner().getData().put(VanillaData.TAMED, tamed);
	}

	public String getOwnerName() {
		return getOwner().getData().get(VanillaData.OWNER);
	}

	public void setOwnerName(String owner) {
		if (isTamed()) {
			getOwner().getData().put(VanillaData.OWNER, owner);
		}
	}

	public boolean haveRedEyes() {
		return redEyes;
	}

	public void setRedEyes(boolean redEyes) {
		this.redEyes = redEyes;
	}

	public boolean isSitting() {
		return getOwner().getData().get(VanillaData.SITTING);
	}

	public void setSitting(boolean sitting) {
		getOwner().getData().put(VanillaData.SITTING, sitting);
	}
}
