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
package org.spout.vanilla.plugin.component.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.gui.Screen;
import org.spout.api.gui.Widget;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.player.hud.ArmorWidget;
import org.spout.vanilla.plugin.component.player.hud.CrosshairWidget;
import org.spout.vanilla.plugin.component.player.hud.ExpBarWidget;
import org.spout.vanilla.plugin.component.player.hud.VanillaHotBar;
import org.spout.vanilla.plugin.component.player.hud.HotBarWidget;
import org.spout.vanilla.plugin.component.player.hud.VanillaArmorWidget;
import org.spout.vanilla.plugin.component.player.hud.VanillaCrosshair;
import org.spout.vanilla.plugin.component.player.hud.VanillaExpBar;

/**
 * Component attached to clients-only that updates the Heads Up Display.
 */
public class HUDComponent extends EntityComponent {

    private static final float SCALE = 0.75f; // TODO: Apply directly from engine
    private static final float START_X = -0.71f * SCALE;
    // The main hud screen
    private final Screen hud = new Screen();
    // The core elements of the main hud
    private final Widget crosshair = new Widget();
    private final Widget hotbar = new Widget();
    private final Widget exp = new Widget();
    private final Widget armor = new Widget();
    
    private HotBarWidget hotBar;
    private ArmorWidget armorWidget;
    private ExpBarWidget expBar;
    private CrosshairWidget crosshairWidget;

    @Override
    public void onAttached() {
        if (!(getOwner() instanceof Player)) {
            throw new IllegalStateException("May only attach this component to players!");
        }
        if (Spout.getPlatform() != Platform.CLIENT) {
            throw new IllegalStateException("This component is only attached to clients!");
        }

        setDefault(VanillaHotBar.class);
        setDefault(VanillaArmorWidget.class);
        setDefault(VanillaExpBar.class);
        setDefault(VanillaCrosshair.class);
        initHUD();
        // TODO: Call these methods in DrowningComponent, HealthComponent, etc.
        setArmor(15);
        setHotbarSlot(0);
    }

    public void setDefault(Class clazz) {
        Object widget = new Object();
        try {
            widget = clazz.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(HUDComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HUDComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (widget instanceof HotBarWidget) {
            hotBar = (HotBarWidget)widget;
        }
        
        if (widget instanceof ArmorWidget) {
            armorWidget = (ArmorWidget) widget;
        }
        
        if (widget instanceof ExpBarWidget) {
            expBar = (ExpBarWidget) widget;
        }
        
        if (widget instanceof CrosshairWidget) {
            crosshairWidget = (CrosshairWidget) widget;
        }
    }

    public void attachWidget(Widget widget) {
        hud.attachWidget(VanillaPlugin.getInstance(), widget);
    }

    public void openHUD() {
        ((Client) Spout.getEngine()).getScreenStack().openScreen(hud);
    }

    /**
     * Sets the amount of armor to display.
     *
     * @param amount Amount of armor to display
     */
    public void setArmor(int amount) {
        armorWidget.setUpdateArmor(armor, amount);
    }

    /**
     * Modify the advancement of the xp bar.
     *
     * @param percent The advancement between 0 and 1
     */
    public void setExp(float percent) {
        expBar.setExpBar(exp, percent, SCALE, START_X);
    }

    /**
     * Sets the selected hotbar slot.
     *
     * @param slot Index of the slot to set
     */
    public void setHotbarSlot(int slot) {
        hotBar.setHotbarSlot(hotbar, slot, SCALE);
    }

    private void initHUD() {
        hud.setTakesInput(false);

        // Setup crosshairs
        crosshairWidget.init(crosshair, SCALE, START_X);
        hud.attachWidget(VanillaPlugin.getInstance(), crosshair);

        // Setup the hotbar
        hotBar.init(hotbar, SCALE, START_X);
        hud.attachWidget(VanillaPlugin.getInstance(), hotbar);

        // Experience level text
        expBar.init(exp, SCALE, START_X);

        // Setup survival-specific hud components
        boolean survival = true;
        if (survival) {
            // Experience bar
            setExp(0);

            // Armor bar
            armorWidget.init(armor, SCALE, START_X);
            hud.attachWidget(VanillaPlugin.getInstance(), armor);
        }

        hud.attachWidget(VanillaPlugin.getInstance(), exp);
    }
}
