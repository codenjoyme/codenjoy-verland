package com.codenjoy.dojo.verland.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
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


import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.settings.AllSettings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public class GameSettings extends SettingsImpl implements AllSettings<GameSettings> {

    public enum Keys implements Key {

        COUNT_CONTAGIONS("[Score] Count contagions"),
        POTIONS_COUNT("[Score] Potions count"),

        CURE_SCORE("[Score] Cure score"),
        CLEAN_AREA_SCORE("[Score] Clear area score"),
        WIN_SCORE("[Score] Win score"),

        GOT_INFECTED_PENALTY("[Game] Got infected penalty"),
        SUICIDE_PENALTY("[Game] Suicide penalty"),
        NO_MORE_POTIONS_PENALTY("[Score] No more potions penalty"),
        FORGOT_POTION_PENALTY("[Score] Forgot potion penalty"),
        SCORE_COUNTING_TYPE(ScoresImpl.SCORE_COUNTING_TYPE.key());

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        initAll();

        integer(COUNT_CONTAGIONS, 30);
        integer(POTIONS_COUNT, 100);

        integer(CURE_SCORE, 10);
        integer(CLEAN_AREA_SCORE, 1);
        integer(WIN_SCORE, 300);

        integer(GOT_INFECTED_PENALTY, -15);
        integer(SUICIDE_PENALTY, -100);
        integer(NO_MORE_POTIONS_PENALTY, -2);
        integer(FORGOT_POTION_PENALTY, -2);

        Levels.setup(this);
    }

    public Calculator<Void> calculator() {
        return new Calculator<>(new Scores(this));
    }
}