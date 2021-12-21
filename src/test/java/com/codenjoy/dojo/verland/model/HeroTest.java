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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.verland.model.items.Cell;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HeroTest {

    private static final int CONTAGIONS_COUNT = 4;
    private static final int BOARD_SIZE = 5;
    private static final int POTIONS_COUNT = 8;

    private Field field;
    private EventListener listener;
    private GameSettings settings;
    private Dice dice = mock(Dice.class);

    @Before
    public void setup() {
        givenFl("☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼☼☼☼☼\n");
    }

    private void givenFl(String map) {
        settings = new GameSettings()
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, CONTAGIONS_COUNT)
                .integer(GameSettings.Keys.POTIONS_COUNT, POTIONS_COUNT);

        Level level = new Level(map);
        Hero hero = level.heroes().get(0);
        field = new Verland(dice, level, settings);
        listener = mock(EventListener.class);
        dice(hero.getX(), hero.getY());
        field.newGame(new Player(listener, settings));
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    @Test
    public void shouldBoardConsistOfCells() {
        assertNotNull(field.cells());
    }

    @Test
    public void shouldFreeCellsNumberBeMoreThanZero() {
        assertEquals(true, field.cells().size() > 0);
    }

    @Test
    public void shouldContagionsCountLessThenAllCells_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.COUNT_CONTAGIONS, 100);

        // when
        setup();

        // then
        assertEquals(4, (int)settings.integer(GameSettings.Keys.COUNT_CONTAGIONS));
    }

    @Test
    public void shouldPotionsChargeMoreThanContagions_whenGameStart() {
        // given
        settings.integer(GameSettings.Keys.COUNT_CONTAGIONS, 20)
                .integer(GameSettings.Keys.POTIONS_COUNT, 10);

        // when
        setup();

        // then
        assertEquals(4, (int)settings.integer(GameSettings.Keys.COUNT_CONTAGIONS));
        assertEquals(8, (int)settings.integer(GameSettings.Keys.POTIONS_COUNT));
    }

    @Test
    public void shouldBoardSizeSpecify_whenGameStart() {
        // given
        // when
        givenFl("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼********☼\n" +
                "☼♥*******☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        // then
        assertEquals(10, field.size());
    }

    @Test
    public void shouldBoardBeSquare() {
        assertEquals(5*5, field.cells().size());
    }

    @Test
    public void shouldBoardCellsNumberBeMoreThanOne() {
        assertEquals(true, field.cells().size() > 1);
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
        assertNotNull(field.contagions());
    }

    @Test
    public void shouldContagionsCountSpecify_whenGameStart() {
        assertNotNull(field.contagions().size());
    }

    @Test
    public void shouldFreeCellsDecrease_whenCreatesHeroAndContagions() {
        // given
        Set<Point> all = new HashSet<>();

        // when
        all.addAll(field.walls().all());
        all.addAll(field.cells().filter(Predicate.not(Cell::isClean)));
        all.addAll(field.heroes().all());
        all.addAll(field.contagions().all());

        // then
        assertEquals(field.cells().size(),
                all.size());
    }

    @Test
    public void shouldHeroMoveToUp() {
        int oldYPosition = hero().getY();

        hero().moveTo(Direction.UP);

        assertEquals(hero().getY(), oldYPosition + 1);
    }

    @Test
    public void shouldHeroMoveToDown() {
        hero().moveTo(Direction.UP);

        int oldYPosition = hero().getY();

        hero().moveTo(Direction.DOWN);

        assertEquals(hero().getY(), oldYPosition - 1);
    }

    @Test
    public void shouldHeroMoveToLeft() {
        hero().moveTo(Direction.RIGHT);

        int oldXPosition = hero().getX();

        hero().moveTo(Direction.LEFT);

        assertEquals(hero().getX(), oldXPosition - 1);
    }

    @Test
    public void shouldHeroMoveToRight() {
        int oldXPosition = hero().getX();

        hero().moveTo(Direction.RIGHT);

        assertEquals(hero().getX(), oldXPosition + 1);
    }

    private void givenHeroMovedToContagion() {
        placeContagionUpFromHero();
        hero().moveTo(Direction.UP);
    }

    private void placeContagionUpFromHero() {
        Point upstairs = Direction.UP.change(hero());
        if (!field.contagions().contains(upstairs)) {
            field.tryCreateContagion(upstairs);
        }
    }

    @Test
    public void shouldGameIsOver_whenHeroIsDead() {
        givenHeroMovedToContagion();

        assertEquals(true, hero().isGameOver());
        assertEquals(true, hero().isDead());
    }

    @Test
    public void shouldNextTurn_whenHeroMove() {
        int turnBeforeHeroMotion = hero().getTurn();

        hero().moveTo(Direction.UP);
        int turnAfterHeroMotion = hero().getTurn();

        assertEquals(turnBeforeHeroMotion, turnAfterHeroMotion - 1);
    }

    @Test
    public void shouldHeroKnowsHowMuchContagionsNearHim_whenAtLeastOneIsDownFromHero() {
        placeContagionUpFromHero();

        assertEquals(true, field.contagionsNear(hero()) > 0);
    }

    @Test
    public void shouldPotionsHaveCharge() {
        assertNotNull(potions().charge());
    }

    @Test
    public void shouldPotionsChargeMoreThanContagionsOnBoard() {
        assertEquals(true, potions().charge() > field.contagions().size());
    }

    @Test
    public void shouldHeroDestroyMine_whenMineExistInGivenDirection() {
        for (Direction direction : Direction.values()) {
            field.cure(hero(), direction);
            assertEquals(false, field.contagions().contains(
                    direction.change(hero())));
        }
    }

    @Test
    public void shouldPotionsChargeDecreaseByOne_whenUse() {
        int potionsCharge = potions().charge();

        field.cure(hero(), Direction.UP);

        assertEquals(potionsCharge, potions().charge() + 1);
    }

    private Potions potions() {
        return hero().potions();
    }

    @Test
    public void shouldContagionsCountDecreaseByOne_whenContagionsIsCured() {
        placeContagionUpFromHero();
        int count = field.contagions().size();

        field.cure(hero(), Direction.UP);

        assertEquals(count, field.contagions().size() + 1);
    }

    @Test
    public void shouldWin_whenNoMoreContagions() {
        placeContagionUpFromHero();

        field.cure(hero(), Direction.UP);

        assertEquals(true, hero().isWin());
    }

    @Test
    public void shouldGameOver_whenNoMoreCharge() {
        hero().moveTo(Direction.UP);
        placeContagionUpFromHero();
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼ **☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field));

        field.cure(hero(), Direction.DOWN);
//        board.usePotionsToGivenDirection(Direction.UP);  // there is contagion
        field.cure(hero(), Direction.LEFT);
        field.cure(hero(), Direction.RIGHT);
        hero().moveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼1♥*☼\n" +
                "☼!**☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field));

        field.cure(hero(), Direction.DOWN);
        field.cure(hero(), Direction.UP);
        field.cure(hero(), Direction.LEFT);
        field.cure(hero(), Direction.RIGHT);
        hero().moveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼*!*☼\n" +
                "☼!!♥☼\n" +
                "☼!!*☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field));

        field.cure(hero(), Direction.DOWN);
        field.cure(hero(), Direction.UP);
        field.cure(hero(), Direction.LEFT);
        field.cure(hero(), Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼o!!☼\n" +
                "☼!!♥☼\n" +
                "☼!!!☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field));

        assertEquals(false, hero().isDead());
        assertEquals(true, hero().isGameOver());
    }

    private Hero hero() {
        return field.hero();
    }

    private String getBoardAsString(Field board) {
        return (String) new PrinterFactoryImpl<Element, Player>()
                .getPrinter(board.reader(),
                        new Player(listener, settings)).print();
    }

}
