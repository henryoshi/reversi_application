package model;

import java.util.ArrayList;

/**
 * An interface representing a non-mutable model of Reversi.
 */
public interface ReadOnlyReversiModel {
  /**
   * Returns a boolean representing whether the current Reversi
   * game is over or not. A game is defined as over when both players
   * pass one after the other, forced or not.
   *
   * @return whether the game is over
   * @throws IllegalStateException if the game hasn't started yet
   */
  boolean isGameOver() throws IllegalStateException;

  /**
   * Gets the tile at the given coordinates.
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @throws IllegalArgumentException if q, r, and s do not equal 0 after sum
   *                                  or are off the board.
   * @throws IllegalStateException    if the game is not started.
   */
  Tile getTileAt(int q, int r, int s)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Returns the radius of the hexagonal board that is being used to play.
   *
   * @return radius of the hexagonal board.
   */
  int getRadius();

  /**
   * returns a copy of the board.
   *
   * @return copy of tiles
   */
  ReadOnlyReversiModel getCopy();

  /**
   * What is the score of the given player?.
   *
   * @param pt player to get score of.
   * @return score of the player (calculated as number of player's tiles on board)
   */
  int getScore(PlayerTurn pt);

  /**
   * Who's turn is it right now.
   *
   * @return the current player's turn
   */
  PlayerTurn getPlayerTurn();

  /**
   * Is the given move valid?.
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @return whether a given move at position (q, r, s) is valid.
   */
  boolean isMoveValid(int q, int r, int s);

  /**
   * Get a deep copy of list of tiles (this.tiles).
   *
   * @return deep copy of the board
   */
  ArrayList<Tile> copyTiles();

  /**
   * Calculates the amount of flips that will result from this move.
   * Or, in other words, how many points you will gain this turn if you were to make this move.
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @return the amount of flips resulting from the given move being played
   */
  int countFlipsForMove(int q, int r, int s);
}
