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
package org.spout.vanilla.inventory.window;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.Slot;

/**
 * Represents the arguments of a click on a
 * {@link org.spout.vanilla.inventory.window.Window}
 */
public class ClickArguments {
	private final Slot slot;
	private final ClickAction action;

	public ClickArguments(Inventory inventory, int slot, ClickAction action) {
		this(new Slot(inventory, slot), action);
	}

	public ClickArguments(Slot invSlot, ClickAction action) {
		this.slot = invSlot;
		this.action = action;
	}

	/**
	 * Returns true if the client was holding shift when the window was clicked
	 * NOTE: this is only a shortcut method
	 * @return true if shift was being held down
	 */
	public boolean isShiftClick() {
		return action == ClickAction.SHIFT_LEFT_MOUSE || action == ClickAction.SHIFT_RIGHT_MOUSE;
	}

	public ClickAction getAction() {
		return action;
	}

	/**
	 * Gets the slot that was clicked, from which the inventory and item index can be obtained
	 * @return Clicked Slot
	 */
	public Slot getSlot() {
		return slot;
	}

	@Override
	public String toString() {
		return "ClickArguments {Slot=" + slot.getIndex() + ", Button=" + action + "}";
	}

	public enum ClickAction {
		LEFT_CLICK(0, 0),
		RIGHT_CLICK(0, 1),
		SHIFT_LEFT_MOUSE(1, 0),
		SHIFT_RIGHT_MOUSE(1, 1),
		NUMBER_1(2, 0),
		NUMBER_2(2, 1),
		NUMBER_3(2, 2),
		NUMBER_4(2, 3),
		NUMBER_5(2, 4),
		NUMBER_6(2, 5),
		NUMBER_7(2, 6),
		NUMBER_8(2, 7),
		NUMBER_9(2, 8),
		MIDDLE_CLICK(3, 2),
		DROP(4, 0),
		CTRL_DROP(4, 1),
		EMPTY_LEFT_OUTSIDE_CLICK(4, 0),
		EMPTY_RIGHT_OUTSIDE_CLICK(4, 1),
		START_LEFT_PAINT(5, 0),
		START_RIGHT_PAINT(5, 4),
		LEFT_PAINT_PROGRESS(5, 1),
		RIGHT_PAINT_PROGRESS(5, 5),
		END_LEFT_PAINT(5, 2),
		END_RIGHT_PAINT(5, 6),
		DOUBLE_CLICK(6, 0);
		private byte mode, button;

		private ClickAction(int mode, int button) {
			this.mode = (byte) mode;
			this.button = (byte) button;
		}

		public static ClickAction getAction(byte mode, byte button) {
			for (ClickAction a : values()) {
				if (mode == a.mode && button == a.button) {
					return a;
				}
			}
			throw new IllegalArgumentException("Invalid mode{" + mode + "} and button{" + button + "} combination has occured!");
		}

		@Override
		public String toString() {
			return "ClickAction{" + "mode=" + mode + ", button=" + button + '}';
		}
	}
}
