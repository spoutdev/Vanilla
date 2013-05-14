/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.material.block.component;

import org.spout.api.component.type.BlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.ComplexMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.block.VanillaBlockComponent;
import org.spout.vanilla.material.VanillaBlockMaterial;

public class ComponentMaterial extends VanillaBlockMaterial implements ComplexMaterial {
	private final Class<? extends BlockComponent> componentType;

	public ComponentMaterial(String name, int id, short data, VanillaBlockMaterial parent, Class<? extends BlockComponent> componentType, String model) {
		super(name, id, data, parent, model);
		this.componentType = componentType;
	}

	public ComponentMaterial(short dataMask, String name, int id, Class<? extends BlockComponent> componentType, String model) {
		super(dataMask, name, id, model);
		this.componentType = componentType;
	}

	public ComponentMaterial(String name, int id, Class<? extends BlockComponent> componentType, String model) {
		super(name, id, model);
		this.componentType = componentType;
	}

	public ComponentMaterial(String name, int id, int data, ComponentMaterial parent, String model) {
		super(name, id, data, parent, model);
		this.componentType = parent.componentType;
    }

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		super.onInteract(entity, block, type, clickedFace);
		BlockComponent c = block.add(componentType);
		((VanillaBlockComponent) c).onInteractBy(entity, type, clickedFace);
	}

	@Override
	public Class<? extends BlockComponent> getBlockComponent() {
		try {
			return componentType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
