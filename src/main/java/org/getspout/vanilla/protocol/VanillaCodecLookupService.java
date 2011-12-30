/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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