package org.spout.vanilla.event.cause;


public enum HealCause {
	/**
	 * Health gained due to regeneration on peaceful mode.
	 */
 	REGENERATION,
 	/**
	 * Health gained due to regeneration from being satiated.
	 */
 	SATIATED,
 	/**
	 * Health gained from consumables.
	 */
 	CONSUMABLE,
 	/**
	 * Health gained by an Ender Dragon from an Ender Crystal.
	 */
 	ENDER_CRYSTAL,
 	/**
	 * Health gained from a potion.
	 */
 	MAGIC,
 	/**
	 * Health gained from the HoT effect of a potion.
	 */
 	MAGIC_REGEN,
 	/**
	 * Health gained by the Wither when it is spawning.
	 */
 	WITHER_SPAWN,
 	/**
	 * Health gained due to an unknown source.
	 */
 	UNKNOWN;
 	
 	public boolean equals(HealCause... causes) {
		for (HealCause cause : causes) {
			if (equals(cause)) {
				return true;
			}
		}
		return false;
	}
}
