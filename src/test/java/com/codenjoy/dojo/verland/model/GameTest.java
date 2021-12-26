package com.codenjoy.dojo.verland.model;

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


import org.junit.Test;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;

public class GameTest extends AbstractGameTest {

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveRight();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveDown();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼**♥☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveLeft();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼*♥ ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveLeft() {
        hero().left();
        tick();
    }

    private void moveDown() {
        hero().down();
        tick();
    }

    private void moveRight() {
        hero().right();
        tick();
    }

    private void suicide() {
        hero().act(0);
        tick();
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveDown();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveRight();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*1♥☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveUp();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        // when
        moveUp();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼*1 ☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardUp() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertF("☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼♥ *☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertF("☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardLeft() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetRight() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetUp() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetDown() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        cureDown();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetLeft() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldDie_whenHeroAtContagion() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertDie();
    }

    private void assertDie() {
        assertEquals(true, hero().isGameOver());
        assertEquals(true, player().shouldLeave());
        assertEquals(false, hero().isAlive());
    }

    @Test
    public void shouldSaveCommandAndActAfterTick() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        hero().right();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        tick();

        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertDie();
    }

    @Test
    public void shouldPrintAllContagions_whenHeroAtContagion() {
        givenFl("☼☼☼☼☼\n" +
                "☼*oo☼\n" +
                "☼*♥o☼\n" +
                "☼*oo☼\n" +
                "☼☼☼☼☼\n");

        cureUp();
        cureDown();
        cureLeft();
        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼ xo☼\n" +
                "☼!3X☼\n" +
                "☼ xo☼\n" +
                "☼☼☼☼☼\n");

        assertDie();
    }

    @Test
    public void shouldPrintBoard_whenNearHeroNoContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼***☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroOneContagion() {
        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroTwoContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroThreeContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥o☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFourContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥o☼\n" +
                "☼*oo☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFiveContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼*oo☼\n" +
                "☼*♥o☼\n" +
                "☼*oo☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

    }

    @Test
    public void shouldPrintBoard_whenNearHeroSixContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼*♥o☼\n" +
                "☼*oo☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroSevenContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼*♥o☼\n" +
                "☼ooo☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroEightContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o♥o☼\n" +
                "☼ooo☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertF("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o8o☼\n" +
                "☼oXo☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenContagionAtRight() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCure_whenContagionAtRightAndLeft() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureRight();
        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtRight() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtDown() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        cureDown();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥ ☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtDown() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtUp() {
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼ ♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtUp() {
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureDown();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtLeft() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    private void assertWin() {
        assertEquals(true, hero().isWin());
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtLeft() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldWin_whenDestroyAllContagions() {
        settings().integer(POTIONS_COUNT, 8);

        givenFl("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o♥o☼\n" +
                "☼ooo☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        cureDown();
        cureRight();
        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveUp();
        cureLeft();
        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼!♥!☼\n" +
                "☼!2!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveDown();
        moveDown();

        assertF("☼☼☼☼☼\n" +
                "☼!!!☼\n" +
                "☼!2!☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼xxx☼\n" +
                "☼x x☼\n" +
                "☼x♥x☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldLeaveContagionOnMap_whenWalkBetweenContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*o*☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼1♥*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼11♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼**♥☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼**2☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveUp() {
        hero().up();
        tick();
    }

    private void assertStillNotWin() {
        assertEquals(false, hero().isWin());
    }

    private void cureUp() {
        hero().act();
        moveUp();
    }

    private void cureRight() {
        hero().act();
        moveRight();
    }

    private void cureDown() {
        hero().act();
        moveDown();
    }

    private void cureLeft() {
        hero().act();
        moveLeft();
    }

    @Test
    public void shouldFireEvent_whenDie() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[GOT_INFECTED]");
    }

    @Test
    public void shouldFireEvent_whenSuicide() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        suicide();

        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ ♥o☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[SUICIDE]");

        assertDie();

        field().newGame(player());
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldFireEvent_whenOpenSpace() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldNotFireEvent_whenReturnsHome() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥ ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldFireEvent_whenNoMoreCharge() {
        settings().integer(POTIONS_COUNT, 3);

        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        cureDown();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼!♥!☼\n" +
                "☼o! ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION, FORGOT_POTION, FORGOT_POTION, NO_MORE_POTIONS]");

        cureUp();

        verifyAllEvents("");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case1() {
        settings().integer(POTIONS_COUNT, 4);

        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼o♥o☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        cureRight();
        cureLeft();
        cureDown();
        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼x♥x☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case2() {
        settings().integer(POTIONS_COUNT, 4);

        givenFl("☼☼☼☼☼\n" +
                "☼o*o☼\n" +
                "☼*♥*☼\n" +
                "☼o*o☼\n" +
                "☼☼☼☼☼\n");

        cureRight();
        cureLeft();
        cureDown();
        cureUp();

        assertF("☼☼☼☼☼\n" +
                "☼o!o☼\n" +
                "☼!♥!☼\n" +
                "☼o!o☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case3() {
        settings().integer(POTIONS_COUNT, 2);

        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥*☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        cureRight();
        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼ 1o☼\n" +
                "☼!♥!☼\n" +
                "☼ 1o☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case4() {
        settings().integer(POTIONS_COUNT, 2);

        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥*☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        cureDown();

        assertF("☼☼☼☼☼\n" +
                "☼ 1o☼\n" +
                "☼!♥2☼\n" +
                "☼ !o☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldFireEvent_whenCureContagion() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE]");
    }

    @Test
    public void shouldFireEvent_whenCleanAllContagions() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN]");
    }

    @Test
    public void shouldOnlyOneFlagPerSpace() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        cureRight();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        cureRight();
    }

    @Test
    public void shouldCantGoOnBoard() {
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        moveRight();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        moveUp();

        assertF("☼☼☼☼☼\n" +
                "☼  ♥☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCantCureOnBoard() {
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        verifyAllEvents("");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        cureDown();
        verifyAllEvents("");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();

        cureRight();
        verifyAllEvents("");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();

        cureUp();
        verifyAllEvents("");

        assertF("☼☼☼☼☼\n" +
                "☼  ♥☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
}
