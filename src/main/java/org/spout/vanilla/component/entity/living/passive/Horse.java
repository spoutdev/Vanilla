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
package org.spout.vanilla.component.entity.living.passive;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.api.inventory.Container;

import org.spout.vanilla.component.entity.living.Animal;
import org.spout.vanilla.component.entity.living.Passive;
import org.spout.vanilla.component.entity.living.Tameable;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.entity.HorseInventory;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

public class Horse extends Animal implements Container, Passive, Tameable {
	public static final int DEFAULT_HEALTH = 22;
	public static final int TYPE_INDEX = 19; // MC Data index for horse type
	public static final int VARIANT_INDEX = 20; // MC Data index for horse variant & marking
	public static final int OWNER_INDEX = 21; // MC Data index for owner's name
	public static final int ARMOR_TYPE_INDEX = 22; // MC Data index for amount of armor type the horse currently wears, None 0, Iron 1, Gold 2, Diamond 3

	@Override
	public void onAttached() {
		setEntityProtocol(new CreatureProtocol(CreatureType.HORSE));
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(DEFAULT_HEALTH);
		}
		super.onAttached();

		// Add metadata for the Horse
		MetadataComponent metadata = getOwner().add(MetadataComponent.class);

		//TODO: Implement tame/saddle/bred/chest/etc. for index 16 (see protocol page)
		metadata.addMeta(new Metadata<Integer>(Metadata.TYPE_INT, 16) {
			@Override
			public Integer getValue() {
				int value = 0;
				if (isTamed()) {
					value |= 2;
				}
				if (getInventory().hasSaddle()) {
					value |= 4;
				}
				/*
				TODO: Check if inventory is setup to have a chest or not.
				if (hasChest()) {
					value |= 8;
				}
				TODO: Check if horse has already bred.
				if (hasBred()) {
					value |= 16;
				}
			    */

				if (isEatingBlocking()) {
					value |= 32;
				}
				/*
				TODO: rearing
				if (isRearing() {
					value |= 64;
				}

				 */
				return value;
			}

			@Override
			public void setValue(Integer value) {
				// TODO Auto-generated method stub
			}
		});

		// Metadata for the type ID of the Horse
		metadata.addMeta(Metadata.TYPE_BYTE, TYPE_INDEX, VanillaData.ENTITY_CATEGORY);

		// Metadata for variant and marking
		metadata.addMeta(new Metadata<Integer>(Metadata.TYPE_INT, VARIANT_INDEX) {
			@Override
			public Integer getValue() {
				return (getVariant().getVariantId() & 0x00ff) | (getMarking().getMarkingId() & 0xff00);
			}

			@Override
			public void setValue(Integer value) {
				int intValue = value.intValue();
				setVariantAndMarking(Variant.fromId(intValue & 0x00ff), Marking.fromId(intValue & 0xff00));
			}
		});

		// Metadata for the owner name of the Horse
		metadata.addMeta(OWNER_INDEX, Metadata.TYPE_STRING, VanillaData.OWNER);

		// Metadata for the currently equipped Armor
		metadata.addMeta(new Metadata<Integer>(Metadata.TYPE_INT, ARMOR_TYPE_INDEX) {
			@Override
			public Integer getValue() {
				return getHorseTypeId() > 0 ? 0 : getInventory().getArmorTypeId();
			}

			@Override
			public void setValue(Integer value) {
				// Intentionally left blank, armor types can not be set via metadata
			}
		});
	}

	@Override
	public boolean isTamed() {
		return getOwner().getData().get(VanillaData.TAMED);
	}

	@Override
	public void setTamed(boolean tamed) {
		getOwner().getData().put(VanillaData.TAMED, tamed);
	}

	@Override
	public boolean canBeTamed() {
		return getHorseTypeId() <= 2;
	}

	@Override
	public String getOwnerName() {
		return getOwner().getData().get(VanillaData.OWNER);
	}

	@Override
	public void setOwnerName(String owner) {
		if (isTamed()) {
			getOwner().getData().put(VanillaData.OWNER, owner);
		}
	}

	/**
	 * Affects how close to being tamed the horse is
	 *
	 * @return temper
	 */
	public int getTemper() {
		return getOwner().getData().get(VanillaData.TEMPER);
	}

	public void setVariantAndMarking(Variant variant, Marking marking) {
		getOwner().getData().put(VanillaData.VARIANT, variant.getVariantId() | marking.getMarkingId());
	}

	public void setVariant(Variant variant) {
		Marking marking = getMarking();
		getOwner().getData().put(VanillaData.VARIANT, variant.getVariantId() | marking.getMarkingId());
	}

	public Variant getVariant() {
		return Variant.fromId(getOwner().getData().get(VanillaData.VARIANT));
	}

	public void setMarking(Marking marking) {
		Variant variant = getVariant();
		getOwner().getData().put(VanillaData.VARIANT, variant.getVariantId() | marking.getMarkingId());
	}

	public Marking getMarking() {
		return Marking.fromId(getOwner().getData().get(VanillaData.VARIANT));
	}

	/**
	 * The value associated with this horse type.
	 *
	 * @return byte
	 */
	public byte getHorseTypeId() {
		return getOwner().getData().get(VanillaData.ENTITY_CATEGORY);
	}

	public void setHorseTypeId(byte id) {
		getOwner().getData().put(VanillaData.ENTITY_CATEGORY, id);
	}

	@Override
	public HorseInventory getInventory() {
		return getOwner().getData().get(VanillaData.HORSE_INVENTORY);
	}

	public enum Variant {
		NONE(0x0),
		WHITE(0x0),
		CREAMY(0x1),
		CHESTNUT(0x2),
		BROWN(0x3),
		BLACK(0x4),
		GRAY(0x5),
		DARK_BROWN(0x6);
		public static final byte VARIANT_MASK = 0xF;
		private static final TIntObjectMap<Variant> ids = new TIntObjectHashMap<Variant>();
		private final int id;

		Variant(int id) {
			this.id = id;
		}

		public int getVariantId() {
			return this.id;
		}

		public static Variant fromId(int id) {
			return ids.get(id & VARIANT_MASK);
		}

		static {
			for (Variant variant : Variant.values()) {
				ids.put(variant.getVariantId(), variant);
			}
		}
	}

	public enum Marking {
		NONE(0x0),
		WHITE(256),
		WHITEFIELD(512),
		WHITEDOTS(768),
		BLACKDOTS(1024);
		private final int markingId;
		private static final TIntObjectMap<Marking> ids = new TIntObjectHashMap<Marking>();
		public static final int MARKING_MASK = 0xF00;

		Marking(int markingId) {
			this.markingId = markingId;
		}

		public int getMarkingId() {
			return this.markingId;
		}

		public static Marking fromId(int id) {
			return ids.get(id & MARKING_MASK);
		}

		static {
			for (Marking marking : Marking.values()) {
				ids.put(marking.getMarkingId(), marking);
			}
		}
	}
}
