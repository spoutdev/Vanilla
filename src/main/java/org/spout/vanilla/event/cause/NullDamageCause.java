package org.spout.vanilla.event.cause;

public class NullDamageCause implements DamageCause<Object>{

	private final DamageType type;

	/**
	 * Contains the source and type of damage.
	 * @param type The cause of the damage.
	 */
	public NullDamageCause(DamageType type) {
		this.type = type;
	}

	public DamageType getType() {
		return type;
	}

	@Override
	public Object getSource() {
		return null;
	}
}
