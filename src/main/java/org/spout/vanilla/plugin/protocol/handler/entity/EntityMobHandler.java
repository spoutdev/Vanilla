/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.protocol.handler.entity;

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityPrefab;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.QuaternionMath;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.Platform;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.plugin.component.test.TransformDebugComponent;
import org.spout.vanilla.plugin.protocol.msg.entity.spawn.EntityMobMessage;

public class EntityMobHandler extends MessageHandler<EntityMobMessage> {

	@Override
	public void handleClient(Session session, EntityMobMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		
		Player player = session.getPlayer();
		World world = player.getWorld();
		
		int entityID = message.getEntityId();
		EntityPrefab prefab;
		
		Point point = new Point(world, message.getX(), message.getY(), message.getZ());
		Quaternion quaternion = QuaternionMath.rotation(message.getPitch(), message.getYaw(), 0);
		Transform ts = new Transform(point, quaternion, Vector3.ONE);
		
		//TODO : Replace by a cool enum or a util method ?
		switch (message.getEntityId()) {
		case 50: 	//Creeper 	0.6 	1.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/creeper/creeper.sep");
			break;
		case 51: 	//Skeleton 	0.6 	1.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/skeleton/skeleton.sep");
			break;
		case 52: 	//Spider 	1.4 	0.9
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 53: 	//Giant Zombie 	3.6 	10.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 54: 	//Zombie 	0.6 	1.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/zombie/zombie.sep");
			break;
		case 55: 	//Slime 	0.6 * size 	0.6 * size
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 56: 	//Ghast 	4 	4
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 57: 	//Zombie Pigman 	0.6 	1.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 58:	//Enderman
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 59:	//Cave Spider 		
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 60: 	//Silverfish 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 61: 	//Blaze 		
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 62: 	//Magma Cube 	0.6 * size 	0.6 * size
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 63: 	//Ender Dragon 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 64: 	//Wither 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 65: 	//Bat 		
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 66: 	//Witch 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 90: 	//Pig 	0.9 	0.9
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 91: 	//Sheep 	0.6 	1.3
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 92: 	//Cow 	0.9 	1.3
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 93: 	//Chicken 	0.3 	0.4
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 94: 	//Squid 	0.95 	0.95
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 95: 	//Wolf 	0.6 	1.8
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		case 96: 	//Mooshroom 
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;		
		case 97: 	//Snowman 
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;		
		case 98: 	//Ocelot 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 99: 	//Iron Golem 	
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;	
		case 120: 	//Villager 
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		default:
			prefab = (EntityPrefab) Spout.getFilesystem().getResource("entity://Vanilla/entities/enderman/enderman.sep");
			break;
		}
		
		Entity entity = prefab.createEntity(ts);

		if (Spout.getPlatform() == Platform.SERVER && Spout.debugMode()) {
			entity.add(TransformDebugComponent.class);
		}

		world.spawnEntity(entity,entityID);
	}

}
