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


import com.codenjoy.dojo.games.verland.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.utils.Smoke;
import com.codenjoy.dojo.utils.SmokeUtils;
import com.codenjoy.dojo.verland.services.Event;
import com.codenjoy.dojo.verland.services.GameRunner;
import com.codenjoy.dojo.verland.services.GameSettings;
import com.codenjoy.dojo.verland.services.ai.AISolver;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static junit.framework.TestCase.assertEquals;

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
        // about 0.27 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы ни разу не проиграли и всегда правильно отгадывали, где мины
            assertEquals(true, actual.contains(Event.GOT_INFECTED.name()));
            assertEquals(false, actual.contains(Event.FORGOT_POTION.name()));
            assertEquals(false, actual.contains(Event.SUICIDE.name()));
        };

        smoke.settings().removeWhenWin(true);
        smoke.settings().removeWhenGameOver(true);
        smoke.settings().reloadPlayersWhenGameOverAll(true);
        smoke.settings().increaseLevelAfterReload(false);

        smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        int level = LevelProgress.levelsStartsFrom1;
                        return new TestGameSettings()
                                .clearLevelMaps(level)
                                .setLevelMaps(level,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼♥************☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .integer(COUNT_CONTAGIONS, 10);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }

    @Test
    public void test2() {
        // about 0.37 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы все же проиграли
            assertEquals(false, actual.contains(Event.GOT_INFECTED.name()));
            assertEquals(false, actual.contains(Event.FORGOT_POTION.name()));
            assertEquals(true, actual.contains(Event.SUICIDE.name()));
        };

        smoke.settings().removeWhenWin(true);
        smoke.settings().removeWhenGameOver(true);
        smoke.settings().reloadPlayersWhenGameOverAll(true);
        smoke.settings().increaseLevelAfterReload(true);

        smoke.play(ticks, "SmokeTest2.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        int level = LevelProgress.levelsStartsFrom1;
                        String map = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼******************☼\n" +
                                "☼♥*****************☼\n" +
                                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n";
                        return new TestGameSettings()
                                .clearLevelMaps(level)
                                .setLevelMaps(level, map)
                                .setLevelMaps(level + 1, map)
                                .setLevelMaps(level + 2, map)
                                .integer(COUNT_CONTAGIONS, 40);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }

    @Test
    public void testMultiplayer() {
        // about 0.27 sec
        int ticks = 1000;
        int players = 4;

        smoke.settings().removeWhenWin(false);
        smoke.settings().removeWhenGameOver(false);
        smoke.settings().reloadPlayersWhenGameOverAll(false);
        smoke.settings().increaseLevelAfterReload(false);

        smoke.play(ticks, "SmokeTestMultiplayer.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        int level = LevelProgress.levelsStartsFrom1;
                        return new TestGameSettings()
                                .clearLevelMaps(level)
                                .setLevelMaps(level,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼♥ ********* ♥☼\n" +
                                        "☼  *********  ☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼  *********  ☼\n" +
                                        "☼♥ ********* ♥☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .integer(COUNT_CONTAGIONS, 10);
                    }
                },
                players,
                () -> new AISolver(dice),
                Board::new);
    }
}