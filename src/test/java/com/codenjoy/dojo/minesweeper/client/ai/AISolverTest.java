package com.codenjoy.dojo.minesweeper.client.ai;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AISolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new AISolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }

    // проверяем что мы можем заходить в узкие проходы,
    // для этого анализиурем что было под героем в прошлом тике
    @Test
    public void should1() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼     1*2    *☼" +
                "☼  1111*1    *☼" +
                "☼  1******☺  *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼    ***2    *☼" +
                "☼  111111    *☼" +
                "☼  1*****☺   *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼    ***2    *☼" +
                "☼  111111    *☼" +
                "☼  1****☺    *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼    ***2    *☼" +
                "☼  11111☺    *☼" +
                "☼  1****     *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼     1*2    *☼" +
                "☼  1111*1    *☼" +
                "☼  1**** ☺   *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼     1*2    *☼" +
                "☼  1111*1    *☼" +
                "☼  1****☺    *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼     1*21   *☼" +
                "☼  1111*1    *☼" +
                "☼  1***☺     *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼     1*21   *☼" +
                "☼  1111☺1    *☼" +
                "☼  1***      *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT,UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼      ‼11   *☼" +
                "☼  111☺      *☼" +
                "☼  1***      *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼      ‼11   *☼" +
                "☼  111       *☼" +
                "☼  1**☺      *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼      ‼11   *☼" +
                "☼  111       *☼" +
                "☼  1*☺       *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT,LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼      ‼11   *☼" +
                "☼  111       *☼" +
                "☼  1*☺       *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT,LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼      ‼11   *☼" +
                "☼            *☼" +
                "☼   ‼☺       *☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }

    // проверяем, что он может построить кратчайший путь в зону, в которой опасно
    // если только мы там собрались ставить флажок
    @Test
    public void should2() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼    1********☼" +
                "☼    1********☼" +
                "☼    12*☺   **☼" +
                "☼‼   ‼      **☼" +
                "☼           **☼" +
                "☼       ‼   **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼ ‼         **☼" +
                "☼        ‼  **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼   ‼ ‼     **☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼    1********☼" +
                "☼    1********☼" +
                "☼    12☺1   **☼" +
                "☼‼   ‼      **☼" +
                "☼           **☼" +
                "☼       ‼   **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼ ‼         **☼" +
                "☼        ‼  **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼   ‼ ‼     **☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT,UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼    1********☼" +
                "☼    1*‼******☼" +
                "☼    11☺    **☼" +
                "☼‼   ‼      **☼" +
                "☼           **☼" +
                "☼       ‼   **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼ ‼         **☼" +
                "☼        ‼  **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼   ‼ ‼     **☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼    1********☼" +
                "☼    1*‼******☼" +
                "☼    1☺1    **☼" +
                "☼‼   ‼      **☼" +
                "☼           **☼" +
                "☼       ‼   **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼ ‼         **☼" +
                "☼        ‼  **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼   ‼ ‼     **☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "ACT,UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼     ********☼" +
                "☼     ‼‼******☼" +
                "☼     ☺     **☼" +
                "☼‼   ‼      **☼" +
                "☼           **☼" +
                "☼       ‼   **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼ ‼         **☼" +
                "☼        ‼  **☼" +
                "☼           **☼" +
                "☼           **☼" +
                "☼   ‼ ‼     **☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }

    // если рядом обезвреженная бомба, то это для героя все равно что там *,
    // т.е. мы не знаем точно сколько бомб вокруг
    @Test
    public void should3() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼    ☺ ***☼" +
                "☼       ‼*****☼" +
                "☼  ‼    ******☼" +
                "☼       ******☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN"); // пока безопасно


        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼☺****☼" +
                "☼  ‼    ******☼" +
                "☼       ******☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT"); // а вот дальше опасно идти вниз

        // будет так
//        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
//                "☼      x      ☼" +
//                "☼             ☼" +
//                "☼  x x        ☼" +
//                "☼             ☼" +
//                "☼        x    ☼" +
//                "☼   x         ☼" +
//                "☼       x11   ☼" +
//                "☼  x    1Ѡ1   ☼" +
//                "☼       111   ☼" +
//                "☼             ☼" +
//                "☼      x      ☼" +
//                "☼             ☼" +
//                "☼   x         ☼" +
//                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "STOP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ☺1****☼" +
                "☼  ‼    ******☼" +
                "☼       ******☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼1****☼" +
                "☼  ‼    ☺*****☼" +
                "☼       ******☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼1****☼" +
                "☼  ‼    1*****☼" +
                "☼       ☺*****☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼1****☼" +
                "☼  ‼    1*****☼" +
                "☼       1☺****☼" +
                "☼        *****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼1****☼" +
                "☼  ‼    1*****☼" +
                "☼       11****☼" +
                "☼        ☺****☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼      ‼    **☼" +
                "☼           **☼" +
                "☼  ‼ ‼      **☼" +
                "☼           **☼" +
                "☼        ‼  **☼" +
                "☼   ‼      ***☼" +
                "☼       ‼1****☼" +
                "☼  ‼    1*****☼" +
                "☼       11****☼" +
                "☼         ☺***☼" +
                "☼      ‼  ****☼" +
                "☼         ****☼" +
                "☼   ‼     ****☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");
    }

    @Test
    public void should4() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼111‼*********☼" +
                "☼    ☺********☼" +
                "☼    1********☼" +
                "☼    1********☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");

        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼*************☼" +
                "☼111‼☺********☼" +
                "☼    1********☼" +
                "☼    1********☼" +
                "☼    1********☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }
}
