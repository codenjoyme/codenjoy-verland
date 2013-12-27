package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Direction;
import com.codenjoy.dojo.minesweeper.model.objects.Mine;
import com.codenjoy.dojo.minesweeper.model.objects.Sapper;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface Board extends Game {

    List<Point> getFreeCells();

    List<Point> getCells();

    int getSize();

    Sapper getSapper();

    List<Mine> getMines();

    int getMinesCount();

    void sapperMoveTo(Direction direction);

    boolean isSapperOnMine();

    Point getCellPossiblePosition(Direction direction);

    int getMinesNearSapper();

    boolean isEmptyDetectorButPresentMines();

    boolean isWin();

    void useMineDetectorToGivenDirection(Direction direction);

    Mine createMineOnPositionIfPossible(Point cell);

    int getTurn();

    boolean isGameOver();

    boolean isMine(Point pt);

    boolean walkAt(Point pt);

    boolean isFlag(Point pt);

    boolean isSapper(Point pt);

    int minesNear(Point pt);
}