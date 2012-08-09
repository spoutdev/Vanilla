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
package org.spout.vanilla.entity.living;

import org.spout.api.data.Data;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class Human extends Living {
	protected Vector3 lookingAt;
	protected boolean isDigging, onGround, sprinting;
	protected Point diggingPosition;
	protected long diggingStartTime;
	protected int miningDamagePosition = 0;
	protected long previousDiggingTime = 0;
	protected int miningDamageAllowance = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ALLOWANCE.getInt(), miningDamagePeriod = VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_PERIOD.getInt();
	protected int[] miningDamage;
	protected String title; //TODO title isn't really a good name...
	protected ItemStack renderedItemInHand;

	public Human() {
		super(VanillaControllerTypes.HUMAN);
		setHeadHeight(1.62f);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

	@Override
	public void onAttached() {
		super.onAttached();
		getParent().setObserver(true);
	}

	@Override
	public void onSave() {
		super.onSave();
		data().put(Data.TITLE, title);
	}

	@Override
	public boolean isSavable() {
		return true;
	}

	@Override
	public float getHeadHeight() {
		float height = super.getHeadHeight();
		if (this.crouching) {
			height -= 0.08f;
		}
		return height;
	}

	@Override
	public boolean needsPositionUpdate() {
		return true;
	}

	@Override
	public boolean needsVelocityUpdate() {
		return true;
	}

	/**
	 * Gets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @return rendered item in hand
	 */
	public ItemStack getRenderedItemInHand() {
		return renderedItemInHand;
	}

	/**
	 * Sets the item rendered in the human's hand; not neccassaily the actual item in the human's hand.
	 * @param renderedItemInHand
	 */
	public void setRenderedItemInHand(ItemStack renderedItemInHand) {
		this.renderedItemInHand = renderedItemInHand;
	}

	/**
	 * Gets the name displayed above the human's head.
	 * @return title name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the name displayed above the human's head.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets whether or not th player is
	 * @param sprinting
	 */
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	/**
	 * Whether or not the player is sprinting.
	 * @return true if sprinting
	 */
	public boolean isSprinting() {
		return sprinting;
	}

	/**
	 * Sets whether or not the player is perceived by the client as being on the ground.
	 * @param onGround
	 */
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * Whether or not the player is on the ground.
	 * @return true if on ground.
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Sets the position where the player should look.
	 * @param lookingAt {@link org.spout.api.math.Vector3} to look at
	 */
	public void setLookingAtVector(Vector3 lookingAt) {
		this.lookingAt = lookingAt;
	}

	/**
	 * Gets the {@link Vector3} the player is currently looking at.
	 * @return position the player is looking at
	 */
	public Vector3 getLookingAt() {
		return lookingAt;
	}

	/**
	 * Returns the digging state of the entity
	 * @return true if player is digging
	 */
	public boolean isDigging() {
		return isDigging;
	}

	/**
	 * Sets isDigging true and records start time, unless already digging
	 * @return true if successful
	 */
	public boolean startDigging(Point position) {
		if (getParent().getPosition().getDistance(position) > 6) { // TODO: Actually get block reach from somewhere instead of just using 6
			return false;
		}
		isDigging = true;
		diggingPosition = position;
		diggingStartTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * Sets isDigging false and records total time, unless the dig was invalid/never started.
	 * @return true if successful
	 */
	public boolean stopDigging(Point position) {
		if (!isDigging) {
			return false;
		}
		previousDiggingTime = getDiggingTime();
		isDigging = false;
		VanillaNetworkUtil.sendPacketsToNearbyPlayers(getParent(), getParent().getViewDistance(), new EntityAnimationMessage(getParent().getId(), EntityAnimationMessage.ANIMATION_NONE));
		if (!position.equals(diggingPosition)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets time spent digging
	 * @return time spent digging
	 */
	public long getDiggingTime() {
		if (!isDigging) {
			return previousDiggingTime;
		}

		// Is this correct?
		return System.currentTimeMillis() - diggingStartTime;
	}

	/**
	 * Gets last time spent digging in real(client) ticks
	 * @return ticks spent digging
	 */
	public long getDiggingTicks() {
		return getDiggingTime() / 50;
	}

	/**
	 * Adds and checks mining speed for cheating.
	 * @param damageRemaining Remaining damage on block
	 * @return false if player is cheating
	 */
	public boolean addAndCheckMiningSpeed(int damageRemaining) {
		if (!VanillaConfiguration.PLAYER_SPEEDMINING_PREVENTION_ENABLED.getBoolean()) {
			return true;
		}

		miningDamage[miningDamagePosition++] = damageRemaining;

		if (miningDamagePosition >= miningDamagePeriod) {
			miningDamagePosition = 0;
		}

		return checkMiningSpeed();
	}

	/**
	 * Checks mining speed for cheating.
	 * @return false if player is cheating
	 */
	public boolean checkMiningSpeed() {
		if (MathHelper.mean(miningDamage) > miningDamageAllowance) { // TODO: Make this configurable?
			return false;
		}
		return true;
	}
}
