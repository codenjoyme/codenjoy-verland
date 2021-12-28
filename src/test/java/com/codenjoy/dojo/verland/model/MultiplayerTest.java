package com.codenjoy.dojo.verland.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.Direction;
import org.junit.Test;

public class MultiplayerTest extends AbstractGameTest {

    @Test
    public void severalHeroesCanAppearOnTheMap_eachHeroAppearsOnHisOwnSpot () {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼o**♥☼\n" +
                "☼☼☼☼☼☼\n");

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♥☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 2);
    }

    @Test
    public void severalHeroesCanAppearOnTheMap_ifThereAreNotEnoughSpots_heroesAppearAlongWithOthers() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼o***☼\n" +
                "☼☼☼☼☼☼\n");

        // генерируем еще 2 героя
        level().heroesSpots().forEach(this::givenPlayer);

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 3);
    }

    @Test
    public void heroesСanMoveIndependently() {
        // given
        severalHeroesCanAppearOnTheMap_ifThereAreNotEnoughSpots_heroesAppearAlongWithOthers();

        // when
        hero(0).down();
        hero(1).down();
        hero(2).right();
        hero(3).left();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [CLEAN_AREA]\n" +
                "listener(1) => [CLEAN_AREA]\n" +
                "listener(2) => [CLEAN_AREA]\n" +
                "listener(3) => [CLEAN_AREA]\n");

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♠ ☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♠ ☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♥♠ ☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♥ ☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 3);
    }

    @Test
    public void heroesCanCureContagionsIndependently() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼o**o☼\n" +
                "☼****☼\n" +
                "☼o***☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(0).cure(Direction.DOWN);
        hero(1).cure(Direction.DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼!**!☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼!**!☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 1);

        verifyAllEvents(
                "listener(0) => [CURE]\n" +
                "listener(1) => [CURE]\n");
    }

    @Test
    public void allHeroesWhoRemainOnTheFieldWinTheGame() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼o***☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(0).cure(Direction.DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥  ♠☼\n" +
                "☼x   ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠  ♥☼\n" +
                "☼x   ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        verifyAllEvents(
                "listener(0) => [CURE, WIN]\n" +
                "listener(1) => [WIN]\n");
    }
}