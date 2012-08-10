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
package org.spout.vanilla.entity.living.creature.passive;

import java.util.List;

import org.spout.api.Source;
import org.spout.api.inventory.ItemStack;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.VanillaEntityController;
import org.spout.vanilla.entity.component.ai.other.SheepEatGrassComponent;
import org.spout.vanilla.entity.living.Creature;
import org.spout.vanilla.entity.living.creature.Passive;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.solid.Wool;

public class Sheep extends Creature implements Passive {
	private boolean isSheared = false;
	private short sheepColor = 0;

	public Sheep() {
		super(VanillaControllerTypes.SHEEP);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getHealth().setSpawnHealth(8);
		final SheepEatGrassComponent eatGrassComponent = new SheepEatGrassComponent(TickPriority.NORMAL);
		addComponent(eatGrassComponent.getClass());
		isSheared = getDataMap().get("sheep_sheared", false);
		getHealth().setHurtEffect(SoundEffects.MOB_SHEEP);
	}

	@Override
	public void onSave() {
		super.onSave();
		getDataMap().put("sheep_sheared", isSheared);
		getDataMap().put("sheep_color", sheepColor);
	}

	/**
	 * Whether or not the sheep's wool has been sheared.
	 * @return true if sheared.
	 */
	public boolean isSheared() {
		return isSheared;
	}

	/**
	 * Sets whether or not the sheep's wool has been sheared.
	 * @param sheared
	 */
	public void setSheared(boolean sheared) {
		this.isSheared = sheared;
	}

	/**
	 * Gets the color of the sheep.
	 * @return color of the sheep.
	 */
	public Wool.WoolColor getColor() {
		return Wool.WoolColor.getById(sheepColor);
	}

	/**
	 * Sets the color of the sheep.
	 * @param color
	 */
	public void setColor(Wool.WoolColor color) {
		sheepColor = color.getData();
	}

	@Override
	public List<ItemStack> getDrops(Source source, VanillaEntityController lastDamager) {
		List<ItemStack> drops = super.getDrops(source, lastDamager);
		if (!isSheared()) {
			if (source == DamageCause.BURN) {
				drops.add(new ItemStack(Wool.GRAY_WOOL, 1));
			} else {
				drops.add(new ItemStack(VanillaMaterials.WOOL.getSubMaterial(sheepColor), 1));
			}
		}
		return drops;
	}
}
