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
package org.spout.vanilla.plugin.component.misc;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.geo.discrete.Point;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Rectangle;
import org.spout.api.math.Vector3;
import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.plugin.component.substance.Item;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.data.Animation;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.VanillaRenderMaterials;
import org.spout.vanilla.plugin.event.cause.DamageCause;
import org.spout.vanilla.plugin.event.cause.DamageCause.DamageType;
import org.spout.vanilla.plugin.event.cause.HealCause;
import org.spout.vanilla.plugin.event.cause.HealthChangeCause;
import org.spout.vanilla.plugin.event.cause.NullDamageCause;
import org.spout.vanilla.plugin.event.entity.EntityAnimationEvent;
import org.spout.vanilla.plugin.event.entity.EntityDamageEvent;
import org.spout.vanilla.plugin.event.entity.EntityHealEvent;
import org.spout.vanilla.plugin.event.entity.EntityHealthChangeEvent;
import org.spout.vanilla.plugin.event.entity.EntityStatusEvent;
import org.spout.vanilla.plugin.event.entity.VanillaEntityDeathEvent;
import org.spout.vanilla.plugin.event.player.PlayerDeathEvent;
import org.spout.vanilla.plugin.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.plugin.inventory.player.PlayerArmorInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerCraftingInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerMainInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityStatusMessage;

/**
 * Component that adds a health-like attribute to resources.entities.
 */
public class HealthComponent extends EntityComponent {
	private static final int DEATH_TIME_TICKS = 30;
	// Damage
	private DamageCause<?> lastDamageCause = new NullDamageCause(DamageType.UNKNOWN);
	private Object lastDamager;
	// Client only
	private final Widget hearts = new Widget();
	private boolean animateHearts;
	private int heartAnimationTicks;
	private final Random random = new Random();
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private static final float START_X = -0.71f * SCALE;

	@Override
	public void onAttached() {
		if (Spout.getEngine() instanceof Client && getOwner() instanceof Player) {
			float x = START_X;
			float dx = 0.06f * SCALE;

			// Health bar
			final RenderPartsHolderComponent heartsRect = hearts.add(RenderPartsHolderComponent.class);
			float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
			for (int i = 0; i < 10; i++) {
				final RenderPart heart = new RenderPart();
				heart.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
				heart.setColor(Color.WHITE);
				heart.setSprite(new Rectangle(x + 0.005f, -0.77f, 0.065f * SCALE, 0.065f));
				heart.setSource(new Rectangle(53f / 256f, y, 9f / 256f, 9f / 256f));
				heartsRect.add(heart);
				x += dx;
			}

			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart heartBg = new RenderPart();
				heartBg.setRenderMaterial(VanillaRenderMaterials.ICONS_MATERIAL);
				heartBg.setColor(Color.WHITE);
				heartBg.setSprite(new Rectangle(x, -0.77f, 0.065f * SCALE, 0.065f));
				heartBg.setSource(new Rectangle(16f / 256f, y, 9f / 256f, 9f / 256f));
				heartsRect.add(heartBg);
				x += dx;
			}

			getOwner().add(HUDComponent.class).attachWidget(hearts);
		}
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		switch (Spout.getPlatform()) {
			case PROXY:
			case SERVER:
				if (isDying()) {
					setDeathTicks(getDeathTicks() - 1);
					if (getDeathTicks() <= 0) {
						onDeath();
					}
				} else if (isDead()) {
					if (hasDeathAnimation()) {
						setDeathTicks(DEATH_TIME_TICKS);
						getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_DEAD));
					} else {
						onDeath();
					}
				}
				break;
			case CLIENT:
				if (!(getOwner() instanceof Player)) {
					return;
				}
				List<RenderPart> heartParts = hearts.get(RenderPartsHolderComponent.class).getRenderParts();
				if (animateHearts) {
					float x = 0;
					if (heartAnimationTicks == 3) {
						// Flash off
						x = 16f;
						heartAnimationTicks++;
					} else if (heartAnimationTicks == 6) {
						// Flash on
						x = 25f;
						heartAnimationTicks++;
					} else if (heartAnimationTicks == 9) {
						// Flash off final time
						x = 16f;
						heartAnimationTicks = 0;
						animateHearts = false;
					} else {
						heartAnimationTicks++;
					}

					if (x != 0) {
						float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
						for (int i = 10; i < 20; i++) {
							heartParts.get(i).setSource(new Rectangle(x / 256f, y, 9f / 256f, 9f / 256f));
						}

						hearts.update();
					}
				}

				float x = START_X;
				float y = -0.77f;
				float dx = 0.06f * SCALE;

				if (getHealth() <= 4) {
					List<RenderPart> parts = hearts.get(RenderPartsHolderComponent.class).getRenderParts();
					for (int i = 0; i < 10; i++) {
						RenderPart heart = parts.get(i);
						RenderPart heartBg = parts.get(i + 10);

						if (random.nextBoolean()) {
							y = -0.765f; // Twitch up
						} else {
							y = -0.775f; // Twitch down
						}
						heart.setSprite(new Rectangle(x + 0.005f, y, 0.065f * SCALE, 0.065f));
						heartBg.setSprite(new Rectangle(x, y, 0.065f * SCALE, 0.065f));

						x += dx;
					}

					hearts.update();
				}
				break;
		}
	}

	/**
	 * Called when the resources.entities' health hits zero and is considered "dead" by Vanilla game standards
	 */
	private void onDeath() {
		VanillaEntityDeathEvent event;
		Entity owner = getOwner();
		if (owner instanceof Player) {
			event = new PlayerDeathEvent((Player) owner);
		} else {
			event = new VanillaEntityDeathEvent(owner);
		}
		//TODO: xp drops?
		if (!Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
			if (!(owner instanceof Player)) {
				owner.remove();
			} else {
				dropPlayerItems((Player) owner);
			}
			DropComponent dropComponent = owner.get(DropComponent.class);
			if (dropComponent != null) {
				List<ItemStack> drops = dropComponent.getDrops();
				Point entityPosition = owner.getTransform().getPosition();
				for (ItemStack stack : drops) {
					if (stack != null) {
						Item.drop(entityPosition, stack, Vector3.ZERO);
					}
				}
			}
			HungerComponent hungerComponent = owner.get(HungerComponent.class);
			if (hungerComponent != null) {
				hungerComponent.reset();
			}
		}
	}

	/**
	 * Drops all items from all Inventories of a Player.
	 * @param owner the player.
	 */
	private void dropPlayerItems(Player owner) {
		PlayerInventory playerInventory = owner.get(PlayerInventory.class);
		if (playerInventory != null) {
			Set<ItemStack> toDrop = new HashSet<ItemStack>();
			PlayerArmorInventory armorInventory = playerInventory.getArmor();
			PlayerCraftingInventory craftingGrid = playerInventory.getCraftingGrid();
			PlayerMainInventory mainInventory = playerInventory.getMain();
			PlayerQuickbar quickbar = playerInventory.getQuickbar();
			toDrop.addAll(armorInventory);
			toDrop.addAll(craftingGrid);
			toDrop.addAll(mainInventory);
			toDrop.addAll(quickbar);
			Point position = owner.getTransform().getPosition();
			for (ItemStack stack : toDrop) {
				if (stack != null) {
					Item.dropNaturally(position, stack);
				}
			}
			playerInventory.clear();
			playerInventory.updateAll();
		}
	}

	/**
	 * Gets the last cause of the damage
	 * @return the last damager
	 */
	public DamageCause<?> getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this entity
	 * @return last damager
	 */
	public Object getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this entity can have
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return getData().get(VanillaData.MAX_HEALTH);
	}

	/**
	 * Sets the maximum health this entity can have
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(int maxHealth) {
		getData().put(VanillaData.MAX_HEALTH, maxHealth);
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(int maxHealth) {
		this.setMaxHealth(maxHealth);
		//Do not call setHealth yet, network has not been initialized if loading from file
		getData().put(VanillaData.HEALTH, maxHealth);
	}

	/**
	 * Gets the health of this entity (hitpoints)
	 * @return the health value
	 */
	public int getHealth() {
		return getData().get(VanillaData.HEALTH);
	}

	/**
	 * Sets the current health value for this entity
	 * @param health hitpoints value to set to
	 * @param cause of the change
	 */
	public void setHealth(int health, HealthChangeCause cause) {
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getOwner(), cause, health - getHealth());
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getHealth() + event.getChange() > getMaxHealth()) {
				getData().put(VanillaData.HEALTH, getMaxHealth());
			} else {
				getData().put(VanillaData.HEALTH, getHealth() + event.getChange());
			}
		}

		if (getOwner() instanceof Player) {
			getOwner().getNetwork().callProtocolEvent(new PlayerHealthEvent(((Player) getOwner())));
		}
	}

	/**
	 * Heals this entity
	 * @param amount amount the entity will be healed by
	 */
	public void heal(int amount) {
		heal(amount, HealCause.UNKNOWN);
	}

	/**
	 * Heals this entity with the given {@link HealCause}
	 * @param amount amount the entity will be healed by
	 * @param cause cause of this entity being healed
	 */
	public void heal(int amount, HealCause cause) {
		EntityHealEvent event = new EntityHealEvent(getOwner(), amount, cause);
		Spout.getEngine().getEventManager().callEvent(event);

		setHealth(getHealth() + event.getHealAmount(), HealthChangeCause.HEAL);
	}

	/**
	 * Sets the health value to 0
	 * @param cause of the change
	 */
	public void kill(HealthChangeCause cause) {
		setHealth(0, cause);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 * @return dead
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}

	/**
	 * @return
	 */
	public boolean isDying() {
		return getDeathTicks() > 0;
	}

	/**
	 * @return
	 */
	public int getDeathTicks() {
		return getData().get(VanillaData.DEATH_TICKS);
	}

	/**
	 * @param deathTicks
	 */
	public void setDeathTicks(int deathTicks) {
		if (deathTicks > DEATH_TIME_TICKS) {
			deathTicks = DEATH_TIME_TICKS;
		}
		getData().put(VanillaData.DEATH_TICKS, deathTicks);
	}

	/**
	 * Damages this entity
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 */
	public void damage(int amount) {
		damage(amount, new NullDamageCause(DamageType.UNKNOWN));
	}

	/**
	 * Damages this entity with the given {@link DamageCause}.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 */
	public void damage(int amount, DamageCause<?> cause) {
		damage(amount, cause, true);
	}

	/**
	 * Damages this entity with the given {@link DamageCause} and damager.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 * @param damager entity that damaged this entity
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(int amount, DamageCause<?> cause, boolean sendHurtMessage) {
		Cause<?> eventCause;
		if (cause instanceof Cause<?>) {
			eventCause = (Cause<?>) cause;
		} else {
			eventCause = new NullDamageCause(cause.getType());
		}
		// TODO take potion effects into account
		EntityDamageEvent event = Spout.getEngine().getEventManager().callEvent(new EntityDamageEvent(getOwner(), amount, eventCause, sendHurtMessage));
		if (event.isCancelled()) {
			return;
		}

		setHealth(getHealth() - event.getDamage(), HealthChangeCause.DAMAGE);
		lastDamager = event.getDamager();
		lastDamageCause = cause;

		// Add exhaustion to damaged
		if (getOwner().has(HungerComponent.class)) {
			HungerComponent hungerComponent = getOwner().get(HungerComponent.class);
			hungerComponent.setExhaustion(hungerComponent.getExhaustion() + 0.3f);
		}

		if (event.getSendMessage()) {
			getOwner().getNetwork().callProtocolEvent(new EntityAnimationEvent(getOwner(), Animation.DAMAGE_ANIMATION));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
			//getHurtEffect().playGlobal(getParent().getParent().getPosition());
		}
	}

	public boolean hasDeathAnimation() {
		return getData().get(VanillaData.HAS_DEATH_ANIMATION);
	}

	public void setDeathAnimation(boolean hasDeathAnimation) {
		getData().put(VanillaData.HAS_DEATH_ANIMATION, hasDeathAnimation);
	}

	public boolean hasInfiniteHealth() {
		return getHealth() == -1;
	}
}
