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
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void suicide() {
        scores.event(Events.SUICIDE);
    }

    public void cure() {
        scores.event(Events.CURE);
    }

    public void forgotPotion() {
        scores.event(Events.FORGOT_POTION);
    }

    public void gotInfected() {
        scores.event(Events.GOT_INFECTED);
    }

    public void noMorePotions() {
        scores.event(Events.NO_MORE_POTIONS);
    }

    public void cleanArea() {
        scores.event(Events.CLEAN_AREA);
    }

    public void win() {
        scores.event(Events.WIN);
    }

    @Before
    public void setup() {
        settings = new GameSettings();
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = new Scores(140, settings);

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
                        - settings.integer(NO_MORE_POTIONS_PENALTY)
                        - 2 * settings.integer(GOT_INFECTED_PENALTY)
                        + 2 * settings.integer(CLEAN_AREA_SCORE)
                        + settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenDead() {
        // given
        scores = new Scores(0, settings);

        // when
        gotInfected();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZero_whenSuicide() {
        // given
        scores = new Scores(0, settings);

        // when
        suicide();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldPenalty_whenSuicide() {
        // given
        scores = new Scores(100, settings);

        // when
        suicide();

        // then
        assertEquals(100
                - settings.integer(SUICIDE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenForgotPotion() {
        // given
        scores = new Scores(100, settings);

        // when
        forgotPotion();

        // then
        assertEquals(100
                - settings.integer(FORGOT_POTION_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenNoMorePotions() {
        // given
        scores = new Scores(0, settings);

        // when
        noMorePotions();

        // then
        assertEquals(0,
                scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZero_whenDead() {
        // given
        scores = new Scores(100, settings);

        // when
        cure();
        gotInfected();

        cure();

        // then
        assertEquals(100
                + 2 * settings.integer(CURE_SCORE)
                - settings.integer(GOT_INFECTED_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCount_whenForgotPotions() {
        // given
        scores = new Scores(100, settings);

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
                - settings.integer(NO_MORE_POTIONS_PENALTY)
                + 3 * settings.integer(CURE_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZero_whenManyTimesForgotPotions() {
        // given
        scores = new Scores(100, settings);

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
                - 3 * settings.integer(FORGOT_POTION_PENALTY)
                + settings.integer(CURE_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldScore_whenWin() {
        // given
        scores = new Scores(100, settings);

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
        scores = new Scores(100, settings);

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
        scores = new Scores(100, settings);

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