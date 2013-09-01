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

import org.spout.vanilla.EngineFaker;

public class LevelTest {
	static {
		EngineFaker.setupEngine();
	}

	@Test
	public void testLevelComponent() {
		// TODO: UI Updates during testing break as required Network Component does not exist on the entity. Needs fixing
		/*
		Player player = EntityMocker.mockPlayer();
		Level levelComponent = player.add(Level.class);
		assertTrue(levelComponent.setExperience((short) 500));
		short level = Level.convertXpToLevel((short) 500);
		short xp = Level.convertLevelToXp(level);
		assertEquals(500, levelComponent.getExperience());
		assertEquals(Level.convertXpToLevel(levelComponent.getExperience()), levelComponent.getLevel());
		assertEquals(level, Level.convertXpToLevel(xp));

		levelComponent.addExperience(30);
		assertEquals(530, levelComponent.getExperience());

		levelComponent.addExperience(-30);
		assertEquals(500, levelComponent.getExperience());

		levelComponent.addExperience(-510);
		assertEquals(0, levelComponent.getExperience());

		levelComponent.addExperience(500);
		level = levelComponent.getLevel();
		levelComponent.removeLevels(1);
		assertEquals(level - 1, levelComponent.getLevel());

		levelComponent.removeLevels(levelComponent.getLevel() + 1);
		assertEquals(0, levelComponent.getLevel());

		levelComponent.addLevel(5);
		assertEquals(5, levelComponent.getLevel());
		*/
	}
}
