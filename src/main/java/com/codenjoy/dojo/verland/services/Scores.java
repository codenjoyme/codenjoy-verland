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

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public class Scores implements PlayerScores {

    private volatile int score;
    private GameSettings settings;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        score += scoreFor(settings, event);
        score = Math.max(0, score);
    }

    public static int scoreFor(GameSettings settings, Object input) {
        if (!(input instanceof Events)) {
            return 0;
        }

        Events event = (Events) input;

        if (event.equals(Events.CURE)) {
            return + settings.integer(CURE_SCORE);
        }

        if (event.equals(Events.FORGOT_POTION)) {
            return - settings.integer(FORGOT_POTION_PENALTY);
        }

        if (event.equals(Events.GOT_INFECTED)) {
            return - settings.integer(GOT_INFECTED_PENALTY);
        }

        if (event.equals(Events.NO_MORE_POTIONS)) {
            return - settings.integer(GOT_INFECTED_PENALTY);
        }

        if (event.equals(Events.WIN)) {
            return + settings.integer(WIN_SCORE);
        }

        if (event.equals(Events.SUICIDE)) {
            return - settings.integer(SUICIDE_PENALTY);
        }

        if (event.equals(Events.CLEAN_AREA)) {
            return + settings.integer(CLEAN_AREA_SCORE);
        }

        return 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.parseInt(score.toString());
    }
}
