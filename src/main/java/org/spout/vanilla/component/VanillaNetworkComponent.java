package org.spout.vanilla.component;

import org.spout.vanilla.protocol.EntityProtocol;

public interface VanillaNetworkComponent {

	/**
	 * Returns the {@link EntityProtocol} for this type of entity
	 *
	 * @return The entity protocol for the specified id.
	 */
	EntityProtocol getEntityProtocol();

	/**
	 * Registers the {@code protocol} as the Entity's protocol
	 *
	 * @param protocol The protocol to set
	 */
	void setEntityProtocol(EntityProtocol protocol);
    
}
