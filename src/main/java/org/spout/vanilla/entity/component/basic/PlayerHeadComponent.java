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
package org.spout.vanilla.entity.component.basic;

import static org.spout.vanilla.util.VanillaMathHelper.getLookAtPitch;
import static org.spout.vanilla.util.VanillaMathHelper.getLookAtYaw;

import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;
import org.spout.vanilla.entity.VanillaPlayerController;

public class PlayerHeadComponent extends HeadComponent {

	@Override
	public VanillaPlayerController getParent() {
		return (VanillaPlayerController) super.getParent();
	}

	@Override
	public float getHeight() {
		float height = super.getHeight();
		if (this.getParent().isCrouching()) {
			height -= 0.08f;
		}
		return height;
	}

	@Override
	public Transform getTransform() {
		Transform trans = new Transform();
		trans.setPosition(this.getPosition());
		Vector3 offset = this.getLookingAt();
		trans.setRotation(MathHelper.rotation(getLookAtPitch(offset), getLookAtYaw(offset), 0.0f));
		return trans;
	}
}
