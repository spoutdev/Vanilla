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
package org.spout.vanilla.plugin.component.living.hostile;

import java.util.Random;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.component.Hostile;
import org.spout.vanilla.api.component.misc.DamageComponent;
import org.spout.vanilla.api.data.Difficulty;
import org.spout.vanilla.api.data.VanillaData;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.EntityDrops;
import org.spout.vanilla.plugin.component.misc.Health;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.SpiderEntityProtocol;

/**
 * A component that identifies the entity as a Spider.
 */
public class Spider extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new SpiderEntityProtocol());
		EntityDrops dropComponent = getOwner().add(EntityDrops.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.STRING, random.nextInt(2)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.SPIDER_EYE, random.nextInt(1)));
		dropComponent.addXpDrop((short) 5);
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(16);
		}
		DamageComponent damage = getOwner().add(DamageComponent.class);
		damage.getDamageLevel(Difficulty.EASY).setAmount(2);
		damage.getDamageLevel(Difficulty.NORMAL).setAmount(2);
		damage.getDamageLevel(Difficulty.HARD).setAmount(3);
		damage.getDamageLevel(Difficulty.HARDCORE).setAmount(damage.getDamageLevel(Difficulty.HARD).getAmount());
	}

	public boolean isAggressive() {
		return getData().get(VanillaData.AGGRESSIVE);
	}

	public void setAggressive(boolean aggro) {
		getData().put(VanillaData.AGGRESSIVE, aggro);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, aggro ? (byte) 1 : 0));
	}
}
