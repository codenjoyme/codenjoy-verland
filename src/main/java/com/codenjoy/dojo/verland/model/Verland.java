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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.verland.model.items.*;
import com.codenjoy.dojo.verland.services.Events;
import com.codenjoy.dojo.verland.services.GameSettings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.field.Generator.generate;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;
import static java.util.stream.Collectors.toList;

public class Verland implements Field {

    private Level level;
    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private int maxScore;
    private int score;

    private Player player;

    public Verland(Dice dice, Level level, GameSettings settings) {
        this.level = level;
        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();

        clearScore();
    }

    @Override
    public void clearScore() {
        level.saveTo(field);
        field.init(this);

        maxScore = 0;
        score = 0;

        generateAll();
    }

    private void generateAll() {
        validateContagions();
        generateContagions();
    }

    private void validateContagions() {
        Parameter<Integer> contagions = settings.integerValue(COUNT_CONTAGIONS);
        Parameter<Integer> potions = settings.integerValue(POTIONS_COUNT);
        while (contagions.getValue() > ((size() - 1) * (size() - 1) - 1)) {
            contagions.update(contagions.getValue() / 2);
        }

        if (potions.getValue() < contagions.getValue()) {
            potions.update(contagions.getValue());
        }
    }

    @Override
    public boolean isFree(Point pt) {
        return clean().contains(pt)
                && !walls().contains(pt);
    }

    public boolean isFreeForContagion(Point pt) {
        return !contagions().contains(pt)
                && !walls().contains(pt);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        // метод отдает только те координаты, которые в изначальной
        // карте отмечены как стартовые для героев, когда точек больше
        // не останется, будет возвращать -1 на что isFree скажет false
        List<Integer> numbers = level.heroes().stream()
                .flatMap(pt -> Arrays.stream(new Integer[]{pt.getX(), pt.getY()}))
                .collect(toList());

        return BoardUtils.freeRandom(size(), new NumbersDice(numbers, -1), this::isFree);
    }

    private Optional<Point> freeRandomForContagions(GamePlayer player) {
        return BoardUtils.freeRandom(size(), dice, this::isFreeForContagion);
    }

    private void generateContagions() {
        generate(contagions(), settings, COUNT_CONTAGIONS,
                this::freeRandomForContagions,
                Contagion::new);
    }

    @Override
    public int size() {
        return field.size();
    }

    @Override
    public boolean isContagion(Point pt) {
        return contagions().contains(pt);
    }

    @Override
    public BoardReader<Player> reader() { // TODO сделать красиво
        return new BoardReader<>() {
            @Override
            public int size() {
                return field.size();
            }

            @Override
            public void addAll(Player player, Consumer processor) {
                if (Verland.this.isGameOver()) {
                    process(player, processor,
                            Hero.class,
                            Wall.class,
                            Contagion.class,
                            Cured.class,
                            Cure.class,
                            Cell.class);
                } else {
                    process(player, processor,
                            Hero.class,
                            Wall.class,
                            Cure.class,
                            Cell.class);
                }
            }

            private void process(Player player, Consumer processor, Class... classes) {
                field.reader(classes).addAll(player, processor);
            }
        };
    }

    // TODO удалить метод, когда полноценно будут юзеры
    private boolean isGameOver() {
        return hero() == null || hero().isGameOver();
    }

    // TODO удалить метод, когда полноценно будут юзеры
    @Override
    public Hero hero() {
        List<Hero> heroes = heroes().all();
        return (heroes.isEmpty()) ? null : heroes.get(0);
    }

    @Override
    public void newGame(Player player) {
        remove(player);
        this.player = player;
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        this.player = null;
        if (player.getHero() != null) {
            heroes().removeExact(player.getHero());
        }
    }

    @Override
    public Contagion tryCreateContagion(Point cell) {
        Contagion result = new Contagion(cell);
        contagions().add(result);
        return result;
    }

    @Override
    public int contagionsNear(Point pt) {
        return (int)Arrays.stream(QDirection.values())
                .map(direction -> direction.change(pt))
                .filter(around -> !around.isOutOf(size()))
                .filter(around -> contagions().contains(around))
                .count();
    }

    @Override
    public void cure(Hero hero, Direction direction) {
        Point to = direction.change(hero);
        if (walls().contains(to)) {
            return;
        }

        if (hero.noMorePotions()) {
            return;
        }

        if (cures().contains(to)) {
            return;
        }

        hero.tryToCure(() -> {
            cures().add(new Cure(to));
            if (contagions().contains(to)) {
                removeContagion(to);
            } else {
                player.event(Events.FORGOT_POTION);
            }
        });

        if (hero.isNoPotionsButPresentContagions()) {
            openAllBoard();
            player.event(Events.NO_MORE_POTIONS);
        }
    }

    private void removeContagion(Point pt) {
        cured().add(new Cured(pt));
        contagions().removeAt(pt);
        increaseScore();
        player.event(Events.CURE);
        if (contagions().size() == 0) {
            openAllBoard();
            player.event(Events.WIN);
        }
    }

    @Override
    public void openAllBoard() {
        cells().forEach(Cell::open);
    }

    private void increaseScore() {
        score++;
        maxScore = Math.max(score, maxScore);
    }

    @Override
    public void tick() {
        heroes().forEach(Hero::tick);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public Accessor<Cell> cells() {
        return field.of(Cell.class);
    }

    @Override
    public Accessor<Contagion> contagions() {
        return field.of(Contagion.class);
    }

    @Override
    public Accessor<Cured> cured() {
        return field.of(Cured.class);
    }

    @Override
    public Accessor<Wall> walls() {
        return field.of(Wall.class);
    }

    @Override
    public Accessor<Cure> cures() {
        return field.of(Cure.class);
    }

    @Override
    public Accessor<Hero> heroes() {
        return field.of(Hero.class);
    }

    @Override
    public List<Cell> clean() {
        return cells().filter(Cell::isClean);
    }
}
