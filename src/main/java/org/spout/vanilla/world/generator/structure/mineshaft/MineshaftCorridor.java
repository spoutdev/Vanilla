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
package org.spout.vanilla.world.generator.structure.mineshaft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.LootChestObject;
import org.spout.vanilla.world.generator.structure.ComponentCuboidPart;
import org.spout.vanilla.world.generator.structure.SimpleBlockMaterialPicker;
import org.spout.vanilla.world.generator.structure.Structure;
import org.spout.vanilla.world.generator.structure.StructureComponent;

public class MineshaftCorridor extends StructureComponent {
	private byte sections = 5;
	private boolean hasRails = false;
	private boolean caveSpiders = false;
	private boolean hasSpawner = false;

	public MineshaftCorridor(Structure parent) {
		super(parent);
		randomize();
	}

	@Override
	public boolean canPlace() {
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		box.setMinMax(-1, -1, -1, 3, 3, sections * 5);
		return !box.intersectsLiquids();
	}

	@Override
	public void place() {
		// rng
		final Random random = getRandom();
		// properties
		final short lenght = (short) (sections * 5 - 1);
		// building objects
		final ComponentCuboidPart box = new ComponentCuboidPart(this);
		final SimpleBlockMaterialPicker picker = new SimpleBlockMaterialPicker();
		box.setPicker(picker);
		// the basic tunnel
		picker.setOuterInnerMaterials(VanillaMaterials.AIR, VanillaMaterials.AIR);
		box.setMinMax(0, 0, 0, 2, 1, lenght);
		box.fill(false);
		box.offsetMinMax(0, 2, 0, 0, 1, 0);
		box.randomFill(0.8f, false);
		// spider webs for spawner
		if (caveSpiders) {
			picker.setOuterMaterial(VanillaMaterials.WEB);
			box.setMinMax(0, 0, 0, 2, 1, lenght);
			box.randomFill(0.6f, false);
		}
		// decorate sections
		for (byte section = 0; section < sections; section++) {
			final short sectionZ = (short) (section * 5 + 2);
			// fences
			picker.setOuterMaterial(VanillaMaterials.WOODEN_FENCE);
			box.setMinMax(0, 0, sectionZ, 0, 1, sectionZ);
			box.fill(false);
			box.offsetMinMax(2, 0, 0, 2, 0, 0);
			box.fill(false);
			// ceiling planks
			picker.setOuterMaterial(VanillaMaterials.PLANK);
			if (random.nextInt(4) != 0) {
				box.setMinMax(0, 2, sectionZ, 2, 2, sectionZ);
				box.fill(false);
			} else {
				box.setMinMax(0, 2, sectionZ, 0, 2, sectionZ);
				box.fill(false);
				box.offsetMinMax(2, 0, 0, 2, 0, 0);
				box.fill(false);
			}
			// webs and torches
			randomSetBlockMaterial(0.9f, 0, 2, sectionZ - 1, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.9f, 2, 2, sectionZ - 1, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.9f, 0, 2, sectionZ + 1, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.9f, 2, 2, sectionZ + 1, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.95f, 0, 2, sectionZ - 2, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.95f, 2, 2, sectionZ - 2, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.95f, 0, 2, sectionZ + 2, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.95f, 2, 2, sectionZ + 2, VanillaMaterials.WEB);
			randomSetBlockMaterial(0.95f, 1, 2, sectionZ - 1, VanillaMaterials.TORCH);
			randomSetBlockMaterial(0.95f, 1, 2, sectionZ + 1, VanillaMaterials.TORCH);
			// loot
			if (random.nextInt(100) == 0) {
				final LootChestObject chest = new LootChestObject();
				//TODO: give them proper loot
				placeObject(2, 0, sectionZ - 1, chest);
			}
			if (random.nextInt(100) == 0) {
				final LootChestObject chest = new LootChestObject();
				//TODO: give them proper loot
				placeObject(0, 0, sectionZ + 1, chest);
			}
			// spawner
			if (!caveSpiders || hasSpawner) {
				continue;
			}
			hasSpawner = true;
			setBlockMaterial(1, 0, sectionZ + random.nextInt(3), VanillaMaterials.MONSTER_SPAWNER);
		}
		// bridge gaps
		for (byte xx = 0; xx <= 2; xx++) {
			for (byte zz = 0; zz <= lenght; zz++) {
				final Block block = getBlock(xx, -1, zz);
				if (block.isMaterial(VanillaMaterials.AIR)) {
					block.setMaterial(VanillaMaterials.PLANK);
				}
			}
		}
		// rails
		if (hasRails) {
			for (byte zz = 0; zz <= lenght; zz++) {
				if (getBlockMaterial(1, -1, zz).isOpaque()) {
					randomSetBlockMaterial(0.3f, 1, 0, zz, VanillaMaterials.RAIL);
				}
			}
		}
	}

	@Override
	public final void randomize() {
		final Random random = getRandom();
		sections = (byte) (random.nextInt(4) + 5);
		hasRails = random.nextInt(3) == 0;
		caveSpiders = !hasRails && random.nextInt(23) == 0;
	}

	@Override
	public List<StructureComponent> getNextComponents() {
		final List<StructureComponent> components = new ArrayList<StructureComponent>(1);
		final StructureComponent component;
		final float draw = getRandom().nextFloat();
		if (draw > 0.8) {
			component = new MineshaftRoom(parent);
			component.setPosition(position.add(0, -1, 0));
		} else if (draw > 0.6) {
			component = new MineshaftStaircase(parent);
			component.setPosition(position.add(0, -5, 0));
			component.setRotation(rotation);
		} else if (draw > 0.1) {
			component = new MineshaftIntersection(parent);
			component.setPosition(position);
		} else {
			return components;
		}
		component.randomize();
		switch ((int) rotation.getYaw()) {
			case 90:
				component.offsetPosition(sections * 5, 0, 0);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(0, 0, -room.getDepth() / 2 - 1);
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(1, 0, -3);
				}
				break;
			case 180:
				component.offsetPosition(0, 0, -sections * 5);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght() / 2 - 1, 0, -room.getDepth());
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(-2, 0, -4);
				}
				break;
			case -90:
				component.offsetPosition(-sections * 5, 0, 0);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght(), 0, -room.getDepth() / 2 + 1);
				} else if (component instanceof MineshaftIntersection) {
					component.offsetPosition(-3, 0, -1);
				}
				break;
			default:
				component.offsetPosition(0, 0, sections * 5);
				if (component instanceof MineshaftRoom) {
					final MineshaftRoom room = ((MineshaftRoom) component);
					component.offsetPosition(-room.getLenght() / 2 + 1, 0, 0);
				}
		}
		components.add(component);
		return components;
	}

	public boolean hasCaveSpiders() {
		return caveSpiders;
	}

	public void setCaveSpiders(boolean caveSpiders) {
		this.caveSpiders = caveSpiders;
	}

	public boolean hasRails() {
		return hasRails;
	}

	public void setHasRails(boolean hasRails) {
		this.hasRails = hasRails;
	}

	public byte getSections() {
		return sections;
	}

	public void setSections(byte sections) {
		this.sections = sections;
	}

	@Override
	public BoundingBox getBoundingBox() {
		final Vector3 rotatedMin = transform(0, 0, 0);
		final Vector3 rotatedMax = transform(2, 2, sections * 5 - 1);
		return new BoundingBox(MathHelper.min(rotatedMin, rotatedMax), MathHelper.max(rotatedMin, rotatedMax));
	}
}
