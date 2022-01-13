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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.annotations.PerformanceOptimized;
import com.codenjoy.dojo.services.dice.DiceRandomWrapper;
import com.codenjoy.dojo.services.dice.NumbersCycleDice;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.utils.whatsnext.WhatsNextUtils;
import com.codenjoy.dojo.verland.model.items.*;
import com.codenjoy.dojo.verland.services.GameSettings;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.services.field.Generator.generate;
import static com.codenjoy.dojo.verland.services.Event.*;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.COUNT_CONTAGIONS;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;
import static java.util.stream.Collectors.toList;

public class Verland extends RoundField<Player, Hero> implements Field {

    private Level level;
    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private Dice heroDice;

    public Verland(Dice dice, Level level, GameSettings settings) {
        super(START_ROUND, WIN_ROUND, settings);

        this.level = level;
        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();

        clearScore();
    }

    @Override
    public void clearScore() {
        if (level == null) return;

        level.saveTo(field);
        field.init(this);

        generateAll();
        heroDice = initHeroDice();

        super.clearScore();
    }

    @Override
    protected void onAdd(Player player) {
        player.newHero(this);
    }

    @Override
    protected void onRemove(Player player) {
        heroes().removeExact(player.getHero());
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    @Override
    protected void tickField() {
        heroes().copy().forEach(Hero::tick);

        for (Contagion contagion : contagions().copy()) {
            List<Cure> cures = cures().getAt(contagion);
            for (Cure cure : cures) {
                cured().add(new Cured(cure));
                contagions().removeAt(cure);

                Hero hero = cure.owner();
                if (hero != null) {
                    hero.getPlayer().event(CURE);
                }
            }
        }

        if (!isContagionsExists()) {
            if (!settings().isRoundsEnabled()) {
                players.forEach(player -> {
                    player.event(WIN_ROUND);
                    player.leaveBoard();
                });
            }
        }
    }

    @Override
    public boolean isContagionsExists() {
        return contagions().size() > 0;
    }

    @Override
    public int size() {
        return field.size();
    }

    private Dice initHeroDice() {
        // метод отдает только те координаты, которые в изначальной
        // карте отмечены как стартовые для героев
        List<HeroSpot> spots = level.heroesSpots();
        // мы их перемешаем, чтобы была какая-то рендомность
        Collections.shuffle(spots, new DiceRandomWrapper(dice));
        // а когда точек больше не останется, пойдем по новому кругу
        List<Integer> numbers = spots.stream()
                .flatMap(Point::stream)
                .collect(toList());
        return new NumbersCycleDice(numbers, -1);
    }

    private void generateAll() {
        validateContagions();
        generateContagions();
    }

    private void validateContagions() {
        Parameter<Integer> contagions = settings.integerValue(COUNT_CONTAGIONS);
        Parameter<Integer> potions = settings.integerValue(POTIONS_COUNT);

        // размещать инфекции можно и под туманом и в чистых ячейках,
        // кроме тех мест, где уже есть инфекции указанные в level
        contagions.update(Math.min(contagions.getValue(),
                level.hidden().size() + level.clear().size() + level.contagions().size()));

        // зелья должно быть достаточно в любом случае на все инфекции,
        // но может быть и больше
        potions.update(Math.max(potions.getValue(),
                contagions.getValue()));
    }

    private void generateContagions() {
        generate(contagions(), size(), settings, COUNT_CONTAGIONS,
                this::freeRandomForContagions,
                Contagion::new);
    }

    @Override
    @PerformanceOptimized
    public boolean isFree(Point pt) {
        return clean().contains(pt)
                && !walls().contains(pt);
    }

    @PerformanceOptimized
    public boolean isFreeForContagion(Point pt) {
        return !pt.isOutOf(size())
                && field.at(pt).noneOf(Wall.class, HeroSpot.class, Contagion.class);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size(), heroDice, this::isFree);
    }

    private Optional<Point> freeRandomForContagions(GamePlayer player) {
        return BoardUtils.freeRandom(size(), dice, this::isFreeForContagion);
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
            hero.fireMorePotions();
            return;
        }

        Cure cure = new Cure(to, hero);
        if (cures().getAt(to).contains(cure)) {
            return;
        }

        hero.tryToCure(() -> {
            if (cures().contains(cure)) {
                // TODO тут чисто теоретически может случиться переполение
                //      ячейки флажками если несколько героев тыцвнет туда
                //      флажок. Элементов может быть всего 7 в клетке.
                //      Но если я тут ограничу, то другие герои не получат
                //      очки за флаг, если там была бомба, на что есть тест.
            }
            cures().add(cure);
            if (contagions().contains(to)) {
                return;
            }
            hero.fireUsedPotion();
        });
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public BoardReader<Player> reader() {
        return field.reader(
                Hero.class,
                Wall.class,
                Contagion.class,
                Cured.class,
                Cure.class,
                Cell.class);
    }

    @Override
    public List<Player> load(String board, Function<Hero, Player> player) {
        level = new Level(board);
        List<Hero> heroes = level.heroesSpots().stream()
                .map(Hero::new)
                .collect(toList());
        return WhatsNextUtils.load(this, heroes, player);
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
    public Accessor<HeroSpot> heroSpots() {
        return field.of(HeroSpot.class);
    }

    @Override
    public List<Cell> clean() {
        return cells().filter(Cell::isClean);
    }
}