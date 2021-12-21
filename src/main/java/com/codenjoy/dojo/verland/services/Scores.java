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
    private volatile int destroyed;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
        destroyed = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.CURE)) {
            onCure();
        } else if (event.equals(Events.FORGOT_POTION)) {
            onForgotPotion();
        } else if (event.equals(Events.GOT_INFECTED)) {
            onGotInfected();
        } else if (event.equals(Events.NO_MORE_POTIONS)) {
            onNoMorePotions();
        } else if (event.equals(Events.WIN)) {
            onWin();
        } else if (event.equals(Events.SUICIDE)) {
            onSuicide();
        } else if (event.equals(Events.CLEAN_AREA)) {
            onCleanArea();
        }
        score = Math.max(0, score);
    }

    private void onCleanArea() {
        score += settings.integer(CLEAN_AREA_SCORE);
    }

    private void onWin() {
        score += settings.integer(WIN_SCORE);
    }

    private void onSuicide() {
        score -= settings.integer(SUICIDE_PENALTY);
    }

    private void onNoMorePotions() {
        onGotInfected();
    }

    private void onCure() {
        destroyed++;
        score += destroyed;
    }

    private void onForgotPotion() {
        score -= settings.integer(DESTROYED_FORGOT_PENALTY);
        destroyed -= settings.integer(DESTROYED_PENALTY);
        destroyed = Math.max(0, destroyed);
    }

    private void onGotInfected() {
        score -= settings.integer(GOT_INFECTED_PENALTY);
        destroyed = 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.parseInt(score.toString());
    }
}
