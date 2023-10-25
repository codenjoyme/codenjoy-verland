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