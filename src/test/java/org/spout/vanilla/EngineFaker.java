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

import java.util.logging.Logger;

import org.mockito.Mockito;

import org.spout.api.Engine;
import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.EventManager;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.plugin.PluginDescriptionFile;
import org.spout.api.resource.FileSystem;

import org.spout.vanilla.event.entity.EntityDamageEvent;

@SuppressWarnings ("deprecation")
public class EngineFaker {
	private final static Engine engineInstance;

	static {
		Engine engine = Mockito.mock(Engine.class);
		FileSystem filesystem = Mockito.mock(FileSystem.class);
		Mockito.when(engine.getPlatform()).thenReturn(Platform.SERVER);
		Mockito.when(engine.getFileSystem()).thenReturn(filesystem);
		Mockito.when(engine.getEventManager()).thenReturn(new TestEventManager());
		Mockito.when(engine.getLogger()).thenReturn(Mockito.mock(Logger.class));

		VanillaPlugin plugin = new VanillaTestPlugin();
		plugin.initialize(null, engine, new PluginDescriptionFile("Vanilla", "dev", "org.spout.vanilla.VanillaPlugin", "all"), null, null, null);
		VanillaPlugin.instance = plugin;

		Spout.setEngine(engine);
		engineInstance = engine;
	}

	public static Engine setupEngine() {
		return engineInstance;
	}

	public static void main(String[] args) {

	}

	private static class VanillaTestPlugin extends VanillaPlugin {
		@Override
		public void onEnable() {
		}

		@Override
		public void onDisable() {
		}

		@Override
		public void onReload() {
		}
	}

	private static class TestEventManager implements EventManager {
		@Override
		public <T extends Event> T callEvent(T event) {
			if (event instanceof EntityDamageEvent) {
				((EntityDamageEvent) event).setSendHurtMessage(false);
			}
			return event;
		}

		@Override
		public <T extends Event> void callDelayedEvent(T event) {

		}

		@Override
		public void registerEvents(Listener listener, Object owner) {

		}

		@Override
		public void registerEvent(Class<? extends Event> event, Order priority, EventExecutor executor, Object owner) {

		}
	}
}
