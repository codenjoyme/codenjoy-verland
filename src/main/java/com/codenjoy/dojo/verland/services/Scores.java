package com.codenjoy.dojo.verland.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public class Scores extends ScoresMap<Void> {

    public Scores(SettingsReader settings) {
        super(settings);

        put(Event.CURE,
                value -> settings.integer(CURE_SCORE));

        put(Event.FORGOT_POTION,
                value -> heroDie(FORGOT_POTION_PENALTY));

        put(Event.GOT_INFECTED,
                value -> heroDie(GOT_INFECTED_PENALTY));

        put(Event.NO_MORE_POTIONS,
                value -> settings.integer(NO_MORE_POTIONS_PENALTY));

        put(Event.WIN_ROUND,
                value -> settings.integer(WIN_SCORE));

        put(Event.SUICIDE,
                value -> heroDie(SUICIDE_PENALTY));

        put(Event.CLEAN_AREA,
                value -> settings.integer(CLEAN_AREA_SCORE));
    }
}