package org.spout.vanilla.protocol.controller;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.math.Vector3;
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
			Vector3 pos = entity.getPosition().multiply(32).floor();
			return new Message[]{new SpawnVehicleMessage(id, this.getSpawnID(), pos)};
		} else {
			return null;
		}
	}
}
