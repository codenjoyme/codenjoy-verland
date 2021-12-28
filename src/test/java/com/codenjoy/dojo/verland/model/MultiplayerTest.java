package com.codenjoy.dojo.verland.model;

import org.junit.Test;

public class MultiplayerTest extends AbstractGameTest {

    @Test
    public void severalHeroesCanAppearOnTheMap_eachHeroAppearsOnHisOwnSpot () {
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

    @Test
    public void severalHeroesCanAppearOnTheMap_ifThereAreNotEnoughSpots_heroesAppearAlongWithOthers() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼♥**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼o***☼\n" +
                "☼☼☼☼☼☼\n");

        // генерируем еще 2 героя
        level().heroesSpots().forEach(this::givenPlayer);

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertF("☼☼☼☼☼☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 3);
    }

    @Test
    public void heroesСanMoveIndependently() {
        // given
        severalHeroesCanAppearOnTheMap_ifThereAreNotEnoughSpots_heroesAppearAlongWithOthers();

        // when
        hero(0).down();
        hero(1).down();
        hero(2).right();
        hero(3).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♠ ☼\n" +
                "☼♥**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♠ ☼\n" +
                "☼♠**♥☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♥♠ ☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertF("☼☼☼☼☼☼\n" +
                "☼ ♠♥ ☼\n" +
                "☼♠**♠☼\n" +
                "☼****☼\n" +
                "☼****☼\n" +
                "☼☼☼☼☼☼\n", 3);
    }
}
