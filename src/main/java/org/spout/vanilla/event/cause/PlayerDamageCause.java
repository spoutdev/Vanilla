package org.spout.vanilla.event.cause;

import org.spout.api.entity.Player;

public class PlayerDamageCause implements DamageCause<Player>{

	private final DamageType type;
	private final Player player;

	/**
	 * Contains the source and type of damage.
	 * @param player The player causing the damage
	 * @param type The cause of the damage.
	 */
	public PlayerDamageCause(Player player, DamageType type) {
		this.player = player;
		this.type = type;
	}

	public DamageType getType() {
		return type;
	}

	@Override
	public Player getSource() {
		return player;
	}
}
