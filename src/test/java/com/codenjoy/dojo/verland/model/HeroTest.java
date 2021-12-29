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
import com.codenjoy.dojo.verland.TestGameSettings;
import com.codenjoy.dojo.verland.model.items.Cell;
import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.verland.model.items.HeroSpot;
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
        settings = new TestGameSettings()
                .integer(GameSettings.Keys.COUNT_CONTAGIONS, CONTAGIONS_COUNT)
                .integer(GameSettings.Keys.POTIONS_COUNT, POTIONS_COUNT);

        Level level = new Level(map);
        HeroSpot spot = level.heroesSpots().get(0);
        field = new Verland(dice, level, settings);
        listener = mock(EventListener.class);
        dice(spot.getX(), spot.getY());
        Player player = new Player(listener, settings);
        field.newGame(player);
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
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
            field.contagions().add(new Contagion(upstairs));
        }
    }

    @Test
    public void shouldGameIsOver_whenHeroIsDead() {
        givenHeroMovedToContagion();

        assertEquals(false, hero().isAlive());
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
        Player player = (Player) hero().getPlayer();
        hero().moveTo(Direction.UP);
        placeContagionUpFromHero();
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼♥**☼\n" +
                "☼ **☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field, player));

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
                "☼☼☼☼☼\n", getBoardAsString(field, player));

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
                "☼☼☼☼☼\n", getBoardAsString(field, player));

        field.cure(hero(), Direction.DOWN);
        field.cure(hero(), Direction.UP);
        field.cure(hero(), Direction.LEFT);
        field.cure(hero(), Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼*!!☼\n" +
                "☼!!♥☼\n" +
                "☼!!!☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(field, player));

        assertEquals(true, hero().isAlive());
    }

    private Hero hero() {
        return field.heroes().all().get(0);
    }

    private String getBoardAsString(Field board, Player player) {
        return (String) new PrinterFactoryImpl<Element, Player>()
                .getPrinter(board.reader(), player).print();
    }
}