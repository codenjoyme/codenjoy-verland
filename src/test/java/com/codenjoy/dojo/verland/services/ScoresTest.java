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
        scores = new Scores(140, settings);

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

        assertEquals(140
                        + 1 + 2 + 3 + 4
                        - settings.integer(DESTROYED_FORGOT_PENALTY)
                        - 2 * settings.integer(GOT_INFECTED_PENALTY)
                        + 2 * settings.integer(CLEAN_AREA_SCORE)
                        + settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenDead() {
        scores = new Scores(0, settings);

        gotInfected();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZero_whenSuicide() {
        scores = new Scores(0, settings);

        suicide();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldPenalty_whenSuicide() {
        scores = new Scores(110, settings);

        suicide();

        assertEquals(110
                        - settings.integer(SUICIDE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZero_whenForgotPotion() {
        scores = new Scores(0, settings);

        forgotPotion();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZero_whenNoMorePotions() {
        scores = new Scores(0, settings);

        noMorePotions();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldDestroyMinesCountStartsFromZero_whenDead() {
        scores = new Scores(0, settings);

        cure();
        gotInfected();

        cure();

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldDecreaseMinesCount_whenForgotPotions() {
        settings.integer(DESTROYED_PENALTY, 2);

        scores = new Scores(0, settings);

        cure();
        cure();
        cure();
        cure();
        cure();

        forgotPotion();

        cure();
        cure();
        cure();

        assertEquals(1 + 2 + 3 + 4 + 5
                - settings.integer(DESTROYED_FORGOT_PENALTY)
                + 4 + 5 + 6, scores.getScore());
    }

    @Test
    public void shouldMinesCountIsZero_whenManyTimesForgotPotions() {
        scores = new Scores(0, settings);

        cure();
        cure();

        forgotPotion();
        forgotPotion();
        forgotPotion();

        cure();

        assertEquals(1, scores.getScore());
    }

    @Test
    public void shouldScore_whenWin() {
        scores = new Scores(0, settings);

        win();

        assertEquals(settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldScore_whenCleanArea() {
        scores = new Scores(0, settings);

        cleanArea();

        assertEquals(settings.integer(CLEAN_AREA_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        scores = new Scores(0, settings);

        cleanArea();

        scores.clear();

        assertEquals(0, scores.getScore());
    }
}