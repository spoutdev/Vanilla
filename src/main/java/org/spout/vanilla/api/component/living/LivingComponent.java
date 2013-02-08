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
package org.spout.vanilla.api.component.living;

import org.spout.api.ai.goap.GoapAIComponent;
import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.component.VanillaComponent;
import org.spout.vanilla.api.component.misc.DrowningComponent;
import org.spout.vanilla.api.component.misc.HeadComponent;
import org.spout.vanilla.api.component.misc.HealthComponent;
import org.spout.vanilla.api.data.VanillaData;

public abstract class LivingComponent extends VanillaComponent {

	public boolean isOnGround() {
		return getOwner().getData().get(VanillaData.IS_ON_GROUND);
	}

	public void setOnGround(boolean onGround) {
		getOwner().getData().put(VanillaData.IS_ON_GROUND, onGround);
	}

	public abstract HeadComponent getHead();

	public abstract HealthComponent getHealth();

	public abstract DrowningComponent getDrowning();

	public abstract NavigationComponent getNavigation();

	public abstract GoapAIComponent getAI();

	public boolean isRiding() {
		return getOwner().getData().get(VanillaData.IS_RIDING);
	}

	public void setRiding(boolean isRiding) {
		getOwner().getData().put(VanillaData.IS_RIDING, isRiding);
		sendMetaData();
	}

	public boolean isEatingBlocking() {
		return getOwner().getData().get(VanillaData.IS_EATING_BLOCKING);
	}

	public void setEatingBlocking(boolean isEatingBlocking) {
		getOwner().getData().put(VanillaData.IS_EATING_BLOCKING, isEatingBlocking);
		sendMetaData();
	}

	public boolean isSneaking() {
		return getOwner().getData().get(VanillaData.IS_SNEAKING);
	}

	public void setSneaking(boolean isSneaking) {
		getOwner().getData().put(VanillaData.IS_SNEAKING, isSneaking);
		sendMetaData();
	}

	public void sendMetaData() {
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 0, getCommonMetadata()));
	}

	protected abstract byte getCommonMetadata();
}
