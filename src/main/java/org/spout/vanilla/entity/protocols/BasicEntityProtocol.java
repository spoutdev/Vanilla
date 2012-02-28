package org.spout.vanilla.entity.protocols;

import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;

public abstract class BasicEntityProtocol implements EntityProtocol {

	@Override
	public Message getDestroyMessage(Entity entity) {
		return new DestroyEntityMessage(entity.getId());
	}

	@Override
	public Message getUpdateMessage(Entity entity) {
		int id = entity.getId();
		int x = (int) (entity.getX() * 32);
		int y = (int) (entity.getY() * 32);
		int z = (int) (entity.getZ() * 32);
		int r = (int) (entity.getYaw() * 32);
		int p = (int) (entity.getPitch() * 32);
		// TODO - improve efficiency
		return new EntityTeleportMessage(id, x, y, z, r, p);
	}
}
