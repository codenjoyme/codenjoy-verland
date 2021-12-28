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


import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.verland.TestGameSettings;
import com.codenjoy.dojo.verland.model.items.HeroSpot;
import com.codenjoy.dojo.verland.services.Event;
import com.codenjoy.dojo.verland.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import com.codenjoy.dojo.utils.smart.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractGameTest {

    private static final int DESPITE_LEVEL = -1;

    private List<EventListener> listeners;
    private List<Game> games;
    private List<Player> players;

    private Dice dice;
    private PrinterFactory<Element, Player> printer;
    private Verland field;
    private GameSettings settings;
    private EventsListenersAssert events;
    private Level level;

    @Before
    public void setup() {
        listeners = new LinkedList<>();
        players = new LinkedList<>();
        games = new LinkedList<>();

        dice = mock(Dice.class);
        settings = new TestGameSettings();
        setupSettings();
        printer = new PrinterFactoryImpl<>();
        events = new EventsListenersAssert(() -> listeners, Event.class);
    }

    @After
    public void after() {
        verifyAllEvents("");
        SmartAssert.checkResult();
    }

    public void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    public void givenFl(String... maps) {
        int levelNumber = LevelProgress.levelsStartsFrom1;
        settings.setLevelMaps(levelNumber, maps);
        level = settings.level(levelNumber, dice, Level::new);

        beforeCreateField();

        field = new Verland(dice, level, settings);
        level.heroesSpots().forEach(this::givenPlayer);

        afterCreateField();
    }

    private void afterCreateField() {
        settings.integer(COUNT_CONTAGIONS, field.contagions().size());
    }

    private void beforeCreateField() {
        if (isNegative(settings.integer(COUNT_CONTAGIONS))) {
            // таким хитрым костыльным способом мы сообщаем, что будем
            // игнорировать количество заражений на поле,
            // и попробуем до-генерировать их генератором
            settings.integer(COUNT_CONTAGIONS, Math.abs(settings.integer(COUNT_CONTAGIONS)));
        } else {
            settings.integer(COUNT_CONTAGIONS, level.contagions().size());
        }
    }

    private boolean isNegative(int number) {
        return number / Math.abs(number) == DESPITE_LEVEL;
    }

    protected void givenPlayer(HeroSpot spot) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Player player = new Player(listener, settings);
        players.add(player);

        Game game = new Single(player, printer);
        games.add(game);

        // так как метод поиска свободных мест бегает не по свободным,
        // координатам как в других играх, а по спотам, то тут нужен только
        // рандомайзер для collections shuffle, а не
        // dice(spot.getX(), spot.getY());
        dice(1);

        game.on(field);
        game.newGame();
    }

    protected int despiteLevel(int countContagions) {
        return DESPITE_LEVEL * countContagions;
    }

    public void assertEquals(Object expected, Object actual) {
        SmartAssert.assertEquals(expected, actual);
    }

    protected void setupSettings() {
        // do something with settings
    }

    public void tick() {
        field.tick();
    }

    // getters & asserts

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events().getEvents());
    }

    public GameSettings settings() {
        return settings;
    }

    public Verland field() {
        return field;
    }

    public Level level() {
        return level;
    }

    public EventsListenersAssert events() {
        return events;
    }

    public void assertF(String expected, int index) {
        assertEquals(expected, game(index).getBoardAsString());
    }

    public Game game(int index) {
        return games.get(index);
    }

    public Player player(int index) {
        return players.get(index);
    }

    public Hero hero(int index) {
        return (Hero) game(index).getPlayer().getHero();
    }

    // getters, if only one player

    public void assertF(String expected) {
        assertF(expected, 0);
    }

    public Game game() {
        return game(0);
    }

    public Player player() {
        return player(0);
    }

    public Hero hero() {
        return hero(0);
    }

    // other methods

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
        assertEquals(true, hero(index).isActive());
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
        // тут false потому что Single.isGameOver = true и так будет обновление борды,
        // а shouldLeave надо делать, когда на поле не умер игрок, но выиграл и больше
        // ему тут делать нечего
        assertEquals(false, player(index).shouldLeave());
    }
}