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