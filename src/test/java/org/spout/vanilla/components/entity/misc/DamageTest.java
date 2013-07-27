/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.components.entity.misc;

import org.junit.Test;

import org.spout.api.entity.Entity;

import org.spout.vanilla.EntityMocker;
import org.spout.vanilla.component.entity.misc.Damage;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.effect.EntityEffect;
import org.spout.vanilla.data.effect.EntityEffectType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DamageTest {
	@Test
	public void testLevelComponent() {
		Entity entity = EntityMocker.mockEntity();
		Damage damageComponent = entity.add(Damage.class);
		damageComponent.getDamageLevel(Difficulty.EASY).setAmount(40);
		assertEquals(40, damageComponent.getDamageLevel(Difficulty.EASY).getAmount());
		assertNull(damageComponent.getDamageLevel(null));
		assertEquals(0, damageComponent.getDamageLevel(Difficulty.HARD).getAmount());
		EntityEffect container = new EntityEffect(EntityEffectType.HASTE, 4);
		damageComponent.getDamageLevel(Difficulty.EASY).setEffect(container);
		assertEquals(container, damageComponent.getDamageLevel(Difficulty.EASY).getEffect());
		assertEquals(null, damageComponent.getDamageLevel(Difficulty.HARD).getEffect());
	}
}
