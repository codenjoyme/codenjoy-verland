<!-- Code generated by ManualGeneratorRunner.java
  !!!DO NOT EDIT!!! -->
<meta charset="UTF-8">

## Intro

The game server is available for familiarization reasons
[http://codenjoy.com/codenjoy-contest](http://codenjoy.com/codenjoy-contest).

This is the open source game. To realize your game, correct errors in the current
version and make the other corrections, you should
[fork the project](https://github.com/codenjoyme/codenjoy) at first.
There is the description in the Readme.md file in the repository root.
It is specified in the description what to do next.

If any questions, please write in [skype:alexander.baglay](skype:alexander.baglay)
or Email [apofig@gmail.com](mailto:apofig@gmail.com).

Game project (for writing your bot) can be 
found [here](https://github.com/codenjoyme/codenjoy-clients.git)

## What is the game about

You have to write your own bot for the hero that beats the other bots
on points. Each player plays on his own field. The hero can move
through the free cells in all four directions.

On his way, the hero may encounter infestations. If the hero becomes infected. 
he will die. The hero does not know where the sources of infection are,
so he may attempt to perform a healing ritual in a restricted area 
in a given direction. For each successful healing the hero will receive 
bonus points[(?)](#ask). For his death he receives penalty points[(?)](#ask).

You can see if there is an infection around the cell by walking on it. If it has
there's a number - it means there's a corresponding number
infected. If there is no number - all around is clear.

Points are added up. The player with the most points wins (before the agreed
time).

[(?)](#ask)The exact number of points for any action as well as other
settings at this point in the game, check with Sensei.

## Connect to the server

So, the player [registers on the server](../../../register?gameName=verland)
and joining the game.

Then you should connect from client code to the server via websockets.
This [collection of clients](https://github.com/codenjoyme/codenjoy-clients.git)
for different programming languages will help you. How to start a
client please check at the root of the project in the README.md file.

If you can't find your programming language, you're gonna
have to write your client (and then send us to the mail:
[apofig@gmail.com](mailto:apofig@gmail.com))

Address to connect the game on the server looks like this (you can
copy it from your game room):

`https://[server]/codenjoy-contest/board/player/[user]?code=[code]`

Here `[server]` - domain/id of server, `[user]` is your player id
and `[code]` is your security token. Make sure you keep the code
safe from prying eyes. Any participant, knowing your code, can
play on your behalf.

In the client code, you need to find a similar line and replace it
with your URL - thereby, you set the login / password to access the
server. Then start your client and make sure the server receives
your client's commands. After that, you can start working on the
logic of the bot.

## Message format

After connection, the client will regularly (every second) receive 
a line of characters with the encoded state of the field. The format:

`^board=(.*)$`

You can use this regular expression to extract a board from
the resulting string.

## Field example

Here's a sample string answer from the server:

<pre>board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼*************☼☼**3! *********☼☼**2♥!********☼☼111!*********☼☼ ************☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The length of the string is equal to the area of the field `N*N`. If you insert a character line break character every `N=sqrt(length(string))` characters, then you get a readable image of the field:

<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼*************☼
☼**3!*********☼
☼**2♥!********☼
☼111!*********☼
☼ ************☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The first character of the line corresponds to the cell located in the upper left corner and has the coordinate `[0, 14]`.
The coordinate `[0, 0]` corresponds to the lower left corner.
In this example, the position of the hero (symbol `♥`) is `[4, 3]`.

What this field looks like in real life:

![](/codenjoy-contest/resources/verland/help/board.png)

<meta charset="UTF-8">

## Symbol breakdown
| Sprite | Code | Description |
| -------- | -------- | -------- |
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/hero_dead.png" style="width:40px;" /> | `HERO_DEAD('X')` | Мой герой заразился инфекцией. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/hero.png" style="width:40px;" /> | `HERO('♥')` | Мой герой. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/hero_cure.png" style="width:40px;" /> | `HERO_CURE('!')` | Попытка моим героем зачистить инфекцию. Если инфекция была устранена ситуация вокруг обновится. Если герой ошибся и зона была не инфицирована - штраф. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/hero_healing.png" style="width:40px;" /> | `HERO_HEALING('x')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить герою. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/other_hero_dead.png" style="width:40px;" /> | `OTHER_HERO_DEAD('Y')` | Герой из моей команды заразился инфекцией. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/other_hero.png" style="width:40px;" /> | `OTHER_HERO('♠')` | Герой из моей команды в работе. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/other_hero_cure.png" style="width:40px;" /> | `OTHER_HERO_CURE('+')` | Попытка героем из моей команды зачистить инфекцию. Если инфекция была устранена ситуация вокруг обновится.  Если герой ошибся и зона была не инфицирована - штраф. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/other_hero_healing.png" style="width:40px;" /> | `OTHER_HERO_HEALING('y')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить герою из моей команды. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/enemy_hero_dead.png" style="width:40px;" /> | `ENEMY_HERO_DEAD('Z')` | Вражеский герой заразился инфекцией. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/enemy_hero.png" style="width:40px;" /> | `ENEMY_HERO('♣')` | Вражеский герой в работе. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/enemy_hero_healing.png" style="width:40px;" /> | `ENEMY_HERO_HEALING('z')` | На секунду после окончания игры поле открывается и можно увидеть какую инфекцию удалось обеззаразить вражескому герою. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/infection.png" style="width:40px;" /> | `INFECTION('o')` | На секунду после смерти героя поле открывается и можно увидеть где была инфекция. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/hidden.png" style="width:40px;" /> | `HIDDEN('*')` | Туман - место где еще не бывал герой. Возможно эта зона инфицирована. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/pathless.png" style="width:40px;" /> | `PATHLESS('☼')` | Непроходимые территории - обычно граница поля, но может быть и простое на пути героя. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/clear.png" style="width:40px;" /> | `CLEAR(' ')` | Вокруг этой зоны нет заражений. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_one.png" style="width:40px;" /> | `CONTAGION_ONE('1')` | Вокруг этой зоны было зафиксировано одно заражение. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_two.png" style="width:40px;" /> | `CONTAGION_TWO('2')` | Вокруг этой зоны было зафиксировано два заражения. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_three.png" style="width:40px;" /> | `CONTAGION_THREE('3')` | Вокруг этой зоны было зафиксировано три заражения. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_four.png" style="width:40px;" /> | `CONTAGION_FOUR('4')` | Вокруг этой зоны было зафиксировано четыре заражения. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_five.png" style="width:40px;" /> | `CONTAGION_FIVE('5')` | Вокруг этой зоны было зафиксировано пять заражений. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_six.png" style="width:40px;" /> | `CONTAGION_SIX('6')` | Вокруг этой зоны было зафиксировано шесть заражений. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_seven.png" style="width:40px;" /> | `CONTAGION_SEVEN('7')` | Вокруг этой зоны было зафиксировано семь заражений. | 
|<img src="https://github.com/codenjoyme/codenjoy-verland/raw/master/src/main/webapp/resources/verland/sprite/contagion_eight.png" style="width:40px;" /> | `CONTAGION_EIGHT('8')` | Вокруг этой зоны было зафиксировано восемь заражений. | 


## What to do

The game is turn-based, every second the server sends your client
the state of the updated field for the current moment and waits for a response
command to the hero. In the next second, the player has to give
command to the hero. If he doesn't, the hero stands still.

Your goal is to make the hero move according to your algorithm.
The hero on the field must be able to score as many points as he can.
The main goal of the game is to beat all opponents by points.

## Control Commands

There are several commands:

* UP, DOWN, LEFT, RIGHT, move the hero
  in a given direction by 1 cell. 
* `ACT,<DIRECTION> ` - attempt to heal the infection in a given in 
  `<DIRECTION>=LEFT|RIGHT|UP|DOWN` direction. 
* `ACT(0)` - self elimination from this field 
  (results in penalty points)[(?)](#ask). 

## Settings.

The settings will change[(?)](#ask) as the game progresses. The default values
are given in the table below:

| Event | Title | Points |
|---------|----------|------|
| COUNT_CONTAGIONS | 30[(?)](#ask) |
| number of healing potions | POTIONS_COUNT | 100[(?)](#ask) |
| CURE_SCORE | 10[(?)](#ask)
| CLEAN_AREA_SCORE | 1[(?)](#ask)
| WIN_SCORE | 300[(?)](#ask)
| GOT_INFECTED_PENALTY | -15[(?)](#ask)
| Points penalty for suicide | SUICIDE_PENALTY | -100[(?)](#ask)
| Penalty on points for failed heal attempt | NO_MORE_POTIONS_PENALTY | -2[(?)](#ask) |
| Penalty for failed healing attempt | FORGOT_POTION_PENALTY | -2[(?)](#ask) |

## Cases

## Hints

The primary task is to write a websocket client that connects
to the server. Then get the hero on the field to obey commands.
This will prepare the player for the main game.
The primary goal is to run a meaningful game and win.

If you don't know where to start, try implementing the following algorithms:

* Move to a random empty adjacent cell.
* Take a step back and analyze around which cells there is definitely no infection. 
* Try to heal the supposed place of infection.

## FAQ

## <a id="ask"></a> Ask Sensei

You can always see the settings of the current game
[here](/codenjoy-contest/rest/settings/player).
Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Client and API

The organizers provide players with prepared clients in the source
code in several languages. Each of these clients already knows how to communicate
with the server, receive and parse messages from the server (usually called a board)
and send commands to the server.

The client code doesn't give too much of a head start to the players because this code
still needs to be sorted out, but there is some logic to communicate with the server +
some high-level API to work with the board (which is already nice).

All languages in one way or another have a similar set of methods:

* `Solver`.
  An empty class with one method - you have to fill it with clever logic.
* `Direcion`.
  Possible directions of movement for this game.
* `Point`
  `x`, `y` coordinates.
* `Element`.
  Type of element on the board.
* `Board`.
  Contains logic for easy search and manipulation of elements on the board.
  You can find the following methods in the Board class:
* `int boardSize();`
  Board size.
* `boolean isAt(Point point, Element element);`
  Is the given element at the point position?
* `boolean isAt(Point point, Collection<Element>elements);`
  Is there anything from the given set at the point position?
* `boolean isNear(Point point, Element element);`
  Is there a given element around the cell with the point coordinate?
* `int countNear(Point point, Element element);`
  How many elements of the given type are there around the cell with point?
* `Element getAt(Point point);`
  Element in the current cell.
* `Point getHero();`
  My hero's position on the board.
* `boolean isGameOver();`
  Is my hero alive?
* ``Collection<Point> getOtherHeroes();``
  The positions of all the other heroes on the board.
* `Collection<Point> getEnemyHeroes();`
  Positions of all enemy heroes on the board (in the case of team play).
* `Collection<Point> getBarriers();`
  Positions of all objects blocking movement.
* `boolean isBarrierAt(Point point);`
  Is there an obstacle in the point cell?
* `Collection<Point> getContagions();`
  Positions all the locations that point to a contagion around it.
* ``Collection<Point> getWalls();``
  The positions of all the walls.
* and so on...

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy.git).
All instructions are in Readme.md file, you'll know what to do 
next once you read it.

If you have any questions reach me 
in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).

Good luck and may the best win!
