package com.codenjoy.dojo.verland.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.verland.model.items.*;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.games.verland.Element.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Hero> heroes() {
        return find(Hero::new, HERO);
    }

    public List<Contagion> contagions() {
        return find(Contagion::new, INFECTION);
    }

    public List<Cured> cured() {
        return find(Cured::new, HERO_HEALING);
    }

    public List<Wall> walls() {
        return find(Wall::new, PATHLESS);
    }

    public List<Cure> cures() {
        return find(Cure::new, HERO_CURE);
    }

    public List<Cell> hidden() {
        return find(Cell::hidden,
                HIDDEN);
    }

    public List<Cell> clear() {
        return find(Cell::clean,
                CLEAR);
    }

    @Override
    protected void fill(PointField field) {
        // туман войны
        field.addAll(hidden());
        // чистое поле
        field.addAll(clear());
        // места заражения
        List<Contagion> contagions = contagions();
        field.addAll(contagions);
        // всю инфекцию надо спрятать за туманом
        field.addAll(contagions.stream()
                .map(Cell::hidden)
                .collect(toList()));
        // стены
        field.addAll(walls());
        // попытки исцелить
        field.addAll(cures());
        // cell должны быть по всему полю, так как они сигналят о заражении
        // заполняем остаток поля
        field.addAll(otherCells(field.of(Cell.class)));
    }

    private List<Cell> otherCells(Accessor<Cell> other) {
        List<Cell> result = new ArrayList<>();
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                Point pt = pt(x, y);
                if (!other.contains(pt)) {
                    result.add(Cell.clean(pt));
                }
            }
        }
        return result;
    }
}