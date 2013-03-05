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

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

import org.spout.api.entity.Player;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.Session.UncaughtExceptionHandler;
import org.spout.api.scheduler.TaskPriority;

import org.spout.vanilla.VanillaPlugin;

public class PasteExceptionHandler implements UncaughtExceptionHandler {
	private static final String PASTEBIN_URL = "http://pastebin.com";
	private final Session session;

	public PasteExceptionHandler(Session session) {
		this.session = session;
	}

	@Override
	public void uncaughtException(Message message, MessageHandler<?> handle, Exception ex) {
		Player player = session.getPlayer();
		player.getEngine().getLogger().log(Level.SEVERE, "Message handler for " + message.getClass().getSimpleName() + " threw exception for player " + (session.getPlayer() != null ? session.getPlayer().getName() : "null"));
		ex.printStackTrace();
		if (player != null && player.hasPermission("vanilla.exception.paste")) {
			StringBuilder builder = new StringBuilder("Vanilla Error Report:\n");
			builder.append("( Please submit this report to http://spout.in/issues )\n");
			builder.append("    Version: ").append(VanillaPlugin.getInstance().getDescription().getVersion()).append("\n");
			builder.append("----------------------------------------------------------------------").append("\n");
			builder.append("Stack Trace:").append("\n");
			builder.append("    Exception: ").append(ex.getClass().getSimpleName()).append("\n");
			logTrace(builder, ex);
			Runnable task = new PasteRunnable(session, builder.toString(), "Message handler exception for " + message.getClass().getSimpleName());
			player.getEngine().getScheduler().scheduleAsyncDelayedTask(VanillaPlugin.getInstance(), task, 0, TaskPriority.CRITICAL);
		} else {
			session.disconnect(false, new Object[]{"Message handler exception for ", message.getClass().getSimpleName()});
		}
	}

	private void logTrace(StringBuilder builder, Exception e) {
		Throwable parent = e;
		String indent = "    ";
		while (parent != null) {
			if (parent == e) {
				builder.append(indent).append("Trace:").append("\n");
			} else {
				builder.append(indent).append("Caused By: (").append(parent.getClass().getSimpleName()).append(")").append("\n");
				builder.append(indent).append("    ").append("[").append(parent.getMessage()).append("]").append("\n");
			}
			for (StackTraceElement ele : e.getStackTrace()) {
				builder.append(indent).append("    ").append(ele.toString()).append("\n");
			}
			indent += "    ";
			parent = parent.getCause();
		}
	}

	private static class PasteRunnable implements Runnable {
		private final Session session;
		private final String message;
		private final String backupMessage;

		PasteRunnable(Session session, String message, String backupMessage) {
			this.session = session;
			this.message = message;
			this.backupMessage = backupMessage;
		}

		@Override
		public void run() {
			PasteBinAPI pastebin = new PasteBinAPI("963f01dd506cb3f607a487bc34b60d16");
			String response = "";
			try {
				response = pastebin.makePaste(message, "vanilla_" + System.currentTimeMillis(), "text");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (response.startsWith(PASTEBIN_URL)) {
				session.disconnect(true, new Object[]{"Unhandled exception: " + response});
			} else {
				session.disconnect(false, new Object[]{backupMessage});
			}
		}
	}

	private static class PasteBinAPI {
		private final static String pasteURL = "http://www.pastebin.com/api/api_post.php";
		private String token;
		private final String devkey;

		public PasteBinAPI(String devkey) {
			this.devkey = devkey;
		}

		public String checkResponse(String response) {
			if (response.substring(0, 15).equals("Bad API request")) {
				return response.substring(17);
			}
			return "";
		}

		public String makePaste(String message, String name, String format) throws UnsupportedEncodingException {
			String content = URLEncoder.encode(message, "UTF-8");
			String title = URLEncoder.encode(name, "UTF-8");
			String data = "api_option=paste&api_user_key=" + this.token
					+ "&api_paste_private=0&api_paste_name=" + title
					+ "&api_paste_expire_date=N&api_paste_format=" + format
					+ "&api_dev_key=" + this.devkey + "&api_paste_code=" + content;
			String response = this.page(pasteURL, data);
			String check = this.checkResponse(response);
			if (!check.equals("")) {
				return check;
			}
			return response;
		}

		public String page(String uri, String urlParameters) {
			URL url;
			HttpURLConnection connection = null;
			try {
				// Create connection
				url = new URL(uri);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				// Send request
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				// Get Response
				InputStream is = connection.getInputStream();
				return IOUtils.toString(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
			return null;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}
