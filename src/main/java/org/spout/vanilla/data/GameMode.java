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
package org.spout.vanilla.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A GameMode is a game mode (talk about redundant). Basically, an instance of this class is
 * used to determine how different parts of the game behave such as damage and mining. Each GameMode
 * has a backing {@link VanillaGameMode} that is used to determine what 
 */
public abstract class GameMode implements Serializable {
	private static final long serialVersionUID = -140339872394438597L;
	
	private static final Map<Byte, GameMode> idMap = new HashMap<Byte, GameMode>();
	public static final SurvivalGameMode SURVIVAL = new SurvivalGameMode();
	public static final CreativeGameMode CREATIVE = new CreativeGameMode();
	static {
		idMap.put(SURVIVAL.getId(), SURVIVAL);
		idMap.put(CREATIVE.getId(), CREATIVE);
	}
	
	private static final Map<String, GameMode> nameMap = new HashMap<String, GameMode>();
	
	private final VanillaGameMode vgm;
	
	/**
	 * Creates a new GameMode.
	 * 
	 * @param vanilla The VanillaGameMode that will be sent to clients in this GameMode.
	 */
	public GameMode(VanillaGameMode vanilla) {
		vgm = vanilla;
		if(nameMap.containsKey(getName().toUpperCase())) throw new RuntimeException("Only one GameMode with a name of " + getName() + " is allowed.");
		nameMap.put(getName().toUpperCase(), this);
	}
	
	/**
	 * Gets the name of this GameMode.
	 */
	public abstract String getName();
	
	/**
	 * Whether or not the players in this GameMode have health.
	 */
	public boolean hasHealth() {
		return true;
	}

	/**
	 * Does a player in the gamemode have hunger?
	 */
	public boolean hasHunger() {
		return true;
	}

	/**
	 * When a player in this GameMode mines a block, does the block's
	 * itemdrop appear?
	 */
	public boolean hasItemDrops() {
		return true;
	}
	
	/**
	 * When a player in this GameMode places a block, does the block get removed
	 * from their inventory>
	 */
	public boolean hasInfiniteItems() {
		return false;
	}
	
	/**
	 * Does a player in this GameMode mine blocks instantly?
	 */
	public boolean hasInstantBreak() {
		return false;
	}
	
	/**
	 * Do tools get damaged on use?
	 */
	public boolean hasToolDurability() {
		return true;
	}
	
	/**
	 * Gets the backing VanillaGameMode.
	 */
	public VanillaGameMode getVanillaGameMode() {
		return vgm;
	}

	/**
	 * Gets the Vanilla ID.
	 */
	public byte getId() {
		return getVanillaGameMode().getId();
	}

	/**
	 * Gets the GameMode object associated with this VanillaGameMode
	 * ID. For instance, an ID of 0 will return GameMode.SURIVIVAL
	 */
	public static GameMode getById(byte id) {
		return idMap.get(id);
	}
	
	/**
	 * Gets the GameMode with the given name.
	 */
	public static GameMode getByName(String name) {
		return nameMap.get(name.toUpperCase());
	}
	
	public static enum VanillaGameMode {
		SURVIVAL((byte) 0),
		CREATIVE((byte) 1),
		ADVENTURE((byte) 2); //1.3 only
		private final byte id;
		private static final Map<Byte, VanillaGameMode> idMap = new HashMap<Byte, VanillaGameMode>();

		static {
			for (VanillaGameMode mode : VanillaGameMode.values()) {
				idMap.put(mode.getId(), mode);
			}
		}

		private VanillaGameMode(byte id) {
			this.id = id;
		}

		public byte getId() {
			return id;
		}

		public static VanillaGameMode getById(byte id) {
			return idMap.get(id);
		}
	}
}
