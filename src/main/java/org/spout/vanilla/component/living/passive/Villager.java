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
package org.spout.vanilla.component.living.passive;

import java.util.HashMap;

import org.spout.api.Source;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.inventory.window.Window;
import org.spout.vanilla.component.living.Passive;
import org.spout.vanilla.component.living.VanillaEntity;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.CraftingInventory;
import org.spout.vanilla.protocol.entity.CreatureProtocol;

/**
 * A component that identifies the entity as a Villager.
 */
public class Villager extends VanillaEntity implements Container, Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreatureProtocol(120)); //Index 16 (int): Unknown, example: 0
	}

	private HashMap<Player, Window> viewers = new HashMap<Player, Window>();

	@Override
	public void onInteract(Action action, Source source) {
		super.onInteract(action, source);
		if (action == Action.RIGHT_CLICK) {
			// TODO: Open window
		}
	}

	@Override
	public CraftingInventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
}
