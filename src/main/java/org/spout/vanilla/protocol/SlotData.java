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
package org.spout.vanilla.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.spout.nbt.CompoundMap;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.ListTag;
import org.spout.nbt.ShortTag;
import org.spout.nbt.Tag;

/**
 * Contains the information of a Slot protocol entry
 * if the id = -1, the rest of the variables doesn't exist
 * if the item can be enchanted, further info is given
 * @author greatman
 * 
 */
public class SlotData {

	private final short id, damage;
	private final byte count;
	private CompoundMap enchantInfo;
	// TODO: Silly implementation. Need to check if a enchanting thing exist yet.
	private HashMap<Short, Short> enchantList = new HashMap<Short, Short>();

	public SlotData(short id) {
		this(id, (byte) -1, (short) -1);
	}

	public SlotData(short id, byte count, short damage) {
		this(id, count, damage, new CompoundMap());
	}

	public SlotData(short id, byte count, short damage, HashMap<Short, Short> enchantList) {
		this.id = id;
		this.count = count;
		this.damage = damage;
		this.enchantList = enchantList;
	}

	@SuppressWarnings("unchecked")
	public SlotData(short id, byte count, short damage, CompoundMap slotData) {
		this.id = id;
		this.count = count;
		this.damage = damage;
		this.enchantInfo = slotData;
		if (slotData != null) {
			// This will put the enchants in a easy readable format
			Tag tag = this.enchantInfo.get("ench");
			if (tag != null && tag instanceof ListTag) {
				Iterator<CompoundTag> enchantsIterator = ((ListTag<CompoundTag>) tag).getValue().iterator();
				while (enchantsIterator.hasNext()) {
					Tag compoundTag = enchantsIterator.next();

					if (compoundTag instanceof ListTag) {
						ListTag<ShortTag> compoundTagList = (ListTag<ShortTag>) compoundTag;
						// Is it a valid enchant entry?
						if (compoundTagList.getValue().size() == 2) {
							enchantList.put(compoundTagList.getValue().get(0).getValue(), compoundTagList.getValue().get(1).getValue());
						}
					}
				}
			}
		}

	}

	public CompoundMap createNBT() {

		List<Tag> compoundTagList = new ArrayList<Tag>();
		Iterator<Entry<Short, Short>> enchantListIterator = enchantList.entrySet().iterator();
		while (enchantListIterator.hasNext()) {

			CompoundMap compoundMap = new CompoundMap();
			Entry<Short, Short> enchant = enchantListIterator.next();
			compoundMap.put(new ShortTag("id", enchant.getKey()));
			compoundMap.put(new ShortTag("lvl", enchant.getValue()));
			compoundTagList.add(new CompoundTag(null, compoundMap));

		}
		CompoundMap enchantList = new CompoundMap(compoundTagList);
		enchantInfo = enchantList;
		return enchantList;
	}

	public short getId() {
		return id;
	}

	public short getDamage() {
		return damage;
	}

	public byte getCount() {
		return count;
	}

	public CompoundMap getEnchantInfo() {
		return enchantInfo;
	}

	/**
	 * Checks if the ID is a enchantable item.
	 * @param id the item ID to check
	 * @return True if the item is enchantable else false.
	 */
	// TODO: There's something better im sure.
	public static boolean CanEnchant(short id) {
		return (256 <= id && id <= 259) || (267 <= id && id <= 279) || (283 <= id && id <= 286) || (290 <= id && id <= 294) || (298 <= id && id <= 317) || id == 261 || id == 359 || id == 346;
	}
}
