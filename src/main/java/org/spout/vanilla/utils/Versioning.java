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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */

package org.spout.vanilla.utils;

import java.util.HashSet;
import java.util.Set;

public class Versioning {

	private static int latestVersion = 23;
	private static Set<Integer> compatible = new HashSet<Integer>();
	private static boolean acceptNewer, overrideCompatibility;

	public static boolean isCompatible(int protocolId) {
		if (overrideCompatibility) {
			return true;
		}
		if (compatible.contains(protocolId)) {
			return true;
		}
		if (latestVersion == protocolId) {
			return true;
		}
		if (acceptNewer && latestVersion < protocolId) {
			return true;
		}
		return false;
	}

	static {
		//addCompatibility(23);
	}

	public static void addCompatibility(int protocol) {
		compatible.add(protocol);
	}

	public static void setAllowNewerClients(boolean value) {
		acceptNewer = value;
	}

	public static void setOverrideCompatibility(boolean value) {
		overrideCompatibility = value;
	}

	public boolean allowsNewerClients() {
		return acceptNewer;
	}

	public boolean overridesCompatibility() {
		return overrideCompatibility;
	}

	public static int getLatestVersion() {
		return latestVersion;
	}
}
