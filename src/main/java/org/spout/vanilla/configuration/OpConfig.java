/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import java.util.Arrays;
import java.util.List;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;

public class OpConfig extends Configuration {
	
	private static final String[] ops = {"Notch", "jeb", "ez"};
	public static final ConfigurationNode OPS = new ConfigurationNode("ops", Arrays.asList(ops));
	
	public OpConfig() {
		super(new File("plugins/Vanilla/ops.yml"));
	}

	@Override
	public void load() {
		super.load();
		this.addNode(OPS);
		this.save();
	}
	
	public List<String> getOps() {
		return OPS.getStringList();
	}

	public void setOp(String playerName, boolean op) {
		List<String> list = OPS.getStringList();
		if (op) {
			list.add(playerName);
		} else {
			list.remove(playerName);
		}

		OPS.setValue(list, true);
	}

	public boolean isOp(String playerName) {
		return OPS.getStringList().contains(playerName);
	}
}
