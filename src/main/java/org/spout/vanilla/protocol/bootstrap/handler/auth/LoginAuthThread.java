/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.bootstrap.handler.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.spout.api.Spout;
import org.spout.api.protocol.Session;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.protocol.VanillaProtocol;

public class LoginAuthThread implements Runnable {
	private final static String URLBase = "http://session.minecraft.net/game/checkserver.jsp?";
	private final static String userPrefix = "user=";
	private final static String idPrefix = "&serverId=";
	private final static String authString = "YES";
	private final Session session;
	private final String name;
	private final Runnable runnable;

	public LoginAuthThread(Session session, String name, Runnable runnable) {
		this.session = session;
		this.name = name;
		this.runnable = runnable;
	}

	public void run() {
		long start = System.currentTimeMillis();
		String sessionId = session.getDataMap().get(VanillaProtocol.SESSION_ID);
		String encodedUser;
		String encodedId;
		try {
			encodedUser = URLEncoder.encode(name, "UTF-8");
			encodedId = URLEncoder.encode(sessionId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			failed("Unable to uncode username or sessionid");
			return;
		}
		String fullURL = URLBase + userPrefix + encodedUser + idPrefix + encodedId;
		URL authURL;
		try {
			authURL = new URL(fullURL);
		} catch (MalformedURLException e2) {
			failed("Unable to parse URL");
			return;
		}

		URLConnection connection;
		try {
			connection = authURL.openConnection();
		} catch (IOException e2) {
			failed("Unable to open connection");
			return;
		}

		if (!(connection instanceof HttpURLConnection)) {
			failed("Unable to open http connection");
			return;
		}

		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setConnectTimeout(30000);
		httpConnection.setReadTimeout(30000);

		try {
			httpConnection.connect();
		} catch (IOException e) {
			e.printStackTrace();
			failed("Unable to connect to auth server");
			return;
		}

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String reply = in.readLine();
			if (reply != null && reply.equals(authString)) {
				Spout.getEngine().getScheduler().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), runnable);
			} else {
				failed("Auth server refused authentication");
			}
		} catch (IOException e) {
			e.printStackTrace();
			failed("Unable to read reply from auth server");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				} finally {
					httpConnection.disconnect();
				}
			} else {
				httpConnection.disconnect();
			}
		}
		if (Spout.getEngine().debugMode()) {
			Spout.getLogger().info("Authing took " + (System.currentTimeMillis() - start) + "ms");
		}
	}

	private void failed(String message) {
		session.disconnect(message);
	}
}