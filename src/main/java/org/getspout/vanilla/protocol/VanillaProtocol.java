package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.Protocol;

public class VanillaProtocol extends Protocol {

	public VanillaProtocol() {
		super("Vanilla", new VanillaCodecLookupService(), new VanillaHandlerLookupService());
	}
	
}
