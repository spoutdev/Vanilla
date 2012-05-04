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
package org.spout.vanilla.controller.living.creature.neutral;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.controller.living.creature.Neutral;
import org.spout.vanilla.controller.living.creature.Tameable;
import org.spout.vanilla.controller.source.HealthChangeReason;

import java.util.Set;

public class Wolf extends Creature implements Tameable, Neutral {
	private Entity parent;

	protected Wolf() {
		super(VanillaControllerTypes.WOLF);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		parent = getParent();
		parent.setMaxHealth(8);
		parent.setHealth(8, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
		// TODO: Health gets increased to 20 when tamed.
	}

	@Override
	public void subjectTo(Entity entity) {

	}

	@Override
	public Set<ItemStack> getDrops() {
		return null;
	}
}
