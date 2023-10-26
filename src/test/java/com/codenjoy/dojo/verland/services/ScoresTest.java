package com.codenjoy.dojo.verland.services;

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


import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import com.codenjoy.dojo.verland.TestGameSettings;
import org.junit.Test;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 1)
                .integer(CURE_SCORE, 2)
                .integer(CLEAN_AREA_SCORE, 3)
                .integer(FORGOT_POTION_PENALTY, -4)
                .integer(NO_MORE_POTIONS_PENALTY, -5)
                .integer(GOT_INFECTED_PENALTY, -6)
                .integer(SUICIDE_PENALTY, -7);
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "CURE > +2 = 102\n" +
                "CURE > +2 = 104\n" +
                "CURE > +2 = 106\n" +
                "CURE > +2 = 108\n" +
                "FORGOT_POTION > -4 = 104\n" +
                "NO_MORE_POTIONS > -5 = 99\n" +
                "GOT_INFECTED > -6 = 93\n" +
                "CLEAN_AREA > +3 = 96\n" +
                "CLEAN_AREA > +3 = 99\n" +
                "WIN_ROUND > +1 = 100");
    }

    @Test
    public void shouldStillZero_whenDead() {
        assertEvents("12:\n" +
                "GOT_INFECTED > -6 = 6\n" +
                "GOT_INFECTED > -6 = 0\n" +
                "GOT_INFECTED > +0 = 0");
    }

    @Test
    public void shouldStillZero_whenSuicide() {
        assertEvents("14:\n" +
                "SUICIDE > -7 = 7\n" +
                "SUICIDE > -7 = 0\n" +
                "SUICIDE > +0 = 0");
    }

    @Test
    public void shouldPenalty_whenSuicide() {
        assertEvents("100:\n" +
                "SUICIDE > -7 = 93");
    }

    @Test
    public void shouldStillZero_whenForgotPotion() {
        assertEvents("100:\n" +
                "FORGOT_POTION > -4 = 96");
    }

    @Test
    public void shouldStillZero_whenNoMorePotions() {
        assertEvents("10:\n" +
                "NO_MORE_POTIONS > -5 = 5\n" +
                "NO_MORE_POTIONS > -5 = 0\n" +
                "NO_MORE_POTIONS > +0 = 0");
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZero_whenDead() {
        assertEvents("100:\n" +
                "CURE > +2 = 102\n" +
                "GOT_INFECTED > -6 = 96\n" +
                "CURE > +2 = 98");
    }

    @Test
    public void shouldDecreaseMinesCount_whenForgotPotions() {
        assertEvents("100:\n" +
                "CURE > +2 = 102\n" +
                "CURE > +2 = 104\n" +
                "CURE > +2 = 106\n" +
                "CURE > +2 = 108\n" +
                "CURE > +2 = 110\n" +
                "FORGOT_POTION > -4 = 106\n" +
                "CURE > +2 = 108\n" +
                "CURE > +2 = 110\n" +
                "CURE > +2 = 112");
    }

    @Test
    public void shouldMinesCountIsZero_whenManyTimesForgotPotions() {
        assertEvents("100:\n" +
                "CURE > +2 = 102\n" +
                "CURE > +2 = 104\n" +
                "FORGOT_POTION > -4 = 100\n" +
                "FORGOT_POTION > -4 = 96\n" +
                "FORGOT_POTION > -4 = 92\n" +
                "CURE > +2 = 94");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "WIN_ROUND > +1 = 101\n" +
                "WIN_ROUND > +1 = 102");

    }

    @Test
    public void shouldCollectScores_whenCleanArea() {
        // given
        settings.integer(CLEAN_AREA_SCORE, 3);

        // when then
        assertEvents("100:\n" +
                "CLEAN_AREA > +3 = 103\n" +
                "CLEAN_AREA > +3 = 106");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("100:\n" +
                "CLEAN_AREA > +3 = 103\n" +
                "(CLEAN) > -103 = 0\n" +
                "CLEAN_AREA > +3 = 3");
    }
}