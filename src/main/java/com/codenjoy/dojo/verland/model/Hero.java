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


import com.codenjoy.dojo.games.verland.Element;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.joystick.Act;
import com.codenjoy.dojo.services.joystick.RoundsDirectionActJoystick;
import com.codenjoy.dojo.services.printer.state.HeroState;
import com.codenjoy.dojo.services.printer.state.State;
import com.codenjoy.dojo.services.round.RoundPlayerHero;
import com.codenjoy.dojo.verland.model.items.Cell;

import java.util.List;

import static com.codenjoy.dojo.games.verland.Element.*;
import static com.codenjoy.dojo.services.printer.state.StateUtils.filter;
import static com.codenjoy.dojo.verland.services.Event.*;
import static com.codenjoy.dojo.verland.services.GameSettings.Keys.POTIONS_COUNT;

public class Hero extends RoundPlayerHero<Field>
        implements RoundsDirectionActJoystick, State<Element, Player>,
                   HeroState<Element, Hero, Player> {

    private static final int ACT_SUICIDE = 0;

    private int score;
    private Potions potions;
    private Direction direction;
    private boolean cure;

    public Hero(Point pt) {
        super(pt);
        score = 0;
        cure = false;
        direction = null;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        field.heroes().add(this);
        recharge();
    }

    @Override
    public void change(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void act(Act act) {
        if (act.is()) {
            cure = true;
            return;
        }

        if (act.is(ACT_SUICIDE)) {
            die(SUICIDE);
            return;
        }
    }

    void cure(Direction direction) {
        act();
        switch (direction) {
            case UP: up(); break;
            case DOWN: down(); break;
            case LEFT: left(); break;
            case RIGHT: right(); break;
        }
    }

    void suicide() {
        act(ACT_SUICIDE);
    }

    @Override
    public void die() {
        if (isWin()) {
            return;
        }
        die(GOT_INFECTED);
    }

    @Override
    public void tickHero() {
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

    @Override
    public Player getPlayer() {
        return (Player) super.getPlayer();
    }

    @Override
    public int scores() {
        return score;
    }

    public void clearScores() {
        score = 0;
    }

    public void addScore(int added) {
        score = Math.max(0, score + added);
    }

    private void usePotion() {
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

    public void fireMorePotions() {
        getPlayer().event(NO_MORE_POTIONS);
    }

    public void tryToCure(PotionsAction action) {
        if (noMorePotions()) {
            return;
        }

        usePotion();

        if (action != null) {
            action.used();
        }
    }

    public Potions potions() {
        return potions;
    }

    public void moveTo(Direction direction) {
        if (!canMove(direction)) {
            return;
        }

        boolean cleaned = moveMe(direction);
        if (field.contagions().contains(this)) {
            die(GOT_INFECTED);
        } else {
            if (cleaned) {
                getPlayer().event(CLEAN_AREA);
            }
        }
    }

    private boolean canMove(Direction direction) {
        Point to = direction.change(this);
        return !field.walls().contains(to);
    }

    public boolean isWin() {
        return !field.isContagionsExists();
    }

    public boolean isGameOver() {
        return !isAlive() || isWin();
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

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return HeroState.super.state(player, alsoAtPoint);
    }

    @Override
    public Element beforeState(Object... alsoAtPoint) {
        if (isGameOver() && !isWin()) {
            return HERO_DEAD;
        }
        return HERO;
    }
}