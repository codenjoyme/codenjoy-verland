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
import com.codenjoy.dojo.services.printer.state.State;
import com.codenjoy.dojo.verland.model.Player;

public class Cell extends PointImpl implements State<Element, Player> {

    public static final boolean HIDDEN = false;
    public static final boolean CLEAN = !HIDDEN;

    private boolean clean;
    private int near;

    public static Cell hidden(Point pt) {
        return new Cell(pt, Cell.HIDDEN);
    }

    public static Cell clean(Point pt) {
        return new Cell(pt, Cell.CLEAN);
    }

    private Cell(Point pt, boolean clean) {
        super(pt);
        this.clean = clean;
        near = 0;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!clean && !player.getHero().isGameOver()) {
            return Element.HIDDEN;
        }

        return near == 0
                ? Element.CLEAR
                : Element.contagions()[near - 1];
    }

    public boolean isClean() {
        return clean;
    }

    public void open() {
        clean = CLEAN;
    }

    public void increase() {
        near++;
    }

    public void decrease() {
        if (near == 0) {
            throw new IllegalStateException("Negative contagions!");
        }
        near--;
    }
}