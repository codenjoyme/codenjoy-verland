package com.codenjoy.dojo.verland;

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


import com.codenjoy.dojo.games.verland.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.utils.Smoke;
import com.codenjoy.dojo.utils.SmokeUtils;
import com.codenjoy.dojo.verland.services.Events;
import com.codenjoy.dojo.verland.services.GameRunner;
import com.codenjoy.dojo.verland.services.GameSettings;
import com.codenjoy.dojo.verland.services.ai.AISolver;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.BOARD_SIZE;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.MINES_ON_BOARD;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class SmokeTest {

    private Smoke smoke;
    private Dice dice;

    @Before
    public void setup() {
        smoke = new Smoke();
        dice = smoke.dice();
    }

    @Test
    public void test() {
        // about 5 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы ни разу не проиграли и всегда правильно отгадывали где мины
            assertFalse(actual.contains(Events.KILL_ON_MINE.name()));
            assertFalse(actual.contains(Events.FORGET_CHARGE.name()));
        };

        smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .integer(BOARD_SIZE, 15)
                                .integer(MINES_ON_BOARD, 10);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }

    @Test
    public void test2() {
        // about 5 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы все же проиграли
            assertFalse(actual.contains(Events.KILL_ON_MINE.name()));
            assertTrue(actual.contains(Events.FORGET_CHARGE.name()));
            assertTrue(actual.contains(Events.SUICIDE.name()));
        };

        smoke.play(ticks, "SmokeTest2.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        return super.getSettings()
                                .integer(BOARD_SIZE, 20)
                                .integer(MINES_ON_BOARD, 50);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }
}