package org.spout.vanilla.protocol.entity;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

public class BasicObjectEntityProtocol extends BasicEntityProtocol {

	public BasicObjectEntityProtocol(int spawnID) {
		super(spawnID);
	}
	
	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c != null) {
			int id = entity.getId();
			int x = (int) (entity.getPosition().getX() * 32);
			int y = (int) (entity.getPosition().getY() * 32);
			int z = (int) (entity.getPosition().getZ() * 32);

			return new Message[]{new SpawnVehicleMessage(id, this.getSpawnID(), x, y, z)};
		} else {
			return null;
		}
	}
}
