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
        // about 5 sec -> 7 sec -> 3 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы ни разу не проиграли и всегда правильно отгадывали, где мины
            assertEquals(false, actual.contains(Event.GOT_INFECTED.name()));
            assertEquals(false, actual.contains(Event.FORGOT_POTION.name()));
        };

        smoke.settings().removeWhenGameOver(true);
        smoke.settings().reloadPlayersWhenGameOverAll(true);
        smoke.settings().increaseLevelAfterReload(true);

        smoke.play(ticks, "SmokeTest.data",
                new GameRunner() {
                    @Override
                    public Dice getDice() {
                        return dice;
                    }

                    @Override
                    public GameSettings getSettings() {
                        int level = LevelProgress.levelsStartsFrom1;
                        return super.getSettings()
                                .clearLevelMaps(level)
                                .setLevelMaps(level,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*o***********☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼**o**********☼\n" +
                                        "☼******o******☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼**o**********☼\n" +
                                        "☼*************☼\n" +
                                        "☼*****o*******☼\n" +
                                        "☼*************☼\n" +
                                        "☼***oo********☼\n" +
                                        "☼******o******☼\n" +
                                        "☼*************☼\n" +
                                        "☼♥************☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 1,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼******o******☼\n" +
                                        "☼*****oo******☼\n" +
                                        "☼*************☼\n" +
                                        "☼o***o********☼\n" +
                                        "☼*************☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*o***********☼\n" +
                                        "☼********o****☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼♥**o*o*******☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 2,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*****o*******☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼o************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼*************☼\n" +
                                        "☼*****o**o****☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼*************☼\n" +
                                        "☼******o******☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼♥************☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 3,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼o*****o******☼\n" +
                                        "☼******o******☼\n" +
                                        "☼******o******☼\n" +
                                        "☼***o*o*******☼\n" +
                                        "☼**o**********☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼♥************☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 4,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼****o********☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*******o*****☼\n" +
                                        "☼o***o********☼\n" +
                                        "☼*************☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼****o********☼\n" +
                                        "☼**o***o******☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼♥**o*********☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 5,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼**o*o********☼\n" +
                                        "☼****o*o******☼\n" +
                                        "☼*****o*******☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼*************☼\n" +
                                        "☼**o**********☼\n" +
                                        "☼***o*********☼\n" +
                                        "☼******o******☼\n" +
                                        "☼***o****o****☼\n" +
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
        // about 50 sec -> 23 sec -> 3.8 sec
        int ticks = 1000;

        SmokeUtils.recheck = actual -> {
            // мы все же проиграли
            assertEquals(false, actual.contains(Event.GOT_INFECTED.name()));
            assertEquals(false, actual.contains(Event.FORGOT_POTION.name()));
            assertEquals(true, actual.contains(Event.SUICIDE.name()));
        };

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
                        return super.getSettings()
                                .clearLevelMaps(level)
                                .setLevelMaps(level,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*******ooo*****ooo☼\n" +
                                        "☼*****o****o*o*****☼\n" +
                                        "☼o**o*****o*****o**☼\n" +
                                        "☼******************☼\n" +
                                        "☼***o****o****o*o**☼\n" +
                                        "☼*********o********☼\n" +
                                        "☼*******o**********☼\n" +
                                        "☼***o*o*o**o***o***☼\n" +
                                        "☼************o**o**☼\n" +
                                        "☼******o***********☼\n" +
                                        "☼o*****************☼\n" +
                                        "☼**o*o*********o***☼\n" +
                                        "☼**o*o*************☼\n" +
                                        "☼**************o***☼\n" +
                                        "☼****o****o********☼\n" +
                                        "☼***o*********o****☼\n" +
                                        "☼********o*********☼\n" +
                                        "☼♥***o********o****☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 1,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                                        "☼*o****************☼\n" +
                                        "☼***o*o************☼\n" +
                                        "☼***o*o************☼\n" +
                                        "☼*****o************☼\n" +
                                        "☼*oo*o*************☼\n" +
                                        "☼*o**ooo***********☼\n" +
                                        "☼o**o**************☼\n" +
                                        "☼oooo*oo***********☼\n" +
                                        "☼ooo***************☼\n" +
                                        "☼o*****oo**********☼\n" +
                                        "☼*o**o*************☼\n" +
                                        "☼****oo************☼\n" +
                                        "☼*o*o**o***********☼\n" +
                                        "☼ooo***************☼\n" +
                                        "☼**oo**************☼\n" +
                                        "☼***o**************☼\n" +
                                        "☼******************☼\n" +
                                        "☼♥*****************☼\n" +
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .setLevelMaps(level + 2,
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
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
                                        "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n")
                                .integer(COUNT_CONTAGIONS, 40);
                    }
                },
                Arrays.asList(new AISolver(dice)),
                Arrays.asList(new Board()));
    }
}