package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.CodecLookupService;
import org.getspout.vanilla.protocol.codec.HandshakeCodec;
import org.getspout.vanilla.protocol.codec.IdentificationCodec;
import org.getspout.vanilla.protocol.codec.PingCodec;

public class VanillaCodecLookupService extends CodecLookupService {

	public VanillaCodecLookupService() {
	try {
		/* 0x01 */bind(IdentificationCodec.class);
		/* 0x02 */bind(HandshakeCodec.class);
		/* 0xFE */bind(PingCodec.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
}
