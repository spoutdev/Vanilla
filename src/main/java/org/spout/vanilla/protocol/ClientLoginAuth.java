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
package org.spout.vanilla.protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.spout.api.scheduler.TaskPriority;

import org.spout.vanilla.VanillaPlugin;

public class ClientLoginAuth implements Runnable {
	private static final String baseURL = "http://session.minecraft.net/game/joinserver.jsp?";
	private static final String authString = "OK";
	private final String params;
	private final Runnable runnable;

	public ClientLoginAuth(String hash, Runnable runnable) {
		VanillaPlugin p = VanillaPlugin.getInstance();
		String encodedUser = "";
		String encodedId = "";
		try {
			encodedUser = URLEncoder.encode(p.getUsername(), "UTF-8");
			encodedId = URLEncoder.encode(p.getSessionId(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.params = "user=" + encodedUser + "&sessionId=" + encodedId + "&serverId=" + hash;
		System.out.println(params);
		this.runnable = runnable;
	}

	@Override
	public void run() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(baseURL + params);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String reply = in.readLine();

			if (VanillaPlugin.getInstance().getEngine().debugMode()) {
				VanillaPlugin.getInstance().getLogger().info("Logging in " + reply);
			}

			if (reply.equals(authString)) {
				VanillaPlugin.getInstance().getEngine().getScheduler().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), runnable, TaskPriority.CRITICAL);
			} else {
				VanillaPlugin.getInstance().getLogger().info("Error while logging in " + reply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
