package org.spout.vanilla.entity.component;

import static org.junit.Assert.*;

import org.junit.Test;
import org.spout.api.entity.Entity;
import org.spout.vanilla.EntityMocker;
import org.spout.vanilla.component.entity.misc.Level;

public class LevelTest {

	@Test
	public void test() {
		Entity entity = EntityMocker.mockEntity();
		Level level = entity.add(Level.class);
		assertTrue("Level is null!", level != null);
		Level sameLevel = entity.get(Level.class);
		assertTrue("Level does not match!", level == sameLevel);
		
		assertTrue("Level data is null!", level.getData() != null);
	}

}
