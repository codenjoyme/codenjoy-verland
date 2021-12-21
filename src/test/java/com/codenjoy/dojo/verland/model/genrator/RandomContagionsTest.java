package com.codenjoy.dojo.verland.model.genrator;

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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.verland.model.Player;
import com.codenjoy.dojo.verland.model.Verland;
import com.codenjoy.dojo.verland.model.generator.RandomContagions;
import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RandomContagionsTest {

    private GameSettings settings;

    @Before
    public void setup() {
        settings = new GameSettings()
                .integer(GameSettings.Keys.BOARD_SIZE, 16)
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, 0)
                .integer(GameSettings.Keys.POTIONS_COUNT, 1);
    }

    @Test
    public void shouldContagionsRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            List<Contagion> one = generate();
            List<Contagion> another = generate();

            assertNotEquals(one.toString(), another.toString());
        }
    }

    @Test
    public void hasOnlyOneContagionAtSamePlace() {
        for (int index = 0; index < 100; index++) {
            List<Contagion> contagions = generate();
            for (int i = 0; i < contagions.size() - 1; i++) {
                Contagion first = contagions.get(i);
                for (int j = i + 1; j < contagions.size(); j++) {
                    Contagion second = contagions.get(j);
                    if (first.getX() == second.getX() && first.getY() == second.getY()) {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    public void hasNoContagionsInSafeArea() {
        for (int index = 0; index < 100; index++) {
            List<Contagion> mines = generate();
            for (int i = 0; i < mines.size(); i++) {
                if (isInSafeArea(mines.get(i))) {
                    fail();
                }
            }
        }
    }

    private boolean isInSafeArea(Point point) {
        return point.getX() >= RandomContagions.SAFE_AREA_X_0 && point.getX() <= RandomContagions.SAFE_AREA_X_1
                && point.getY() >= RandomContagions.SAFE_AREA_Y_0 && point.getY() <= RandomContagions.SAFE_AREA_Y_1;
    }

    private List<Contagion> generate() {
        return new RandomContagions(new RandomDice()).get(10, new MockBoard());
    }

    private class MockBoard extends Verland {
        public MockBoard() {
            super((count, board) -> Arrays.asList(), settings);
            newGame(new Player(mock(EventListener.class), settings));
        }
    }
}
