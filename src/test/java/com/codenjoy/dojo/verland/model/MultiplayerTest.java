package com.codenjoy.dojo.verland.model;

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

import org.junit.Test;

import static com.codenjoy.dojo.services.Direction.*;

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
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♥☼\n" +
                "☼☼☼☼☼☼\n", 2);
    }

    @Test
    public void severalHeroesCanAppearOnTheMap_ifThereAreNotEnoughSpots_heroesAppearAlongWithOthers() {
        // given
        dice(0); // прогоняем ramdomize для shuffle спотов
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼o***☼\n" +
                "☼☼☼☼☼☼\n");

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

        // генерируем еще 2 героя
        level().heroesSpots().forEach(this::givenPlayer);

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

        assertScores("");
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

        assertScores(
                "hero(0)=1\n" +
                "hero(1)=1\n" +
                "hero(2)=1\n" +
                "hero(3)=1");
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
        hero(0).cure(DOWN);
        hero(1).cure(DOWN);
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

        assertScores(
                "hero(0)=10\n" +
                "hero(1)=10");
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
        hero(0).cure(DOWN);
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
                "listener(0) => [CURE, WIN_ROUND]\n" +
                "listener(1) => [WIN_ROUND]\n");

        assertWin(0);
        assertWin(1);

        assertScores(
                "hero(0)=40\n" +
                "hero(1)=30");
    }

    @Test
    public void shouldPointsAreGainedByBothPlayers_whenCompetitionForAHealedCell() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥o♥*☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(0).cure(RIGHT);
        hero(1).cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥x♠ ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [CURE, WIN_ROUND]\n" +
                "listener(1) => [CURE, WIN_ROUND]\n");

        assertWin(0);
        assertWin(1);
        
        assertScores(
                "hero(0)=40\n" +
                "hero(1)=40");
    }

    @Test
    public void shouldPointsAreGainedByBothPlayers_evenIfOnePlayerHasMorePointsThanOthers() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥o*♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(1).left();
        tick();

        hero(0).cure(RIGHT);
        hero(1).cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥x♠ ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [CURE, WIN_ROUND]\n" +
                "listener(1) => [CLEAN_AREA, CURE, WIN_ROUND]\n");

        assertWin(0);
        assertWin(1);

        assertScores(
                "hero(0)=40\n" +
                "hero(1)=41");
    }

    @Test
    public void shouldCureCell_whenThereIsAnotherHeroOnIt() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼♥♥*☼\n" +
                "☼***☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        assertPotions(
                "hero(0)=3\n" +
                "hero(1)=3");

        // when
        hero(0).cure(RIGHT);
        hero(1).cure(LEFT);
        tick();

        hero(0).down();
        hero(1).down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼!!*☼\n" +
                "☼♥♠*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [FORGOT_POTION, CLEAN_AREA]\n" +
                "listener(1) => [FORGOT_POTION, CLEAN_AREA]\n");

        assertPotions(
                "hero(0)=2\n" +
                "hero(1)=2");
    }

    @Test
    public void shouldGoForward_whenThereIsAnotherHeroOnIt_caseMovingInTurn() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼♥♥*☼\n" +
                "☼***☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ ♥*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼\n" +
                "☼ ♥*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 1);

        verifyAllEvents("");

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼♠♥*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼\n" +
                "☼♥♠*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 1);

        verifyAllEvents("");
    }

    @Test
    public void shouldGoForward_whenThereIsAnotherHeroOnIt_simultaneousMovement() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼♥♥*☼\n" +
                "☼***☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼♠♥*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼\n" +
                "☼♥♠*☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n", 1);

        verifyAllEvents("");
    }
}