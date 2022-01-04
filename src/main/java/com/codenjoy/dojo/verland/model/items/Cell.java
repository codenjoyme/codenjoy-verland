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
import com.codenjoy.dojo.services.field.Fieldable;
import com.codenjoy.dojo.verland.model.Field;
import com.codenjoy.dojo.verland.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Cell extends PointImpl implements Fieldable<Field>, State<Element, Player> {

    public static final boolean HIDDEN = false;
    public static final boolean CLEAN = !HIDDEN;

    private Field field;
    private boolean clean;

    public static Cell hidden(Point pt) {
        return new Cell(pt, Cell.HIDDEN);
    }

    public static Cell clean(Point pt) {
        return new Cell(pt, Cell.CLEAN);
    }

    private Cell(Point pt, boolean clean) {
        super(pt);
        this.clean = clean;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!clean && !player.getHero().isGameOver()) {
            return Element.HIDDEN;
        }

        int count = field.contagionsNear(this);
        return count == 0
                ? Element.CLEAR
                : Element.valueOf(count);
    }

    public boolean isClean() {
        return clean;
    }

    public void open() {
        clean = CLEAN;
    }
}
