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
import org.spout.api.plugin.Platform;
import org.spout.api.render.Font;
import org.spout.api.render.RenderMaterial;

public final class RenderMaterials {
	private static final FileSystem fileSystem = Spout.getFilesystem();
	public static final Font FONT;
	public static final RenderMaterial HOTBAR_MATERIAL;
	public static final RenderMaterial ICONS_MATERIAL;
	public static final RenderMaterial INVENTORY_MATERIAL;
	public static final RenderMaterial CONTAINER_MATERIAL;
	public static final RenderMaterial CRAFTING_MATERIAL;
	public static final RenderMaterial FURNACE_MATERIAL;
	public static final RenderMaterial TRAP_MATERIAL;
	public static final RenderMaterial ENCHANT_MATERIAL;
	public static final RenderMaterial ALCHEMY_MATERIAL;
	public static final RenderMaterial ITEMS_MATERIAL;
	public static final RenderMaterial BLOCKS_MATERIAL;
	public static final RenderMaterial GUI_COLOR;
	// TODO: Missing textures
	public static final RenderMaterial VILLAGER_MATERIAL = null;
	public static final RenderMaterial BEACON_MATERIAL = null;
	public static final RenderMaterial ANVIL_MATERIAL = null;

	static {
		if (Spout.getPlatform() == Platform.CLIENT) {
			FONT = (Font) fileSystem.getResource("font://Spout/resources/resources/fonts/ubuntu/Ubuntu-M.ttf");
			HOTBAR_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/HotbarGUIMaterial.smt");
			ICONS_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/IconsGUIMaterial.smt");
			INVENTORY_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
			CONTAINER_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/ContainerGUIMaterial.smt");
			CRAFTING_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/CraftingGUIMaterial.smt");
			FURNACE_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/FurnaceGUIMaterial.smt");
			TRAP_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/TrapGUIMaterial.smt");
			ENCHANT_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
			ALCHEMY_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/InventoryGUIMaterial.smt");
			ITEMS_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/gui/smt/ItemsGUIMaterial.smt");
			BLOCKS_MATERIAL = (RenderMaterial) fileSystem.getResource("material://Vanilla/resources/materials/terrain.smt");
			GUI_COLOR = (RenderMaterial) fileSystem.getResource("material://Spout/resources/materials/GUIColorMaterial.smt");
		} else {
			FONT = null;
			HOTBAR_MATERIAL = null;
			ICONS_MATERIAL = null;
			INVENTORY_MATERIAL = null;
			CONTAINER_MATERIAL = null;
			CRAFTING_MATERIAL = null;
			FURNACE_MATERIAL = null;
			TRAP_MATERIAL = null;
			ENCHANT_MATERIAL = null;
			ALCHEMY_MATERIAL = null;
			ITEMS_MATERIAL = null;
			BLOCKS_MATERIAL = null;
			GUI_COLOR = null;
		}
	}

	private RenderMaterials() {
	}
}
