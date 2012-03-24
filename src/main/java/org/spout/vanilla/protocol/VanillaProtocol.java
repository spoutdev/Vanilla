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
package org.spout.vanilla.protocol;

import org.spout.api.protocol.Protocol;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.living.creature.neutral.Enderman;
import org.spout.vanilla.controller.living.creature.passive.Chicken;
import org.spout.vanilla.controller.living.creature.passive.Sheep;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.object.Item;
import org.spout.vanilla.protocol.entity.BasicMobEntityProtocol;
import org.spout.vanilla.protocol.entity.living.EndermanEntityProtocol;
import org.spout.vanilla.protocol.entity.living.SheepEntityProtocol;
import org.spout.vanilla.protocol.entity.living.VanillaPlayerEntityProtocol;
import org.spout.vanilla.protocol.entity.object.PickupEntityProtocol;

public class VanillaProtocol extends Protocol {
	public VanillaProtocol() {
		super("Vanilla", new VanillaCodecLookupService(), new VanillaHandlerLookupService(), new VanillaPlayerProtocol());

		VanillaPlayer.setEntityProtocol(VanillaPlugin.vanillaProtocolId, new VanillaPlayerEntityProtocol());
		Sheep.setEntityProtocol(VanillaPlugin.vanillaProtocolId, new SheepEntityProtocol());
		Enderman.setEntityProtocol(VanillaPlugin.vanillaProtocolId, new EndermanEntityProtocol());
		Item.setEntityProtocol(VanillaPlugin.vanillaProtocolId, new PickupEntityProtocol());
		Chicken.setEntityProtocol(VanillaPlugin.vanillaProtocolId, new BasicMobEntityProtocol());
	}
}
