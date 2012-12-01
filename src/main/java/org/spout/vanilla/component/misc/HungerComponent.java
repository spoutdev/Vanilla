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
package org.spout.vanilla.component.misc;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.spout.api.Client;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.gui.Widget;
import org.spout.api.gui.component.RenderPartsHolderComponent;
import org.spout.api.gui.render.RenderPart;
import org.spout.api.math.Rectangle;

import org.spout.vanilla.component.living.neutral.Human;
import org.spout.vanilla.component.player.HUDComponent;
import org.spout.vanilla.data.RenderMaterials;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.source.HealthChangeCause;

public class HungerComponent extends EntityComponent {
	private Human human;
	private float timer = 4;
	private Point lastPos;
	// Client
	private final Widget widget = new Widget();
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private final Random random = new Random();
	private int hungerTicks;

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("HungerComponent may only be attached to players.");
		}
		human = getOwner().add(Human.class);
		if (Spout.getEngine() instanceof Client) {
			// Hunger bar
			final RenderPartsHolderComponent hungerRect = widget.add(RenderPartsHolderComponent.class);
			float x = 0.09f * SCALE;
			float dx = 0.06f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart hunger = new RenderPart();
				hunger.setRenderMaterial(RenderMaterials.ICONS_MATERIAL);
				hunger.setColor(Color.WHITE);
				hunger.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
				hunger.setSource(new Rectangle(52f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
				hungerRect.add(hunger);
				x += dx;
			}

			x = 0.09f * SCALE;
			for (int i = 0; i < 10; i++) {
				final RenderPart hungerBg = new RenderPart();
				hungerBg.setRenderMaterial(RenderMaterials.ICONS_MATERIAL);
				hungerBg.setColor(Color.WHITE);
				hungerBg.setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
				hungerBg.setSource(new Rectangle(16f / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
				hungerRect.add(hungerBg);
				x += dx;
			}

			getOwner().add(HUDComponent.class).attachWidget(widget);
		}
	}

	@Override
	public void onTick(float dt) {

		/*
		 * The Minecraft hunger system has a few different dynamics:
		 *
		 *      1) hunger - the amount of 'shanks' shown on the client.
		 *         1 points = 1/2 shank
		 *
		 *      2) food saturation - an invisible 'safety net' that is a default
		 *         5 points.
		 *
		 *      3) the timer - decreases by the delta of a tick every tick if
		 *         'hunger' > 17 or if 'food level' <= 0 and heals or deals
		 *         one point of damage respectively
		 *
		 *      4) exhaustion - anywhere in between 0 and 4 and increases with
		 *         certain actions. When the exhaustion reaches 4, it is reset
		 *         and subtracts one point from 'food saturation' if
		 *         'food saturation' > 0 or one point from 'hunger' if
		 *         'food saturation' <= 0 and 'hunger' > 0.
		 *
		 *     Exhaustion actions:
		 *         Walking and sneaking (per block) - 0.01
		 *         Sprinting (per block)            - 0.1
		 *         Swimming (per block)             - 0.015
		 *         Jumping (per block)              - 0.2
		 *         Sprint jump (per block)          - 0.8
		 *         Digging                          - 0.025
		 *         Attacking or being attacked      - 0.3
		 *         Food poisoning                   - 15 total over entire
		 *                                            duration
		 */

		switch (Spout.getPlatform()) {
			case PROXY:
			case SERVER:
				final HealthComponent healthComponent = human.getHealth();
				final int health = healthComponent.getHealth();
				final int hunger = getHunger();

				// Regenerate health

				if (health < 20 && hunger > 17) {
					timer -= dt;
					if (timer <= 0) {
						healthComponent.setHealth(health + 1, HealthChangeCause.REGENERATION);
						timer = 4;
					}
				}

				// Damage health

				if (hunger <= 0) {
					timer -= dt;
					if (timer <= 0) {
						healthComponent.setHealth(health - 1, HealthChangeCause.DAMAGE);
						timer = 4;
					}
				}

				// Exhaustion

				final Point pos = getOwner().getTransform().getPosition();
				if (lastPos == null) {
					lastPos = pos;
					return;
				}
				final float distance = (float) pos.distance(lastPos);
				lastPos = pos; // Set the last position for next run

				float exhaustion = getExhaustion();
				final World world = pos.getWorld();
				final boolean sprinting = human.isSprinting();
				final boolean jumping = human.isJumping();
				final boolean digging = getOwner().add(DiggingComponent.class).isDigging();

				if (digging) {
					// digging
					exhaustion += 0.025f;
				}

				if (distance > 0) {
					if (world.getBlock(pos) == VanillaMaterials.WATER && world.getBlock(lastPos) == VanillaMaterials.WATER) {
						// swimming
						exhaustion += 0.015f * distance;
					} else if (sprinting && jumping) {
						// sprint jumping
						exhaustion += 0.8f * distance;
					} else if (jumping) {
						// jumping
						exhaustion += 0.2f * distance;
					} else if (sprinting) {
						// sprinting
						exhaustion += 0.1f * distance;
					} else {
						// walking
						exhaustion += 0.01f * distance;
					}
				}

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
				float x;
				float y;
				float dx = 0.06f * SCALE;

				// Animate hunger bar
				float saturation = getFoodSaturation();
				if (saturation <= 0) {
					List<RenderPart> parts = widget.get(RenderPartsHolderComponent.class).getRenderParts();
					if (hungerTicks == 98) {
						x = 0.09f * SCALE;
						y = -0.77f;
						for (int i = 0; i < 10; i++) {

							RenderPart part = parts.get(i);
							RenderPart partBg = parts.get(i + 10);

							int rand = random.nextInt(3);
							if (rand == 0) {
								y = -0.765f; // Twitch up
							} else if (rand == 1) {
								y = -0.775f; // Twitch down
							}

							part.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));
							partBg.setSprite(new Rectangle(x, y, 0.075f * SCALE, 0.07f));
							x += dx;
						}
						hungerTicks++;
						widget.update();
					} else if (hungerTicks == 100) {
						// Reset hunger bar to normal
						x = 0.09f * SCALE;
						for (int i = 0; i < 10; i++) {
							parts.get(i).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
							parts.get(i + 10).setSprite(new Rectangle(x, -0.77f, 0.075f * SCALE, 0.07f));
							x += dx;
						}
						hungerTicks = 0;
					} else {
						hungerTicks++;
					}
				}
				break;
		}
	}

	public int getHunger() {
		return getData().get(VanillaData.HUNGER);
	}

	public void setHunger(int hunger) {
		getData().put(VanillaData.HUNGER, hunger);
		reload();
		if (Spout.getEngine() instanceof Client) {
			render(52, 16);
		}
	}

	private void render(float fx, float bx) {
		int hunger = getHunger();
		RenderPartsHolderComponent hungerRect = widget.get(RenderPartsHolderComponent.class);
		if (hunger == 0) {

			for (int i = 0; i < 10; i++) {
				hungerRect.get(i).setSource(new Rectangle(142f / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Foreground
			}

			for (int i = 10; i < 20; i++) {
				hungerRect.get(i).setSource(new Rectangle(bx / 256f, 27f / 256f, 9f / 256f, 9f / 256f)); // Background
			}
		} else {

			for (int i = 9; i >= 0; i--) {
				if (hunger == 0) {
					fx = 142f; // Empty
				} else if (hunger == 1) {
					fx += 9f; // Half
					hunger = 0;
				} else {
					hunger -= 2; // Full
				}
				hungerRect.get(i).setSource(new Rectangle(fx / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}

			for (int i = 19; i >= 10; i--) {
				hungerRect.get(i).setSource(new Rectangle(bx / 256f, 27f / 256f, 9f / 256f, 9f / 256f));
			}
		}

		widget.update();
	}

	public float getFoodSaturation() {
		return getData().get(VanillaData.FOOD_SATURATION);
	}

	public void setFoodSaturation(float foodSaturation) {
		getData().put(VanillaData.FOOD_SATURATION, foodSaturation);
		reload();
	}

	public float getExhaustion() {
		return getData().get(VanillaData.EXHAUSTION);
	}

	public void setExhaustion(float exhaustion) {
		getData().put(VanillaData.EXHAUSTION, exhaustion);
	}

	public boolean isPoisoned() {
		return getData().get(VanillaData.POISONED);
	}

	public void setPoisoned(boolean poisoned) {
		getData().put(VanillaData.POISONED, poisoned);
		if (Spout.getEngine() instanceof Client) {
			if (poisoned) {
				render(88, 133);
			} else {
				render(52, 16);
			}
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
}
