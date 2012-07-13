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
package org.spout.vanilla.chat.style;

import java.util.regex.Pattern;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.chat.style.StyleFormatter;
import org.spout.api.chat.style.StyleHandler;

/**
 * A Vanilla implementation of chat styles
 */
public class VanillaStyleHandler extends StyleHandler {
	public static final VanillaStyleHandler INSTANCE = new VanillaStyleHandler();
	public static final int ID = register(INSTANCE);
	private final Pattern stylePattern;

	public VanillaStyleHandler() {
		super();
		registerFormatter(ChatStyle.BLACK, new VanillaStyleFormatter('0'));
		registerFormatter(ChatStyle.DARK_BLUE, new VanillaStyleFormatter('1'));
		registerFormatter(ChatStyle.DARK_GREEN, new VanillaStyleFormatter('2'));
		registerFormatter(ChatStyle.DARK_CYAN, new VanillaStyleFormatter('3'));
		registerFormatter(ChatStyle.DARK_RED, new VanillaStyleFormatter('4'));
		registerFormatter(ChatStyle.PURPLE, new VanillaStyleFormatter('5'));
		registerFormatter(ChatStyle.GOLD, new VanillaStyleFormatter('6'));
		registerFormatter(ChatStyle.GRAY, new VanillaStyleFormatter('7'));
		registerFormatter(ChatStyle.DARK_GRAY, new VanillaStyleFormatter('8'));
		registerFormatter(ChatStyle.BLUE, new VanillaStyleFormatter('9'));
		registerFormatter(ChatStyle.BRIGHT_GREEN, new VanillaStyleFormatter('a'));
		registerFormatter(ChatStyle.CYAN, new VanillaStyleFormatter('b'));
		registerFormatter(ChatStyle.RED, new VanillaStyleFormatter('c'));
		registerFormatter(ChatStyle.PINK, new VanillaStyleFormatter('d'));
		registerFormatter(ChatStyle.YELLOW, new VanillaStyleFormatter('e'));
		registerFormatter(ChatStyle.WHITE, new VanillaStyleFormatter('f'));
		registerFormatter(ChatStyle.CONCEAL, new VanillaStyleFormatter('k'));
		registerFormatter(ChatStyle.BOLD, new VanillaStyleFormatter('l'));
		registerFormatter(ChatStyle.STRIKE_THROUGH, new VanillaStyleFormatter('m'));
		registerFormatter(ChatStyle.UNDERLINE, new VanillaStyleFormatter('n'));
		registerFormatter(ChatStyle.ITALIC, new VanillaStyleFormatter('o'));
		registerFormatter(ChatStyle.RESET, new VanillaStyleFormatter('r'));
		StringBuilder stylePatternString = new StringBuilder();
		stylePatternString.append("(?i)").append(VanillaStyleFormatter.COLOR_CHAR).append("([");
		for (StyleFormatter formatter : getFormatters()) {
			if (formatter instanceof VanillaStyleFormatter) {
				stylePatternString.append(((VanillaStyleFormatter) formatter).getStyleChar());
			}
		}
		stylePatternString.append("])");
		stylePattern = Pattern.compile(stylePatternString.toString());
	}

	public Pattern getStylePattern() {
		return stylePattern;
	}

	public String stripStyle(String formatted) {
		return stylePattern.matcher(formatted).replaceAll("");
	}
}
