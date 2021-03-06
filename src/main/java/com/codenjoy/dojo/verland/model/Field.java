package com.codenjoy.dojo.verland.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.round.RoundGameField;
import com.codenjoy.dojo.verland.model.items.*;
import com.codenjoy.dojo.verland.services.GameSettings;

import java.util.List;

public interface Field extends RoundGameField<Player, Hero> {

    boolean isFree(Point pt);

    boolean isContagionsExists();

    int size();

    void cure(Hero hero, Direction direction);

    GameSettings settings();

    Accessor<Contagion> contagions();

    Accessor<Cell> cells();

    Accessor<Cured> cured();

    Accessor<Wall> walls();

    Accessor<Cure> cures();

    Accessor<Hero> heroes();

    Accessor<HeroSpot> heroSpots();

    List<Cell> clean();
}
