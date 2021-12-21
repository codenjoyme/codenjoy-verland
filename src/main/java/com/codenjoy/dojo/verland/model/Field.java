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


import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;

import java.util.List;

public interface Field extends GameField<Player> {

    List<Point> freeCells();

    List<Point> cells();

    int size();

    List<Contagion> contagions();

    int contagionsCount();

    void moveTo(Direction direction);

    Point positionAfterMove(Direction direction);

    int contagionsNear();

    boolean isNoPotionsButPresentContagions();

    boolean isWin();

    void cure(Direction direction);

    Contagion tryCreateContagion(Point cell);

    int getTurn();

    boolean isGameOver();

    boolean isContagion(Point pt);

    boolean isClean(Point pt);

    boolean isCure(Point pt);

    boolean isWall(Point pt);

    boolean isHero(Point pt);

    int visibleContagionsNear(Point pt);

    Hero hero();
}
