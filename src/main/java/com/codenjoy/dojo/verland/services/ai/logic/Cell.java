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
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.codenjoy.dojo.games.verland.Element.HIDDEN;
import static com.codenjoy.dojo.games.verland.Element.PATHLESS;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class Cell extends PointImpl {

    private Element element;
    private boolean valued;
    private List<Cell> neighbours;
    private Action action;

    public Cell(int x, int y) {
        super(x, y);
        neighbours = new ArrayList<>();
    }

    public void add(Cell cell) {
        if (cell.element() == PATHLESS) return;

        neighbours.add(cell);
    }

    public void set(Element element) {
        valued = (element != HIDDEN);
        if (valued) {
            this.element = element;
        }
    }

    public List<Cell> unknownCells() {
        return new ArrayList<>(neighbours.size()){{
            for (Cell cell : neighbours) {
                if (!cell.isValued()) {
                    add(cell);
                }
            }
        }};
    }

    @Override
    public Cell copy() {
        Cell cell = new Cell(x, y);
        cell.neighbours = neighbours;
        cell.element = element;
        cell.valued = valued;
        return cell;
    }

    @Override
    public String toString() {
        return String.format("%s:value=%s,valued=%s,action=%s",
                super.toString(),
                element,
                valued,
                action);
    }

    public boolean isValued() {
        return valued;
    }

    public Element element() {
        return element;
    }

    public List<Cell> neighbours() {
        return neighbours;
    }

    public void action(Action action) {
        this.action = action;
    }

    public Action action() {
        return action;
    }

}
