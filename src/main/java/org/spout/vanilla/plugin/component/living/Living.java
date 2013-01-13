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
package org.spout.vanilla.plugin.component.living;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.component.impl.PhysicsComponent;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.plugin.ai.VanillaBlockExaminer;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.DrowningComponent;
import org.spout.vanilla.plugin.component.misc.EffectsComponent;
import org.spout.vanilla.plugin.component.misc.FireComponent;
import org.spout.vanilla.plugin.component.misc.HeadComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.effect.StatusEffect;
import org.spout.vanilla.plugin.event.entity.EntityMetaChangeEvent;

public abstract class Living extends EntityComponent {
	private HeadComponent head;
	private HealthComponent health;
	private PhysicsComponent physics;
	private DrowningComponent drowning;
	private NavigationComponent navigation;

	@Override
	public void onAttached() {
		Entity holder = getOwner();
		head = holder.add(HeadComponent.class);
		health = holder.add(HealthComponent.class);
		physics = holder.add(PhysicsComponent.class);
		drowning = holder.add(DrowningComponent.class);
		navigation = holder.add(NavigationComponent.class);
		holder.add(FireComponent.class);
		navigation.setDefaultExaminers(new VanillaBlockExaminer());

		holder.setSavable(true);

		//Tracks the number of times this component has been attached (i.e how many times it's been saved, then loaded. 1 = fresh entity)
		holder.getData().put(VanillaData.ATTACHED_COUNT, getAttachedCount() + 1);
	}

	
	public boolean isOnGround() {
		return getOwner().getData().get(VanillaData.IS_ON_GROUND);
	}

	public void setOnGround(boolean onGround) {
		getOwner().getData().put(VanillaData.IS_ON_GROUND, onGround);
	}

	/**
	 * A counter of how many times this component has been attached to an entity
	 * <p/>
	 * Values > 1 indicate how many times this component has been saved to disk,
	 * and reloaded
	 * <p/>
	 * Values == 1 indicate a new component that has never been saved and loaded.
	 * @return attached count
	 */
	public final int getAttachedCount() {
		return getOwner().getData().get(VanillaData.ATTACHED_COUNT);
	}

	public HeadComponent getHead() {
		return head;
	}

	public HealthComponent getHealth() {
		return health;
	}

	public PhysicsComponent getPhysics() {
		return physics;
	}

	public DrowningComponent getDrowning() {
		return drowning;
	}

	public NavigationComponent getNavigation() {
		return navigation;
	}

	public boolean isRiding() {
		return getOwner().getData().get(VanillaData.IS_RIDING);
	}

	public void setRiding(boolean isRiding) {
		getOwner().getData().put(VanillaData.IS_RIDING, isRiding);
		sendMetaData();
	}

	public boolean isEatingBlocking() {
		return getOwner().getData().get(VanillaData.IS_EATING_BLOCKING);
		
	}

	public void setEatingBlocking(boolean isEatingBlocking) {
		getOwner().getData().put(VanillaData.IS_EATING_BLOCKING, isEatingBlocking);
		sendMetaData();
	}
	
	public boolean isSneaking() {
		return getOwner().getData().get(VanillaData.IS_SNEAKING);
	}

	public void setSneaking(boolean isSneaking) {
		getOwner().getData().put(VanillaData.IS_SNEAKING, isSneaking);
		sendMetaData();
	}
	
	public void sendMetaData() {
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 0, getCommonMetadata()));
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), parameters));
	}
	
	private byte getCommonMetadata() {
		byte value = 0;
		if (getOwner().has(FireComponent.class)) {
			value = (byte) (value | (( getOwner().get(FireComponent.class).isOnFire() ? 1 : 0 ) << 0));
		}
		
		value = (byte) (value | (( isSneaking() ? 1 : 0 ) << 1));
		value = (byte) (value | (( isRiding() ? 1 : 0 ) << 2));
		
		if (getOwner().has(Human.class)) {
			value = (byte) (value | (( getOwner().get(Human.class).isSprinting() ? 1 : 0 ) << 3));
		}
		
		value = (byte) (value | (( isEatingBlocking() ? 1 : 0 ) << 4));
		
		if (getOwner().has(EffectsComponent.class)) {
			value = (byte) (value | (( getOwner().get(EffectsComponent.class).containsEffect(StatusEffect.INVISIBILITY) ? 1 : 0 ) << 5));
		}
		
		return value;
	}
}
