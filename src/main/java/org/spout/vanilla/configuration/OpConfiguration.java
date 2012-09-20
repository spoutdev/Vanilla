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
package org.spout.vanilla.configuration;

import java.io.File;
import java.util.List;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class OpConfiguration {
	private final YamlConfiguration config;

	/**
	 * Creates a new OpConfiguration and instantiates a YamlConfiguration<br>
	 * containing the operators.
	 */
	public OpConfiguration(File dataFolder) {
		config = new YamlConfiguration(new File(dataFolder, "ops.yml"));
	}

	/**
	 * Gets the operators.
	 * @return The operator-names as a List.
	 */
	public List<String> getOps() {
		return config.getNode("ops").getStringList();
	}

	/**
	 * Sets a player as an operator.
	 * @param playerName Player to op/deop.
	 * @param op If true, the player gets opped, if not, then deopped.
	 * @return true if no exception occured during saving the config, false if
	 *         one occured.
	 */
	public boolean setOp(String playerName, boolean op) {
		List<String> list = getOps();
		if (op) {
			list.add(playerName.toLowerCase());
		} else {
			list.remove(playerName.toLowerCase());
		}

		config.getNode("ops").setValue(list);

		try {
			this.save();
			return true;
		} catch (ConfigurationException e) {
			return false;
		}
	}

	/**
	 * Checks wether the passed player is an operator.
	 * @param playerName The name of the player to check.
	 * @return true if player is op, false when not.
	 */
	public boolean isOp(String playerName) {
		return getOps().contains(playerName.toLowerCase());
	}

	/**
	 * Saves the YamlConfiguration containing the operators.
	 * @throws ConfigurationException
	 */
	public void save() throws ConfigurationException {
		config.save();
	}

	/**
	 * Loads the config-values.
	 * @throws ConfigurationException
	 */
	public void load() throws ConfigurationException {
		config.load();
	}
}
