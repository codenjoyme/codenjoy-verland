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

    private List<EventListener> listeners;
    private List<Game> games;
    private List<Player> players;

    private Dice dice;
    private PrinterFactory<Element, Player> printer;
    private Verland field;
    private GameSettings settings;
    private EventsListenersAssert events;

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
        SmartAssert.checkResult(getClass());
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
        Level level = settings.level(levelNumber, dice);

        if (settings.integer(COUNT_CONTAGIONS) < 0) {
            // таким хитрым костыльным способом мы сообщаем, что будем игнорировать
            // количество заражений на поле, и попробуем сгенерировать их генератором
            settings.integer(COUNT_CONTAGIONS, - settings.integer(COUNT_CONTAGIONS));
        } else {
            settings.integer(COUNT_CONTAGIONS, level.contagions().size());
        }

        field = new Verland(dice, level, settings);
        level.heroes().forEach(this::givenPlayer);

        // other field preparation stuff
    }

    protected void givenPlayer(Hero hero) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Player player = new Player(listener, settings);
        players.add(player);

        Game game = new Single(player, printer);
        games.add(game);

        dice(hero.getX(), hero.getY());
        game.on(field);
        game.newGame();
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
}