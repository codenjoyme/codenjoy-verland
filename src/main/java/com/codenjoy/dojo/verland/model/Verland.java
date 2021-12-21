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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.verland.model.items.*;
import com.codenjoy.dojo.verland.services.Events;
import com.codenjoy.dojo.verland.services.GameSettings;

import java.util.*;
import java.util.function.Consumer;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;

public class Verland implements Field {

    private List<Point> cells;
    private List<Contagion> contagions;
    private List<Cured> cured;
    private List<Wall> walls;
    private List<Cure> cures;

    private int turnCount = 0;
    private ContagionsGenerator generator;
    private int maxScore;
    private int score;
    private Map<Point, Integer> clean;
    private int currentSize;

    private Player player;
    private GameSettings settings;

    public Verland(ContagionsGenerator generator, GameSettings settings) {
        this.settings = settings;
        this.generator = generator;

        buildWalls();
    }

    @Override
    public void clearScore() {
        cures = new LinkedList<>();
        contagions = new LinkedList<>();
        cured = new LinkedList<>();
        clean = new HashMap<>();
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells();
        buildWalls();
        hero().charge(settings.integer(POTIONS_COUNT));
        contagions = generator.get(settings.integer(COUNT_CONTAGIONS), this);
    }

    private void buildWalls() {
        walls = new LinkedList<>();
        for (int i = 0; i < size(); i++) {
            walls.add(new Wall(0, i));
            walls.add(new Wall(size() - 1, i));

            walls.add(new Wall(i, 0));
            walls.add(new Wall(i, size() - 1));
        }
    }

    private void validate() {
        if (size() < 5) {
            settings.integer(BOARD_SIZE, 5);
        }

        Parameter<Integer> contagions = settings.integerValue(COUNT_CONTAGIONS);
        Parameter<Integer> potions = settings.integerValue(POTIONS_COUNT);
        while (contagions.getValue() > ((size() - 1) * (size() - 1) - 1)) {
            contagions.update(contagions.getValue() / 2);
        }

        if (potions.getValue() < contagions.getValue()) {
            potions.update(contagions.getValue());
        }
    }
    
    private List<Point> initializeBoardCells() {
        List<Point> result = new ArrayList<>();
        for (int x = 1; x < size() - 1; x++) {
            for (int y = 1; y < size() - 1; y++) {
                Cell cell = new Cell(x, y);
                cell.init(this);
                result.add(cell);
            }
        }
        return result;
    }

    @Override
    public List<Point> freeCells() {
        return cells().stream()
                .filter(cell -> !hero().itsMe(cell))
                .filter(cell -> !isWall(cell)) // TODO test me
                .filter(cell -> !isContagion(cell))
                .collect(toList());
    }

    @Override
    public List<Point> cells() {
        return cells;
    }

    @Override
    public int size() {
        return settings.integer(BOARD_SIZE);
    }

    @Override
    public List<Contagion> contagions() {
        return contagions;
    }

    @Override
    public int contagionsCount() {
        return contagions().size();
    }

    @Override
    public void moveTo(Direction direction) {
        if (!canMove(direction)) {
            return;
        }

        boolean cleaned = move(direction);
        if (isContagion(hero())) {
            hero().die();
            openAllBoard();
            player.event(Events.GOT_INFECTED);
        } else {
            if (cleaned) {
                player.event(Events.CLEAN_AREA);
            }
        }
        nextTurn();
    }

    private boolean move(Direction direction) {
        clean.put(hero().copy(), contagionsNear());
        hero().move(direction);

        boolean wasHere = clean.containsKey(hero());
        return !wasHere;
    }

    private boolean canMove(Direction direction) {
        Point cell = positionAfterMove(direction);
        return cells.contains(cell);
    }

    private void nextTurn() {
        turnCount++;
    }

    @Override
    public Hero hero() {
        return player.getHero();
    }

    @Override
    public boolean isContagion(Point pt) {
        return contagions().contains(pt);
    }

    @Override
    public boolean isClean(Point pt) {
        return clean.containsKey(pt);
    }

    @Override
    public boolean isCure(Point pt) {
        return cures.contains(pt);
    }

    @Override
    public boolean isWall(Point pt) {
        return walls.contains(pt);
    }

    @Override
    public boolean isHero(Point pt) {
        return pt.equals(hero());
    }

    @Override
    public BoardReader reader() {
        return new BoardReader<Player>() {
            private int size = Verland.this.size();

            @Override
            public int size() {
                return size;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(Arrays.asList(hero()));
                if (isGameOver()) {
                    processor.accept(contagions()); // TODO to use contagions field
                    processor.accept(cured);
                }
                processor.accept(cures);
                processor.accept(cells);
                processor.accept(walls);
            }
        };
    }

    @Override
    public void newGame(Player player) {
        validate();
        this.player = player;
        player.newHero(this);
        clearScore();
        tick();
    }

    @Override
    public void remove(Player player) {
        this.player = null;
    }

    @Override
    public Point positionAfterMove(Direction direction) {
        return direction.change(hero());
    }

    @Override
    public Contagion tryCreateContagion(Point cell) {
        Contagion result = new Contagion(cell);
        contagions().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public boolean isGameOver() {
        return !hero().isAlive();
    }

    @Override
    public int contagionsNear() {
        return contagionsNear(hero());
    }

    private int contagionsNear(Point pt) {
        return (int)Arrays.stream(QDirection.values())
                .map(direction -> direction.change(pt))
                .filter(to -> cells.contains(to))
                .filter(to -> contagions().contains(to))
                .count();
    }

    // TODO попытаться избавиться от этого метода в пользу contagionsNear
    @Override
    public int visibleContagionsNear(Point pt) {
        Integer count = clean.get(pt);
        if (count == null) {
            return Element.CLEAR.value();
        }
        return count;
    }

    @Override
    public void cure(Direction direction) {
        Point result = positionAfterMove(direction);
        if (cells.contains(result)) {
            if (hero().noMorePotions()) {
                return;
            }

            if (cures.contains(result)) {
                return;
            }

            hero().tryToCure(() -> {
                cures.add(new Cure(result));
                if (contagions().contains(result)) {
                    removeContagion(result);
                } else {
                    player.event(Events.FORGOT_POTION);
                }
            });

            if (isNoPotionsButPresentContagions()) {
                openAllBoard();
                player.event(Events.NO_MORE_POTIONS);
            }
        }
    }

    private void removeContagion(Point pt) {
        cured.add(new Cured(pt));
        contagions().remove(pt);
        increaseScore();
        recalculateWalkMap();
        player.event(Events.CURE);
        if (contagions().isEmpty()) {
            openAllBoard();
            player.event(Events.WIN);
        }
    }

    private void openAllBoard() {
        clean.clear();

        for (Point cell : cells())  {
            clean.put(cell, contagionsNear(cell));
        }
    }

    private void recalculateWalkMap() {
        for (Map.Entry<Point, Integer> entry : clean.entrySet()) {
            entry.setValue(contagionsNear(entry.getKey()));
        }
    }

    private void increaseScore() {
        score++;
        maxScore = Math.max(score, maxScore);
    }

    @Override
    public boolean isNoPotionsButPresentContagions() {
        return contagions().size() != 0
                && hero().noMorePotions();
    }

    @Override
    public boolean isWin() {
        return contagions().size() == 0 && !hero().isDead();
    }

    @Override
    public void tick() {
        if (currentSize != size()) {  // TODO потестить это
            currentSize = size();
            newGame(player);
            return;
        }

        hero().tick();
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}
