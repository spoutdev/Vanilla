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
package org.spout.vanilla.component.block.material;

import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.Material;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.component.block.VanillaBlockComponent;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.Music;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.item.misc.MusicDisc;
import org.spout.vanilla.util.PlayerUtil;

/**
 * Component that represents a Jukebox in the world.
 */
public class Jukebox extends VanillaBlockComponent {
	@Override
	public void onInteract(final EntityInteractEvent event) {
		super.onInteract(event);
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					Slot inv = PlayerUtil.getHeldSlot(pie.getEntity());
					if (inv != null && this.canPlay(inv.get())) {
						this.setPlayedItem(inv.get().clone().setAmount(1));
						if (!PlayerUtil.isCostSuppressed(pie.getEntity())) {
							inv.addAmount(-1);
						}
					} else {
						this.eject();
					}
			}
		}
	}

	/**
	 * Gets the item that is currently being played by this Jukebox
	 *
	 * @return Played item
	 */
	public ItemStack getPlayedItem() {
		return getData().get(VanillaData.JUKEBOX_ITEM);
	}

	/**
	 * Sets the item that is currently being played by this Jukebox
	 *
	 * @param item to set to
	 */
	public void setPlayedItem(ItemStack item) {
		ItemStack old = getData().put(VanillaData.JUKEBOX_ITEM, item);
		if (old != null) {
			// Drop the old item
			Item.drop(getPoint(), old, Vector3.UP.mul(0.5));
		}
		setPlaying(item != null);
	}

	/**
	 * Tests whether this Jukebox can play the material specified
	 *
	 * @param material to play
	 * @return True if it can play it, False if not
	 */
	public boolean canPlay(Material material) {
		return material != null && material instanceof MusicDisc;
	}

	/**
	 * Tests whether this Jukebox can play the itemstack specified
	 *
	 * @param item to play
	 * @return True if it can play it, False if not
	 */
	public boolean canPlay(ItemStack item) {
		return item != null && canPlay(item.getMaterial());
	}

	/**
	 * Gets the current music this Jukebox plays
	 *
	 * @return Music
	 */
	public Music getMusic() {
		ItemStack current = this.getPlayedItem();
		if (canPlay(current.getMaterial())) {
			return ((MusicDisc) current.getMaterial()).getMusic();
		} else {
			return Music.NONE;
		}
	}

	/**
	 * Ejects the currently playing music disc, if available
	 */
	public void eject() {
		this.setPlayedItem(null);
	}

	/**
	 * Sets whether this Jukebox is playing or not
	 */
	public void setPlaying(boolean playing) {
		// This call is most likely useless, but might be useful once the client
		// changes the displayed model between playing and not-playing
		getBlock().setData(playing ? 1 : 0);
		// Play the effect
		Music music = playing ? this.getMusic() : Music.NONE;
		GeneralEffects.MUSIC_DISC.playGlobal(getPoint(), music);
	}
}
