package com.codenjoy.dojo.verland.model;

import org.junit.Test;

public class MultiplayerTest extends AbstractGameTest {

    @Test
    public void severalHeroesCanAppearOnTheMap() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼o**♥☼\n" +
                "☼☼☼☼☼☼\n");

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♥☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼***♠☼\n" +
                "☼☼☼☼☼☼\n", 2);
    }
}
