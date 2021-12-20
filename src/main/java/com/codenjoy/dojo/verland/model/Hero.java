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
    private MineDetector mineDetector;
    private Direction nextStep;
    private boolean useDetector;
    private Player player;

    public Hero(int x, int y) {
        super(x, y);
        useDetector = false;
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
                !field.isEmptyDetectorButPresentMines() &&
                !field.isWin();
    }
    
    private void useMineDetector() {
        if (mineDetector.getCharge() > 0) {
            mineDetector.useMe();
        }
    }

    public void charge(int charge) {
        this.mineDetector = new MineDetector(charge);
    }

    public boolean isEmptyCharge() {
        return mineDetector.getCharge() == 0;
    }

    public void tryToUseDetector(DetectorAction detectorAction) {
        if (isEmptyCharge()) {
            return;
        }

        useMineDetector();

        if (detectorAction != null) {
            detectorAction.used();
        }
    }

    public MineDetector getMineDetector() {
        return mineDetector;
    }

    @Override
    public Element state(Object player, Object... alsoAtPoint) {
        if (field.isSapperOnMine()) {
            return Element.HERO_DEAD;
        } else {
            return Element.HERO;
        }
    }

    @Override
    public void down() {
        nextStep = Direction.DOWN;
    }

    @Override
    public void up() {
        nextStep = Direction.UP;
    }

    @Override
    public void left() {
        nextStep = Direction.LEFT;
    }

    @Override
    public void right() {
        nextStep = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (p.length == 0) {
            useDetector = true;
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
        if (nextStep == null) {
            return;
        }

        if (useDetector) {
            field.useMineDetectorToGivenDirection(nextStep);
            useDetector = false;
        } else {
            field.sapperMoveTo(nextStep);
        }

        nextStep = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
