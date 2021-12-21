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
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.verland.model.items.Cell;
import com.codenjoy.dojo.verland.services.Events;

import java.util.List;

import static com.codenjoy.dojo.verland.services.Events.SUICIDE;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;

public class Hero extends PlayerHero<Field> implements State<Element, Object> {

    private boolean isDead = false;
    private Potions potions;
    private Direction direction;
    private boolean cure;
    private Player player;
    private int turnCount;

    public Hero(Point pt) {
        super(pt);
        cure = false;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        field.heroes().add(this);
        recharge();
    }

    public boolean isDead() {
        return isDead;
    }

    public void die() {
        isDead = true;
    }

    @Override
    public boolean isAlive() {
        return !isDead() &&
                !isNoPotionsButPresentContagions() &&
                !isWin();
    }
    
    private void cure() {
        if (potions.charge() > 0) {
            potions.useMe();
        }
    }

    public void charge(int charge) {
        this.potions = new Potions(charge);
    }

    public boolean noMorePotions() {
        return potions.charge() == 0;
    }

    public void tryToCure(PotionsAction action) {
        if (noMorePotions()) {
            return;
        }

        cure();

        if (action != null) {
            action.used();
        }
    }

    public Potions potions() {
        return potions;
    }

    @Override
    public Element state(Object player, Object... alsoAtPoint) {
        if (field.isContagion(this)) {
            return Element.HERO_DEAD;
        } else {
            return Element.HERO;
        }
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (p.length == 0) {
            cure = true;
            return;
        }

        if (p.length == 1 && p[0] == 0) {
            player.event(SUICIDE);
            die();
            return;
        }
    }

    @Override
    public void tick() {
        if (direction == null) {
            return;
        }

        if (cure) {
            field.cure(this, direction);
            cure = false;
        } else {
            moveTo(direction);
        }

        direction = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void moveTo(Direction direction) {
        if (!canMove(direction)) {
            return;
        }

        boolean cleaned = moveMe(direction);
        if (field.isContagion(this)) {
            die();
            field.openAllBoard();
            player.event(Events.GOT_INFECTED);
        } else {
            if (cleaned) {
                player.event(Events.CLEAN_AREA);
            }
        }
        nextTurn();
    }

    private boolean canMove(Direction direction) {
        Point to = direction.change(this);
        return !field.walls().contains(to);
    }

    private void nextTurn() {
        turnCount++;
    }

    public int getTurn() {
        return turnCount;
    }

    public boolean isGameOver() {
        return !this.isAlive();
    }

    public boolean isNoPotionsButPresentContagions() {
        return field.contagions().size() != 0
                && noMorePotions();
    }

    public boolean isWin() {
        return field.contagions().size() == 0 && !this.isDead();
    }

    public boolean moveMe(Direction direction) {
        this.move(direction);

        List<Cell> at = field.cells().getAt(this);
        boolean wasHere = at.stream().anyMatch(Cell::isClean);
        at.forEach(Cell::open);

        return !wasHere;
    }

    public void recharge() {
        charge(settings().integer(POTIONS_COUNT));
    }
}
