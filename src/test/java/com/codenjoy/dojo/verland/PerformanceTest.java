package com.codenjoy.dojo.verland;

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


import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.verland.services.GameRunner;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.Test;

import static com.codenjoy.dojo.client.local.DiceGenerator.SOUL;
import static com.codenjoy.dojo.utils.TestUtils.assertPerformance;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;
import static org.junit.Assert.assertEquals;

public class PerformanceTest {

    @Test
    public void test() {
        // about 16.2 sec
        int players = 4;
        int countContagions = 200;
        int ticks = 1100;

        int expectedCreation = 1400;
        int expectedPrint = 14000;
        int expectedTick = 500;

        Dice dice = new DiceGenerator().getDice(SOUL, 3000, 2000);
        GameRunner runner = new GameRunner(){

            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public GameSettings getSettings() {
                return new GameSettings()
                        .integer(COUNT_CONTAGIONS, countContagions)
                        .integer(POTIONS_COUNT, countContagions*2)
                        .setLevelMaps(LevelProgress.levelsStartsFrom1,
                                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                "☼♥  ******************************************  ♥☼\n" +
                                "☼   ******************************************   ☼\n" +
                                "☼   ******************************************   ☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼************************************************☼\n" +
                                "☼   ******************************************   ☼\n" +
                                "☼   ******************************************   ☼\n" +
                                "☼♥  ******************************************  ♥☼\n" +
                                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
            }
        };

        boolean printBoard = false;
        String board = assertPerformance(runner,
                players, ticks,
                expectedCreation, expectedTick, expectedPrint,
                printBoard);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    xx                    x      x x   x x      ☼\n" +
                "☼           x              !  x              x   ☼\n" +
                "☼              x          xx                     ☼\n" +
                "☼    x     x       x x     x    x              x ☼\n" +
                "☼                                           x x  ☼\n" +
                "☼     x             x                          x ☼\n" +
                "☼    x x    x  x                   x             ☼\n" +
                "☼  x                       x     x          x    ☼\n" +
                "☼   x   x            x     x   x   x             ☼\n" +
                "☼x                 x  x     x           x        ☼\n" +
                "☼               x  x x        x   xx   x x x     ☼\n" +
                "☼                x         x   x         x       ☼\n" +
                "☼   x                           x  x            x☼\n" +
                "☼                 x                   x   x      ☼\n" +
                "☼            x                        x          ☼\n" +
                "☼          x     xx    x   x         x  x        ☼\n" +
                "☼  x   x♠                                        ☼\n" +
                "☼      xx      x                 xx           x  ☼\n" +
                "☼                                   x            ☼\n" +
                "☼       x    x  x      x   x x                   ☼\n" +
                "☼     x       xx          x   x         x        ☼\n" +
                "☼                 x          x                   ☼\n" +
                "☼ ♥ ♠    x        x       x  x x     x           ☼\n" +
                "☼   ♠             xx                    x        ☼\n" +
                "☼        x  x                                x  x☼\n" +
                "☼    x     x                      x x x          ☼\n" +
                "☼x                    x      x  xx               ☼\n" +
                "☼x     x             x       x           x  x    ☼\n" +
                "☼     x x                 x                      ☼\n" +
                "☼                                                ☼\n" +
                "☼             x    x      x                x     ☼\n" +
                "☼              x               x       x x       ☼\n" +
                "☼x      x        x               x      x        ☼\n" +
                "☼          x     x                               ☼\n" +
                "☼  x                         x          x      x ☼\n" +
                "☼ x         x                 x        x  x   x  ☼\n" +
                "☼   x                    x x                     ☼\n" +
                "☼     x        x                              x  ☼\n" +
                "☼       x                  x  x      x        x  ☼\n" +
                "☼                  x                  x          ☼\n" +
                "☼         x x  xx                      x         ☼\n" +
                "☼            x         x  x                      ☼\n" +
                "☼         x                x  x              x   ☼\n" +
                "☼                                            x x ☼\n" +
                "☼         x           x        xx      x        x☼\n" +
                "☼    x    x          x x  x       x              ☼\n" +
                "☼                x    x                   x      ☼\n" +
                "☼               x x    x                x        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n", board);
    }
}
