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
import com.codenjoy.dojo.verland.model.items.Cell;
import com.codenjoy.dojo.verland.model.items.Flag;
import com.codenjoy.dojo.verland.model.items.Contagion;
import com.codenjoy.dojo.verland.model.items.Wall;
import com.codenjoy.dojo.verland.services.Events;
import com.codenjoy.dojo.verland.services.GameSettings;

import java.util.*;
import java.util.function.Consumer;

import static com.codenjoy.dojo.verland.services.GameSettings.Keys.*;

public class Verland implements Field {

    private Parameter<Integer> detectorCharge;
    private Parameter<Integer> minesOnBoard;
    private List<Point> cells;
    private List<Contagion> mines;
    private List<Contagion> cured;
    private int turnCount = 0;
    private ContagionsGenerator generator;
    private int maxScore;
    private int score;
    private List<Wall> walls;
    private List<Flag> cures;
    private Map<Point, Integer> walkAt;
    private int currentSize;
    private Player player;

    private GameSettings settings;

    public Verland(ContagionsGenerator generator, GameSettings settings) {
        this.settings = settings;
        this.generator = generator;
        minesOnBoard = settings.integerValue(COUNT_CONTAGIONS);
        detectorCharge = settings.integerValue(POTIONS_COUNT);
        buildWalls();
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

        while (minesOnBoard.getValue() > ((size() - 1) * (size() - 1) - 1)) {
            minesOnBoard.update(minesOnBoard.getValue() / 2);
        }

        if (detectorCharge.getValue() < minesOnBoard.getValue()) {
            detectorCharge.update(minesOnBoard.getValue());
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
        List<Point> result = new LinkedList<>();
        for (Point cell : cells()) {
            boolean isSapper = cell.equals(hero());
            boolean isBoard = cell.getX() == 0 || cell.getY() == 0 || cell.getX() == size() - 1 || cell.getY() == size() - 1;  // TODO test me
            boolean isMine = isContagion(cell);
            if (!isSapper && !isMine && !isBoard) {
                result.add(cell);
            }
        }
        return result;
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
        return mines;
    }

    @Override
    public int contagionsCount() {
        return contagions().size();
    }

    @Override
    public void moveTo(Direction direction) {
        if (canMove(direction)) {
            boolean cleaned = moveSapperAndFillFreeCell(direction);
            if (isOnContagion()) {
                player.getHero().die();
                openAllBoard();
                player.event(Events.GOT_INFECTED);
            } else {
                if (cleaned) {
                    player.event(Events.CLEAN_AREA);
                }
            }
            nextTurn();
        }
    }

    private boolean moveSapperAndFillFreeCell(Direction direction) {
        walkAt.put(hero().copy(), contagionsNear());
        hero().move(direction);

        boolean wasHere = walkAt.containsKey(hero());
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
    public boolean isOnContagion() {
        return contagions().contains(hero());
    }

    @Override
    public Hero hero() {
        return player.getHero();
    }

    @Override
    public boolean isContagion(Point pt) {
        if (contagions() == null) return false;
        return contagions().contains(pt) 
                || (isGameOver() && cured.contains(pt));
    }

    @Override
    public boolean walkAt(Point pt) {
        return walkAt.containsKey(pt);
    }

    @Override
    public boolean isCure(Point pt) {
        return cures.contains(pt);
    }

    @Override
    public boolean isHero(Point pt) {
        return pt.equals(hero());
    }

    @Override
    public int contagionsNear(Point pt) {
        Integer count = walkAt.get(pt);
        if (count == null) {
            return Element.CLEAR.value();
        }
        return count;
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
                processor.accept(contagions());
                processor.accept(cured);
                processor.accept(cures());
                processor.accept(cells());
                processor.accept(walls());
            }
        };
    }



    @Override
    public void newGame(Player player) {
        validate();
        this.player = player;
        cures = new LinkedList<>();
        walkAt = new HashMap<>();
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells();
        player.newHero(this);
        hero().charge(detectorCharge.getValue());
        mines = generator.get(minesOnBoard.getValue(), this);
        cured = new LinkedList<>();
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
        result.init(this);
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
        return contagionsNear2(hero());
    }

    // TODO какая разница в contagionsNear и contagionsNear2
    private int contagionsNear2(Point pt) {
        int result = 0;
        for (QDirection direction : QDirection.values()) {
            Point to = direction.change(pt.copy());
            if (cells.contains(to) && contagions().contains(to)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void cure(Direction direction) {
        final Point result = positionAfterMove(direction);
        if (cells.contains(result)) {
            if (hero().noMorePotions()) {
                return;
            }

            if (cures.contains(result)) {
                return;
            }

            hero().tryToCure(() -> {
                cures.add(new Flag(result));
                if (contagions().contains(result)) {
                    removeMine(result);
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

    private void removeMine(Point result) {
        Contagion mine = new Contagion(result);
        mine.init(this);
        cured.add(mine);
        contagions().remove(result);
        increaseScore();
        recalculateWalkMap();
        player.event(Events.CURE);
        if (contagions().isEmpty()) {
            openAllBoard();
            player.event(Events.WIN);
        }
    }

    private void openAllBoard() {
        walkAt.clear();

        for (Point cell : cells())  {
            walkAt.put(cell, contagionsNear2(cell));
        }
    }

    private void recalculateWalkMap() {
        for (Map.Entry<Point, Integer> entry : walkAt.entrySet()) {
            entry.setValue(contagionsNear2(entry.getKey()));
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

    public List<Wall> walls() {
        return walls;
    }

    public List<Flag> cures() {
        return cures;
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}
