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
package org.spout.vanilla.controller.logic.physics;

import org.spout.api.tickable.LogicPriority;
import org.spout.vanilla.controller.living.player.VanillaPlayer;

/**
 * Plays a step sound effect while walking<br>
 * Takes account of vanilla player states such as falling and flying
 */
public class PlayerStepSoundLogic extends StepSoundLogic {

	public PlayerStepSoundLogic(VanillaPlayer parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public VanillaPlayer getParent() {
		return (VanillaPlayer) super.getParent();
	}

	@Override
	public boolean shouldRun(float dt) {
		return super.shouldRun(dt) && !getParent().isCrouching() && !getParent().isFalling() && !getParent().isFlying();
	}
}
