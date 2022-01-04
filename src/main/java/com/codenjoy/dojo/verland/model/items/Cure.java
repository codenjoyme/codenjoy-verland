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
import com.codenjoy.dojo.verland.model.Hero;
import com.codenjoy.dojo.verland.model.Player;

import java.util.Objects;

public class Cure extends PointImpl implements State<Element, Player> {

    private Hero owner;

    public Cure(Point pt, Hero owner) {
        super(pt);
        this.owner = owner;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return Element.HERO_CURE;
    }

    public Hero owner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cure cure = (Cure) o;
        return Objects.equals(owner, cure.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), owner);
    }
}
