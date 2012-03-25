/*
 * This file is part of Vanilla.
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.object.moving;

import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.ControllerType;
import org.spout.vanilla.controller.object.Substance;



public class XPOrb extends Substance{
	private int amount;
	private Vector3 initial;
	private Vector3 velocity = new Vector3();
	
	public XPOrb(int amount, Vector3 initial) {
		this.amount = amount;
		this.initial = initial;
		setMoveable(true);
	}
	
	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		//TODO move to closest player
	}

	@Override
	public void onAttached() {
		getParent().setData(ControllerType.KEY, ControllerType.XPORB.id);
	}
	
	public int getExperienceAmount() {
		return amount;
	}

}
