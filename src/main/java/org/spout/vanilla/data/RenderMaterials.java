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
package org.spout.vanilla.data;

import org.spout.api.FileSystem;
import org.spout.api.Spout;
import org.spout.api.render.Font;
import org.spout.api.render.RenderMaterial;

public class RenderMaterials {
	private static final FileSystem fileSystem = Spout.getFilesystem();
	public static final Font FONT = (Font) fileSystem.getResource("font://Spout/resources/resources/fonts/ubuntu/Ubuntu-M.ttf");
	public static final RenderMaterial HOTBAR_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/HotbarGUIMaterial.smt");
	public static final RenderMaterial ICONS_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/IconsGUIMaterial.smt");
	public static final RenderMaterial INVENTORY_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
	public static final RenderMaterial CONTAINER_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/ContainerGUIMaterial.smt");
	public static final RenderMaterial CRAFTING_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/CraftingGUIMaterial.smt");
	public static final RenderMaterial FURNACE_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/FurnaceGUIMaterial.smt");
	public static final RenderMaterial TRAP_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/TrapGUIMaterial.smt");
	public static final RenderMaterial ENCHANT_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
	public static final RenderMaterial ALCHEMY_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
	// TODO: Missing textures
	public static final RenderMaterial VILLAGER_MATERIAL = null;
	public static final RenderMaterial BEACON_MATERIAL = null;
	public static final RenderMaterial ANVIL_MATERIAL = null;

	private RenderMaterials() {
	}
}
