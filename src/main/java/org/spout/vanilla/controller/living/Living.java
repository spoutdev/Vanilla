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
package org.spout.vanilla.controller.living;

import org.spout.vanilla.controller.VanillaController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.action.GravityAction;
import org.spout.vanilla.controller.action.WanderAction;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;

public abstract class Living extends VanillaController {
	private int headYaw = 0, headYawLive = 0;

	protected Living(VanillaControllerType type) {
		super(type);
	}


	@Override
	public void onAttached() {
		super.onAttached();
		registerAction(new WanderAction());
		registerAction(new GravityAction());
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		if (headYawLive != headYaw) {
			headYawLive = headYaw;
			broadcastPacket(new EntityHeadYawMessage(getParent().getId(), headYaw));
		}
	}

	/**
	 * Sets the yaw of a controller's head.
	 * @param headYaw
	 */
	public void setHeadYaw(int headYaw) {
		headYawLive = headYaw;
	}

	public int getHeadYaw() {
		return headYaw;
	}
}
