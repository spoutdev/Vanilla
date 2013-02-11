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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cause;
import org.spout.api.geo.discrete.Point;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.gui.render.RenderPartPack;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Rectangle;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.Platform;
import org.spout.api.util.Parameter;

import org.spout.vanilla.api.component.inventory.EntityInventoryComponent;
import org.spout.vanilla.api.component.misc.HealthComponent;
import org.spout.vanilla.api.data.Animation;
import org.spout.vanilla.api.event.cause.DamageCause;
import org.spout.vanilla.api.event.cause.HealthChangeCause;
import org.spout.vanilla.api.event.cause.NullDamageCause;
import org.spout.vanilla.api.event.entity.EntityAnimationEvent;
import org.spout.vanilla.api.event.entity.EntityDamageEvent;
import org.spout.vanilla.api.event.entity.EntityHealthChangeEvent;
import org.spout.vanilla.api.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.api.event.entity.EntityStatusEvent;
import org.spout.vanilla.api.event.entity.VanillaEntityDeathEvent;
import org.spout.vanilla.api.event.player.PlayerDeathEvent;
import org.spout.vanilla.api.event.player.network.PlayerHealthEvent;

import org.spout.vanilla.plugin.component.living.hostile.EnderDragon;
import org.spout.vanilla.plugin.component.player.HUDComponent;
import org.spout.vanilla.plugin.component.substance.XPOrb;
import org.spout.vanilla.plugin.component.substance.object.Item;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.api.data.VanillaData;
import org.spout.vanilla.api.data.VanillaRenderMaterials;
import org.spout.vanilla.plugin.protocol.msg.entity.EntityStatusMessage;

/**
 * Component that adds a health-like attribute to resources.entities.
 */
public class Health extends HealthComponent {
	private static final float START_X = -0.71f * SCALE;

	// Client only
	private Widget hearts;
	private boolean animateHearts;
	private int heartAnimationTicks;

	public Health() {
		if (Spout.getPlatform()==Platform.CLIENT) {
			hearts = ((Client)Spout.getEngine()).getScreenStack().createWidget();
		}
	}

	@Override
	public void onAttached() {
		if (Spout.getEngine() instanceof Client && getOwner() instanceof Player) {
			float x = START_X;
			float dx = 0.06f * SCALE;

			// Health bar
			final RenderPartsHolderComponent heartsRect = hearts.add(RenderPartsHolderComponent.class);
			final RenderPartPack hearts_pack = new RenderPartPack(VanillaRenderMaterials.ICONS_MATERIAL);
			float y = VanillaConfiguration.HARDCORE_MODE.getBoolean() ? 45f / 256f : 0;
			for (int i = 0; i < 10; i++) {
				final RenderPart heart = new RenderPart();
				heart.setColor(Color.WHITE);
				heart.setSprite(new Rectangle(x + 0.005f, -0.77f, 0.065f * SCALE, 0.065f));
				heart.setSource(new Rectangle(53f / 256f, y, 9f / 256f, 9f / 256f));
				hearts_pack.add(heart);
				x += dx;
			}

			x = START_X;
			for (int i = 0; i < 10; i++) {
				final RenderPart heartBg = new RenderPart();
				heartBg.setColor(Color.WHITE);
				heartBg.setSprite(new Rectangle(x, -0.77f, 0.065f * SCALE, 0.065f));
				heartBg.setSource(new Rectangle(16f / 256f, y, 9f / 256f, 9f / 256f));
				hearts_pack.add(heartBg);
				x += dx;
			}
			
			heartsRect.add(hearts_pack);

			getOwner().get(HUDComponent.class).attachWidget(hearts);
		}
	}

	@SuppressWarnings("incomplete-switch")
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
				List<RenderPart> heartParts = hearts.get(RenderPartsHolderComponent.class).getRenderPartPacks().get(0).getRenderParts();
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
					List<RenderPart> parts = hearts.get(RenderPartsHolderComponent.class).getRenderPartPacks().get(0).getRenderParts();
					for (int i = 0; i < 10; i++) {
						RenderPart heart = parts.get(i);
						RenderPart heartBg = parts.get(i + 10);

						if (GenericMath.getRandom().nextBoolean()) {
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
		if (!Spout.getEngine().getEventManager().callEvent(event).isCancelled()) {
			if (!(owner instanceof Player)) {
				owner.remove();
			}
			dropInventoryItems(owner);
			dropDropInventory(owner);
			HungerComponent hungerComponent = owner.get(HungerComponent.class);
			if (hungerComponent != null) {
				hungerComponent.reset();
			}
		}
	}

	/**
	 * Drops all items from all Inventories of a Player.
	 * @param owner the entity.
	 */
	private void dropInventoryItems(Entity owner) {
		EntityInventoryComponent inventory = owner.get(EntityInventoryComponent.class);
		if (inventory != null) {
			Set<ItemStack> toDrop = new HashSet<ItemStack>();
			for (Inventory inv : inventory.getDroppable()) {
				toDrop.addAll(inv);
			}
			Point position = owner.getScene().getPosition();
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
	 * Drops all items/xp in the EntityDropComponent of the given entity
	 * @param owner the entity
	 */
	private void dropDropInventory(Entity owner) {
		EntityDrops dropComponent = owner.get(EntityDrops.class);
		if (dropComponent != null) {
			List<ItemStack> drops = dropComponent.getDrops();
			Point entityPosition = owner.getScene().getPosition();
			for (ItemStack stack : drops) {
				if (stack != null) {
					Item.drop(entityPosition, stack, Vector3.ZERO);
				}
			}
			if (dropComponent.getXpDrop() > 0) {
				Point pos = getOwner().getScene().getPosition();

				XPOrb xporb = pos.getWorld().createEntity(pos, XPOrb.class).add(XPOrb.class);
				xporb.setExperience(dropComponent.getXpDrop());
				pos.getWorld().spawnEntity(xporb.getOwner());
			}
		}
	}

	@Override
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

		// Special cases
		Entity owner = getOwner();
		if (owner instanceof Player) {
			owner.getNetwork().callProtocolEvent(new PlayerHealthEvent(((Player) getOwner())));
		} else if (owner instanceof EnderDragon) {
			List<Parameter<?>> params = new ArrayList<Parameter<?>>(1);
			params.add(new Parameter<Short>(Parameter.TYPE_SHORT, 16, (short) health));
			owner.getNetwork().callProtocolEvent(new EntityMetaChangeEvent(owner, params));
		}
	}

	@Override
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
		HungerComponent hungerComponent = getOwner().get(HungerComponent.class);
		if (hungerComponent != null) {
			hungerComponent.setExhaustion(hungerComponent.getExhaustion() + 0.3f);
		}

		if (event.getSendMessage()) {
			getOwner().getNetwork().callProtocolEvent(new EntityAnimationEvent(getOwner(), Animation.DAMAGE_ANIMATION));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
			//getHurtEffect().playGlobal(getParent().getParent().getPosition());
		}
	}
}
