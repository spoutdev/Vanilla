package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.CodecLookupService;
import org.getspout.vanilla.protocol.codec.VanillaHandshakeCodec;
import org.getspout.vanilla.protocol.codec.VanillaIdentificationCodec;
import org.getspout.vanilla.protocol.codec.VanillaPingCodec;

public class VanillaCodecLookupService extends CodecLookupService {

	public VanillaCodecLookupService() {
	try {
		/* 0x01 */bind(VanillaIdentificationCodec.class);
		/* 0x02 */bind(VanillaHandshakeCodec.class);
		/* 0xFE */bind(VanillaPingCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
}
