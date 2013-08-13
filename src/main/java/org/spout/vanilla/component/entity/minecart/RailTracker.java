package org.spout.vanilla.component.entity.minecart;

import org.spout.api.geo.cuboid.Block;
import org.spout.vanilla.component.entity.VanillaEntityComponent;

public class RailTracker extends VanillaEntityComponent {
	private Block lastRail, currentRail;

	public Block getLastRail() {
		return lastRail;
	}

	public Block getCurrentRail() {
		return currentRail;
	}
}
