/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.entity.living.player;

import org.getspout.api.geo.discrete.Point;
import org.getspout.api.player.Player;
import org.getspout.vanilla.VanillaPlugin;

public class SurvivalPlayer extends MinecraftPlayer {
	public SurvivalPlayer(Player p){
		super(p);
	}

	@Override
	public void onAttached() {
		parent.getTransform().setPosition(new Point(VanillaPlugin.spawnWorld, 0.f, 5.f, 0.f));

	}

	@Override
	public void onTick(float dt) {
		// TODO Auto-generated method stub

	}
}