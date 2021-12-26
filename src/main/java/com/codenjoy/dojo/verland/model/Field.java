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


import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.verland.model.items.*;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;

import java.util.List;

public interface Field extends GameField<Player> {

    boolean isFree(Point pt);

    int size();

    void cure(Hero hero, Direction direction);

    // TODO тут не точно
    Hero hero();

    Contagion tryCreateContagion(Point cell);

    boolean isContagion(Point pt);

    int contagionsNear(Point pt);

    Accessor<Contagion> contagions();

    void openAllBoard();

    Accessor<Cell> cells();

    Accessor<Cured> cured();

    Accessor<Wall> walls();

    Accessor<Cure> cures();

    Accessor<Hero> heroes();

    Accessor<HeroSpot> heroSpots();

    List<Cell> clean();
}
