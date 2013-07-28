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
package org.spout.vanilla.component.entity.living;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.spout.api.component.entity.TextModelComponent;
import org.spout.api.data.Data;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;
import org.spout.api.math.VectorMath;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.misc.Digging;
import org.spout.vanilla.component.entity.misc.EntityHead;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.PlayerItemCollector;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.ViewDistance;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.data.configuration.WorldConfigurationNode;
import org.spout.vanilla.event.entity.HumanAbilityChangeEvent;
import org.spout.vanilla.event.player.PlayerGameModeChangedEvent;
import org.spout.vanilla.event.player.network.PlayerAbilityUpdateEvent;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.protocol.entity.HumanEntityProtocol;
import org.spout.vanilla.protocol.msg.player.PlayerGameStateMessage;

/**
 * A component that identifies the entity as a Vanilla player.
 */
public class Human extends Living {
	@Override
	public void onAttached() {
		super.onAttached();
		Entity holder = getOwner();
		holder.add(PlayerItemCollector.class);
		holder.add(Digging.class);
		holder.getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new HumanEntityProtocol());
		if (getAttachedCount() == 1) {
			holder.add(Health.class).setSpawnHealth(20);
		}
		TextModelComponent textModel = getOwner().get(TextModelComponent.class);
		if (textModel != null) {
			textModel.setSize(0.5f);
			textModel.setTranslation(new Vector3(0, 3f, 0));
		}
	}

	public byte getArrowsInBody() {
		return getData().get(VanillaData.ARROWS_IN_BODY);
	}

	public void setArrowsInBody(byte amount) {
		getData().put(VanillaData.ARROWS_IN_BODY, amount);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 10, amount));
	}

	public ViewDistance getViewDistance() {
		return getData().get(VanillaData.VIEW_DISTANCE);
	}

	public void setViewDistance(ViewDistance distance) {
		getData().put(VanillaData.VIEW_DISTANCE, distance);
		WorldConfigurationNode config = VanillaConfiguration.WORLDS.get(getOwner().getWorld().getName());
		int viewDistance;
		switch (distance) {
			case FAR:
				viewDistance = config.FAR_VIEW_DISTANCE.getInt();
				break;
			case NORMAL:
				viewDistance = config.NORMAL_VIEW_DISTANCE.getInt();
				break;
			case SHORT:
				viewDistance = config.SHORT_VIEW_DISTANCE.getInt();
				break;
			case TINY:
				viewDistance = config.TINY_VIEW_DISTANCE.getInt();
				break;
			default:
				viewDistance = config.NORMAL_VIEW_DISTANCE.getInt();
				break;
		}
		getOwner().setViewDistance(viewDistance * Chunk.BLOCKS.SIZE);
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

	public boolean isInWater() {
		return getOwner().getData().get(VanillaData.IS_IN_WATER);
	}

	public void setInWater(boolean inWater) {
		getOwner().getData().put(VanillaData.IS_IN_WATER, inWater);
	}

	public String getName() {
		return getData().get(Data.NAME);
	}

	public void setName(String name) {
		getData().put(Data.NAME, name);
		TextModelComponent textModel = getOwner().get(TextModelComponent.class);
		if (textModel != null) {
			textModel.setText(name);
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
		EntityHead head = getHead();
		if (head != null) {
			dropFrom = head.getHeadTransform();
		} else {
			dropFrom = getOwner().getPhysics().getTransform();
		}
		// Some constants
		final double impulseForce = 0.3;
		final float maxXZForce = 0.02f;
		final float maxYForce = 0.1f;

		// Create a velocity vector using the transform, apply (random) force
		Vector3 impulse = dropFrom.getRotation().getDirection().multiply(impulseForce);

		// Random rotational offset to avoid dropping at the same position
		Random rand = GenericMath.getRandom();
		float xzLength = maxXZForce * rand.nextFloat();
		float yLength = maxYForce * (rand.nextFloat() - rand.nextFloat());
		impulse = impulse.add(VectorMath.getRandomDirection2D(rand).multiply(xzLength).toVector3(yLength));

		// Slightly dropping upwards
		impulse = impulse.add(0.0, 0.1, 0.0);

		// Conversion factor, some sort of unit problem
		//TODO: This needs an actual value and this value might change when gravity changes!
		impulse = impulse.multiply(100);

		// Finally drop using a 4 second pickup delay
		Item spawnedItem = Item.drop(dropFrom.getPosition(), item, impulse);
		spawnedItem.setUncollectableDelay(4000);
	}

	/**
	 * Drops the player's current item.
	 */
	public void dropItem() {
		PlayerInventory inventory = getOwner().get(PlayerInventory.class);
		if (inventory != null) {
			Slot selected = inventory.getQuickbar().getSelectedSlot();
			ItemStack drop = selected.get();
			if (drop == null) {
				return;
			} else {
				drop = drop.clone().setAmount(1);
			}
			selected.addAmount(-1);
			dropItem(drop);
		}
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
		Number value = getOwner().getData().put(VanillaData.FLYING_SPEED, speed);

		byte previous = value == null ? VanillaData.FLYING_SPEED.getDefaultValue().byteValue() : value.byteValue();

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
			PlayerGameModeChangedEvent event = holder.getEngine().getEventManager().callEvent(new PlayerGameModeChangedEvent((Player) getOwner(), mode));
			if (event.isCancelled()) {
				return;
			}
			changeToFromCreative ^= event.getMode() == GameMode.CREATIVE;
			GameMode old = getGameMode();
			mode = event.getMode();

			//In Survival we shoudn't be able to fly.
			setCanFly(mode == GameMode.CREATIVE);
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
		return getOwner().getEngine().getEventManager().callEvent(new HumanAbilityChangeEvent(this));
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

	private final AtomicReference<Point> livePosition = new AtomicReference<Point>(null);

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		final Point position = getOwner().getPhysics().getPosition();
		livePosition.set(position);
		setInWater(position.getBlock().getMaterial() instanceof Water);
	}

	public Point getLivePosition() {
		return livePosition.get();
	}

	public void setLivePosition(Point point) {
		livePosition.set(point);
		getOwner().getPhysics().setPosition(point);
	}
}
