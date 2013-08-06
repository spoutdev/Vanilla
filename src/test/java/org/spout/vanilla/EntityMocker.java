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
package org.spout.vanilla;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.spout.api.Engine;
import org.spout.api.component.BaseComponentOwner;
import org.spout.api.component.Component;
import org.spout.api.component.entity.EntityComponent;
import org.spout.api.component.entity.NetworkComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;

public class EntityMocker {
	public static Player mockPlayer() {
		Engine engine = EngineFaker.setupEngine();

		final Player player = Mockito.mock(Player.class);
		final EntityComponentAnswer componentHolder = new EntityComponentAnswer(player);
		componentHolder.add(NetworkComponent.class);

		//Set up component holder methods
		Mockito.when(player.add(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(player.get(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(player.getExact(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(player.detach(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(player.getData()).thenReturn(componentHolder.getData());
		Mockito.when(player.getEngine()).thenAnswer(new EntityEngineAnswer(engine));
		//Set up entity tick
		Mockito.doAnswer(new EntityTickAnswer(player)).when(player).onTick(Mockito.anyFloat());

		//Set up event manager
		return player;
	}

	public static Entity mockEntity() {
		// TODO: this is broken, please fix.
		Engine engine = EngineFaker.setupEngine();

		final Entity entity = Mockito.mock(Entity.class);
		final EntityComponentAnswer componentHolder = new EntityComponentAnswer(entity);

		//Set up component holder methods
		Mockito.when(entity.add(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.get(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.getExact(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.detach(Matchers.argThat(new ClassOrSubclassMatcher<EntityComponent>(EntityComponent.class)))).thenAnswer(componentHolder);
		Mockito.when(entity.getData()).thenReturn(componentHolder.getData());
		Mockito.when(entity.getEngine()).thenAnswer(new EntityEngineAnswer(engine));

		//Set up entity tick
		Mockito.doAnswer(new EntityTickAnswer(entity)).when(entity).onTick(Mockito.anyFloat());

		//Set up event manager
		return entity;
	}

	private static class EntityEngineAnswer implements Answer<Engine> {
		private final Engine engine;
		EntityEngineAnswer(Engine engine) {
			this.engine = engine;
		}

		@Override
		public Engine answer(InvocationOnMock invocation) throws Throwable {
			return engine;
		}
		
	}
	private static class EntityTickAnswer implements Answer<Object> {
		private final Entity entity;

		EntityTickAnswer(Entity entity) {
			this.entity = entity;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			float dt = (Float) invocation.getArguments()[0];
			for (Component c : entity.values()) {
				c.tick(dt);
			}
			return null;
		}
	}

	private static class EntityComponentAnswer extends BaseComponentOwner implements Answer<Component> {
		private final Entity entity;

		EntityComponentAnswer(Entity entity) {
			this.entity = entity;
		}

		@Override
		protected void attachComponent(Class<? extends Component> key, Component component, boolean attach) throws Exception {
			if (component.attachTo(entity)) {
				// Uh....tests
				super.attachComponent(key, component, attach);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component answer(InvocationOnMock invocation) throws Throwable {
			Class<? extends EntityComponent> clazz = (Class<? extends EntityComponent>) invocation.getArguments()[0];
			if (invocation.getMethod().getName().equals("add")) {
				return add(clazz);
			} else if (invocation.getMethod().getName().equals("get")) {
				return get(clazz);
			} else if (invocation.getMethod().getName().equals("getExact")) {
				return getExact(clazz);
			} else {
				return detach(clazz);
			}
		}
	}

	private static class ClassOrSubclassMatcher<T> extends BaseMatcher<Class<T>> {
		private final Class<T> targetClass;

		public ClassOrSubclassMatcher(Class<T> targetClass) {
			this.targetClass = targetClass;
		}

		@SuppressWarnings("unchecked")
		public boolean matches(Object obj) {
			if (obj instanceof Class) {
				return targetClass.isAssignableFrom((Class<T>) obj);
			}
			return false;
		}

		public void describeTo(Description desc) {
			desc.appendText("Matches a class or subclass");
		}
	}
}
