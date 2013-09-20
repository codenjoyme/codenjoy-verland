package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.*;
import com.codenjoy.dojo.minesweeper.model.objects.Direction;
import com.codenjoy.dojo.minesweeper.services.MinesweeperEvents;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.EventListener;

import java.util.*;

public class BoardImpl implements Board {

    private List<Point> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private List<Point> removedMines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;
    private EventListener listener;
    private boolean useDetector;
    private int maxScore;
    private int score;
    private int detectorCharge;
    private int minesCount;
    private Printer printer;
    private List<Point> isFlag;
    private Map<Point, Integer> walkAt;
    private Direction nextStep;

    public BoardImpl(int size, int minesCount, int detectorCharge,
                     MinesGenerator minesGenerator, EventListener listener) {
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException();
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        printer = new MinesweeperPrinter(this);

        this.listener = listener; // TODO to use settings
        this.minesGenerator = minesGenerator;
        this.detectorCharge = detectorCharge;
        this.minesCount = minesCount;
    }

    protected Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<Point> initializeBoardCells(int size) {
        List<Point> result = new ArrayList<Point>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new PointImpl(x, y));
            }
        }
        return result;
    }

    @Override
    public List<Point> getFreeCells() {
        List<Point> result = new LinkedList<Point>();
        for (Point cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
            boolean isMine = isMine(cell);
            if (!isSapper && !isMine) {
                result.add(cell);
            }
        }
        return result;
    }

    private boolean isMine(Point cell) {
        boolean isMine = false;
        if (getMines() != null) {
            for (Mine mine : getMines()) {
                isMine |= cell.equals(mine);
            }
        }
        return isMine;
    }

    @Override
    public List<Point> getCells() {
        return cells;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Sapper getSapper() {
        return sapper;
    }

    @Override
    public List<Mine> getMines() {
        return mines;
    }

    @Override
    public int getMinesCount() {
        return getMines().size();
    }

    @Override
    public void sapperMoveTo(Direction direction) {
        if (isSapperCanMoveToDirection(direction)) {
            boolean cleaned = moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die(true);
                openAllBoard();
                fire(MinesweeperEvents.KILL_ON_MINE);
            } else {
                if (cleaned) {
                    fire(MinesweeperEvents.CLEAN_BOARD);
                }
            }
            nextTurn();
        }
    }

    private void fire(MinesweeperEvents event) {
        if (listener != null) {
            listener.event(event.name());
        }
    }

    private boolean moveSapperAndFillFreeCell(Direction direction) {
        walkAt.put(sapper.copy(), getMinesNearSapper());
        direction.change(sapper);

        boolean wasHere = walkAt.containsKey(sapper.copy());
        return !wasHere;
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Point cell = getCellPossiblePosition(direction);
        return cells.contains(cell);
    }

    private void nextTurn() {
        turnCount++;
    }

    @Override
    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
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
            public void act() {
                useDetector = true;
            }
        };
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    @Override
    public int getCurrentScore() {
        return score;
    }

    @Override
    public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines() || isWin();
    }

    @Override
    public boolean isMine(int x, int y) {
        Point pt = new PointImpl(x, y);
        return getMines().contains(pt) || (isGameOver() && removedMines.contains(pt));
    }

    @Override
    public boolean walkAt(int x, int y) {
        return walkAt.containsKey(new PointImpl(x, y));
    }

    @Override
    public boolean isFlag(int x, int y) {
        return isFlag.contains(new PointImpl(x, y));
    }

    @Override
    public boolean isSapper(int x, int y) {
        return new PointImpl(x, y).equals(getSapper());
    }

    @Override
    public int minesNear(int x, int y) {
        Integer count = walkAt.get(new PointImpl(x, y));
        if (count == null) {
            return -1;
        }
        return count;
    }

    @Override
    public void newGame() {
        isFlag = new LinkedList<Point>();
        walkAt = new HashMap<Point, Integer>();
        useDetector = false;
        maxScore = 0;
        score = 0;
        cells = initializeBoardCells(size);
        sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        mines = minesGenerator.get(minesCount, this);
        removedMines = new LinkedList<Point>();
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() {  // TODO test me
        maxScore = 0;
        score = 0;
    }

    @Override
    public Point getCellPossiblePosition(Direction direction) {
        return direction.change(sapper.copy());
    }

    @Override
    public Mine createMineOnPositionIfPossible(Point cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public int getMinesNearSapper() {
        return getMinesNear(sapper);
    }

    private int getMinesNear(Point position) {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Point newPosition = direction.change(position.copy());
            if (cells.contains(newPosition) && getMines().contains(newPosition)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void useMineDetectorToGivenDirection(Direction direction) {
        final Point result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            if (sapper.isEmptyCharge()) {
                return;
            }

            if (isFlag.contains(result)) {
                return;
            }

            sapper.tryToUseDetector(new DetectorAction() {
                @Override
                public void used() {
                    isFlag.add(result);
                    if (getMines().contains(result)) {
                        removeMine(result);
                    } else {
                        fire(MinesweeperEvents.FORGET_CHARGE);
                    }
                }
            });

            if (isEmptyDetectorButPresentMines()) {
                openAllBoard();
                fire(MinesweeperEvents.NO_MORE_CHARGE);
            }
        }
    }

    private void removeMine(Point result) {
        removedMines.add(result);
        getMines().remove(result);
        increaseScore();
        recalculateWalkMap();
        fire(MinesweeperEvents.DESTROY_MINE);
        if (getMines().isEmpty()) {
            openAllBoard();
            fire(MinesweeperEvents.WIN);
        }
    }

    private void openAllBoard() {
        walkAt.clear();

        for (Point cell : getCells())  {
            walkAt.put(cell, getMinesNear(cell));
        }
    }

    private void recalculateWalkMap() {
        for (Map.Entry<Point, Integer> entry : walkAt.entrySet()) {
            entry.setValue(getMinesNear(entry.getKey()));
        }
    }

    private void increaseScore() {
        score++;
        maxScore = Math.max(score, maxScore);
    }

    @Override
    public boolean isEmptyDetectorButPresentMines() {
        return getMines().size() != 0 && sapper.isEmptyCharge();
    }

    @Override
    public boolean isWin() {
        return getMines().size() == 0 && !sapper.isDead();
    }

    @Override
    public void tick() {
        if (nextStep == null) {
            return;
        }

        if (useDetector) {
            useMineDetectorToGivenDirection(nextStep);
            useDetector = false;
        } else {
            sapperMoveTo(nextStep);
        }

        nextStep = null;
    }
}
