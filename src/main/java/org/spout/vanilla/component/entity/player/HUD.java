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
package org.spout.vanilla.component.entity.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

import org.spout.api.Client;
import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.component.entity.player.hud.ArmorWidget;
import org.spout.vanilla.component.entity.player.hud.CrosshairWidget;
import org.spout.vanilla.component.entity.player.hud.DrowningWidget;
import org.spout.vanilla.component.entity.player.hud.ExpBarWidget;
import org.spout.vanilla.component.entity.player.hud.GUIWidget;
import org.spout.vanilla.component.entity.player.hud.HungerWidget;
import org.spout.vanilla.component.entity.player.hud.QuickbarWidget;

/**
 * Component attached to clients-only that updates the Heads Up Display.
 */
public class HUD extends VanillaEntityComponent {
	private static final float SCALE = 0.75f; // TODO: Apply directly from engine
	private static final float START_X = -0.71f * SCALE;
	// The main hud screen
	private final Screen hud = new Screen();
	// The core elements of the main hud
	private QuickbarWidget quickbarWidget = null;
	private ArmorWidget armorWidget = null;
	private ExpBarWidget expBar = null;
	private CrosshairWidget crosshairWidget = null;
	private DrowningWidget drowningWidget = null;
	private HungerWidget hungerWidget = null;
	private final ConcurrentMap<Class<? extends GUIWidget>, GUIWidget> widgets = new ConcurrentHashMap<Class<? extends GUIWidget>, GUIWidget>();

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("May only attach this component to players!");
		}
		if (VanillaPlugin.getInstance().getEngine().getPlatform() != Platform.CLIENT) {
			throw new IllegalStateException("This component is only attached to clients!");
		}
	}

	/**
	 * Will update the class used for each part of the HUD
	 * Will not replace a class if one already is placed
	 * <p>
	 * To replace use setDefault(Class clazz, boolean force)
	 * @param clazz
	 */
	public <T extends GUIWidget> T setDefault(Class<T> clazz) {
		return setDefault(clazz, false);
	}

	/**
	 * Will update the class used for each part of the HUD
	 * <p>
	 * Second param is to force the update, a false will only update the
	 * class if a class isn't already placed, true will replace any class
	 * and force an update (forced update not implemented yet)
	 * @param clazz
	 * @return New or already attached class
	 */
	public <T extends GUIWidget> T setDefault(Class<T> clazz, boolean force) {
		GUIWidget widget = null;
		try {
			widget = clazz.newInstance();
		} catch (InstantiationException ex) {
			Spout.getLogger().log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Spout.getLogger().log(Level.SEVERE, null, ex);
		}

		if (widget instanceof QuickbarWidget) {
			// There has to be a better way to do this.
			if (quickbarWidget != null && !force) {
				return (T) quickbarWidget;
			}
			quickbarWidget = (QuickbarWidget) widget;
		} else if (widget instanceof ArmorWidget) {
			if (armorWidget != null && !force) {
				return (T) armorWidget;
			}
			armorWidget = (ArmorWidget) widget;
		} else if (widget instanceof ExpBarWidget) {
			if (expBar != null && !force) {
				return (T) expBar;
			}
			expBar = (ExpBarWidget) widget;
		} else if (widget instanceof CrosshairWidget) {
			if (crosshairWidget != null && !force) {
				return (T) crosshairWidget;
			}
			crosshairWidget = (CrosshairWidget) widget;
		} else if (widget instanceof DrowningWidget) {
			if (drowningWidget != null && !force) {
				return (T) drowningWidget;
			}
			drowningWidget = (DrowningWidget) widget;
		} else if (widget instanceof HungerWidget) {
			if (hungerWidget != null && !force) {
				return (T) hungerWidget;
			}
			hungerWidget = (HungerWidget) widget;
		} else if (widget instanceof GUIWidget) {
			// Not a CORE GUI add to other attachments and initialize
			if (!force) {
				GUIWidget wid = widgets.putIfAbsent(clazz, widget);
				if (wid != null) {
					widget = wid;
				}
			} else {
				widgets.put(clazz, widget);
			}
			widget.init(widget.getWidget(), this);
		}

		return (T) widget;
	}

	/**
	 * Will retrieve a non core Widget that is attached to hud.
	 * Will return null if class is not found.
	 * @param <T>
	 * @param clazz
	 * @return Widget class or NULL if not found
	 */
	public <T extends GUIWidget> T getWidget(Class<T> clazz) {
		if (widgets.containsKey(clazz)) {
			return (T) widgets.get(clazz);
		}
		return null;
	}

	public void attachWidget(Widget widget) {
		hud.attachWidget(VanillaPlugin.getInstance(), widget);
	}

	public void openHUD() {
		((Client) getOwner().getEngine()).getScreenStack().openScreen(hud);
	}

	/**
	 * Sets the amount of armor to display.
	 * <p>
	 * This method will be removed once armor is handled by Vanilla
	 */
	public void setArmor() {
		armorWidget.update();
	}

	// Needs to be moved to ArmorComponent if one ever exists
	public int getArmor() {
		return 0;
	}

	// Needs to be moved to ExperienceComponent if one ever exists.
	public float getExpPercent() {
		return 0f;
	}

	/**
	 * Modify the advancement of the xp bar.
	 * <p>
	 * This method needs to be removed when experience is handled by
	 * Vanilla code.
	 */
	public void setExp() {
		expBar.update();
	}

	/**
	 * Returns the CORE GUI Widget (AirMeter)
	 * @return
	 */
	public DrowningWidget getAirMeter() {
		return drowningWidget;
	}

	/**
	 * Returns the CORE GUI Widget (Quickbar)
	 * @return
	 */
	public QuickbarWidget getQuickbar() {
		return quickbarWidget;
	}

	/**
	 * Returns the CORE GUI Widget (ExpMeter)
	 * @return
	 */
	public ExpBarWidget getExpMeter() {
		return expBar;
	}

	/**
	 * Returns the CORE GUI Widget (ArmorMeter)
	 * @return
	 */
	public ArmorWidget getArmorMeter() {
		return armorWidget;
	}

	/**
	 * Returns the CORE GUI Widget (Cross hair)
	 * @return
	 */
	public CrosshairWidget getCrosshair() {
		return crosshairWidget;
	}

	/**
	 * Returns the CORE GUI Widget (HungerMeter)
	 * @return
	 */
	public HungerWidget getHungerMeter() {
		return hungerWidget;
	}

	public void setupHUD() {
		initHUD();
		setArmor();
		//setQuickbarSlot();
		setExp();
	}

	public Float getScale() {
		return SCALE;
	}

	public Float getStartX() {
		return START_X;
	}

	private void initHUD() {
		hud.setTakesInput(false);

		// Setup all CORE Vanilla HUD Widgets
		crosshairWidget.init(crosshairWidget.getWidget(), this);
		quickbarWidget.init(quickbarWidget.getWidget(), this);
		expBar.init(expBar.getWidget(), this);
		armorWidget.init(armorWidget.getWidget(), this);
		drowningWidget.init(drowningWidget.getWidget(), this);
		hungerWidget.init(hungerWidget.getWidget(), this);
	}
}
