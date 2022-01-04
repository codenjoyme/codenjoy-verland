package com.codenjoy.dojo.verland.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.games.verland.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.verland.model.Field;
import com.codenjoy.dojo.verland.model.Player;

public class Cured extends PointImpl implements State<Element, Player> {

    public Cured(Point pt) {
        super(pt);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (player.getHero().isGameOver()) {
            return Element.HERO_HEALING;
        }

        // ничего не рисуем, даем возможность другим отрисоваться
        return null;
    }
}
