package org.spout.vanilla.source;

import org.spout.api.Source;

/**
 * Represents the source of a health change.
 */
public enum HealthChangeCause implements Source {
	/**
	 * Health changed due to the execution of a command.
	 */
	COMMAND,
	/**
	 * Health changed due to being damaged.
	 * @see {@link DamageCause}
	 */
	DAMAGE,
	/**
	 * Health changed due to regeneration cycle.
	 */
	REGENERATION,
	/**
	 * Health changed due to eating.
	 */
	EATING,
	/**
	 * Health changed due to the entity spawning.
	 */
	SPAWN,
	/**
	 * Health changed due to some unknown reason.
	 */
	UNKNOWN;
}
