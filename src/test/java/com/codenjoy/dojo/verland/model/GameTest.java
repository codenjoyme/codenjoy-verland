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


import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.verland.services.Events;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameTest {

    private MockBoard game;
    private List<Contagion> contagions;
    private EventListener listener;
    private PrinterFactory printerFactory;
    private GameSettings settings;

    @Before
    public void setup() {
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings()
                .integer(BOARD_SIZE, 5)
                .integer(POTIONS_COUNT, 3);
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardRight() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼**♥☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼*♥ ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveLeft() {
        game.hero().left();
        game.tick();
    }

    private void moveDown() {
        game.hero().down();
        game.tick();
    }

    private void moveRight() {
        game.hero().right();
        game.tick();
    }

    private void suicide() {
        game.hero().act(0);
        game.tick();
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardDown() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*1♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼*1 ☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardUp() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*♥*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼♥ *☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_whenWalkOnBoardLeft() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetRight() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetUp() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetDown() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        cureDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenSetLeft() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldDie_whenHeroAtContagion() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldSaveCommandAndActAfterTick() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        game.hero().right();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldPrintAllContagions_whenHeroAtContagion() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3));

        cureUp();
        cureDown();
        cureLeft();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ xo☼\n" +
                "☼!3X☼\n" +
                "☼ xo☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldPrintBoard_whenNearHeroNoContagions() {
        shouldBoardWith(new Hero(3, 3), new Contagion(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroOneContagion() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroTwoContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroThreeContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFourContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroFiveContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

    }

    @Test
    public void shouldPrintBoard_whenNearHeroSixContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3),
                new Contagion(1, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroSevenContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3),
                new Contagion(1, 3), new Contagion(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearHeroEightContagions() {
        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3),
                new Contagion(1, 3), new Contagion(1, 2), new Contagion(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ooo☼\n" +
                "☼o8o☼\n" +
                "☼oXo☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCure_whenContagionAtRight() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCure_whenContagionAtRightAndLeft() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2), new Contagion(1, 2));

        unbombRight();
        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtRight() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtDown() {
        shouldBoardWith(new Hero(2, 2), new Contagion(2, 1));

        cureDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥ ☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtDown() {
        shouldBoardWith(new Hero(2, 2), new Contagion(2, 1));

        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼*♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtUp() {
        shouldBoardWith(new Hero(2, 2), new Contagion(2, 3));

        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼ ♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtUp() {
        shouldBoardWith(new Hero(2, 2), new Contagion(2, 3));

        cureDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldCure_whenContagionAtLeft() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 2));

        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    private void assertWin() {
        assertTrue(game.isWin());
    }

    @Test
    public void shouldCureOnEmptySpace_whenContagionAtLeft() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldWin_whenDestroyAllContagions() {
        settings.integer(POTIONS_COUNT, 8);

        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3), new Contagion(3, 2), new Contagion(3, 1),
                new Contagion(2, 1), new Contagion(2, 3),
                new Contagion(1, 3), new Contagion(1, 2), new Contagion(1, 1));

        cureLeft();
        cureDown();
        unbombRight();
        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!♥!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveUp();
        cureLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼!♥!☼\n" +
                "☼!2!☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveDown();
        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼!!!☼\n" +
                "☼!2!☼\n" +
                "☼*♥*☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼xxx☼\n" +
                "☼x x☼\n" +
                "☼x♥x☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldLeaveContagionOnMap_whenWalkBetweenContagions() {
        shouldBoardWith(new Hero(1, 1),
                new Contagion(2, 3), new Contagion(2, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼1♥*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼11♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼**♥☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**♥☼\n" +
                "☼**2☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveUp() {
        game.hero().up();
        game.tick();
    }

    private void assertStillNotWin() {
        assertFalse(game.isWin());
    }

    private void cureUp() {
        game.hero().act();
        moveUp();
    }

    private void unbombRight() {
        game.hero().act();
        moveRight();
    }

    private void cureDown() {
        game.hero().act();
        moveDown();
    }

    private void cureLeft() {
        game.hero().act();
        moveLeft();
    }

    private void assertBoard(String expected) {
        assertEquals(expected, printerFactory.getPrinter(
                game.reader(), null).print());
    }

    private void shouldBoardWith(Hero hero, Contagion... mines) {
        listener = mock(EventListener.class);
        game = new MockBoard(hero, mines);
    }

    private class MockBoard extends Verland {
        private Player player;

        public MockBoard(Hero hero, Contagion... contagions) {
            super((count, board) -> new LinkedList<>(Arrays.asList(contagions)),
                    settings.integer(COUNT_CONTAGIONS, contagions.length));

            player = new Player(listener, settings);
            player.setHero(hero);
            hero.setPlayer(player);
            newGame(player);
        }
    }

    @Test
    public void shouldFireEvent_whenDie() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1X☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.GOT_INFECTED);
    }

    @Test
    public void shouldFireEvent_whenSuicide() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2));

        suicide();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥o☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.SUICIDE);

        assertEquals(true, game.isGameOver());
        assertEquals(false, game.hero().isAlive());

        // TODO дальше как-то странно, наверное тест под это не заточен
        game.newGame(game.player);
        game.tick();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ♥o☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void verifyEvents(Events... events) {
        for (Events event : events) {
            verify(listener).event(event);
        }
    }

    @Test
    public void shouldFireEvent_whenOpenSpace() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.CLEAN_AREA);
    }

    @Test
    public void shouldNotFireEvent_whenReturnsHome() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1♥☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.CLEAN_AREA);

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥ ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldFireEvent_whenNoMoreCharge() {
        settings.integer(POTIONS_COUNT, 3);

        shouldBoardWith(new Hero(2, 2), new Contagion(1, 1));

        cureDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼!♥*☼\n" +
                "☼*!*☼\n" +
                "☼☼☼☼☼\n");

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼!♥!☼\n" +
                "☼o! ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(3, Events.FORGOT_POTION);
        verifyEvents(Events.NO_MORE_POTIONS);

        cureUp();

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case1() {
        settings.integer(POTIONS_COUNT, 4);

        shouldBoardWith(new Hero(2, 2),
                new Contagion(2, 1),
                new Contagion(2, 3),
                new Contagion(1, 2),
                new Contagion(3, 2));

        unbombRight();
        cureLeft();
        cureDown();
        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼x♥x☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case2() {
        settings.integer(POTIONS_COUNT, 4);

        shouldBoardWith(new Hero(2, 2),
                new Contagion(1, 1),
                new Contagion(1, 3),
                new Contagion(3, 3),
                new Contagion(3, 1));

        unbombRight();
        cureLeft();
        cureDown();
        cureUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼o!o☼\n" +
                "☼!♥!☼\n" +
                "☼o!o☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case3() {
        settings.integer(POTIONS_COUNT, 2);

        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3),
                new Contagion(3, 1));

        unbombRight();
        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 1o☼\n" +
                "☼!♥!☼\n" +
                "☼ 1o☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintAllBoardContagions_whenNoMoreCharge_case4() {
        settings.integer(POTIONS_COUNT, 2);

        shouldBoardWith(new Hero(2, 2),
                new Contagion(3, 3),
                new Contagion(3, 1));

        cureLeft();
        cureDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 1o☼\n" +
                "☼!♥2☼\n" +
                "☼ !o☼\n" +
                "☼☼☼☼☼\n");
    }

    private void verifyEvents(int count, Events event) {
        verify(listener, times(count)).event(event);
    }

    @Test
    public void shouldFireEvent_whenCureContagion() {
        shouldBoardWith(new Hero(2, 2), new Contagion(3, 2), new Contagion(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.CURE);
    }

    @Test
    public void shouldFireEvent_whenCleanAllContagions() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 2));

        cureLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x♥ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(
                Events.CURE,
                Events.WIN);
    }

    @Test
    public void shouldOnlyOneFlagPerSpace() {
        shouldBoardWith(new Hero(2, 2), new Contagion(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*♥!☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.FORGOT_POTION);

        unbombRight();

        verifyNoMoreInteractions(listener);
    }


    @Test
    public void shouldCantGoOnBoard() {
        shouldBoardWith(new Hero(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ♥☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCantUnbombOnBoard() {
        shouldBoardWith(new Hero(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        cureLeft();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        cureDown();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼♥  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        reset(listener);

        unbombRight();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ♥☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        reset(listener);

        cureUp();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ♥☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
}
