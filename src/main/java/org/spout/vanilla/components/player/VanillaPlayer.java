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
package org.spout.vanilla.components.player;

import org.spout.api.component.components.EntityComponent;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.entity.VanillaPlayerProtocol;

/**
 * A component that identifies the entity as a Vanilla player.
 */
public class VanillaPlayer extends EntityComponent {
	public VanillaPlayer() {
	}

	@Override
	public void onAttached() {
		getHolder().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new VanillaPlayerProtocol());
	}

	public boolean isOnGround() {
		return getHolder().getData().get(VanillaData.IS_ON_GROUND);
	}

	public void setOnGround(boolean onGround) {
		getHolder().getData().put(VanillaData.IS_ON_GROUND, onGround);
	}

	public boolean isFlying() {
		return getHolder().getData().get(VanillaData.IS_FLYING);
	}

	public void setFlying(boolean isFlying) {
		getHolder().getData().put(VanillaData.IS_FLYING, isFlying);
	}

	public boolean isSprinting() {
		return getHolder().getData().get(VanillaData.IS_SPRINTING);
	}

	public void setSprinting(boolean isSprinting) {
		getHolder().getData().put(VanillaData.IS_SPRINTING, isSprinting);
	}

	public boolean isFalling() {
		return getHolder().getData().get(VanillaData.IS_FALLING);
	}

	public void setFalling(boolean isFalling) {
		getHolder().getData().put(VanillaData.IS_FALLING, isFalling);
	}

	public boolean isJumping() {
		return getHolder().getData().get(VanillaData.IS_JUMPING);
	}

	public void setJumping(boolean isJumping) {
		getHolder().getData().put(VanillaData.IS_JUMPING, isJumping);
	}
}
