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
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import static com.codenjoy.dojo.verland.services.Events.SUICIDE;

public class Hero extends PlayerHero<Field> implements State<Element, Object> {

    private boolean isDead = false;
    private Potions potions;
    private Direction direction;
    private boolean cure;
    private Player player;

    public Hero(int x, int y) {
        super(x, y);
        cure = false;
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
                !field.isNoPotionsButPresentContagions() &&
                !field.isWin();
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
        if (field.isOnContagion()) {
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
            field.cure(direction);
            cure = false;
        } else {
            field.moveTo(direction);
        }

        direction = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
