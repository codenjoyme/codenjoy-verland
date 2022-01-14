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


import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.verland.model.items.Contagion;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;
import static java.util.stream.Collectors.toList;

public class GameTest extends AbstractGameTest {

    @Test
    public void shouldBoardSizeSpecify_whenGameStart() {
        // given when
        givenFl("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼*******o☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼♥*******☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼♥*******☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");
    }

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
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼**♥☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼*♥ ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
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
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*1♥☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼*1 ☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼♥ *☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldCure_whenSetRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("[FORGOT_POTION]");
    }

    @Test
    public void shouldCure_whenSetUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("[FORGOT_POTION]");
    }

    @Test
    public void shouldCure_whenSetDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("[FORGOT_POTION]");
    }

    @Test
    public void shouldCure_whenSetLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("[FORGOT_POTION]");
    }

    @Test
    public void shouldDie_whenHeroAtContagion() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertContagions(1);

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertContagions(1);

        verifyAllEvents("[GOT_INFECTED]");

        assertDie();
    }

    @Test
    public void shouldSaveCommandAndActAfterTick() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[GOT_INFECTED]");

        assertDie();
    }

    @Test
    public void shouldPrintAllContagions_whenHeroAtContagion() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼*oo☼\n" +
                "☼*♥o☼\n" +
                "☼*oo☼\n" +
                "☼☼☼☼☼\n");

        assertContagions(5);

        // when
        hero().cure(UP);
        tick();

        // then
        verifyAllEvents("[CURE]");

        assertContagions(4);

        // when
        hero().cure(DOWN);
        tick();

        // then
        verifyAllEvents("[CURE]");

        assertContagions(3);

        // when
        hero().cure(LEFT);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        assertContagions(3);

        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ xo☼\n" +
                "☼!3X☼\n" +
                "☼ xo☼\n" +
                "☼☼☼☼☼\n");

        assertContagions(3);

        verifyAllEvents("[GOT_INFECTED]");

        assertDie();
    }

    @Test
    public void shouldPrintBoard_whenNearHeroNoContagions() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼***☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroOneContagion() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroTwoContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroThreeContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFourContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // when
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFiveContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroSixContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroSevenContagions() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroEightContagions() {
        // given
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

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o8o☼\n" +
                "☼oXo☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[GOT_INFECTED]");

        assertDie();
    }

    @Test
    public void shouldCure_whenContagionAtRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldCure_whenContagionAtRightAndLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(RIGHT);
        tick();

        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        assertAlive();
    }

    @Test
    public void shouldCure_whenContagionAtDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥ ☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        assertAlive();
    }

    @Test
    public void shouldCure_whenContagionAtUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼ ♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        assertAlive();
    }
    
    @Test
    public void shouldCure_whenContagionAtLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        assertAlive();
    }

    @Test
    public void shouldWin_whenDestroyAllContagions() {
        // given
        settings().integer(POTIONS_COUNT, 8);

        givenFl("☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o♥o☼\n" +
                "☼ooo☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=8");

        // when
        hero().cure(LEFT);
        tick();

        hero().cure(DOWN);
        tick();

        hero().cure(RIGHT);
        tick();

        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, CURE, CURE, CURE]");

        assertAlive();

        // when
        hero().up();
        tick();

        hero().cure(LEFT);
        tick();

        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼!♥!☼\n" +
                "☼!2!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA, CURE, CURE]");

        assertAlive();

        // when
        hero().down();
        tick();

        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼!!!☼\n" +
                "☼!2!☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().cure(LEFT);
        tick();

        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼xxx☼\n" +
                "☼x x☼\n" +
                "☼x♥x☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, CURE, WIN_ROUND]");

        assertWin();
    }

    @Test
    public void shouldLeaveContagionOnMap_whenWalkBetweenContagions() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼*o*☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼1♥*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼11♥☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼**♥☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼**2☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldFireEvent_whenDie() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[GOT_INFECTED]");

        assertDie();
    }

    @Test
    public void shouldFireEvent_whenSuicide() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().suicide();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ Xo☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[SUICIDE]");

        assertDie();

        // when
        field().newGame(player());
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldFireEvent_whenOpenSpace() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldNotFireEvent_whenReturnsHome() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥ ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldFireEvent_whenNoMoreCharge() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼o**☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        // when
        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION]");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION, NO_MORE_POTIONS]");

        assertAlive();
    }

    @Test
    public void shouldFireEventAgain_whenNoMoreCharge() {
        // given
        shouldFireEvent_whenNoMoreCharge();

        // when
        hero().cure(UP);
        tick();

        // then
        verifyAllEvents("[NO_MORE_POTIONS]");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(UP);
        tick();

        // then
        verifyAllEvents("[NO_MORE_POTIONS]");

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldWin_whenNoMoreCharge_andNoMoreContagions() {
        // given
        settings().integer(POTIONS_COUNT, 4);

        givenFl("☼☼☼☼☼\n" +
                "☼*o*☼\n" +
                "☼o♥o☼\n" +
                "☼*o*☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=4");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        verifyAllEvents("[CURE]");

        hero().cure(LEFT);
        tick();

        // then
        verifyAllEvents("[CURE]");

        hero().cure(DOWN);
        tick();

        // then
        verifyAllEvents("[CURE]");

        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼x♥x☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");
    }

    @Test
    public void shouldStillAlive_whenNoMoreCharge_caseFourPotions() {
        // given
        settings().integer(POTIONS_COUNT, 4);

        givenFl("☼☼☼☼☼\n" +
                "☼o*o☼\n" +
                "☼*♥*☼\n" +
                "☼o*o☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=4");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        hero().cure(LEFT);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        hero().cure(DOWN);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        hero().cure(UP);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION, NO_MORE_POTIONS]");

        assertAlive();

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼!4!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldStillAlive_whenNoMoreCharge_caseTwoPotions() {
        // given
        settings().integer(POTIONS_COUNT, 2);

        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥*☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION, NO_MORE_POTIONS]");

        assertAlive();

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼!2!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldStillAlive_whenNoMoreCharge_caseTwoPotions_anotherDirections() {
        // given
        settings().integer(POTIONS_COUNT, 2);

        givenFl("☼☼☼☼☼\n" +
                "☼**o☼\n" +
                "☼*♥*☼\n" +
                "☼**o☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        // when
        hero().cure(LEFT);
        tick();

        // then
        verifyAllEvents("[FORGOT_POTION]");

        hero().cure(DOWN);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[FORGOT_POTION, NO_MORE_POTIONS]");

        assertAlive();

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼!2*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CLEAN_AREA]");
    }

    @Test
    public void shouldFireEvent_whenCureContagion() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥o☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE]");
    }

    @Test
    public void shouldFireEvent_whenCleanAllContagions() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(LEFT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[CURE, WIN_ROUND]");
    }

    @Test
    public void shouldOnlyOneFlagPerCell() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼o♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=3");

        // when
        hero().cure(RIGHT);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("[FORGOT_POTION]");

        // when
        hero().cure(RIGHT);
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertPotions("hero(0)=2");

        verifyAllEvents("");
    }

    @Test
    public void shouldCantGo_whenGoToBorder() {
        // given
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

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        hero().right();
        tick();

        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ♥☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().up();
        tick();

        hero().up();
        tick();

        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼  ♥☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // TODO это случается потому, что поле пустое и нет на нем инфекций
        verifyAllEvents("[WIN_ROUND, WIN_ROUND, WIN_ROUND, WIN_ROUND, WIN_ROUND, WIN_ROUND, WIN_ROUND, WIN_ROUND]");
    }

    @Test
    public void shouldCantCure_whenCureBorder() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ o ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼111☼\n" +
                "☼1*1☼\n" +
                "☼♥11☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(LEFT);
        tick();

        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼111☼\n" +
                "☼1*1☼\n" +
                "☼♥11☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().cure(DOWN);
        tick();

        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼111☼\n" +
                "☼1*1☼\n" +
                "☼♥11☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        hero().right();
        tick();

        hero().cure(RIGHT);
        tick();

        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼111☼\n" +
                "☼1*1☼\n" +
                "☼11♥☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().up();
        tick();

        hero().up();
        tick();

        hero().cure(UP);
        tick();

        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼11♥☼\n" +
                "☼1*1☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldWin_whenBoardWithoutContagions() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼ ♥ ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[WIN_ROUND]");

        assertWin();

        // это сделается в ответ на shouldLeave
        // when
        game().newGame();

        // добавляем заразу чтобы не было снова геймовера
        field().contagions().add(new Contagion(pt(3, 3)));

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 11☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("");

        assertEquals(false, hero().isWin());
        assertEquals(false, hero().isGameOver());
        assertEquals(true, hero().isAlive());
        assertEquals(true, hero().isActive());
        // true тут просто потому, что тесты не пересоздают player как это делает Deals
        assertEquals(true, player().shouldLeave());
    }

    @Test
    public void shouldIncreaseNumberOfPotions_whenItLessThanNumberOfContagions() {
        // given
        settings().integer(POTIONS_COUNT, 2);
        settings().integer(COUNT_CONTAGIONS, despiteLevel(100));

        // when
        // всю заразу за пределы поля,
        // а значит ни одной не будет
        dice(-1);
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertEquals(8, settings().integer(POTIONS_COUNT));
        // в тесте ни одной инфекции не получилось поставить на поле
        assertEquals(0, settings().integer(COUNT_CONTAGIONS));
        assertEquals(0, field().contagions().size());

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldGenerateContagions_onlyInHiddenOrCleanCells_noPlaceForContagions() {
        // given
        settings().integer(COUNT_CONTAGIONS, despiteLevel(100));

        // when
        givenFl("☼☼☼☼☼\n" +
                "☼☼☼☼☼\n" +
                "☼☼☼☼☼\n" +
                "☼♥☼☼☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertEquals(0, settings().integer(COUNT_CONTAGIONS));
        assertEquals(0, field().contagions().size());

        assertF("☼☼☼☼☼\n" +
                "☼☼☼☼☼\n" +
                "☼☼☼☼☼\n" +
                "☼♥☼☼☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldGenerateContagions_onlyInHiddenOrCleanCells_onlyOneCellForContagion_generatedUnderFog() {
        // given
        settings().integer(COUNT_CONTAGIONS, despiteLevel(100));

        // when
        // все будет генерироваться в одной клетке,
        // но реально только 1 инфекция там будет, остальные пропустим
        // так как там свободно
        givenFl("☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertEquals(1, settings().integer(COUNT_CONTAGIONS));
        assertEquals(1, field().contagions().size());
        assertEquals("[[3,3]]",
                sorted(field().contagions()));

        assertF("☼☼☼☼☼\n" +
                "☼ 1*☼\n" +
                "☼ 11☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldGenerateContagions_onlyInHiddenCells_fillEverywhere() {
        // given
        settings().integer(COUNT_CONTAGIONS, despiteLevel(100));

        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ *o☼\n" +
                "☼ **☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertEquals(4, settings().integer(COUNT_CONTAGIONS));
        assertEquals(4, field().contagions().size());
        assertEquals("[[2,2], [2,3], [3,2], [3,3]]",
                sorted(field().contagions()));

        assertF("☼☼☼☼☼\n" +
                "☼2**☼\n" +
                "☼2**☼\n" +
                "☼♥22☼\n" +
                "☼☼☼☼☼\n");
    }

    private String sorted(Object list) {
        return ((list instanceof Accessor)
                    ? ((Accessor)list).stream()
                    : ((List)list).stream())
                .sorted()
                .collect(toList())
                .toString();
    }

    @Test
    public void shouldGenerateContagions_onlyInHiddenCells_onlyOneCellForContagion_generatedOnFreeCell() {
        // given
        settings().integer(COUNT_CONTAGIONS, despiteLevel(1));

        // when
        dice(7); // рендомный индекс в списке свободных ячеек
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertEquals(1, settings().integer(COUNT_CONTAGIONS));
        assertEquals(1, field().contagions().size());
        assertEquals("[[3,3]]",
                sorted(field().contagions()));

        assertF("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void performanceTest_freeForContagions() {
        // about (1_000_000 / 11.8 sec)
        // about (100_000 / 2.8 sec)

        // given
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼   *****ooo  ☼\n" +
                "☼   *****ooo  ☼\n" +
                "☼   *****ooooo☼\n" +
                "☼   *****ooooo☼\n" +
                "☼   *****ooooo☼\n" +
                "☼   **********☼\n" +
                "☼!  **********☼\n" +
                "☼!  **********☼\n" +
                "☼!  **********☼\n" +
                "☼♥  **********☼\n" +
                "☼♥            ☼\n" +
                "☼♥            ☼\n" +
                "☼♥♥♥♥!!!      ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        // when
        int ticks = 100_000;

        for (int count = 0; count < ticks; count++) {
            field().freeForContagions();
        }

        // then
        assertEquals("[[4,4], [4,5], [4,6], [4,7], [4,8], [4,9], " +
                        "[4,10], [4,11], [4,12], [4,13], " +
                        "[5,4], [5,5], [5,6], [5,7], [5,8], " +
                        "[5,9], [5,10], [5,11], [5,12], [5,13], " +
                        "[6,4], [6,5], [6,6], [6,7], [6,8], " +
                        "[6,9], [6,10], [6,11], [6,12], [6,13], " +
                        "[7,4], [7,5], [7,6], [7,7], [7,8], " +
                        "[7,9], [7,10], [7,11], [7,12], [7,13], " +
                        "[8,4], [8,5], [8,6], [8,7], [8,8], " +
                        "[8,9], [8,10], [8,11], [8,12], [8,13], " +
                        "[9,4], [9,5], [9,6], [9,7], [9,8], " +
                        "[10,4], [10,5], [10,6], [10,7], [10,8], " +
                        "[11,4], [11,5], [11,6], [11,7], [11,8], " +
                        "[12,4], [12,5], [12,6], [12,7], [12,8], " +
                        "[13,4], [13,5], [13,6], [13,7], [13,8]]",
                sorted(field().freeForContagions()));
    }

    @Test
    public void shouldGenerateContagionsRandom() {
        // given
        settings().integer(COUNT_CONTAGIONS, despiteLevel(20));

        Dice dice = new DiceGenerator().getDice("soul", 20*20, 20);
        dice().then(dice::next);
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼*************☼\n" +
                "☼♥************☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        // when
        hero().die();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ 1o1111    1o☼\n" +
                "☼ 1222o211 122☼\n" +
                "☼  1o223o1 1o1☼\n" +
                "☼  1122o21 111☼\n" +
                "☼    1o21     ☼\n" +
                "☼111 111      ☼\n" +
                "☼1o1      111 ☼\n" +
                "☼1121211112o1 ☼\n" +
                "☼112o2o11o211 ☼\n" +
                "☼1o32322222 11☼\n" +
                "☼12o11o11o1 1o☼\n" +
                "☼ 122211122111☼\n" +
                "☼X 1o1   1o1  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("[GOT_INFECTED]");
    }
}