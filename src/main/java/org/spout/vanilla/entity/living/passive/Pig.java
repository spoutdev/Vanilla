/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.entity.living.passive;

import org.spout.vanilla.Entity;
import org.spout.vanilla.entity.AnimalEntity;
import org.spout.vanilla.entity.PassiveEntity;

public class Pig extends AnimalEntity implements PassiveEntity {
	@Override
	public void onAttached() {
		super.onAttached();
		parent.setData(Entity.KEY, Entity.Pig.id);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}
}