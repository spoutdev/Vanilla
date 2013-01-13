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
package org.spout.vanilla.plugin.component.living.neutral;

import java.util.Random;

import org.spout.api.Spout;
import org.spout.api.chat.ChatArguments;
import org.spout.api.component.impl.TextModelComponent;
import org.spout.api.data.Data;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.SinusHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DamageComponent;
import org.spout.vanilla.plugin.component.misc.DiggingComponent;
import org.spout.vanilla.plugin.component.misc.HeadComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.component.misc.PickupItemComponent;
import org.spout.vanilla.plugin.component.substance.Item;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.data.GameMode;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.event.entity.HumanAbilityChangeEvent;
import org.spout.vanilla.plugin.event.player.PlayerGameModeChangedEvent;
import org.spout.vanilla.plugin.event.player.network.PlayerAbilityUpdateEvent;
import org.spout.vanilla.plugin.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.plugin.inventory.Slot;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.protocol.entity.HumanEntityProtocol;
import org.spout.vanilla.plugin.protocol.msg.player.PlayerGameStateMessage;

/**
 * A component that identifies the entity as a Vanilla player.
 */
public class Human extends Living {
	public static final int SPAWN_HEALTH = 20;

	@Override
	public void onAttached() {
		super.onAttached();
		Entity holder = getOwner();
		holder.add(PickupItemComponent.class);
		holder.add(DiggingComponent.class);
		holder.getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new HumanEntityProtocol());
		//Add height offset if loading from disk
		//		if (holder instanceof Player) {
		//			((Player) holder).teleport(holder.getTransform().getPosition().add(0, 1.85F, 0));
		//		}
		if (getAttachedCount() == 1) {
			holder.add(HealthComponent.class).setSpawnHealth(SPAWN_HEALTH);
		}
		if (getOwner().has(TextModelComponent.class)) {
			getOwner().get(TextModelComponent.class).setSize(0.5f);
			getOwner().get(TextModelComponent.class).setTranslation(new Vector3(0, 3f, 0));
		}
	}

	public boolean isAdventure() {
		return getGameMode() == GameMode.ADVENTURE;
	}

	public boolean isCreative() {
		return getGameMode() == GameMode.CREATIVE;
	}

	public boolean isSurvival() {
		return getGameMode() == GameMode.SURVIVAL;
	}

	public boolean isSprinting() {
		return getOwner().getData().get(VanillaData.IS_SPRINTING);
	}

	public void setSprinting(boolean isSprinting) {
		getOwner().getData().put(VanillaData.IS_SPRINTING, isSprinting);
		sendMetaData();
	}

	public boolean isFalling() {
		return getOwner().getData().get(VanillaData.IS_FALLING);
	}

	public void setFalling(boolean isFalling) {
		getOwner().getData().put(VanillaData.IS_FALLING, isFalling);
	}

	public boolean isJumping() {
		return getOwner().getData().get(VanillaData.IS_JUMPING);
	}

	public void setJumping(boolean isJumping) {
		getOwner().getData().put(VanillaData.IS_JUMPING, isJumping);
	}

	public String getName() {
		return getData().get(Data.NAME);
	}

	public void setName(String name) {
		getData().put(Data.NAME, name);
		if (getOwner().has(TextModelComponent.class)) {
			getOwner().get(TextModelComponent.class).setText(new ChatArguments(name));
		}
	}

	public boolean isOp() {
		return getOwner() instanceof Player && VanillaConfiguration.OPS.isOp(getName());
	}

	/**
	 * Drops the item specified into the direction the player looks, with slight randomness
	 * 
	 * @param item to drop
	 */
	public void dropItem(ItemStack item) {
		final Transform dropFrom;
		if (getOwner().has(HeadComponent.class)) {
			dropFrom = getOwner().get(HeadComponent.class).getHeadTransform();
		} else {
			dropFrom = getOwner().getTransform().getTransform();
		}
		// Some constants
		final double impulseForce = 0.3;
		final float maxXZForce = 0.02f;
		final float maxYForce = 0.1f;

		// Create a velocity vector using the transform, apply (random) force
		Vector3 impulse = dropFrom.getRotation().getDirection().multiply(impulseForce);

		// Random rotational offset to avoid dropping at the same position
		Random rand = MathHelper.getRandom();
		float xzLength = maxXZForce * rand.nextFloat();
		float yLength = maxYForce * (rand.nextFloat() - rand.nextFloat());
		impulse = impulse.add(SinusHelper.getRandom2DAxis(rand).multiply(xzLength).toVector3(yLength));

		// Slightly dropping upwards
		impulse = impulse.add(0.0, 0.1, 0.0);

		// Conversion factor, some sort of unit problem
		//TODO: This needs an actual value and this value might change when gravity changes!
		impulse = impulse.multiply(100);

		// Finally drop
		Item.drop(dropFrom.getPosition(), item, impulse);
	}

	/**
	 * Drops the player's current item.
	 */
	public void dropItem() {
		if (!getOwner().has(PlayerInventory.class)) {
			return;
		}

		PlayerQuickbar quickbar = getOwner().get(PlayerInventory.class).getQuickbar();
		Slot selected = quickbar.getSelectedSlot();
		ItemStack drop = selected.get();
		if (drop == null) {
			return;
		} else {
			drop = drop.clone().setAmount(1);
		}
		selected.addAmount(-1);
		dropItem(drop);
	}

	// Abilities
	public void setFlying(boolean isFlying, boolean updateClient) {
		Boolean previous = getOwner().getData().put(VanillaData.IS_FLYING, isFlying);
		if (callAbilityChangeEvent().isCancelled()) {
			getOwner().getData().put(VanillaData.IS_FLYING, previous);
			return;
		}
		updateAbilities(updateClient);
	}

	public void setFlying(boolean isFlying) {
		setFlying(isFlying, true);
	}

	public boolean isFlying() {
		return getOwner().getData().get(VanillaData.IS_FLYING);
	}

	public void setFlyingSpeed(byte speed, boolean updateClient) {
		byte previous = getOwner().getData().put(VanillaData.FLYING_SPEED, speed).byteValue();
		if (callAbilityChangeEvent().isCancelled()) {
			getOwner().getData().put(VanillaData.FLYING_SPEED, previous);
			return;
		}
		updateAbilities(updateClient);
	}

	public void setFlyingSpeed(byte speed) {
		setFlyingSpeed(speed, true);
	}

	public byte getFlyingSpeed() {
		return getOwner().getData().get(VanillaData.FLYING_SPEED).byteValue();
	}

	public void setWalkingSpeed(byte speed, boolean updateClient) {
		byte previous = getOwner().getData().put(VanillaData.WALKING_SPEED, speed).byteValue();
		if (callAbilityChangeEvent().isCancelled()) {
			getOwner().getData().put(VanillaData.WALKING_SPEED, previous);
			return;
		}
		updateAbilities(updateClient);
	}

	public void setWalkingSpeed(byte speed) {
		setWalkingSpeed(speed, true);
	}

	public byte getWalkingSpeed() {
		return getOwner().getData().get(VanillaData.WALKING_SPEED).byteValue();
	}

	public void setCanFly(boolean canFly, boolean updateClient) {
		Boolean previous = getOwner().getData().put(VanillaData.CAN_FLY, canFly);
		if (callAbilityChangeEvent().isCancelled()) {
			getOwner().getData().put(VanillaData.CAN_FLY, previous);
			return;
		}
		updateAbilities(updateClient);
	}

	public void setCanFly(boolean canFly) {
		setCanFly(canFly, true);
	}

	public boolean canFly() {
		return getOwner().getData().get(VanillaData.CAN_FLY);
	}

	public void setGodMode(boolean godMode, boolean updateClient) {
		Boolean previous = getOwner().getData().put(VanillaData.GOD_MODE, godMode);
		if (callAbilityChangeEvent().isCancelled()) {
			getOwner().getData().put(VanillaData.GOD_MODE, previous);
			return;
		}
		updateAbilities(updateClient);
	}

	public void setGodMode(boolean godMode) {
		setGodMode(godMode, true);
	}

	public boolean getGodMode() {
		return getOwner().getData().get(VanillaData.GOD_MODE);
	}

	public void setCreativeMode(boolean creative, boolean updateClient) {
		if (creative) {
			setGamemode(GameMode.CREATIVE, updateClient);
		} else {
			setGamemode(GameMode.SURVIVAL, updateClient);
		}
	}

	public void setCreativeMode(boolean creative) {
		setCreativeMode(creative, true);
	}

	public void setGamemode(GameMode mode, boolean updateClient) {
		boolean changeToFromCreative = getGameMode() == GameMode.CREATIVE;
		Entity holder = getOwner();
		if (holder instanceof Player) {
			PlayerGameModeChangedEvent event = Spout.getEventManager().callEvent(new PlayerGameModeChangedEvent((Player) getOwner(), mode));
			if (event.isCancelled()) {
				return;
			}
			changeToFromCreative ^= event.getMode() == GameMode.CREATIVE;
			GameMode old = getGameMode();
			mode = event.getMode();
			if (changeToFromCreative) {
				if (callAbilityChangeEvent().isCancelled()) {
					mode = old;
				}
			}
			if (updateClient) {
				holder.getNetwork().callProtocolEvent(new PlayerGameStateEvent((Player) holder, PlayerGameStateMessage.CHANGE_GAME_MODE, mode), (Player) getOwner());
			}
		}
		getData().put(VanillaData.GAMEMODE, mode);
	}

	public void setGamemode(GameMode mode) {
		setGamemode(mode, true);
	}

	public GameMode getGameMode() {
		return getData().get(VanillaData.GAMEMODE);
	}

	public HumanAbilityChangeEvent callAbilityChangeEvent() {
		return Spout.getEventManager().callEvent(new HumanAbilityChangeEvent(this));
	}

	// This is here to eliminate repetitive code above
	private void updateAbilities(boolean updateClient) {
		if (!updateClient || !(getOwner() instanceof Player)) {
			return;
		}
		((Player) getOwner()).getNetworkSynchronizer().callProtocolEvent(new PlayerAbilityUpdateEvent((Player) getOwner()));
	}

	public void updateAbilities() {
		updateAbilities(true);
	}
}
