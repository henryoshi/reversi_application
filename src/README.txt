This codebase is responsible for the model of the Reversi game, as well as a Textual View (ASCII view of the board) of the game.
It also includes testing for both of them.

Quick Start:
The following code template can be used to start a game, make moves on the board, and print out the textual view of the board:

ReversiModel model = new ReversiModel();
ReversiModelTextView view = new ReversiModelTextView(model);
model.startGame(4);
model.placeTile(1, 2, 3);
model.pass();
System.out.println(view.textRender());

This code snippet starts a game with board radius = 4, places a tile at position (q=1, r=2, s=3),
passes a turn, and then renders the game.


Currently, the model drives the control-flow of the program,
while the TextualView can be used to get a rendering of the game
(although the two are not connected yet, so users have to manually print out view output).

The Reversi board uses axial coordinates.
We decided to use this system because it ensures that each coordinate represents a single unique tile,
and it allows for more flexibility in delegating actions to individual tiles instead of having to manipulate a warped board.

Each tile is responsible for knowing its own position (with fields q, r, and s),
as well as the FillType (enum representing state of the tile, either empty, black, or white).

Although the ’s’ coordinate isn’t explicitly required as a field for this,
we decided to keep it for code readability and for our invariant (“q + r + s = 0” is always true).

Key Model Subcomponents:
- ‘tiles’ is a list of all tiles on the board
- ‘radius’ is an integer representing the radius/size of the board
- ‘isStarted’ is a boolean which represents whether the game is started
- ‘lastPasses’ represents which of the last moves were passes
- The ‘player’ field, which is a PlayerTurn enum, is used to keep track of which player’s turn it is

Source Organization:
‘/src/model’ is the path for all classes involved with the model of the game.
‘/src/view’ is the path for all classes involving the view of the game
‘/src/controller’ is the path for all classes involving the controller (not implemented yet).
‘/test’ is the path for all testing classes

Changes for part 2:
"->" indicates when a change was made that supplements the previously mentioned change

- Added observation methods for the model.
- Updated isGameOver to also check whether neither of the players can move
-> added helpers "doesValidMoveExistForCurrentPlayer" and "doesValidMoveExist"
--> added helper "getFillTypeOfPlayerTurn" (made it public to be used by ReadOnly classes too)
- added public methods "getCopy", "getScore", and "getRadius" (also for ReadOnly)
- added public ITile method "getClone" and its implementation in HexTile
-> added private constructor in HexTile with FillType as parameter

Created "SimpleAI" class, which plays Reversi with this simple strategy:
- every turn, place tile where it gains the most possible points in that given turn
- when multiple tiles have same value, break ties by prioritizing top-most, and then by left-most

GUI Design:
The GUI takes in a ReadOnlyModel and using its radius and a loop recreates a set of tiles
to use and display throughout methods.

When called to paint, each tile calls its respective draw method,
except for the currently selected tile (this.selected) which will call it's drawHighlighted method.

Each of these sub-methods involves copying the Graphics object, casting it to a Graphics2D object,
and using the passed AffineTransform as the applied transformation.
This AffineTransform scales accordingly and centers the image of the game.

The canvas and window are both added as TileClickedListeners, which wait for mouse clicks and then
determine which tile was clicked and acts on them accordingly.
In addition, they are implementing keyboard listeners which respond to:
 - input '1' to attempt to place a tile, if there is a selected tile.
 - input '2' to attempt a pass.

The future implementation of our controller can use this input to run the game.
Or use the created IReversiPlayer, depending on the type of Player (AI or Human).


Assignment 7 updates:

TestMain.java tests command line input, and also returns the strategy transcript of SimpleAI, added
controller.

Assignment 8 updates:
Added Provider wrappers to call our implementation of IReversiModel using the provided view and
strategies.

Successfully implemented passing, placing tiles, highlighting tiles, AI strategies as given by
providers, automatic and correct game ending, revealing of the winner and score at the end. The only
unsuccessful part was due to the lack of HumanPlayer being provided as described in PeerReview.txt.

Running the jar - Once in the correct directory call java -jar hw5.jar [parameter1] [parameter2], with
parameter1 being either simpleAI or human, representing two different types of players for
an IReversiPlayer, and parameter2 being either one of; CaptureMostPieces, PlayCornersMaxScore,
CherryPickerCMSOptimizer, AvoidCornerAdjacencyMaxScore, OptimizeCornerStratMaxScore, each leading
to an AI from the provider with the given strategy name as the strategy used.

assignment 9 updates:
- added 'countFlipsForMove(int q, int r, int s)' to IReversiModel to abstract the move calculation,
which also makes it easier for our strategy to work with any type of board (hex and square)

- moved 'countFlipsForMove' to ReadOnlyReversiModel for simpleAI use.

- added new class HintDecoratedCanvas that extends the original view and decorates it with an
optional hint on the selected tile. This feature can be enabled and disabled using '3'. This makes
all moves: '1' placetile, '2' pass, '3' hints on/off.

- added new abstract class ATile to handle repeated code among model tiles.

- added SquareReversiCanvas, SquareReversiGUI, SquareReversiTextualView, and SquareTileView all
serving the same function as their respective Hex counterparts, but with a square board. They
implements the same interfaces too.

- refactored some classes to be more descriptive, ie. ReversiGUI -> HexReversiGUI.

- added tests for the square model, player behavior, AI behavior, and text representation view


