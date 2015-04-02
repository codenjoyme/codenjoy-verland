package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: oleksii.morozov
 * Date: 10/18/12
 * Time: 6:54 PM
 */
public class RandomMinesGenerator implements MinesGenerator {

    private Field board;

    public List<Mine> get(int count, Field board) {
        this.board = board;

        List<Mine> result = new ArrayList<Mine>();
        for (int index = 0; index < count; index++) {
            Mine mine = new Mine(getRandomFreeCellOnBoard());
            mine.setBoard(board);
            result.add(mine);
        }
        return result;
    }


    private Point getRandomFreeCellOnBoard() {
        List<Point> freeCells = board.getFreeCells();
        if (!freeCells.isEmpty()) {
            int place = new Random().nextInt(freeCells.size());
            return freeCells.get(place);
        }

        throw new IllegalStateException("This exception should not be present");
    }
}