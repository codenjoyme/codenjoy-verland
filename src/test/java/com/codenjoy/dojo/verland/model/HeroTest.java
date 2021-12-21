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


import com.codenjoy.dojo.games.verland.Element;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.verland.model.generator.Contagions;
import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class HeroTest {

    private static final int CONTAGIONS_COUNT = 4;
    private static final int BOARD_SIZE = 5;
    private static final int POTIONS_COUNT = 8;
    private static final Contagions NO_CONTAGIONS = new MockGenerator();

    private Field board;
    private EventListener listener;
    private GameSettings settings;

    @Before
    public void before() {
        settings = new GameSettings()
                .integer(GameSettings.Keys.BOARD_SIZE, BOARD_SIZE)
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, CONTAGIONS_COUNT)
                .integer(GameSettings.Keys.POTIONS_COUNT, POTIONS_COUNT);

        board = new Verland(NO_CONTAGIONS, settings);
        board.newGame(new Player(listener, settings));
        listener = mock(EventListener.class);
    }

    static class MockGenerator implements Contagions {

        @Override
        public List<Contagion> get(int count, Field board) {
            return new ArrayList<>();
        }
    }

    @Test
    public void shouldBoardConsistOfCells() {
        assertNotNull(board.cells());
    }

    @Test
    public void shouldFreeCellsNumberBeMoreThanZero() {
        assertEquals(true, board.freeCells().size() > 0);
    }

    @Test
    public void shouldBoardSizeMoreThanOne_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.BOARD_SIZE, 0);

        // when
        new Verland(NO_CONTAGIONS, settings)
                .newGame(new Player(listener, settings));

        // then
        assertEquals(5, (int)settings.integer(GameSettings.Keys.BOARD_SIZE));
    }

    @Test
    public void shouldContagionsCountLessThenAllCells_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.BOARD_SIZE, 2)
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, 100);

        // when
        new Verland(NO_CONTAGIONS, settings)
                .newGame(new Player(listener, settings));

        // then
        assertEquals(12, (int)settings.integer(GameSettings.Keys.COUNT_CONTAGIONS));
    }

    @Test
    public void shouldPotionsChargeMoreThanContagions_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.BOARD_SIZE, 100)
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, 20)
                .integer(GameSettings.Keys.POTIONS_COUNT, 10);

        // when
        new Verland(NO_CONTAGIONS, settings)
                .newGame(new Player(listener, settings));

        // then
        assertEquals(20, (int)settings.integer(GameSettings.Keys.COUNT_CONTAGIONS));
        assertEquals(20, (int)settings.integer(GameSettings.Keys.POTIONS_COUNT));
    }

    @Test
    public void shouldBoardSizeSpecify_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.BOARD_SIZE, 10);

        // when
        new Verland(NO_CONTAGIONS, settings)
                .newGame(new Player(listener, settings));

        // then
        assertEquals(10, (int)settings.integer(GameSettings.Keys.BOARD_SIZE));
        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoardBeSquare() {
        assertEquals(board.cells().size() % (board.size() - 2), 0);
    }

    @Test
    public void shouldBoardCellsNumberBeMoreThanOne() {
        assertEquals(true, board.cells().size() > 1);
    }

    @Test
    public void shouldHeroOnBoard() {
        assertNotNull(hero());
    }

    @Test
    public void shouldHeroBeAtBoardDefaultPosition() {
        assertEquals(hero(), pt(1, 1));
    }

    @Test
    public void shouldContagionsOnBoard() {
        assertNotNull(board.contagions());
    }

    @Test
    public void shouldContagionsCountSpecify_whenGameStart() {
        assertNotNull(board.contagions().size());
    }

    @Test
    public void shouldFreeCellsDecrease_whenCreatesHeroAndContagions() {
        int borders = 0;
        int freeCells = board.freeCells().size();
        int hero = 1;
        int contagions = board.contagions().size();

        assertEquals(board.cells().size(),
                freeCells + contagions + hero + borders);
    }

    @Test
    public void shouldHeroMoveToUp() {
        int oldYPosition = hero().getY();

        board.moveTo(Direction.UP);

        assertEquals(hero().getY(), oldYPosition + 1);
    }

    @Test
    public void shouldHeroMoveToDown() {
        board.moveTo(Direction.UP);

        int oldYPosition = hero().getY();

        board.moveTo(Direction.DOWN);

        assertEquals(hero().getY(), oldYPosition - 1);
    }

    @Test
    public void shouldHeroMoveToLeft() {
        board.moveTo(Direction.RIGHT);

        int oldXPosition = hero().getX();

        board.moveTo(Direction.LEFT);

        assertEquals(hero().getX(), oldXPosition - 1);
    }

    @Test
    public void shouldHeroMoveToRight() {
        int oldXPosition = hero().getX();

        board.moveTo(Direction.RIGHT);

        assertEquals(hero().getX(), oldXPosition + 1);
    }

    private void givenHeroMovedToContagion() {
        placeContagionUpFromHero();
        board.moveTo(Direction.UP);
    }

    private void placeContagionUpFromHero() {
        Point upstairs = Direction.UP.change(hero());
        if (!board.contagions().contains(upstairs)) {
            board.tryCreateContagion(upstairs);
        }
    }

    @Test
    public void shouldGameIsOver_whenHeroIsDead() {
        givenHeroMovedToContagion();

        assertEquals(true, board.isGameOver());
        assertEquals(true, hero().isDead());
    }

    @Test
    public void shouldNextTurn_whenHeroMove() {
        int turnBeforeHeroMotion = board.getTurn();

        board.moveTo(Direction.UP);
        int turnAfterHeroMotion = board.getTurn();

        assertEquals(turnBeforeHeroMotion, turnAfterHeroMotion - 1);
    }

    @Test
    public void shouldHeroKnowsHowMuchContagionsNearHim_whenAtLeastOneIsDownFromHero() {
        placeContagionUpFromHero();

        assertEquals(true, board.contagionsNear() > 0);
    }

    @Test
    public void shouldPotionsHaveCharge() {
        assertNotNull(potions().charge());
    }

    @Test
    public void shouldPotionsChargeMoreThanContagionsOnBoard() {
        assertEquals(true, potions().charge() > board.contagions().size());
    }

    @Test
    public void shouldHeroDestroyMine_whenMineExistInGivenDirection() {
        for (Direction direction : Direction.values()) {

            board.cure(direction);
            boolean isMineInDirection = board.contagions().contains(
                    board.positionAfterMove(direction));

            assertEquals(true, !isMineInDirection);
        }
    }

    @Test
    public void shouldPotionsChargeDecreaseByOne_whenUse() {
        int potionsCharge = potions().charge();

        board.cure(Direction.UP);

        assertEquals(potionsCharge, potions().charge() + 1);
    }

    private Potions potions() {
        return hero().potions();
    }

    @Test
    public void shouldContagionsCountDecreaseByOne_whenContagionsIsCured() {
        placeContagionUpFromHero();
        int count = board.contagions().size();

        board.cure(Direction.UP);

        assertEquals(count, board.contagions().size() + 1);
    }

    @Test
    public void shouldWin_whenNoMoreContagions() {
        placeContagionUpFromHero();

        board.cure(Direction.UP);

        assertEquals(true, board.isWin());
    }

    @Test
    public void shouldGameOver_whenNoMoreCharge() {
        board.moveTo(Direction.UP);
        placeContagionUpFromHero();
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼ **☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.cure(Direction.DOWN);
//        board.usePotionsToGivenDirection(Direction.UP);  // there is contagion
        board.cure(Direction.LEFT);
        board.cure(Direction.RIGHT);
        board.moveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼1♥*☼\n" +
                "☼!**☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.cure(Direction.DOWN);
        board.cure(Direction.UP);
        board.cure(Direction.LEFT);
        board.cure(Direction.RIGHT);
        board.moveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!!♥☼\n" +
                "☼!!*☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.cure(Direction.DOWN);
        board.cure(Direction.UP);
        board.cure(Direction.LEFT);
        board.cure(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼o!!☼\n" +
                "☼!!♥☼\n" +
                "☼!!!☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        assertEquals(false, hero().isDead());
        assertEquals(true, board.isGameOver());
    }

    private Hero hero() {
        return board.hero();
    }

    private String getBoardAsString(Field board) {
        return (String) new PrinterFactoryImpl<Element, Player>()
                .getPrinter(board.reader(),
                        new Player(listener, settings)).print();
    }

}
