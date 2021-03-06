package com.codenjoy.dojo.verland.services.ai.logic;

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
import com.codenjoy.dojo.games.verland.ElementUtils;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.games.verland.Element.CLEAR;
import static com.codenjoy.dojo.games.verland.Element.HERO;

public class Group {

    private List<Cell> list;
    private Element element;

    public Group(List<Cell> cells, Element element) {
        this.element = element;

        list = new ArrayList<>(cells.size());
        for (Cell cell : cells) {
            list.add(cell.copy());
        }

        Action action = action();
        list.forEach(cell -> cell.action(action));
    }

    public List<Cell> list() {
        return list;
    }

    public int size() {
        return list.size();
    }

    private Action action() {
        if (element == CLEAR || element == HERO) {
            return Action.GO;
        }

        if (size() == ElementUtils.contagions(element)) {
            return Action.CURE;
        }

        return Action.NOTHING;
    }
}
