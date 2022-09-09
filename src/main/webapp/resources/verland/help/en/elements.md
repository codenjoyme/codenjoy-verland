<meta charset="UTF-8">

## Symbol breakdown
| Sprite | Code | Description |
| -------- | -------- | -------- |
|<img src="/codenjoy-contest/resources/verland/sprite/hero_dead.png" style="height:auto;" /> | `HERO_DEAD('X')` | Мой герой заразился инфекцией. | 
|<img src="/codenjoy-contest/resources/verland/sprite/hero.png" style="height:auto;" /> | `HERO('♥')` | Мой герой. | 
|<img src="/codenjoy-contest/resources/verland/sprite/hero_cure.png" style="height:auto;" /> | `HERO_CURE('!')` | Попытка моим героем зачистить инфекцию. Если инфекция была устранена ситуация вокруг обновится. Если герой ошибся и зона была не инфицирована - штраф. | 
|<img src="/codenjoy-contest/resources/verland/sprite/hero_healing.png" style="height:auto;" /> | `HERO_HEALING('x')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить герою. | 
|<img src="/codenjoy-contest/resources/verland/sprite/other_hero_dead.png" style="height:auto;" /> | `OTHER_HERO_DEAD('Y')` | Герой из моей команды заразился инфекцией. | 
|<img src="/codenjoy-contest/resources/verland/sprite/other_hero.png" style="height:auto;" /> | `OTHER_HERO('♠')` | Герой из моей команды в работе. | 
|<img src="/codenjoy-contest/resources/verland/sprite/other_hero_cure.png" style="height:auto;" /> | `OTHER_HERO_CURE('+')` | Попытка героем из моей команды зачистить инфекцию. Если инфекция была устранена ситуация вокруг обновится.  Если герой ошибся и зона была не инфицирована - штраф. | 
|<img src="/codenjoy-contest/resources/verland/sprite/other_hero_healing.png" style="height:auto;" /> | `OTHER_HERO_HEALING('y')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить герою из моей команды. | 
|<img src="/codenjoy-contest/resources/verland/sprite/enemy_hero_dead.png" style="height:auto;" /> | `ENEMY_HERO_DEAD('Z')` | Вражеский герой заразился инфекцией. | 
|<img src="/codenjoy-contest/resources/verland/sprite/enemy_hero.png" style="height:auto;" /> | `ENEMY_HERO('♣')` | Вражеский герой в работе. | 
|<img src="/codenjoy-contest/resources/verland/sprite/enemy_hero_healing.png" style="height:auto;" /> | `ENEMY_HERO_HEALING('z')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить вражескому герою. | 
|<img src="/codenjoy-contest/resources/verland/sprite/infection.png" style="height:auto;" /> | `INFECTION('o')` | На секунду после смерти героя поле открывается и можно увидеть где была инфекция. | 
|<img src="/codenjoy-contest/resources/verland/sprite/hidden.png" style="height:auto;" /> | `HIDDEN('*')` | Туман - место где еще не бывал герой. Возможно эта зона инфицирована. | 
|<img src="/codenjoy-contest/resources/verland/sprite/pathless.png" style="height:auto;" /> | `PATHLESS('☼')` | Непроходимые территории - обычно граница поля, но может быть и простое на пути героя. | 
|<img src="/codenjoy-contest/resources/verland/sprite/clear.png" style="height:auto;" /> | `CLEAR(' ')` | Вокруг этой зоны нет заражений. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_one.png" style="height:auto;" /> | `CONTAGION_ONE('1')` | Вокруг этой зоны было зафиксировано одно заражение. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_two.png" style="height:auto;" /> | `CONTAGION_TWO('2')` | Вокруг этой зоны было зафиксировано два заражения. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_three.png" style="height:auto;" /> | `CONTAGION_THREE('3')` | Вокруг этой зоны было зафиксировано три заражения. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_four.png" style="height:auto;" /> | `CONTAGION_FOUR('4')` | Вокруг этой зоны было зафиксировано четыре заражения. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_five.png" style="height:auto;" /> | `CONTAGION_FIVE('5')` | Вокруг этой зоны было зафиксировано пять заражений. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_six.png" style="height:auto;" /> | `CONTAGION_SIX('6')` | Вокруг этой зоны было зафиксировано шесть заражений. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_seven.png" style="height:auto;" /> | `CONTAGION_SEVEN('7')` | Вокруг этой зоны было зафиксировано семь заражений. | 
|<img src="/codenjoy-contest/resources/verland/sprite/contagion_eight.png" style="height:auto;" /> | `CONTAGION_EIGHT('8')` | Вокруг этой зоны было зафиксировано восемь заражений. | 
