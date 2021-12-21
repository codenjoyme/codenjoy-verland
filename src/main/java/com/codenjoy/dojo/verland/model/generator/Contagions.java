package com.codenjoy.dojo.verland.model.generator;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.verland.model.Field;
import com.codenjoy.dojo.verland.model.items.Contagion;

import java.util.ArrayList;
import java.util.List;

public class Contagions implements Generator {

    public static int SAFE_AREA_X_0 = 1;
    public static int SAFE_AREA_X_1 = 3;
    public static int SAFE_AREA_Y_0 = 1;
    public static int SAFE_AREA_Y_1 = 3;

    private List<Point> free;
    private Dice dice;

    public Contagions(Dice dice) {
        this.dice = dice;
    }

    public List<Contagion> get(int count, Field field) {
        free = field.freeCells();
        removeSafeAreaFromFreeCells();
        List<Contagion> result = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            Contagion it = new Contagion(freeCell());
            result.add(it);
            free.remove(it);
        }
        return result;
    }

    private void removeSafeAreaFromFreeCells() {
        for (int i = 0; i < free.size(); i++) {
            Point point = free.get(i);
            if (isInSafeArea(point)) {
                free.remove(i--);
            }
        }
    }

    private boolean isInSafeArea(Point point) {
        return point.getX() >= SAFE_AREA_X_0 && point.getX() <= SAFE_AREA_X_1
                && point.getY() >= SAFE_AREA_Y_0 && point.getY() <= SAFE_AREA_Y_1;
    }

    private Point freeCell() {
        if (!free.isEmpty()) {
            int place = dice.next(free.size());
            return free.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}