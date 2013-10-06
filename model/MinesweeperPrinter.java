package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Printer;

public class MinesweeperPrinter implements Printer {
    private int boardSize;
    private Board board;

    public MinesweeperPrinter(Board board) {
        this.board = board;
        boardSize = board.getSize();
    }

    @Override
    public String print() {
        StringBuffer result = new StringBuffer();
        for (int y = boardSize - 1; y >= 0; y--) {
            for (int x = 0; x <= boardSize - 1; x++) {
                result.append(printCell(x, y));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private PlotColor printCell(int x, int y) {
        if (isBoardBound(x, y)) {
            return PlotColor.BORDER;
        } else if (board.isSapper(x, y)) {
            if (board.isSapperOnMine()) {
                return PlotColor.BANG;
            } else {
                return PlotColor.DETECTOR;
            }
        } else if (board.isGameOver() && board.isMine(x, y)) {
            if (board.isFlag(x, y)) {
                return PlotColor.DESTROYED_BOMB;
            } else {
                return PlotColor.HERE_IS_BOMB;
            }
        } else if (board.isFlag(x, y)) {
            return PlotColor.FLAG;
        } else if (board.walkAt(x, y) || board.isGameOver()) {
            int minesNear = board.minesNear(x, y);
            if (minesNear == 0) {
                return PlotColor.NO_MINE;
            } else {
                return PlotColor.printMinesCount(minesNear);
            }
        } else {
            return PlotColor.HIDDEN;
        }
    }

    private boolean isBoardBound(int x, int y) {
        return y == 0 || x == 0 || y == boardSize - 1 || x == boardSize - 1;
    }
}
