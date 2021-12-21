package com.codenjoy.dojo.verland.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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


import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public final class GameSettings extends SettingsImpl
        implements SettingsReader<GameSettings> {

    public enum Keys implements Key {

        WIN_SCORE("[Score] Win score"),
        BOARD_SIZE("[Map] Board size"),
        SUICIDE_PENALTY("[Game] Suicide penalty"),
        GOT_INFECTED_PENALTY("[Game] Got infected penalty"),
        DESTROYED_PENALTY("[Score] Forgot penalty"),
        DESTROYED_FORGOT_PENALTY("[Score] Destroyed forgot penalty"),
        CLEAN_AREA_SCORE("[Score] Clear area score"),
        COUNT_CONTAGIONS("[Score] Count contagions"),
        POTIONS_COUNT("[Score] Potions count");

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
        integer(WIN_SCORE, 300);
        integer(GOT_INFECTED_PENALTY, 15);
        integer(SUICIDE_PENALTY, 100);
        integer(DESTROYED_PENALTY, 5);
        integer(DESTROYED_FORGOT_PENALTY, 2);
        integer(CLEAN_AREA_SCORE, 1);

        integer(BOARD_SIZE, 15);
        integer(COUNT_CONTAGIONS, 30);
        integer(POTIONS_COUNT, 100);
    }

}
