package com.codenjoy.dojo.minesweeper.console;

import com.codenjoy.dojo.minesweeper.model.Board;
import com.codenjoy.dojo.minesweeper.model.MinesweeperPrinter;
import com.codenjoy.dojo.minesweeper.model.objects.Direction;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.Printer;

/**
 * User: oleksii.morozov Date: 10/16/12 Time: 3:33 PM
 */
public class GameController {
    private static final String BOARD_INFORMATION = "Information:\n"
            + "Controls:\n" + "w - up\n" + "s - down\n" + "a - left\n"
            + "d - right\n" + "r - use detector\n"
            + "\nLegend:\n" + "@ - Sapper\n" + "# - wall\n" + ". - free cell\n"
            + "* - mine\n" + "After each command press ENTER\n";

    public static final String CHOOSE_DIRECTION_MINE_DETECTOR = "Choose direction mine detector.";

    private Board board;
    private Console console;

    public GameController(Console console, Board board) {
        this.console = console;
        this.board = board;
    }

    public void startNewGame() {
        console.print(BOARD_INFORMATION);

        while (!board.isGameOver()) {
            printBoard();

            char command = console.read().charAt(0);

            if (command == 'r' || command == 'к') {
                console.print(CHOOSE_DIRECTION_MINE_DETECTOR);
                board.useMineDetectorToGivenDirection(handleDirectionCommand(console.read().charAt(0)));
            } else  {
                board.sapperMoveTo(handleDirectionCommand(command));
            }
        }
        printBoard();
        printEndGameMessage();
    }

    private void printBoard() {
        console.print(new Printer(board.getSize(), new MinesweeperPrinter(board)).toString());
    }

    private void printEndGameMessage() {
        if (board.isWin()) {
            console.print("I win");
        } else if (board.isSapperOnMine()) {
            console.print("Ops, mine...");
        } else if (board.isEmptyDetectorButPresentMines()) {
            console.print("Ops, I have no charge, but mines present...");
        }
    }


    private Direction handleDirectionCommand(char command) {
        if (command == 'w' || command == 'ц') {
            return Direction.UP;
        } else if (command == 'a' || command == 'ф') {
            return Direction.LEFT;
        } else if (command == 'd' || command == 'в') {
            return Direction.RIGHT;
        } else if (command == 's' || command == 'ы') {
            return Direction.DOWN;
        }
        return null;
    }

}
