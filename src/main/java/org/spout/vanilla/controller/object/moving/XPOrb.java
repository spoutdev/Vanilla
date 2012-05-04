/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * vanilla is distributed in the hope that it will be useful,
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
package org.spout.vanilla.controller.object.moving;

import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.object.Substance;

public class XPOrb extends Substance {
	private int amount;
	@SuppressWarnings("unused")
	private Vector3 velocity = Vector3.ZERO;

	public XPOrb(int amount) {
		super(VanillaControllerTypes.XP_ORB);
		this.amount = amount;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		//TODO move to closest player
	}

	public int getExperienceAmount() {
		return amount;
	}
}
