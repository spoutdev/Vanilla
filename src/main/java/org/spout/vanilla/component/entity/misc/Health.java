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
package org.spout.vanilla.component.entity.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.Client;
import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.component.widget.RenderPartPacksComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.gui.Widget;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.component.entity.inventory.EntityInventory;
import org.spout.vanilla.component.entity.living.hostile.EnderDragon;
import org.spout.vanilla.component.entity.living.hostile.Wither;
import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.component.entity.substance.XPOrb;
import org.spout.vanilla.data.Animation;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.VanillaRenderMaterials;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.event.cause.DamageCause;
import org.spout.vanilla.event.cause.HealCause;
import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.event.cause.NullDamageCause;
import org.spout.vanilla.event.entity.EntityDamageEvent;
import org.spout.vanilla.event.entity.EntityDeathEvent;
import org.spout.vanilla.event.entity.EntityHealEvent;
import org.spout.vanilla.event.entity.EntityHealthChangeEvent;
import org.spout.vanilla.event.entity.EntityMaxHealthChangeEvent;
import org.spout.vanilla.event.entity.network.EntityAnimationEvent;
import org.spout.vanilla.event.entity.network.EntityMetaChangeEvent;
import org.spout.vanilla.event.entity.network.EntityStatusEvent;
import org.spout.vanilla.event.player.PlayerDeathEvent;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;

/**
 * Component that adds a health-like attribute to resources.entities.
 */
public class Health extends VanillaEntityComponent {
	protected static final int DEATH_TIME_TICKS = 30;
	// Damage
	protected DamageCause<?> lastDamageCause = new NullDamageCause(DamageCause.DamageType.UNKNOWN);
	protected Object lastDamager;
	protected static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private static final float START_X = -0.71f * SCALE;
	// Client only
	private Widget hearts;
	private boolean animateHearts;
	private int heartAnimationTicks;

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onAttached() {
		if (getEngine() instanceof Client && getOwner() instanceof Player) {
			hearts = ((Client) getEngine()).getScreenStack().createWidget();
			float x = START_X;
			float dx = 0.06f * SCALE;

			// Health bar
			final RenderPartPacksComponent heartsRect = hearts.add(RenderPartPacksComponent.class);
			final RenderPartPack hearts_pack = new RenderPartPack(VanillaRenderMaterials.ICONS_MATERIAL);
			float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
			for (int i = 0; i < 10; i++) {
				final RenderPart heart = new RenderPart();
				heart.setColor(Color.WHITE);
				heart.setSprite(new org.spout.api.math.Rectangle(x + 0.005f, -0.77f, 0.065f * SCALE, 0.065f));
				heart.setSource(new org.spout.api.math.Rectangle(53f / 256f, y, 9f / 256f, 9f / 256f));
				hearts_pack.add(heart);
				x += dx;
			}

			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart heartBg = new RenderPart();
				heartBg.setColor(Color.WHITE);
				heartBg.setSprite(new org.spout.api.math.Rectangle(x, -0.77f, 0.065f * SCALE, 0.065f));
				heartBg.setSource(new org.spout.api.math.Rectangle(16f / 256f, y, 9f / 256f, 9f / 256f));
				hearts_pack.add(heartBg);
				x += dx;
			}

			heartsRect.add(hearts_pack);

			getOwner().get(HUD.class).attachWidget(hearts);
		}
	}

	@SuppressWarnings ("incomplete-switch")
	@Override
	public void onTick(float dt) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		switch (getEngine().getPlatform()) {
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
				java.util.List<RenderPart> heartParts = hearts.get(RenderPartPacksComponent.class).getRenderPartPacks().get(0).getRenderParts();
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
							heartParts.get(i).setSource(new org.spout.api.math.Rectangle(x / 256f, y, 9f / 256f, 9f / 256f));
						}

						hearts.update();
					}
				}

				float x = START_X;
				float y = -0.77f;
				float dx = 0.06f * SCALE;

				if (getHealth() <= 4) {
					java.util.List<RenderPart> parts = hearts.get(RenderPartPacksComponent.class).getRenderPartPacks().get(0).getRenderParts();
					for (int i = 0; i < 10; i++) {
						RenderPart heart = parts.get(i);
						RenderPart heartBg = parts.get(i + 10);

						if (GenericMath.getRandom().nextBoolean()) {
							y = -0.765f; // Twitch up
						} else {
							y = -0.775f; // Twitch down
						}
						heart.setSprite(new org.spout.api.math.Rectangle(x + 0.005f, y, 0.065f * SCALE, 0.065f));
						heartBg.setSprite(new org.spout.api.math.Rectangle(x, y, 0.065f * SCALE, 0.065f));

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
		EntityDeathEvent event;
		Entity owner = getOwner();
		if (owner instanceof Player) {
			event = new PlayerDeathEvent((Player) owner, lastDamageCause, lastDamager);
		} else {
			event = new EntityDeathEvent(owner, lastDamageCause, lastDamager);
		}
		if (!getEngine().getEventManager().callEvent(event).isCancelled()) {
			if (!(owner instanceof Player)) {
				owner.remove();
			}
			dropInventoryItems(owner);
			dropDropInventory(owner);
			Hunger hunger = owner.get(Hunger.class);
			if (hunger != null) {
				hunger.reset();
			}
		}
	}

	/**
	 * Drops all items from all Inventories of a Player.
	 *
	 * @param owner the entity.
	 */
	private void dropInventoryItems(Entity owner) {
		EntityInventory inventory = owner.get(EntityInventory.class);
		if (inventory != null) {
			Set<ItemStack> toDrop = new HashSet<ItemStack>();
			for (Inventory inv : inventory.getDroppable()) {
				toDrop.addAll(inv);
			}
			org.spout.api.geo.discrete.Point position = owner.getPhysics().getPosition();
			for (ItemStack stack : toDrop) {
				if (stack != null) {
					Item.dropNaturally(position, stack);
				}
			}
			inventory.clear();
			inventory.updateAll();
		}
	}

	/**
	 * Drops all items/xp in the DeathDrops of the given entity
	 *
	 * @param owner the entity
	 */
	private void dropDropInventory(Entity owner) {
		DeathDrops dropComponent = owner.get(DeathDrops.class);
		if (dropComponent != null) {
			java.util.List<ItemStack> drops = dropComponent.getDrops();
			org.spout.api.geo.discrete.Point entityPosition = owner.getPhysics().getPosition();
			for (ItemStack stack : drops) {
				if (stack != null) {
					Item.drop(entityPosition, stack, Vector3.ZERO);
				}
			}
			if (dropComponent.getXpDrop() > 0 && VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_XP.getBoolean()) {
				org.spout.api.geo.discrete.Point pos = getOwner().getPhysics().getPosition();

				XPOrb xporb = pos.getWorld().createEntity(pos, XPOrb.class).add(XPOrb.class);
				xporb.setExperience(dropComponent.getXpDrop());
				pos.getWorld().spawnEntity(xporb.getOwner());
			}
		}
	}

	/**
	 * Gets the last cause of the damage
	 *
	 * @return the last damager
	 */
	public DamageCause<?> getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this entity
	 *
	 * @return last damager
	 */
	public Object getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this entity can have
	 *
	 * @return the maximum health
	 */
	public float getMaxHealth() {
		return getData().get(VanillaData.MAX_HEALTH);
	}

	/**
	 * Sets the maximum health this entity can have
	 *
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(float maxHealth) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
        EntityMaxHealthChangeEvent event = new EntityMaxHealthChangeEvent(getOwner(), maxHealth);
        getEngine().getEventManager().callEvent(event);
        if (!event.isCancelled()) {
            getData().put(VanillaData.MAX_HEALTH, event.getMaxHealth());
        }
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 *
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(float maxHealth) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		this.setMaxHealth(maxHealth);
		this.setHealth(maxHealth, HealthChangeCause.SPAWN);
	}

	/**
	 * Gets the health of this entity (hitpoints)
	 *
	 * @return the health value
	 */
	public float getHealth() {
		return getData().get(VanillaData.HEALTH);
	}

	/**
	 * Sets the current health value for this entity
	 *
	 * @param health hitpoints value to set to
	 * @param cause of the change
	 */
	public void setHealth(float health, HealthChangeCause cause) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getOwner(), cause, health - getHealth());
		getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getHealth() + event.getChange() > getMaxHealth()) {
				getData().put(VanillaData.HEALTH, getMaxHealth());
			} else {
				getData().put(VanillaData.HEALTH, getHealth() + event.getChange());
			}
		}

		// Special cases
		Entity owner = getOwner();
		if (Spout.getPlatform() == Platform.SERVER) {
			if (owner instanceof Player) {
				owner.getNetwork().callProtocolEvent(new PlayerHealthEvent((Player) getOwner()));
			} else if (owner instanceof EnderDragon || owner instanceof Wither) {
				java.util.List<Parameter<?>> params = new ArrayList<Parameter<?>>(1);
				params.add(new Parameter<Short>(Parameter.TYPE_SHORT, 16, (short) health));
				owner.getNetwork().callProtocolEvent(new EntityMetaChangeEvent(owner, params));
			}
		}
	}

	/**
	 * Heals this entity
	 *
	 * @param amount amount the entity will be healed by
	 */
	public void heal(float amount) {
		heal(amount, HealCause.UNKNOWN);
	}

	/**
	 * Heals this entity with the given {@link org.spout.vanilla.event.cause.HealCause}
	 *
	 * @param amount amount the entity will be healed by
	 * @param cause cause of this entity being healed
	 */
	public void heal(float amount, HealCause cause) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		EntityHealEvent event = new EntityHealEvent(getOwner(), amount, cause);
		EntityHealEvent healEvent = getOwner().getEngine().getEventManager().callEvent(event);
		if (!healEvent.isCancelled()) {
			setHealth(getHealth() + event.getHealAmount(), HealthChangeCause.HEAL);
		}
	}

	/**
	 * Sets the health value to 0
	 *
	 * @param cause of the change
	 */
	public void kill(HealthChangeCause cause) {
		setHealth(0, cause);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 *
	 * @return True if the entity is dead (Health less than 0) else false.
	 */
	public boolean isDead() {
		return getHealth() <= 0.0f;
	}

	/**
	 * Returns true if the entity is currently dying, death ticks is greater than 0.
	 *
	 * @return true if the entity is dying
	 */
	public boolean isDying() {
		return getDeathTicks() > 0;
	}

	/**
	 * Retrieve the death ticks of this entity
	 *
	 * @return The death ticks amount
	 */
	public int getDeathTicks() {
		return getData().get(VanillaData.DEATH_TICKS);
	}

	/**
	 * Set the death ticks of this entity
	 *
	 * @param deathTicks the amount of death ticks.
	 */
	public void setDeathTicks(int deathTicks) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		if (deathTicks > DEATH_TIME_TICKS) {
			deathTicks = DEATH_TIME_TICKS;
		}
		getData().put(VanillaData.DEATH_TICKS, deathTicks);
	}

	/**
	 * Damages this entity
	 *
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 */
	public void damage(float amount) {
		damage(amount, new NullDamageCause(DamageCause.DamageType.UNKNOWN));
	}

	/**
	 * Damages this entity with the given {@link org.spout.vanilla.event.cause.DamageCause}.
	 *
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 */
	public void damage(float amount, DamageCause<?> cause) {
		damage(amount, cause, true);
	}

	/**
	 * Damages this entity with the given {@link org.spout.vanilla.event.cause.DamageCause} and damager.
	 *
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(float amount, DamageCause<?> cause, boolean sendHurtMessage) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		Cause<?> eventCause;
		if (cause instanceof Cause<?>) {
			eventCause = (Cause<?>) cause;
		} else {
			eventCause = new NullDamageCause(cause.getType());
		}
		// TODO take potion effects into account
		EntityDamageEvent event = getEngine().getEventManager().callEvent(new EntityDamageEvent(getOwner(), amount, eventCause, sendHurtMessage));
		if (event.isCancelled()) {
			return;
		}

		setHealth(getHealth() - event.getDamage(), HealthChangeCause.DAMAGE);
		lastDamager = event.getDamager();
		lastDamageCause = cause;

		// Add exhaustion to damaged
		Hunger hunger = getOwner().get(Hunger.class);
		if (hunger != null) {
			hunger.setExhaustion(hunger.getExhaustion() + 0.3f);
		}

		if (event.getSendMessage()) {
			getOwner().getNetwork().callProtocolEvent(new EntityAnimationEvent(getOwner(), Animation.DAMAGE_ANIMATION));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
			//getHurtEffect().playGlobal(getParent().getParent().getPosition());
		}
	}

	/**
	 * True if the specific entity has an animation when it dies.
	 *
	 * @return true if animated death
	 */
	public boolean hasDeathAnimation() {
		return getData().get(VanillaData.HAS_DEATH_ANIMATION);
	}

	/**
	 * Sets whether this entity has a death animation or not.
	 */
	public void setDeathAnimation(boolean hasDeathAnimation) {
		if (!VanillaConfiguration.PLAYER_SURVIVAL_ENABLE_HEALTH.getBoolean()) {
			return;
		}
		getData().put(VanillaData.HAS_DEATH_ANIMATION, hasDeathAnimation);
	}

	/**
	 * True if the entity has infinite health.
	 *
	 * @return true if entity has infinite health
	 */
	public boolean hasInfiniteHealth() {
		return getHealth() == -1.0f;
	}
}
