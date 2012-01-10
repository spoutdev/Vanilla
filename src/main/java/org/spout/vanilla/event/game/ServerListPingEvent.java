package org.spout.vanilla.event.game;

import java.net.InetAddress;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

public class ServerListPingEvent extends Event {
	InetAddress address;
	String motd;
	int numPlayers, maxPlayers;
	private static HandlerList handlers = new HandlerList();
	
	public ServerListPingEvent(InetAddress address, String motd, int numPlayers, int maxPlayers) {
		this.address = address;
		this.motd = motd;
		this.numPlayers = numPlayers;
		this.maxPlayers = maxPlayers;
	}
	
	/**
	 * Gets the address that requested the ping response
	 * 
	 * @return address
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Gets the motd to send
	 * 
	 * @return motd
	 */
	public String getMotd() {
		return motd;
	}
	 
	/**
	 * Sets the motd to send
	 * 
	 * @param motd to set, can not be null
	 */
	public void setMotd(String motd) {
		if (motd == null) throw new NullPointerException("Message of the day can not be null");
		this.motd = motd;
	}
	
	/**
	 * Gets the number of players reported to be online
	 * 
	 * @return players online
	 */
	public int getNumPlayers() {
		return numPlayers;
	}
	
	/**
	 * Sets the number of players online
	 * 
	 * @param numPlayers online
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	/**
	 * Gets the maximum number of players that can log in for this server
	 * 
	 * @return maximum number of players
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	/**
	 * Sets the maximum number of players
	 * 
	 * @param maxPlayers that can log in
	 */
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	/**
	 * Gets the full message response that will be sent to the packet.
	 * 
	 * @return packet message
	 */
	public String getMessage() {
		return motd + "\u00A7" + numPlayers + "\u00A7" + maxPlayers + "\u00A7";
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
