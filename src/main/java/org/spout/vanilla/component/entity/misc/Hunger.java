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
package org.spout.vanilla.component.entity.misc;

import org.spout.api.Client;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Slot;

import org.spout.vanilla.component.entity.living.neutral.Human;
import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.cause.DamageCause.DamageType;
import org.spout.vanilla.event.cause.HealCause;
import org.spout.vanilla.event.cause.NullDamageCause;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.material.block.liquid.Water;
import org.spout.vanilla.material.item.Food;
import org.spout.vanilla.material.item.potion.PotionItem;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;

/**
 * Component to handle everything related to the hunger system.
 * It controls the Hunger level, the food saturation value, the exhaustion value and the eating process.
 */
public class Hunger extends EntityComponent {
	//Timer used for when eating. Prevents insta-eating.
	private float eatingTimer;
	private Slot foodEating;
	private Human human;
	private static final float TIMER_START = 4;
	private float timer = TIMER_START;
	private Point lastPos;
	
	private float fx;
	private float bx;

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("Hunger may only be attached to players.");
		}
		human = getOwner().add(Human.class);
	}

	@Override
	public boolean canTick() {
		return !human.isCreative() && !human.getHealth().isDead();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onTick(float dt) {
		/*
		 * The Minecraft hunger system has a few different dynamics:
		 * 
		 * 1) hunger - the amount of 'shanks' shown on the client. 1 points = 1/2 shank
		 * 
		 * 2) food saturation - an invisible 'safety net' that is a default 5 points.
		 * 
		 * 3) the timer - decreases by the delta of a tick every tick if 'hunger' > 17 or if 'food level' <= 0 and heals or deals one point of damage respectively
		 * 
		 * 4) exhaustion - anywhere in between 0 and 4 and increases with certain actions. When the exhaustion reaches 4, it is reset and subtracts one point from 'food saturation' if 'food
		 * saturation' > 0 or one point from 'hunger' if 'food saturation' <= 0 and 'hunger' > 0.
		 * 
		 * Exhaustion actions: Walking and sneaking (per block) - 0.01 Sprinting (per block) - 0.1 Swimming (per block) - 0.015 Jumping (per block) - 0.2 Sprint jump (per block) - 0.8 Digging - 0.025
		 * Attacking or being attacked - 0.3 Food poisoning - 15 total over entire duration
		 */

		switch (Spout.getPlatform()) {
			case PROXY:
			case SERVER:
				final Health healthComponent = human.getHealth();
				final int health = healthComponent.getHealth();
				final int hunger = getHunger();

				//Timer when eating. Sends a Enting done if the player eated the food the whole time.
				if (eatingTimer != 0f) {
					if (eatingTimer >= 1.5f) {
						((Player) getOwner()).getSession().send(false, new EntityStatusMessage(getOwner().getId(), EntityStatusMessage.EATING_ACCEPTED));
						if (foodEating.get() != null) {
							if (foodEating.get().getMaterial() instanceof Food) {
								((Food) foodEating.get().getMaterial()).onEat(getOwner(), foodEating);
							} else if (foodEating.get().getMaterial() instanceof PotionItem) {
								((PotionItem)foodEating.get().getMaterial()).onDrink(getOwner(),foodEating);
							}
						}
						eatingTimer = 0f;
						foodEating = null;
					} else {
						eatingTimer += dt;
					}
				}

				// Regenerate health
				if (health < 20 && hunger > 17) {
					timer -= dt;
					if (timer <= 0) {
						healthComponent.heal(1, HealCause.REGENERATION);
						timer = TIMER_START;
					}
				}

				// Damage health

				if (hunger <= 0) {
					timer -= dt;
					if (timer <= 0) {
						healthComponent.damage(1, new NullDamageCause(DamageType.STARVATION));
						timer = TIMER_START;
					}
				}

				// Exhaustion

				final Point pos = getOwner().getScene().getPosition();
				if (lastPos == null) {
					lastPos = pos;
					return;
				}

				float exhaustion = getExhaustion();
				final World world = pos.getWorld();

				// Did not move 1 block pos
				if (lastPos.getBlockX() != pos.getBlockX() || lastPos.getBlockY() != pos.getBlockY() || lastPos.getBlockZ() != pos.getBlockZ()) {
					int dx = lastPos.getBlockX() - pos.getBlockX();
					int dy = lastPos.getBlockY() - pos.getBlockY();
					int dz = lastPos.getBlockZ() - pos.getBlockZ();

					final boolean sprinting = human.isSprinting();
					final boolean jumping = human.isJumping();
					if (world.getBlock(pos).getMaterial() instanceof Water && world.getBlock(lastPos).getMaterial() instanceof Water) {
						// swimming						;
						exhaustion += 0.015F * Math.sqrt(dx * dx + dy * dy + dz * dz);
					} else if (sprinting && jumping) {
						// sprint jumping
						exhaustion += 0.8f;
					} else if (jumping) {
						// jumping
						exhaustion += 0.2f;
					} else if (sprinting) {
						// sprinting
						exhaustion += 0.1f * Math.sqrt(dx * dx + dz * dz);
					} else {
						// walking
						exhaustion += Math.sqrt(dx * dx + dz * dz) * 0.01F;
					}
					lastPos = pos; // Set the last position for next run
				}

				final Digging diggingComponent = getOwner().add(Digging.class);
				final int digging = diggingComponent.getBlockBroken();
				for (int i = 0; i < digging; i++) {
					exhaustion += 0.025f;
				}

				diggingComponent.setBlockBroken(0);

				final float foodSaturation = getFoodSaturation();
				if (exhaustion >= 4) {
					if (foodSaturation > 0) {
						setFoodSaturation(foodSaturation - 1);
					} else if (hunger > 0) {
						setHunger(hunger - 1);
					}
					exhaustion = 0;
				}

				setExhaustion(exhaustion);
				break;

			case CLIENT:
				if (!(getOwner() instanceof Player)) {
					return;
				}

				HUD HUD = getOwner().get(org.spout.vanilla.component.entity.player.HUD.class);
				HUD.getHungerMeter().animate();

				break;
		}
	}

	/**
	 * Retrieve the hunger level of the entity.
	 * @return The hunger level.
	 */
	public int getHunger() {
		return getData().get(VanillaData.HUNGER);
	}

	// Need to confirm what fx/bx equals to rename methods propertly
	public float getFx() {
		return fx;
	}
	
	public float getBx() {
		return bx;
	}
	
	/**
	 * Sets the hunger level of the entity.
	 * The maximum is 20.
	 * @param hunger The hunger level of the entity
	 */
	public void setHunger(int hunger) {
		getData().put(VanillaData.HUNGER, Math.min(hunger, 20));
		reload();
		if (Spout.getEngine() instanceof Client) {
			//render(52, 16);
			fx = 52;
			bx = 16;
			getOwner().get(HUD.class).getHungerMeter().update();
		}
	}

	/**
	 * Retrieve the food saturation value of the entity.
	 * @return The food saturation value.
	 */
	public float getFoodSaturation() {
		return getData().get(VanillaData.FOOD_SATURATION);
	}

	/**
	 * Sets the food saturation level of the entity.
	 * A value of 0 makes the food bar "jump". It can't be higher than the current hunger level.
	 * @param foodSaturation The food saturation value.
	 */
	public void setFoodSaturation(float foodSaturation) {
		getData().put(VanillaData.FOOD_SATURATION, Math.min(foodSaturation, getHunger()));
		reload();
	}

	/**
	 * Retrieve the exhaustion value of the entity.
	 * @return The exhaustion value.
	 */
	public float getExhaustion() {
		return getData().get(VanillaData.EXHAUSTION);
	}

	/**
	 * Sets the exhaustion value of the entity.
	 * @param exhaustion The exhaustion value.
	 */
	public void setExhaustion(float exhaustion) {
		getData().put(VanillaData.EXHAUSTION, exhaustion);
	}

	/**
	 * Retrieve the poisoned status.
	 * @return True if the entity is poisoned else false
	 */
	public boolean isPoisoned() {
		return getData().get(VanillaData.POISONED);
	}

	/**
	 * Set the entity poisoned by food or not
	 * @param poisoned True if he is poisoned else false
	 */
	public void setPoisoned(boolean poisoned) {
		getData().put(VanillaData.POISONED, poisoned);
		if (Spout.getEngine() instanceof Client) {
			if (poisoned) {
				fx = 88;
				bx = 133;
			} else {
				fx = 52;
				bx = 16;	
			}
			getOwner().get(HUD.class).getHungerMeter().update();
		}
	}

	public Player getPlayer() {
		return (Player) getOwner();
	}

	public void reload() {
		if (Spout.getEngine() instanceof Server) {
			getPlayer().getNetworkSynchronizer().callProtocolEvent(new PlayerHealthEvent(getPlayer()));
		}
	}

	/**
	 * Reset all the variables of the Hunger component to the default ones.
	 */
	public void reset() {
		setHunger(VanillaData.HUNGER.getDefaultValue());
		setFoodSaturation(VanillaData.FOOD_SATURATION.getDefaultValue());
		setExhaustion(VanillaData.EXHAUSTION.getDefaultValue());
		setPoisoned(VanillaData.POISONED.getDefaultValue());
	}

	/**
	 * Sets the player as eating. This will starts a timer to be sure the player doesn't instant-eat the food. Does nothing if eating is true but slot is null.
	 * @param eating Is the player eating? If true, starts the timer.
	 * @param slot The slot associated with the food being used.
	 */
	public void setEating(boolean eating, Slot slot) {
		if (eating && slot != null) {
			eatingTimer = 0.01f; // The tick works only if it's higher than 0.
			foodEating = slot;
		} else {
			eatingTimer = 0f;
		}
	}
}
