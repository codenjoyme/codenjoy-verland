package com.codenjoy.dojo.verland.model.items;

import com.codenjoy.dojo.games.verland.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class HeroSpot extends PointImpl implements State<Element, Object> {

    public HeroSpot(Point pt) {
        super(pt);
    }

    @Override
    public Element state(Object player, Object... alsoAtPoint) {
        return Element.CLEAR;
    }
}