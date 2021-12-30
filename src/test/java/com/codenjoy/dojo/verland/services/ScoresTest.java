package com.codenjoy.dojo.verland.services;

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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.verland.TestGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void suicide() {
        scores.event(Event.SUICIDE);
    }

    public void cure() {
        scores.event(Event.CURE);
    }

    public void forgotPotion() {
        scores.event(Event.FORGOT_POTION);
    }

    public void gotInfected() {
        scores.event(Event.GOT_INFECTED);
    }

    public void noMorePotions() {
        scores.event(Event.NO_MORE_POTIONS);
    }

    public void cleanArea() {
        scores.event(Event.CLEAN_AREA);
    }

    public void win() {
        scores.event(Event.WIN_ROUND);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        cure();
        cure();
        cure();
        cure();

        forgotPotion();

        noMorePotions();

        gotInfected();

        cleanArea();
        cleanArea();

        win();

        // then
        assertEquals(140
                        + 4 * settings.integer(CURE_SCORE)
                        + settings.integer(FORGOT_POTION_PENALTY)
                        + settings.integer(NO_MORE_POTIONS_PENALTY)
                        + settings.integer(GOT_INFECTED_PENALTY)
                        + 2 * settings.integer(CLEAN_AREA_SCORE)
                        + settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenDead() {
        // given
        givenScores(0);

        // when
        gotInfected();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZero_whenSuicide() {
        // given
        givenScores(0);

        // when
        suicide();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldPenalty_whenSuicide() {
        // given
        givenScores(100);

        // when
        suicide();

        // then
        assertEquals(100
                + settings.integer(SUICIDE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenForgotPotion() {
        // given
        givenScores(100);

        // when
        forgotPotion();

        // then
        assertEquals(100
                + settings.integer(FORGOT_POTION_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenNoMorePotions() {
        // given
        givenScores(0);

        // when
        noMorePotions();

        // then
        assertEquals(0,
                scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZero_whenDead() {
        // given
        givenScores(100);

        // when
        cure();
        gotInfected();

        cure();

        // then
        assertEquals(100
                + 2 * settings.integer(CURE_SCORE)
                + settings.integer(GOT_INFECTED_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCount_whenForgotPotions() {
        // given
        givenScores(100);

        // when
        cure();
        cure();
        cure();
        cure();
        cure();

        forgotPotion();

        cure();
        cure();
        cure();

        // then
        assertEquals(100
                + 5 * settings.integer(CURE_SCORE)
                + settings.integer(NO_MORE_POTIONS_PENALTY)
                + 3 * settings.integer(CURE_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZero_whenManyTimesForgotPotions() {
        // given
        givenScores(100);

        // when
        cure();
        cure();

        forgotPotion();
        forgotPotion();
        forgotPotion();

        cure();

        // then
        assertEquals(100
                + 2 * settings.integer(CURE_SCORE)
                + 3 * settings.integer(FORGOT_POTION_PENALTY)
                + settings.integer(CURE_SCORE),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldScore_whenWin() {
        // given
        givenScores(100);

        // when
        win();

        // then
        assertEquals(100
                + settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldScore_whenCleanArea() {
        // given
        givenScores(100);

        // when
        cleanArea();

        // then
        assertEquals(100
                + settings.integer(CLEAN_AREA_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(100);

        cleanArea();

        // then
        assertEquals(100
                + settings.integer(CLEAN_AREA_SCORE),
                scores.getScore());

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }
}