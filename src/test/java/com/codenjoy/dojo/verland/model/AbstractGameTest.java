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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.TriFunction;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.gametest.AbstractBaseGameTest;
import com.codenjoy.dojo.verland.TestGameSettings;
import com.codenjoy.dojo.verland.services.Event;
import com.codenjoy.dojo.verland.services.GameSettings;
import org.junit.After;
import org.junit.Before;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;

public abstract class AbstractGameTest
        extends AbstractBaseGameTest<Player, Verland, GameSettings, Level, Hero> {

    private static final int DESPITE_LEVEL = -1;

    @Before
    public void setup() {
        super.setup();
    }

    @After
    public void after() {
        super.after();
    }

    @Override
    protected void afterCreateField() {
        settings().integer(COUNT_CONTAGIONS, field().contagions().size());
    }

    @Override
    protected  void beforeCreateField() {
        if (isNegative(settings().integer(COUNT_CONTAGIONS))) {
            // таким хитрым костыльным способом мы сообщаем, что будем
            // игнорировать количество заражений на поле,
            // и попробуем до-генерировать их генератором
            settings().integer(COUNT_CONTAGIONS, Math.abs(settings().integer(COUNT_CONTAGIONS)));
        } else {
            settings().integer(COUNT_CONTAGIONS, level().contagions().size());
        }
    }

    @Override
    protected GameSettings setupSettings() {
        return new TestGameSettings();
    }

    @Override
    protected Function<String, Level> createLevel() {
        return Level::new;
    }

    @Override
    protected BiFunction<EventListener, GameSettings, Player> createPlayer() {
        return Player::new;
    }

    @Override
    protected TriFunction<Dice, Level, GameSettings, Verland> createField() {
        return Verland::new;
    }

    @Override
    protected Class<?> eventClass() {
        return Event.class;
    }

    // other methods

    private boolean isNegative(int number) {
        return number / Math.abs(number) == DESPITE_LEVEL;
    }

    protected int despiteLevel(int countContagions) {
        return DESPITE_LEVEL * countContagions;
    }

    public void assertPotions(String expected) {
        assertEquals(expected,
                TestUtils.collectHeroesData(players(), "potions", true));
    }

    public void assertContagions(int expected) {
        assertEquals(expected, field().contagions().size());
    }

    public void assertWin() {
        assertWin(0);
    }

    public void assertAlive() {
        assertAlive(0);
    }

    public void assertDie() {
        assertDie(0);
    }

    public void assertWin(int index) {
        assertEquals(true, hero(index).isWin());
        assertEquals(true, hero(index).isGameOver());
        assertEquals(true, hero(index).isAlive());
        assertEquals(false, hero(index).isActive());
        assertEquals(true, player(index).shouldLeave());
    }

    public void assertAlive(int index) {
        assertEquals(false, hero(index).isWin());
        assertEquals(false, hero(index).isGameOver());
        assertEquals(true, hero(index).isAlive());
        assertEquals(true, hero(index).isActive());
        assertEquals(false, player(index).shouldLeave());
    }

    public void assertDie(int index) {
        assertEquals(false, hero(index).isWin());
        assertEquals(true, hero(index).isGameOver());
        assertEquals(false, hero(index).isAlive());
        assertEquals(true, hero(index).isActive());
        assertEquals(true, player(index).shouldLeave());
    }
}