/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.handler.player.pos;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TLongList;
import gnu.trove.list.linked.TDoubleLinkedList;
import gnu.trove.list.linked.TLongLinkedList;

import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.ServerSession;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.player.Ping;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.VanillaServerNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionMessage;

public final class PlayerPositionHandler extends MessageHandler<PlayerPositionMessage> {
	long last = System.nanoTime();
	//Player movement is 0.21 apart
	//Player running is 0.27 apart
	//Player swimming is 0.14-0.11 apart
	//Player flying is 0.5+
	private final Cache<Session, PositionTracker> cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.SECONDS).build();
	private final Callable<PositionTracker> loader = new Callable<PositionTracker>() {
		public PositionTracker call() {
			return new PositionTracker();
		}
	};

	@Override
	public void handleClient(ClientSession session, PlayerPositionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player player = session.getPlayer();

		World world = session.getEngine().getDefaultWorld();
		player.getPhysics().setPosition(new Point(world, (float) message.getX(), (float) message.getY(), (float) message.getZ()));
		// TODO: player position isnt updated
		System.out.println(message.toString());
	}

	@Override
	public void handleServer(ServerSession session, PlayerPositionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		final Player holder = session.getPlayer();
		final RepositionManager rmInverse = holder.getNetworkSynchronizer().getRepositionManager().getInverse();

		Ping ping = holder.get(Ping.class);
		if (ping != null) {
			ping.refresh();
		}

		final Point rawPosition = new Point(message.getPosition(), holder.getWorld());
		final Point newPosition = rmInverse.convert(rawPosition);
		final Point position = holder.getPhysics().getPosition();

		if (!(session.getNetworkSynchronizer() instanceof VanillaServerNetworkSynchronizer)) throw new IllegalStateException("Using Vanilla Protocol without using VanillaNetworkSynchronizer");
		VanillaServerNetworkSynchronizer sync = (VanillaServerNetworkSynchronizer) session.getNetworkSynchronizer();
		if (sync.isTeleportPending()) {
			if (position.getX() == newPosition.getX() && position.getZ() == newPosition.getZ() && Math.abs(position.getY() - newPosition.getY()) < 16) {
				sync.clearTeleportPending();
			}
		} else {
			if (!position.equals(newPosition)) {
				final Human human = holder.get(Human.class);
				//Compare against live instead of snapshot value
				Point livePosition = human.getLivePosition();
				if (livePosition == null) {
					livePosition = position;
				}
				human.setLivePosition(newPosition);

				//Don't use client onGround value, calculate ourselves
				//MC Client is on ground if y value is whole number, or half step (e.g 55.0, or 65.5)
				float yDiff = Math.abs(newPosition.getBlockY() - newPosition.getY());
				if (yDiff > 0.4) {
					yDiff -= 0.5F; //half blocks
				}
				final BlockMaterial ground = newPosition.getBlock().translate(BlockFace.BOTTOM).getMaterial();
				final boolean onGround = yDiff < 0.01 && (ground instanceof VanillaBlockMaterial && ground.getShape() != null);
				final boolean wasOnGround = human.isOnGround();
				human.setOnGround(onGround);

				//Update falling state
				final boolean wasFalling = human.isFalling();
				if (!onGround && newPosition.getY() < position.getY()) {
					human.setFalling(true);
				} else {
					human.setFalling(false);
				}

				//Hover tracking
				if (wasOnGround && !onGround) {
					human.getData().put("position_on_ground", livePosition);
					human.getData().put("time_in_air", holder.getWorld().getAge());
				} else if (!wasOnGround && !onGround) {
					//Changed directions
					if (wasFalling && !human.isFalling() || human.isInWater()) {
						human.getData().remove("time_in_air");
					}
					float time = human.getData().get("time_in_air", holder.getWorld().getAge());
					//hovering or still rising
					if (time + 2000 < holder.getWorld().getAge() && newPosition.getY() - livePosition.getY() >= 0) {
						if (!human.canFly()) {
							holder.sendMessage("Hover cheating?");
						}
					}
				} else if (!wasOnGround && onGround) {
					human.getData().remove("position_on_ground");
					human.getData().remove("time_in_air");
				}

				//Movement tracking
				PositionTracker tracker = null;
				try {
					tracker = cache.get(session, loader);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
				tracker.updateTracker(human, livePosition, newPosition, message.getCreationTimestamp());

				//Debug
				/*
				final double dx = livePosition.getX() - newPosition.getX();
				final double dy = livePosition.getY() - newPosition.getY();
				final double dz = livePosition.getZ() - newPosition.getZ();
				System.out.println("Player position packet statistics:");
				System.out.println("    Stale position: (" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")");
				System.out.println("    Prev position: (" + livePosition.getX() + ", " + livePosition.getY() + ", " + livePosition.getZ() + ")");
				System.out.println("    New position: (" + newPosition.getX() + ", " + newPosition.getY() + ", " + newPosition.getZ() + ")");
				System.out.println("    DX: " + dx);
				System.out.println("    DY: " + dy);
				System.out.println("    DZ: " + dz);
				System.out.println("    Distance: " + livePosition.distance(newPosition));
				System.out.println("    Time since last packet: " + (System.nanoTime() - last) / 1E6D + " ms");
				System.out.println("    Avg Distance: " + tracker.getAvgMovement());
				System.out.println("    Avg packet delta: " + (tracker.getAvgMessageTime() / 1E6D) + " ms");
				System.out.println("    Message On Ground: " + message.isOnGround());
				System.out.println("    Calculated On Ground: " + onGround);
				System.out.println("    Sneaking: " + human.isSneaking());
				System.out.println("    Sprinting: " + human.isSprinting());
				System.out.println("    Swimming: " + human.isInWater());
				System.out.println("    Ping: " + holder.get(Ping.class).getPing());
				last = System.nanoTime();
				*/
				//TODO This is way too aggressive, needs to be revised.
				//				if (tracker.isFilled()) {
				//					//Flying?
				//					if (tracker.getAvgMovement() > 0.3D && !human.canFly()) {
				//						holder.sendMessage("Flying? (Speed: " + tracker.getAvgMovement());
				//					}
				//					//Flooding packets?
				//					if (tracker.getAvgMessageTime() < 40F * 1E6F) {
				//						holder.sendMessage("Speed Hacking?");
				//					}
				//				}
			}
		}
	}

	private static class PositionTracker {
		private final TLongList messageTimeDeltas = new TLongLinkedList();
		private final TDoubleList distanceDeltas = new TDoubleLinkedList();
		private long lastMessage = System.nanoTime();

		public boolean isFilled() {
			return messageTimeDeltas.size() >= 50;
		}

		public void updateTracker(Human human, Point prevPoint, Point newPoint, long created) {
			//Don't track updates if the last one was > 500 ms ago
			if (created - lastMessage > 500 * 1E6) {
				lastMessage = created;
			} else {
				messageTimeDeltas.add(created - lastMessage);
				distanceDeltas.add(normalizeDistance(human, prevPoint, newPoint));
				if (messageTimeDeltas.size() > 50) {
					messageTimeDeltas.removeAt(0);
				}
				if (distanceDeltas.size() > 50) {
					distanceDeltas.removeAt(0);
				}
				lastMessage = created;
			}
		}

		private double normalizeDistance(Human human, Point prevPoint, Point newPoint) {
			final float dx = prevPoint.getX() - newPoint.getX();
			final float dz = prevPoint.getZ() - newPoint.getZ();
			final float dist = (float) Math.sqrt(dx * dx + dz * dz);
			final double tpsModifier = 1D / Math.max(1F, 20F / VanillaPlugin.getInstance().getTPSMonitor().getTPS());

			if (human.isSneaking()) {
				return (dist / 0.08D) * 0.22D * tpsModifier;
			}
			if (human.isSprinting()) {
				return (dist / 0.32D) * 0.22D * tpsModifier;
			}
			return dist * tpsModifier;
		}

		public double getAvgMovement() {
			if (distanceDeltas.size() == 0) {
				return 0;
			}
			double total = 0;
			TDoubleIterator i = distanceDeltas.iterator();
			while (i.hasNext()) {
				total += i.next();
			}
			return total / distanceDeltas.size();
		}

		public double getAvgMessageTime() {
			if (messageTimeDeltas.size() == 0) {
				return 0;
			}
			long total = 0;
			TLongIterator i = messageTimeDeltas.iterator();
			while (i.hasNext()) {
				total += i.next();
			}
			return total / (double) messageTimeDeltas.size();
		}
	}
}
