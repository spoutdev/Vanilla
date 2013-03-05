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
package org.spout.vanilla.component.entity.player.hud;

import org.spout.api.Client;
import org.spout.api.gui.Widget;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.player.HUD;

/**
 * Basic GUI item used for HUD Widgets
 */
public abstract class GUIWidget {
	protected float SCALE;
	protected float START_X;
	protected Widget widget = ((Client) VanillaPlugin.getInstance().getEngine()).getScreenStack().createWidget();
	protected HUD hud;

	/**
	 * Initial code for a Widget, this method needs to be called as super
	 * from init in Widget main code.
	 * @param widget
	 * @param hud
	 */
	public void init(Widget widget, HUD hud) {
		SCALE = hud.getScale();
		START_X = hud.getStartX();
		this.widget = widget;
		this.hud = hud;
	}

	/**
	 * Update code for a widget, should be used to update the
	 * widget after any data has changed.
	 */
	public abstract void update();

	/**
	 * Animate code for a widget, should be used if data has
	 * not been updated but animates.
	 */
	public abstract void animate();

	/**
	 * Code to show/maximize the view of the widget.
	 */
	public abstract void show();

	/**
	 * Code to hide/minimize the view of the widget.
	 */
	public abstract void hide();

	/**
	 * Returns the widget
	 * @return widget
	 */
	public Widget getWidget() {
		return widget;
	}

	/**
	 * Attaches the widget to the main hud.
	 */
	public void attach() {
		hud.attachWidget(widget);
	}
}
